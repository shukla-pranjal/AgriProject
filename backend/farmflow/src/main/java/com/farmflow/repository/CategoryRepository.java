package com.farmflow.repository;

import com.farmflow.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {
    boolean existsByNameIgnoreCase(String name);

    @Query("""
    SELECT c FROM Category c
    WHERE (:name IS NULL OR c.name LIKE %:name%)
      AND (:description IS NULL OR c.description LIKE %:description%)
    """)
    List<Category> searchCategories(String name, String description);

    @Query("""
    SELECT c FROM Category c
    WHERE (:name IS NULL OR c.name LIKE %:name%)
      AND (:description IS NULL OR c.description LIKE %:description%)
    """)
    Page<Category> searchCategoriesPaged(String name, String description,  Pageable pageable);

    // CategoryRepository pagination method

}
