/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.agriproject.service;

import java.util.List;

import com.agriproject.dto.ReviewsDTO;

public interface ReviewService {

    List<ReviewsDTO> getAllReviews();

    ReviewsDTO getReviewById(Long id)throws  Exception;

    ReviewsDTO createReview(ReviewsDTO reviewsDTO);

    ReviewsDTO updateReview(Long id, ReviewsDTO reviewsDTO)throws  Exception;

    void deleteReview(Long id)throws  Exception;

    List<ReviewsDTO> getReviewsByProduct(Long productId)throws  Exception;

}
