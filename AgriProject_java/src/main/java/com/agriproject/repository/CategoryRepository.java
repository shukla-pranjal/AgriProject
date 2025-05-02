package com.agriproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.agriproject.enitity.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

}
