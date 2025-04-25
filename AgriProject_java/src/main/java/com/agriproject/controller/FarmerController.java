package com.agriproject.controller;

import com.agriproject.dto.FarmerDTO;
import com.agriproject.service.FarmerService;
import com.agriproject.util.CommonUtil;

import lombok.AllArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/farmers")
public class FarmerController {
    
    private final FarmerService farmerService;

    @GetMapping
    public ResponseEntity<?> getAll() {
        List<FarmerDTO> farmers = farmerService.getAllFarmers();
        return CommonUtil.createBuildResponse(farmers, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        FarmerDTO farmer = farmerService.getFarmerById(id);
        return CommonUtil.createBuildResponse(farmer, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody FarmerDTO farmerDTO) {
        FarmerDTO created = farmerService.createFarmer(farmerDTO);
        return CommonUtil.createBuildResponse(created, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody FarmerDTO farmerDTO) {
        FarmerDTO updated = farmerService.updateFarmer(id, farmerDTO);
        return CommonUtil.createBuildResponse(updated, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        farmerService.deleteFarmer(id);
        return CommonUtil.createBuildResponseMessage("Farmer deleted successfully", HttpStatus.OK);
    }
    

    @GetMapping("/city")
    public ResponseEntity<?> getFarmersByLocation(@RequestParam String city){
        List<FarmerDTO> farmers = farmerService.getFarmersByLocation(city);
        return CommonUtil.createBuildResponse(farmers, HttpStatus.OK);
    }


}
