package com.tempagriproject2.service;

import com.tempagriproject2.dto.FarmerDTO;

import java.util.List;

public interface FarmerService {

    List<FarmerDTO> getAllFarmers();

    FarmerDTO getFarmerById(Integer id) throws Exception;

    FarmerDTO createFarmer(FarmerDTO farmerDTO)  throws Exception;

    FarmerDTO updateFarmer(Integer id, FarmerDTO farmerDTO) throws Exception;

    void deleteFarmer(Integer id) throws Exception;

    boolean isUserFarmer(Integer userId) throws Exception ;
}
