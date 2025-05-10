package com.tempagriproject2.service;

import com.tempagriproject2.dto.UserDTO;

import java.util.List;

public interface UserService {

    List<UserDTO> getAllUsers();

    UserDTO getUserById(Integer id)throws  Exception;

    UserDTO createUser(UserDTO userDTO);

    UserDTO updateUser(Integer id, UserDTO userDTO)throws  Exception;

    void deleteUser(Integer id)throws  Exception;

}
