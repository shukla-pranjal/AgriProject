package com.farmflow.repository;

import com.farmflow.entity.Product;
import com.farmflow.enums.Unit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository <Product, Integer> {
    List<Product> findByQuantityGreaterThanAndAvailableTrue(Double quantity);
    List<Product> findByCategoryId(Integer categoryId);

    List<Product> findByFarmerId(Integer farmerId);

    @Query("""
    SELECT p FROM Product p
    WHERE (:name IS NULL OR LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%')))
      AND (:categoryId IS NULL OR p.category.id = :categoryId)
      AND (:farmerId IS NULL OR p.farmer.id = :farmerId)
      AND (:available IS NULL OR p.available = :available)
      AND (:unit IS NULL OR p.unit = :unit)
      AND (:priceMin IS NULL OR p.price >= :priceMin)
      AND (:priceMax IS NULL OR p.price <= :priceMax)
""")
    List<Product> searchProducts(
            @Param("name") String name,
            @Param("categoryId") Integer categoryId,
            @Param("farmerId") Integer farmerId,
            @Param("available") Boolean available,
            @Param("unit") Unit unit,
            @Param("priceMin") Double priceMin,
            @Param("priceMax") Double priceMax
    );

    Page<Product> findByFarmerId(Integer farmerId, Pageable pageable);
    Page<Product> findByCategoryId(Integer categoryId, Pageable pageable);
    Page<Product> findByQuantityGreaterThanAndAvailableTrue(Double quantity, Pageable pageable);

    @Query("""
    SELECT p FROM Product p
    WHERE (:name IS NULL OR LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%')))
      AND (:categoryId IS NULL OR p.category.id = :categoryId)
      AND (:farmerId IS NULL OR p.farmer.id = :farmerId)
      AND (:available IS NULL OR p.available = :available)
      AND (:unit IS NULL OR p.unit = :unit)
      AND (:priceMin IS NULL OR p.price >= :priceMin)
      AND (:priceMax IS NULL OR p.price <= :priceMax)
""")
    Page<Product> searchProductsPaged(
            @Param("name") String name,
            @Param("categoryId") Integer categoryId,
            @Param("farmerId") Integer farmerId,
            @Param("available") Boolean available,
            @Param("unit") Unit unit,
            @Param("priceMin") Double priceMin,
            @Param("priceMax") Double priceMax,
            Pageable pageable
    );


}
