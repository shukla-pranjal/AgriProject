package com.tempagriproject2.endpoint;

import com.tempagriproject2.dto.FarmerDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/v1/farmers")
public interface FarmerEndpoint {

    @GetMapping
    ResponseEntity<?> getAll();

    @GetMapping("/{id}")
    ResponseEntity<?> getById(@PathVariable Integer id) throws Exception;

    @PostMapping
    ResponseEntity<?> create(@RequestBody FarmerDTO farmerDTO)  throws Exception ;

    @PutMapping("/{id}")
    ResponseEntity<?> update(@PathVariable Integer id, @RequestBody FarmerDTO farmerDTO) throws Exception;

    @DeleteMapping("/{id}")
    ResponseEntity<?> delete(@PathVariable Integer id) throws Exception;

    @GetMapping("/isFarmer/{userId}")
    ResponseEntity<?> isUserFarmer(@PathVariable Integer userId) throws Exception ;
}
