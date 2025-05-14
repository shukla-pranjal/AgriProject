package com.tempagriproject2.repository;

import com.tempagriproject2.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Integer> {
    List<Review> findByProductId(Integer productId);

    List<Review> findByUserId(Integer userId);

    Optional<Review> findByProductIdAndUserId(Integer productId, Integer userId);
}
