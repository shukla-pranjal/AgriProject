package com.agriproject.controller;

import com.agriproject.dto.CropDTO;
import com.agriproject.service.CropService;
import com.agriproject.util.CommonUtil;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/crops")
@RequiredArgsConstructor
public class CropController {

    private final CropService cropService;

    @GetMapping
    public ResponseEntity<?> getAll() {
        List<CropDTO> crops = cropService.getAllCrops();
        return CommonUtil.createBuildResponse(crops, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) throws Exception {
        CropDTO crop = cropService.getCropById(id);
        return CommonUtil.createBuildResponse(crop, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody CropDTO cropDTO) {
        CropDTO created = cropService.createCrop(cropDTO);
        return CommonUtil.createBuildResponse(created, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody CropDTO cropDTO) throws Exception {
        CropDTO updated = cropService.updateCrop(id, cropDTO);
        return CommonUtil.createBuildResponse(updated, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) throws Exception {
        cropService.deleteCrop(id);
        return CommonUtil.createBuildResponseMessage("Crop deleted successfully", HttpStatus.OK);
    }
}
