package com.tempagriproject2.endpoint;

import com.tempagriproject2.dto.CategoryDTO;
import com.tempagriproject2.dto.ProductDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/v1/products")
public interface ProductEndpoint {

    @GetMapping
    ResponseEntity<?> getAll();

    @GetMapping("/{id}")
    ResponseEntity<?> getById(@PathVariable Integer id) throws Exception;

    @PostMapping
    ResponseEntity<?> create(@RequestBody ProductDTO productDTO) throws Exception;

    @PutMapping("/{id}")
    ResponseEntity<?> update(@PathVariable Integer id, @RequestBody ProductDTO productDTO) throws Exception;

    @DeleteMapping("/{id}")
    ResponseEntity<?> delete(@PathVariable Integer id) throws Exception;

    @GetMapping("/category/{categoryId}")
    ResponseEntity<?> getByCategoryId(@PathVariable Integer categoryId) throws Exception;

    @GetMapping("/farmer/{farmerId}")
    ResponseEntity<?> getByFarmerId(@PathVariable Integer farmerId) throws Exception;
}

