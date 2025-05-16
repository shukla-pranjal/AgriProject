package com.tempagriproject2.endpoint;

import com.tempagriproject2.dto.AddressDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/v1/addresses")
public interface AddressEndpoint {

    @GetMapping
    ResponseEntity<?> getAll();

    @GetMapping("/{id}")
    ResponseEntity<?> getById(@PathVariable Integer id) throws Exception;

    @PostMapping
    ResponseEntity<?> create(@RequestBody AddressDTO addressDTO) throws Exception;

    @PutMapping("/{id}")
    ResponseEntity<?> update(@PathVariable Integer id, @RequestBody AddressDTO addressDTO) throws Exception;

    @DeleteMapping("/{id}")
    ResponseEntity<?> delete(@PathVariable Integer id) throws Exception;

    @GetMapping("/user/{userId}")
    ResponseEntity<?> getByUserId(@PathVariable Integer userId)throws Exception;

}