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

    @Override
    @Cacheable(value = "userCache", key = "'all'")
    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(user -> modelMapper.map(user, UserDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    @Cacheable(value = "userCache", key = "#id")
    public UserDTO getUserById(Integer id) throws Exception {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(Constants.RESOURCE_NOT_FOUND, Constants.USER, id)));
        authService.validateUser(id);

        return modelMapper.map(user, UserDTO.class);
    }

    @Override
    @CacheEvict(value = "userCache", key = "#id")
    public void deleteUser(Integer id) throws Exception {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(Constants.RESOURCE_NOT_FOUND, Constants.USER, id)));
        authService.validateUser(id);
        emailComposerService.sendAccountDeletionNotification(existingUser);
        userRepository.delete(existingUser);
    }

    @Override
    @CacheEvict(value = "userCache", key = "#id")
    @CachePut(value = "userCache", key = "#result.id")
    public UserDTO updateUser(Integer id, UserDTO userDTO) throws Exception {
        validation.userValidate(userDTO);
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(Constants.RESOURCE_NOT_FOUND, Constants.USER, id)));
        authService.validateUser(id);

        if (!existingUser.getEmail().equalsIgnoreCase(userDTO.getEmail()) &&
                userRepository.existsByEmailIgnoreCase(userDTO.getEmail())) {
            throw new DuplicateResourceException(String.format(Constants.EMAIL_IN_USE, userDTO.getEmail()));
        }
        modelMapper.map(userDTO, existingUser);  // update fields
        User updatedUser = userRepository.save(existingUser);
        return modelMapper.map(updatedUser, UserDTO.class);
    }

    @Override
    public List<UserDTO> searchUsers(String name, String email, String phone) {
        List<User> users = userRepository.searchUsers(name, email, phone);
        return users.stream()
                .map(user -> modelMapper.map(user, UserDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public Page<UserDTO> getAllUsersPaged(PaginationRequest paginationRequest) {
        Pageable pageable = paginationRequest.toPageable();
        Page<User> page = userRepository.findAll(pageable);
        return page.map(user -> modelMapper.map(user, UserDTO.class));
    }

    @Override
    public Page<UserDTO> searchUsersPaged(PaginationRequest paginationRequest, String name, String email, String phone) {
        Pageable pageable = paginationRequest.toPageable();
        Page<User> page = userRepository.searchUsersPaged(name, email, phone, pageable);
        return page.map(user -> modelMapper.map(user, UserDTO.class));
    }

    @Override
    @CacheEvict(value = "userCache", key = "#userId")
    @CachePut(value = "userCache", key = "#result.id")
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
    @CacheEvict(value = "userCache", key = "#userId")
    @CachePut(value = "userCache", key = "#result.id")
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