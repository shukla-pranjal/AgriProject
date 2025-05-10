package com.tempagriproject2.controller;

import com.tempagriproject2.dto.FarmerDTO;
import com.tempagriproject2.endpoint.FarmerEndpoint;
import com.tempagriproject2.service.FarmerService;
import com.tempagriproject2.util.CommonUtil;
import lombok.RequiredArgsConstructor;
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
    public ResponseEntity<?> create(FarmerDTO farmerDTO)  throws Exception  {
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
    public ResponseEntity<?> isUserFarmer(Integer userId) throws Exception  {
        Boolean isUserFarmer = farmerService.isUserFarmer(userId);

        return CommonUtil.createBuildResponse(isUserFarmer, HttpStatus.OK);
    }


}
