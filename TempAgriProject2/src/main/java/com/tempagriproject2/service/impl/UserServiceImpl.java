package com.tempagriproject2.service.impl;

import com.tempagriproject2.dto.UserDTO;
import com.tempagriproject2.entity.User;
import com.tempagriproject2.exception.ResourceNotFoundException;
import com.tempagriproject2.repository.UserRepository;
import com.tempagriproject2.service.UserService;
import com.tempagriproject2.util.Validation;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
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
        return modelMapper.map(user, UserDTO.class);
    }

    @Override
    public void deleteUser(Integer id) throws  Exception {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + id));
        userRepository.delete(existingUser);
    }

    @Override
    public UserDTO createUser(UserDTO userDTO) {
        validation.userValidate(userDTO);
        User user = modelMapper.map(userDTO, User.class);
        User savedUser = userRepository.save(user);
        return modelMapper.map(savedUser, UserDTO.class);
    }

    @Override
    public UserDTO updateUser(Integer id, UserDTO userDTO) throws  Exception {
        validation.userValidate(userDTO);
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + id));

        modelMapper.map(userDTO, existingUser);  // update fields
        User updatedUser = userRepository.save(existingUser);
        return modelMapper.map(updatedUser, UserDTO.class);
    }
}
