package com.tempagriproject2.controller;


import com.tempagriproject2.dto.UserDTO;
import com.tempagriproject2.endpoint.UserEndpoint;
import com.tempagriproject2.service.UserService;
import com.tempagriproject2.util.CommonUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;




@RestController
@RequiredArgsConstructor
public class UserController implements UserEndpoint {

    private final UserService userService;

    @Override
    public ResponseEntity<?> getAll() {
        List<UserDTO> userList = userService.getAllUsers();
        return CommonUtil.createBuildResponse(userList, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> getById(Integer id) throws Exception {
        UserDTO user = userService.getUserById(id);
        return CommonUtil.createBuildResponse(user, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> create(UserDTO userDTO) {
        UserDTO created = userService.createUser(userDTO);
        return CommonUtil.createBuildResponse(created, HttpStatus.CREATED);
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


}
