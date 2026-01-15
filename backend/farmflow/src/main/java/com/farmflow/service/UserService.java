package com.farmflow.service;

import com.farmflow.dto.PaginationRequest;
import com.farmflow.dto.UserDTO;
import org.springframework.data.domain.Page;

import java.util.List;

public interface UserService {

    List<UserDTO> getAllUsers();

    UserDTO getUserById(Integer id)throws  Exception;

    UserDTO updateUser(Integer id, UserDTO userDTO)throws  Exception;

    void deleteUser(Integer id)throws  Exception;

    List<UserDTO> searchUsers(String name, String email, String phone);

    Page<UserDTO> getAllUsersPaged(PaginationRequest paginationRequest);

    Page<UserDTO> searchUsersPaged(PaginationRequest paginationRequest, String name, String email, String phone);

    void promote(Integer userId)throws Exception;

    void demote(Integer userId)throws Exception;
}
