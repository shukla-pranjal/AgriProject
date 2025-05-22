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
import com.farmflow.util.Validation;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private Validation validation;

    @Autowired
    private AuthService authService;

    @Override
    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(user -> modelMapper.map(user, UserDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public UserDTO getUserById(Integer id) throws  Exception{
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + id));
        authService.validateUser(id);

        return modelMapper.map(user, UserDTO.class);
    }

    @Override
    public void deleteUser(Integer id) throws  Exception {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + id));
        authService.validateUser(id);
        userRepository.delete(existingUser);
    }

    @Override
    public UserDTO updateUser(Integer id, UserDTO userDTO) throws  Exception {
        validation.userValidate(userDTO);
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + id));
        authService.validateUser(id);

        if (!existingUser.getEmail().equalsIgnoreCase(userDTO.getEmail()) &&
                userRepository.existsByEmailIgnoreCase(userDTO.getEmail())) {
            throw new DuplicateResourceException("Email " + userDTO.getEmail() + " is already in use.");
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
    public void promote(Integer userId) throws Exception {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));

        if (user.getRoles().contains(Role.ROLE_ADMIN)) {
            throw new ValidationException("User is already an admin.");
        }

        user.getRoles().add(Role.ROLE_ADMIN);
        userRepository.save(user);
    }

    @Override
    public void demote(Integer userId) throws Exception {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));

        if (!user.getRoles().contains(Role.ROLE_ADMIN)) {
            throw new ValidationException("User is not an admin.");
        }

        user.getRoles().remove(Role.ROLE_ADMIN);
        userRepository.save(user);
    }

}
