package com.agriproject.service;

import java.util.List;

import com.agriproject.dto.UserDTO;

public interface UserService {

    List<UserDTO> getAllUsers();

    UserDTO getUserById(Long id)throws  Exception;

    UserDTO createUser(UserDTO userDTO);

    UserDTO updateUser(Long id, UserDTO userDTO)throws  Exception;

    void deleteUser(Long id)throws  Exception;

}
