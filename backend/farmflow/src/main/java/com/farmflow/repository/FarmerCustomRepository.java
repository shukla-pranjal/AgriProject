package com.farmflow.repository;

import com.farmflow.entity.Farmer;

import java.util.List;

public interface FarmerCustomRepository {
    List<Farmer> searchFarmers( String farmName, String farmType, String locationDiscription, String governmentId);

}
