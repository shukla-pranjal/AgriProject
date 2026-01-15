package com.farmflow.service.impl;

import com.farmflow.config.AuditAwareConfig;
import com.farmflow.config.security.CustomUserDetails;
import com.farmflow.dto.*;
import com.farmflow.entity.AccountMetadata;
import com.farmflow.entity.User;
import com.farmflow.enums.AccountStatus;
import com.farmflow.enums.Role;
import com.farmflow.exception.ResourceNotFoundException;
import com.farmflow.exception.ValidationException;
import com.farmflow.repository.UserRepository;
import com.farmflow.service.AuthService;
import com.farmflow.service.JwtService;
import com.farmflow.service.email.EmailComposerService;
import com.farmflow.util.Constants;
import com.farmflow.util.Validation;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Slf4j
@Service
public class AuthServiceImpl implements AuthService {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private Validation validation;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private AuditAwareConfig auditAwareConfig;
    @Autowired
    private EmailComposerService emailComposerService;

    @Override
    public LoginResponse login(LoginRequest loginRequest) throws ResourceNotFoundException {

        // 1️⃣ Find user
        User user = userRepository.findByEmail(loginRequest.getEmail().toLowerCase())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        AccountMetadata metadata = user.getMetadata();

        // 2️⃣ Check status
        if (metadata.getStatus() == AccountStatus.PENDING) {
            throw new ValidationException("Account not verified. Please verify email.");
        }

        if (metadata.getStatus() == AccountStatus.DISABLED) {
            throw new ValidationException("Account is disabled.");
        }

        if (metadata.getStatus() == AccountStatus.LOCKED) {
            if (metadata.getLockedUntil() != null && metadata.getLockedUntil().isAfter(LocalDateTime.now())) {
                throw new ValidationException("Account is locked until " + metadata.getLockedUntil());
            } else {
                // Unlock after lock duration expires
                metadata.setStatus(AccountStatus.ACTIVE);
                metadata.setFailedLoginAttempts(0);
                metadata.setLockedUntil(null);
                userRepository.save(user);
            }
        }

        // 3️⃣ Authenticate credentials
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(), loginRequest.getPassword()
                )
        );

        // 4️⃣ Reset failed attempts after successful login
        metadata.setFailedLoginAttempts(0);
        metadata.setLockedUntil(null);
        metadata.setLastLoginAt(LocalDateTime.now());
        userRepository.save(user);

        // 5️⃣ Generate token
        String token = jwtService.generateToken(user);
        UserDTO userDto = modelMapper.map(user, UserDTO.class);

        return LoginResponse.builder()
                .token(token)
                .user(userDto)
                .build();
    }

    @Override
    public UserDTO createUser(UserDTO userDTO) {
        validation.userValidate(userDTO);
        if (userRepository.existsByEmailIgnoreCase(userDTO.getEmail())) {
            throw new ValidationException("User with email " + userDTO.getEmail() + " already exists.");
        }
        User user = modelMapper.map(userDTO, User.class);

        String verficationCode = emailComposerService.sendVerificationEmail(userDTO);
        AccountMetadata accountMetadata = AccountMetadata.builder()
                .status(AccountStatus.PENDING)
                .verificationCodeCreatedAt(LocalDateTime.now())
                .verificationCode(verficationCode)
                .failedLoginAttempts(0)
                .build();

        user.setMetadata(accountMetadata);

        if (verficationCode == null){
            accountMetadata.setStatus(AccountStatus.ACTIVE);
            accountMetadata.setVerificationCode(null);
            accountMetadata.setVerificationCodeCreatedAt(null);
        }

        user.setPassword(bCryptPasswordEncoder.encode(userDTO.getPassword()));

        user.setRoles(Set.of(Role.ROLE_USER));

        User savedUser = userRepository.save(user);
        return modelMapper.map(savedUser, UserDTO.class);
    }

    @Override
    public boolean isOwnerOrAdmin(Integer resourceOwnerId) {
        Integer currentUserId = auditAwareConfig.getCurrentUserId();
        return currentUserId != null &&
                (currentUserId.equals(resourceOwnerId) || isAdmin());
    }

    @Override
    public void validateUser(Integer id) {
        if (!isOwnerOrAdmin(id)) {
            throw new AccessDeniedException("Access Denied");
        }
    }

    @Override
    public void verifyEmail(EmailVerificationRequest request) throws Exception {
        User user = userRepository.findByEmail(request.getEmail().toLowerCase())
                .orElseThrow(() -> new ResourceNotFoundException(Constants.USER_EMAIL_NOT_EXISTS));

        AccountMetadata metadata = user.getMetadata();
        if (metadata.getStatus() != AccountStatus.PENDING) {
            throw new ValidationException("Account already verified or inactive");
        }

        if (!request.getVerificationCode().equals(metadata.getVerificationCode())) {
            throw new ValidationException("Invalid verification code");
        }

        if (metadata.getVerificationCodeCreatedAt()
                .isBefore(LocalDateTime.now().minusHours(24))) {
            throw new ValidationException("Verification code expired");
        }

        metadata.setStatus(AccountStatus.ACTIVE);
        metadata.setVerificationCode(null);
        metadata.setVerificationCodeCreatedAt(null);


        userRepository.save(user);
    }

    @Override
    public void resendVerification(ResendVerificationRequest request) throws Exception {
        User user = userRepository.findByEmail(request.getEmail().toLowerCase())
                .orElseThrow(() -> new ResourceNotFoundException(Constants.USER_EMAIL_NOT_EXISTS));

        AccountMetadata metadata = user.getMetadata();
        if (metadata.getStatus() == AccountStatus.ACTIVE) {
            throw new ValidationException("Account is already verified");
        }

        if (metadata.getStatus() != AccountStatus.PENDING && metadata.getStatus() != AccountStatus.DISABLED) {
            throw new ValidationException("Verification code can only be resent for pending or disabled accounts");
        }

        if (metadata.getVerificationCodeCreatedAt() != null &&
                metadata.getVerificationCodeCreatedAt().isAfter(LocalDateTime.now().minusMinutes(10))) {
            throw new ValidationException("You can request a new code after 10 minutes");
        }

        String newCode = emailComposerService.sendVerificationEmail(modelMapper.map(user, UserDTO.class));
        metadata.setVerificationCode(newCode);
        metadata.setVerificationCodeCreatedAt(LocalDateTime.now());

        userRepository.save(user);
    }

    @Override
    public void changePassword(ChangePasswordRequest request) throws ResourceNotFoundException {

        Integer currentUserId = auditAwareConfig.getCurrentUserId();
        if (currentUserId == null) {
            throw new AccessDeniedException("Not logged in");
        }

        User user = userRepository.findById(currentUserId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (!bCryptPasswordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new ValidationException("Old password does not match");
        }

        user.setPassword(bCryptPasswordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
    }

    @Override
    public void forgotPassword(ForgotPasswordRequest request) throws ResourceNotFoundException {
        User user = userRepository.findByEmail(request.getEmail().toLowerCase())
                .orElseThrow(() -> new ResourceNotFoundException(Constants.USER_EMAIL_NOT_EXISTS));

        AccountMetadata metadata = user.getMetadata();

        // prevent spamming reset requests
        if (metadata.getVerificationCodeCreatedAt() != null &&
                metadata.getVerificationCodeCreatedAt().isAfter(LocalDateTime.now().minusMinutes(10))) {
            throw new ValidationException("You can request a new reset code after 10 minutes");
        }

        String resetCode = emailComposerService.sendPasswordResetEmail(modelMapper.map(user, UserDTO.class));
        metadata.setVerificationCode(resetCode);
        metadata.setVerificationCodeCreatedAt(LocalDateTime.now());

        userRepository.save(user);
    }

    @Override
    public void resetPassword(ResetPasswordRequest request) throws ResourceNotFoundException {
        User user = userRepository.findByEmail(request.getEmail().toLowerCase())
                .orElseThrow(() -> new ResourceNotFoundException(Constants.USER_EMAIL_NOT_EXISTS));

        AccountMetadata metadata = user.getMetadata();

        if (!request.getVerificationCode().equals(metadata.getVerificationCode())) {
            throw new ValidationException("Invalid verification code");
        }

        if (metadata.getVerificationCodeCreatedAt()
                .isBefore(LocalDateTime.now().minusHours(1))) {
            throw new ValidationException("Reset code expired");
        }

        user.setPassword(bCryptPasswordEncoder.encode(request.getNewPassword()));
        metadata.setVerificationCode(null);
        metadata.setVerificationCodeCreatedAt(null);

        userRepository.save(user);
    }

    @Override
    public void changeEmail(ChangeEmailRequest request) throws ResourceNotFoundException {
        Integer currentUserId = auditAwareConfig.getCurrentUserId();
        if (currentUserId == null) {
            throw new AccessDeniedException("Not logged in");
        }

        User user = userRepository.findById(currentUserId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (userRepository.existsByEmailIgnoreCase(request.getNewEmail())) {
            throw new ValidationException("Email already in use");
        }

        AccountMetadata metadata = user.getMetadata();

        String verficationCode = emailComposerService.sendVerificationEmail( modelMapper.map(user, UserDTO.class));


        AccountMetadata accountMetadata = AccountMetadata.builder()
                .status(AccountStatus.PENDING)
                .verificationCodeCreatedAt(LocalDateTime.now())
                .verificationCode(verficationCode)
                .build();


        if (verficationCode == null){
            accountMetadata.setStatus(AccountStatus.ACTIVE);
            accountMetadata.setVerificationCode(null);
            accountMetadata.setVerificationCodeCreatedAt(null);
        }
        user.setMetadata(accountMetadata);

        user.setEmail(request.getNewEmail());


        emailComposerService.sendEmailChangeAlert(request);

        userRepository.save(user);
    }


    public boolean isAdmin() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth != null && auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
    }


}

