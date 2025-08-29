package com.farmflow.service.impl;

import com.farmflow.dto.PaginationRequest;
import com.farmflow.dto.UserDTO;
import com.farmflow.entity.User;
import com.farmflow.enums.Role;
import com.farmflow.exception.DuplicateResourceException;
import com.farmflow.exception.ResourceNotFoundException;
import com.farmflow.exception.ValidationException;
import com.farmflow.repository.UserRepository;
import com.farmflow.service.AuthService;
import com.farmflow.service.UserService;
import com.farmflow.service.email.EmailComposerService;
import com.farmflow.util.Constants;
import com.farmflow.util.Validation;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor; // Added for consistency with @RequiredArgsConstructor
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final Validation validation;
    private final AuthService authService;
    private final EmailComposerService emailComposerService;

    // ====================== GET USERS ======================
    @Override
    @Cacheable(value = "userCacheAll", key = "'all'")
    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(user -> modelMapper.map(user, UserDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    @Cacheable(value = "userCacheById", key = "#id")
    public UserDTO getUserById(Integer id) throws Exception {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(Constants.RESOURCE_NOT_FOUND, Constants.USER, id)));
        authService.validateUser(id);

        return modelMapper.map(user, UserDTO.class);
    }

    // ====================== DELETE USER ======================
    @Override
    @Caching(evict = {
            @CacheEvict(value = "userCacheById", key = "#id"),
            @CacheEvict(value = "userCacheAll", allEntries = true)
    })
    public void deleteUser(Integer id) throws Exception {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(Constants.RESOURCE_NOT_FOUND, Constants.USER, id)));

        authService.validateUser(id);
        emailComposerService.sendAccountDeletionNotification(existingUser);
        userRepository.delete(existingUser);
    }

    // ====================== UPDATE USER ======================
    @Override
    @Caching(
            evict = { @CacheEvict(value = "userCacheAll", allEntries = true) },
            put = { @CachePut(value = "userCacheById", key = "#result.id") }
    )
    public UserDTO updateUser(Integer id, UserDTO userDTO) throws Exception {
        validation.userValidate(userDTO);

        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(Constants.RESOURCE_NOT_FOUND, Constants.USER, id)));

        authService.validateUser(id);

        if (!existingUser.getEmail().equalsIgnoreCase(userDTO.getEmail()) &&
                userRepository.existsByEmailIgnoreCase(userDTO.getEmail())) {
            throw new DuplicateResourceException(String.format(Constants.EMAIL_IN_USE, userDTO.getEmail()));
        }

        userDTO.setEmail(existingUser.getEmail());
        userDTO.setPassword(existingUser.getPassword());

        modelMapper.map(userDTO, existingUser);
        User updatedUser = userRepository.save(existingUser);

        return modelMapper.map(updatedUser, UserDTO.class);
    }

    // ====================== SEARCH USERS ======================
    @Override
    public List<UserDTO> searchUsers(String name, String email, String phone) {
        return userRepository.searchUsers(name, email, phone).stream()
                .map(user -> modelMapper.map(user, UserDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public Page<UserDTO> getAllUsersPaged(PaginationRequest paginationRequest) {
        Pageable pageable = paginationRequest.toPageable();
        return userRepository.findAll(pageable)
                .map(user -> modelMapper.map(user, UserDTO.class));
    }

    @Override
    public Page<UserDTO> searchUsersPaged(PaginationRequest paginationRequest, String name, String email, String phone) {
        Pageable pageable = paginationRequest.toPageable();
        return userRepository.searchUsersPaged(name, email, phone, pageable)
                .map(user -> modelMapper.map(user, UserDTO.class));
    }

    // ====================== PROMOTE/DEMOTE ======================
    @Override
    @Caching(
            evict = { @CacheEvict(value = "userCacheAll", allEntries = true) },
            put = { @CachePut(value = "userCacheById", key = "#userId") }
    )
    public void promote(Integer userId) throws Exception {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(Constants.RESOURCE_NOT_FOUND, Constants.USER, userId)));

        if (user.getRoles().contains(Role.ROLE_ADMIN)) {
            throw new ValidationException(Constants.ALREADY_ADMIN);
        }

        user.getRoles().add(Role.ROLE_ADMIN);
        emailComposerService.sendRolePromoted(user);
        userRepository.save(user);
    }

    @Override
    @Caching(
            evict = { @CacheEvict(value = "userCacheAll", allEntries = true) },
            put = { @CachePut(value = "userCacheById", key = "#userId") }
    )
    public void demote(Integer userId) throws Exception {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(Constants.RESOURCE_NOT_FOUND, Constants.USER, userId)));

        if (!user.getRoles().contains(Role.ROLE_ADMIN)) {
            throw new ValidationException(Constants.NOT_ADMIN);
        }

        user.getRoles().remove(Role.ROLE_ADMIN);
        emailComposerService.sendRoleDemoted(user);
        userRepository.save(user);
    }
}
