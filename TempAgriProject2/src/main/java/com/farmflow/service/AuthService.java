package com.farmflow.service;

import com.farmflow.dto.LoginRequest;
import com.farmflow.dto.LoginResponse;
import com.farmflow.dto.UserDTO;

public interface AuthService {
    LoginResponse login(LoginRequest loginRequest);
    UserDTO createUser(UserDTO userDTO);
    boolean isOwnerOrAdmin(Integer resourceOwnerId);
    void validateUser(Integer id);
}
