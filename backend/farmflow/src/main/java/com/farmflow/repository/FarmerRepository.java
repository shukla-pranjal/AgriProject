package com.farmflow.repository;

import com.farmflow.entity.Farmer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FarmerRepository extends JpaRepository<Farmer, Integer>,  FarmerCustomRepository{

//    Optional<Farmer> findByUser(User user);
    Optional<Farmer> findByUserId(Integer userId);

    @Query("SELECT f FROM Farmer f WHERE " +
            "(f.farmName LIKE %:farmName% OR :farmName IS NULL) AND " +
            "(f.farmType LIKE %:farmType% OR :farmType IS NULL) AND " +
            "(f.locationDiscription LIKE %:locationDiscription% OR :locationDiscription IS NULL) AND " +
            "(f.governmentId LIKE %:governmentId% OR :governmentId IS NULL)")
    Page<Farmer> searchFarmersPaged(String farmName, String farmType, String locationDiscription, String governmentId, Pageable pageable);

}
