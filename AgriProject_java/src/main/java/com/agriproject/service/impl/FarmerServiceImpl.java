package com.agriproject.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.agriproject.dto.FarmerDTO;
import com.agriproject.service.FarmerService;

@Service
public class FarmerServiceImpl implements FarmerService {

    @Override
    public List<FarmerDTO> getAllFarmers() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAllFarmers'");
    }

    @Override
    public FarmerDTO getFarmerById(Long id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getFarmerById'");
    }

    @Override
    public FarmerDTO createFarmer(FarmerDTO farmerDTO) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'createFarmer'");
    }

    @Override
    public FarmerDTO updateFarmer(Long id, FarmerDTO farmerDTO) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateFarmer'");
    }

    @Override
    public void deleteFarmer(Long id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteFarmer'");
    }

    @Override
    public List<FarmerDTO> getFarmersByLocation(String city) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getFarmersByLocation'");
    }
    
}