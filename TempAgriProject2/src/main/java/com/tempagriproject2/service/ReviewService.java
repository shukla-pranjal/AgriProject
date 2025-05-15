package com.tempagriproject2.service;

import com.tempagriproject2.dto.ReviewDTO;

import java.util.List;

public interface ReviewService {

    List<ReviewDTO> getAllReviews();

    ReviewDTO getReviewById(Integer id) throws Exception;

    ReviewDTO createReview(ReviewDTO reviewDTO) throws Exception;

    ReviewDTO updateReview(ReviewDTO reviewDTO) throws Exception;

    void deleteReview(Integer id) throws Exception;

    List<ReviewDTO> getReviewsByProductId(Integer productId)throws Exception;

    List<ReviewDTO> getReviewsByUserId(Integer userId) throws Exception;
}
