package com.farmflow.service;

import com.farmflow.dto.FarmerDTO;
import com.farmflow.dto.PaginationRequest;
import org.springframework.data.domain.Page;

import java.util.List;

public interface FarmerService {

    List<FarmerDTO> getAllFarmers();

    FarmerDTO getFarmerById(Integer id) throws Exception;

    FarmerDTO createFarmer(FarmerDTO farmerDTO)  throws Exception;

    FarmerDTO updateFarmer(Integer id, FarmerDTO farmerDTO) throws Exception;

    void deleteFarmer(Integer id) throws Exception;

    boolean isUserFarmer(Integer userId) throws Exception ;

    List<FarmerDTO> searchFarmers(String farmName, String farmType, String locationDiscription, String governmentId);

    Page<FarmerDTO> searchFarmersPaged(PaginationRequest paginationRequest, String farmName, String farmType, String locationDiscription, String governmentId);

    Page<FarmerDTO> getAllFarmersPaged(PaginationRequest paginationRequest);
}
