/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.agriproject.service;

import java.util.List;

import com.agriproject.dto.FarmerDTO;

public interface  FarmerService {

    List<FarmerDTO> getAllFarmers();

    FarmerDTO getFarmerById(Long id);

    FarmerDTO createFarmer(FarmerDTO farmerDTO);

    FarmerDTO updateFarmer(Long id, FarmerDTO farmerDTO);

    void deleteFarmer(Long id);

    List<FarmerDTO> getFarmersByLocation(String city);

}
