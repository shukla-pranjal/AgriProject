package com.agriproject.service.impl;

import com.agriproject.dto.CropDTO;
import com.agriproject.enitity.Crop;
import com.agriproject.exception.ResourceNotFoundException;
import com.agriproject.repository.CropRepository;
import com.agriproject.service.CropService;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CropServiceImpl implements CropService {

    private final CropRepository cropRepository;
    private final ModelMapper modelMapper;

    @Override
    public List<CropDTO> getAllCrops() {
        return cropRepository.findAll().stream()
                .map(crop -> modelMapper.map(crop, CropDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public CropDTO getCropById(Long id) throws Exception {
        Crop crop = cropRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Crop not found with ID: " + id));
        return modelMapper.map(crop, CropDTO.class);
    }

    @Override
    public CropDTO createCrop(CropDTO cropDTO) {
        Crop crop = modelMapper.map(cropDTO, Crop.class);
        Crop saved = cropRepository.save(crop);
        return modelMapper.map(saved, CropDTO.class);
    }

    @Override
    public CropDTO updateCrop(Long id, CropDTO cropDTO) throws Exception {
        Crop existing = cropRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Crop not found with ID: " + id));

        modelMapper.map(cropDTO, existing); // Updates fields in existing entity
        Crop updated = cropRepository.save(existing);
        return modelMapper.map(updated, CropDTO.class);
    }

    @Override
    public void deleteCrop(Long id) throws Exception {
        Crop existing = cropRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Crop not found with ID: " + id));
        cropRepository.delete(existing);
    }
}
