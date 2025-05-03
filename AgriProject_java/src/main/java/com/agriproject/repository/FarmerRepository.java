package com.agriproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.agriproject.enitity.Farmer;

@Repository
public interface FarmerRepository extends JpaRepository<Farmer, Long> {

}
