package com.farmflow.service;

import com.farmflow.dto.PaginationRequest;
import com.farmflow.dto.ReviewDTO;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ReviewService {

    List<ReviewDTO> getAllReviews();

    ReviewDTO getReviewById(Integer id) throws Exception;

    ReviewDTO createReview(ReviewDTO reviewDTO) throws Exception;

    ReviewDTO updateReview(ReviewDTO reviewDTO) throws Exception;

    void deleteReview(Integer id) throws Exception;

    List<ReviewDTO> getReviewsByProductId(Integer productId)throws Exception;

    List<ReviewDTO> getReviewsByUserId(Integer userId) throws Exception;

    List<ReviewDTO> searchReviews(Integer productId, Integer userId, Integer minRating, Integer maxRating, String comment);

    Page<ReviewDTO> getAllReviewsPaged(PaginationRequest paginationRequest);

    Page<ReviewDTO> getReviewsByProductIdPaged(Integer productId, PaginationRequest paginationRequest)throws Exception;

    Page<ReviewDTO> getReviewsByUserIdPaged(Integer userId, PaginationRequest paginationRequest)throws Exception;

    Page<ReviewDTO> searchReviewsPaged(PaginationRequest paginationRequest, Integer productId, Integer userId, Integer minRating, Integer maxRating, String comment);
}
