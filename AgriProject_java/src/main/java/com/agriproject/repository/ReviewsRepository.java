package com.agriproject.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.agriproject.enitity.Product;
import com.agriproject.enitity.Reviews;

@Repository
public interface ReviewsRepository extends JpaRepository<Reviews, Long> {
    List<Reviews> findByProduct(Product product); 

}
