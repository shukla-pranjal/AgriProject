package com.tempagriproject2.endpoint;

import com.tempagriproject2.dto.CategoryDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/v1/categories")
public interface CategoryEndpoint {

    @GetMapping
    ResponseEntity<?> getAll();

    @GetMapping("/{id}")
    ResponseEntity<?> getById(@PathVariable Integer id) throws Exception;

    @PostMapping
    ResponseEntity<?> create(@RequestBody CategoryDTO categoryDTO) throws Exception;

    @PutMapping("/{id}")
    ResponseEntity<?> update(@PathVariable Integer id, @RequestBody CategoryDTO categoryDTO) throws Exception;

    @DeleteMapping("/{id}")
    ResponseEntity<?> delete(@PathVariable Integer id) throws Exception;
}


