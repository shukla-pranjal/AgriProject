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
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;



@RestController
@RequiredArgsConstructor
@Slf4j
public class UserController implements UserEndpoint {

    private final ModelMapper modelMapper;
    private final UserService userService;
    private static final String CLASS_NAME = UserController.class.getSimpleName();

    @Override
    public ResponseEntity<?> getAll() {
        String methodName = "getAll";
        log.debug("{} : {} :: Started", CLASS_NAME, methodName);
        List<UserDTO> userList = userService.getAllUsers();
        log.info("{} : {} :: Successfully retrieved {} users", CLASS_NAME, methodName, userList.size());
        return CommonUtil.createBuildResponse(userList, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> getProfile() {
        String methodName = "getProfile";
        log.debug("{} : {} :: Started", CLASS_NAME, methodName);

        User loggedInUser = CommonUtil.getLoggedInUser();
        if (loggedInUser == null) {
            log.warn("{} : {} :: No logged-in user found", CLASS_NAME, methodName);
            return CommonUtil.createBuildResponse("No user is logged in", HttpStatus.UNAUTHORIZED);
        }

        UserResponse userResponse = modelMapper.map(loggedInUser, UserResponse.class);
        log.info("{} : {} :: Successfully retrieved profile for user: {}", CLASS_NAME, methodName, loggedInUser.getEmail());
        // Return the response
        return CommonUtil.createBuildResponse(userResponse, HttpStatus.OK);
    }


    @Override
    public ResponseEntity<?> getById(Integer id) throws Exception {
        String methodName = "getById";
        log.debug("{} : {} :: Started with id: {}", CLASS_NAME, methodName, id);
        try {
            UserDTO user = userService.getUserById(id);
            log.info("{} : {} :: Successfully retrieved user for id: {}", CLASS_NAME, methodName, id);
            return CommonUtil.createBuildResponse(user, HttpStatus.OK);
        } catch (Exception e) {
            log.error("{} : {} :: Failed to retrieve user for id: {}, error: {}", CLASS_NAME, methodName, id, e.getMessage());
            throw e;
        }
    }



    @Override
    public ResponseEntity<?> update(Integer id, UserDTO userDTO) throws Exception {
        String methodName = "update";
        log.debug("{} : {} :: Started with id: {}, userDTO: {}", CLASS_NAME, methodName, id, userDTO);
        try {
            UserDTO updated = userService.updateUser(id, userDTO);
            log.info("{} : {} :: Successfully updated user for id: {}", CLASS_NAME, methodName, id);
            return CommonUtil.createBuildResponse(updated, HttpStatus.OK);
        } catch (Exception e) {
            log.error("{} : {} :: Failed to update user for id: {}, error: {}", CLASS_NAME, methodName, id, e.getMessage());
            throw e;
        }
    }

    @Override
    public ResponseEntity<?> delete(Integer id) throws Exception {
        String methodName = "delete";
        log.debug("{} : {} :: Started with id: {}", CLASS_NAME, methodName, id);
        try {
            userService.deleteUser(id);
            log.info("{} : {} :: Successfully deleted user for id: {}", CLASS_NAME, methodName, id);
            return CommonUtil.createBuildResponseMessage("User deleted successfully", HttpStatus.OK);
        } catch (Exception e) {
            log.error("{} : {} :: Failed to delete user for id: {}, error: {}", CLASS_NAME, methodName, id, e.getMessage());
            throw e;
        }
    }

    @Override
    public ResponseEntity<?> searchUsers(String name, String email, String phone) {
        String methodName = "searchUsers";
        log.debug("{} : {} :: Started with name: {}, email: {}, phone: {}", CLASS_NAME, methodName, name, email, phone);
        List<UserDTO> users = userService.searchUsers(name, email, phone);
        log.info("{} : {} :: Successfully retrieved {} users", CLASS_NAME, methodName, users.size());
        return CommonUtil.createBuildResponse(users, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> getAllPaged(PaginationRequest paginationRequest) {
        String methodName = "getAllPaged";
        log.debug("{} : {} :: Started with paginationRequest: {}", CLASS_NAME, methodName, paginationRequest);
        Page<UserDTO> pagedUsers = userService.getAllUsersPaged(paginationRequest);
        log.info("{} : {} :: Successfully retrieved paged users, page: {}, size: {}", CLASS_NAME, methodName, pagedUsers.getNumber(), pagedUsers.getSize());
        PaginatedResponse<UserDTO> response = PaginatedResponse.fromPage(pagedUsers);
        return CommonUtil.createBuildResponse(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> searchUsersPaged(PaginationRequest paginationRequest, String name, String email, String phone) {
        String methodName = "searchUsersPaged";
        log.debug("{} : {} :: Started with paginationRequest: {}, name: {}, email: {}, phone: {}", CLASS_NAME, methodName, paginationRequest, name, email, phone);
        Page<UserDTO> pagedResults = userService.searchUsersPaged(paginationRequest, name, email, phone);
        log.info("{} : {} :: Successfully retrieved paged users, page: {}, size: {}", CLASS_NAME, methodName, pagedResults.getNumber(), pagedResults.getSize());
        PaginatedResponse<UserDTO> response = PaginatedResponse.fromPage(pagedResults);
        return CommonUtil.createBuildResponse(response, HttpStatus.OK);
    }


}