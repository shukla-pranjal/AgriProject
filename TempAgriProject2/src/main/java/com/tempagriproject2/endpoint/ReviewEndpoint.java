package com.tempagriproject2.endpoint;

import com.tempagriproject2.dto.ReviewDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/v1/reviews")
public interface ReviewEndpoint {

    @GetMapping
    ResponseEntity<?> getAll();

    @GetMapping("/{id}")
    ResponseEntity<?> getById(@PathVariable Integer id) throws Exception;

    @PostMapping
    ResponseEntity<?> create(@RequestBody ReviewDTO reviewDTO) throws Exception;

    @PutMapping("/{id}")
    ResponseEntity<?> update(@PathVariable Integer id, @RequestBody ReviewDTO reviewDTO) throws Exception;

    @DeleteMapping("/{id}")
    ResponseEntity<?> delete(@PathVariable Integer id) throws Exception;

    @GetMapping("/product/{productId}")
    ResponseEntity<?> getByProductId(@PathVariable Integer productId);
}
