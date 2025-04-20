package com.agriproject.service;

import com.agriproject.dto.CropDTO;

import java.util.List;

public interface CropService {
    List<CropDTO> getAllCrops();

    CropDTO getCropById(Long id) throws Exception;

    CropDTO createCrop(CropDTO cropDTO);

    CropDTO updateCrop(Long id, CropDTO cropDTO) throws Exception;

    void deleteCrop(Long id) throws Exception;
}
