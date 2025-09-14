package com.farmflow.controller;


import com.farmflow.dto.PaginatedResponse;
import com.farmflow.dto.PaginationRequest;
import com.farmflow.dto.UserDTO;
import com.farmflow.dto.UserResponse;
import com.farmflow.endpoint.UserEndpoint;
import com.farmflow.entity.User;
import com.farmflow.service.UserService;
import com.farmflow.util.CommonUtil;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;



@RestController
@RequiredArgsConstructor
public class UserController implements UserEndpoint {

    private final ModelMapper modelMapper;
    private final UserService userService;

    @Override
    public ResponseEntity<?> getAll() {
        List<UserDTO> userList = userService.getAllUsers();
        return CommonUtil.createBuildResponse(userList, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> getProfile() {

        User loggedInUser = CommonUtil.getLoggedInUser();
        if (loggedInUser == null) {
            return CommonUtil.createBuildResponse("No user is logged in", HttpStatus.UNAUTHORIZED);
        }

        UserResponse userResponse = modelMapper.map(loggedInUser, UserResponse.class);
        // Return the response
        return CommonUtil.createBuildResponse(userResponse, HttpStatus.OK);
    }


    @Override
    public ResponseEntity<?> getById(Integer id) throws Exception {
            UserDTO user = userService.getUserById(id);
            return CommonUtil.createBuildResponse(user, HttpStatus.OK);
    }



    @Override
    public ResponseEntity<?> update(Integer id, UserDTO userDTO) throws Exception {
            UserDTO updated = userService.updateUser(id, userDTO);
            return CommonUtil.createBuildResponse(updated, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> delete(Integer id) throws Exception {
            userService.deleteUser(id);
            return CommonUtil.createBuildResponseMessage("User deleted successfully", HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> searchUsers(String name, String email, String phone) {
        List<UserDTO> users = userService.searchUsers(name, email, phone);
        return CommonUtil.createBuildResponse(users, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> getAllPaged(PaginationRequest paginationRequest) {
        Page<UserDTO> pagedUsers = userService.getAllUsersPaged(paginationRequest);
        PaginatedResponse<UserDTO> response = PaginatedResponse.fromPage(pagedUsers);
        return CommonUtil.createBuildResponse(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> searchUsersPaged(PaginationRequest paginationRequest, String name, String email, String phone) {
        Page<UserDTO> pagedResults = userService.searchUsersPaged(paginationRequest, name, email, phone);
        PaginatedResponse<UserDTO> response = PaginatedResponse.fromPage(pagedResults);
        return CommonUtil.createBuildResponse(response, HttpStatus.OK);
    }


}