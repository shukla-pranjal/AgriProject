package com.farmflow.service.impl;

import com.farmflow.config.AuditAwareConfig;
import com.farmflow.config.security.CustomUserDetails;
import com.farmflow.dto.LoginRequest;
import com.farmflow.dto.LoginResponse;
import com.farmflow.dto.UserDTO;
import com.farmflow.entity.AccountStatus;
import com.farmflow.entity.User;
import com.farmflow.enums.Role;
import com.farmflow.exception.ValidationException;
import com.farmflow.repository.UserRepository;
import com.farmflow.service.AuthService;
import com.farmflow.service.JwtService;
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


    @Override
    public LoginResponse login(LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

        if (authentication.isAuthenticated()) {
            CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
            String token = jwtService.generateToken(customUserDetails.getUser());
            UserDTO userDto = modelMapper.map(customUserDetails.getUser(), UserDTO.class);

            return LoginResponse.builder()
                    .token(token)
                    .user(userDto)
                    .build();
        }
        return null;

    }
    @Override
    public UserDTO createUser(UserDTO userDTO) {
        validation.userValidate(userDTO);
        if (userRepository.existsByEmailIgnoreCase(userDTO.getEmail())) {
            throw new ValidationException("User with email " + userDTO.getEmail() + " already exists.");
        }
        User user = modelMapper.map(userDTO, User.class);
        AccountStatus status = AccountStatus.builder()
                .isActive(true)
                .verificationCode(UUID.randomUUID().toString())
                .build();
        user.setStatus(status);
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

    public boolean isAdmin() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth != null && auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
    }
}
