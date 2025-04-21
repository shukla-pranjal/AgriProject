package com.agriproject.controller;

import com.agriproject.dto.UserDTO;
import com.agriproject.service.UserService;
import com.agriproject.util.CommonUtil;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    
    private final UserService userService;

    @GetMapping
    public ResponseEntity<?> getAll() {
        List<UserDTO> userList = userService.getAllUsers();
        return CommonUtil.createBuildResponse(userList, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) throws  Exception{
        UserDTO user = userService.getUserById(id);
        return CommonUtil.createBuildResponse(user, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody UserDTO userDTO) {
        UserDTO created = userService.createUser(userDTO);
        return CommonUtil.createBuildResponse(created, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody UserDTO userDTO)throws  Exception {
        UserDTO updated = userService.updateUser(id, userDTO);
        return CommonUtil.createBuildResponse(updated, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) throws  Exception{
        userService.deleteUser(id);
        return CommonUtil.createBuildResponseMessage("User deleted successfully", HttpStatus.OK);
    }
}
