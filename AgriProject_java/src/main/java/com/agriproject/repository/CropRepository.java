package com.agriproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.agriproject.enitity.Crop;

@Repository
public interface CropRepository extends JpaRepository<Crop, Long> {

}
