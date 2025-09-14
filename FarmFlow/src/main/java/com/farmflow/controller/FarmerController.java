package com.farmflow.controller;

import com.farmflow.dto.FarmerDTO;
import com.farmflow.dto.PaginatedResponse;
import com.farmflow.dto.PaginationRequest;
import com.farmflow.endpoint.FarmerEndpoint;
import com.farmflow.service.FarmerService;
import com.farmflow.util.CommonUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class FarmerController implements FarmerEndpoint {

    private final FarmerService farmerService;

    @Override
    public ResponseEntity<?> getAll() {
        List<FarmerDTO> farmerList = farmerService.getAllFarmers();
        return CommonUtil.createBuildResponse(farmerList, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> getById(Integer id) throws Exception {
        FarmerDTO farmer = farmerService.getFarmerById(id);
        return CommonUtil.createBuildResponse(farmer, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> create(FarmerDTO farmerDTO) throws Exception {
        FarmerDTO createdFarmer = farmerService.createFarmer(farmerDTO);
        return CommonUtil.createBuildResponse(createdFarmer, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<?> update(Integer id, FarmerDTO farmerDTO) throws Exception {
        FarmerDTO updatedFarmer = farmerService.updateFarmer(id, farmerDTO);
        return CommonUtil.createBuildResponse(updatedFarmer, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> delete(Integer id) throws Exception {
        farmerService.deleteFarmer(id);
        return CommonUtil.createBuildResponseMessage("Farmer deleted successfully", HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> isUserFarmer(Integer userId) throws Exception {
        Boolean isUserFarmer = farmerService.isUserFarmer(userId);
        return CommonUtil.createBuildResponse(isUserFarmer, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> searchFarmers(String farmName, String farmType, String locationDiscription, String governmentId) {
        List<FarmerDTO> farmerList = farmerService.searchFarmers(farmName, farmType, locationDiscription, governmentId);
        return CommonUtil.createBuildResponse(farmerList, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> getAllFarmersPaged(PaginationRequest paginationRequest) {
        Page<FarmerDTO> page = farmerService.getAllFarmersPaged(paginationRequest);
        PaginatedResponse<FarmerDTO> paginatedResponse = PaginatedResponse.fromPage(page);
        return CommonUtil.createBuildResponse(paginatedResponse, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> searchFarmersPaged(PaginationRequest paginationRequest, String farmName, String farmType, String locationDiscription, String governmentId) {
        Page<FarmerDTO> page = farmerService.searchFarmersPaged(paginationRequest, farmName, farmType, locationDiscription, governmentId);
        PaginatedResponse<FarmerDTO> paginatedResponse = PaginatedResponse.fromPage(page);
        return CommonUtil.createBuildResponse(paginatedResponse, HttpStatus.OK);
    }
}