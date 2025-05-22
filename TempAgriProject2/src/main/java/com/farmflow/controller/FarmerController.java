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
@Slf4j
public class FarmerController implements FarmerEndpoint {

    private final FarmerService farmerService;
    private static final String CLASS_NAME = FarmerController.class.getSimpleName();

    @Override
    public ResponseEntity<?> getAll() {
        String methodName = "getAll";
        log.debug("{} : {} :: Started", CLASS_NAME, methodName);
        List<FarmerDTO> farmerList = farmerService.getAllFarmers();
        log.info("{} : {} :: Successfully retrieved {} farmers", CLASS_NAME, methodName, farmerList.size());
        return CommonUtil.createBuildResponse(farmerList, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> getById(Integer id) throws Exception {
        String methodName = "getById";
        log.debug("{} : {} :: Started with id: {}", CLASS_NAME, methodName, id);
        try {
            FarmerDTO farmer = farmerService.getFarmerById(id);
            log.info("{} : {} :: Successfully retrieved farmer for id: {}", CLASS_NAME, methodName, id);
            return CommonUtil.createBuildResponse(farmer, HttpStatus.OK);
        } catch (Exception e) {
            log.error("{} : {} :: Failed to retrieve farmer for id: {}, error: {}", CLASS_NAME, methodName, id, e.getMessage());
            throw e;
        }
    }

    @Override
    public ResponseEntity<?> create(FarmerDTO farmerDTO) throws Exception {
        String methodName = "create";
        log.debug("{} : {} :: Started with farmerDTO: {}", CLASS_NAME, methodName, farmerDTO);
        try {
            FarmerDTO createdFarmer = farmerService.createFarmer(farmerDTO);
            log.info("{} : {} :: Successfully created farmer", CLASS_NAME, methodName);
            return CommonUtil.createBuildResponse(createdFarmer, HttpStatus.CREATED);
        } catch (Exception e) {
            log.error("{} : {} :: Failed to create farmer, error: {}", CLASS_NAME, methodName, e.getMessage());
            throw e;
        }
    }

    @Override
    public ResponseEntity<?> update(Integer id, FarmerDTO farmerDTO) throws Exception {
        String methodName = "update";
        log.debug("{} : {} :: Started with id: {}, farmerDTO: {}", CLASS_NAME, methodName, id, farmerDTO);
        try {
            FarmerDTO updatedFarmer = farmerService.updateFarmer(id, farmerDTO);
            log.info("{} : {} :: Successfully updated farmer for id: {}", CLASS_NAME, methodName, id);
            return CommonUtil.createBuildResponse(updatedFarmer, HttpStatus.OK);
        } catch (Exception e) {
            log.error("{} : {} :: Failed to update farmer for id: {}, error: {}", CLASS_NAME, methodName, id, e.getMessage());
            throw e;
        }
    }

    @Override
    public ResponseEntity<?> delete(Integer id) throws Exception {
        String methodName = "delete";
        log.debug("{} : {} :: Started with id: {}", CLASS_NAME, methodName, id);
        try {
            farmerService.deleteFarmer(id);
            log.info("{} : {} :: Successfully deleted farmer for id: {}", CLASS_NAME, methodName, id);
            return CommonUtil.createBuildResponseMessage("Farmer deleted successfully", HttpStatus.OK);
        } catch (Exception e) {
            log.error("{} : {} :: Failed to delete farmer for id: {}, error: {}", CLASS_NAME, methodName, id, e.getMessage());
            throw e;
        }
    }

    @Override
    public ResponseEntity<?> isUserFarmer(Integer userId) throws Exception {
        String methodName = "isUserFarmer";
        log.debug("{} : {} :: Started with userId: {}", CLASS_NAME, methodName, userId);
        try {
            Boolean isUserFarmer = farmerService.isUserFarmer(userId);
            log.info("{} : {} :: Successfully checked if user is farmer for userId: {}, result: {}", CLASS_NAME, methodName, userId, isUserFarmer);
            return CommonUtil.createBuildResponse(isUserFarmer, HttpStatus.OK);
        } catch (Exception e) {
            log.error("{} : {} :: Failed to check if user is farmer for userId: {}, error: {}", CLASS_NAME, methodName, userId, e.getMessage());
            throw e;
        }
    }

    @Override
    public ResponseEntity<?> searchFarmers(String farmName, String farmType, String locationDiscription, String governmentId) {
        String methodName = "searchFarmers";
        log.debug("{} : {} :: Started with farmName: {}, farmType: {}, locationDiscription: {}, governmentId: {}", CLASS_NAME, methodName, farmName, farmType, locationDiscription, governmentId);
        List<FarmerDTO> farmerList = farmerService.searchFarmers(farmName, farmType, locationDiscription, governmentId);
        log.info("{} : {} :: Successfully retrieved {} farmers", CLASS_NAME, methodName, farmerList.size());
        return CommonUtil.createBuildResponse(farmerList, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> getAllFarmersPaged(PaginationRequest paginationRequest) {
        String methodName = "getAllFarmersPaged";
        log.debug("{} : {} :: Started with paginationRequest: {}", CLASS_NAME, methodName, paginationRequest);
        Page<FarmerDTO> page = farmerService.getAllFarmersPaged(paginationRequest);
        log.info("{} : {} :: Successfully retrieved paged farmers, page: {}, size: {}", CLASS_NAME, methodName, page.getNumber(), page.getSize());
        PaginatedResponse<FarmerDTO> paginatedResponse = PaginatedResponse.fromPage(page);
        return CommonUtil.createBuildResponse(paginatedResponse, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> searchFarmersPaged(PaginationRequest paginationRequest, String farmName, String farmType, String locationDiscription, String governmentId) {
        String methodName = "searchFarmersPaged";
        log.debug("{} : {} :: Started with paginationRequest: {}, farmName: {}, farmType: {}, locationDiscription: {}, governmentId: {}", CLASS_NAME, methodName, paginationRequest, farmName, farmType, locationDiscription, governmentId);
        Page<FarmerDTO> page = farmerService.searchFarmersPaged(paginationRequest, farmName, farmType, locationDiscription, governmentId);
        log.info("{} : {} :: Successfully retrieved paged farmers, page: {}, size: {}", CLASS_NAME, methodName, page.getNumber(), page.getSize());
        PaginatedResponse<FarmerDTO> paginatedResponse = PaginatedResponse.fromPage(page);
        return CommonUtil.createBuildResponse(paginatedResponse, HttpStatus.OK);
    }
}