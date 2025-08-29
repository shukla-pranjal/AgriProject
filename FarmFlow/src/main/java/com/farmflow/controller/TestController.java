package com.farmflow.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/test")
    public ResponseEntity<?> getAll() {

        return  ResponseEntity.status(HttpStatus.OK).body(new java.util.HashMap<String, String>() {{
            put("service", "Farm flow");
            put("status", "running");
        }});
    }
}
