package com.farmflow.repository;

import com.farmflow.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Integer> {
    List<Review> findByProductId(Integer productId);

    List<Review> findByUserId(Integer userId);

    Optional<Review> findByProductIdAndUserId(Integer productId, Integer userId);

    @Query("""
    SELECT r FROM Review r
    WHERE (:productId IS NULL OR r.product.id = :productId)
      AND (:userId IS NULL OR r.user.id = :userId)
      AND (:minRating IS NULL OR r.rating >= :minRating)
      AND (:maxRating IS NULL OR r.rating <= :maxRating)
      AND (:comment IS NULL OR LOWER(r.comment) LIKE LOWER(CONCAT('%', :comment, '%')))
""")
    List<Review> searchReviews(
            @Param("productId") Integer productId,
            @Param("userId") Integer userId,
            @Param("minRating") Integer minRating,
            @Param("maxRating") Integer maxRating,
            @Param("comment") String comment
    );

    Page<Review> findByProductId(Integer productId, Pageable pageable);
    Page<Review> findByUserId(Integer userId, Pageable pageable);

    @Query("""
    SELECT r FROM Review r
    WHERE (:productId IS NULL OR r.product.id = :productId)
      AND (:userId IS NULL OR r.user.id = :userId)
      AND (:minRating IS NULL OR r.rating >= :minRating)
      AND (:maxRating IS NULL OR r.rating <= :maxRating)
      AND (:comment IS NULL OR LOWER(r.comment) LIKE LOWER(CONCAT('%', :comment, '%')))
""")
    Page<Review> searchReviewsPaged(@Param("productId") Integer productId,
                                    @Param("userId") Integer userId,
                                    @Param("minRating") Integer minRating,
                                    @Param("maxRating") Integer maxRating,
                                    @Param("comment") String comment,
                                    Pageable pageable);

}
