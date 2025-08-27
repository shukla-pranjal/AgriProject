package com.farmflow.controller;

import com.farmflow.dto.PaginatedResponse;
import com.farmflow.dto.PaginationRequest;
import com.farmflow.dto.ReviewDTO;
import com.farmflow.endpoint.ReviewEndpoint;
import com.farmflow.service.ReviewService;
import com.farmflow.util.CommonUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ReviewController implements ReviewEndpoint {

    private final ReviewService reviewService;

    @Override
    public ResponseEntity<?> getAll() {
        List<ReviewDTO> reviews = reviewService.getAllReviews();
        return CommonUtil.createBuildResponse(reviews, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> getById(Integer id) throws Exception {
            ReviewDTO review = reviewService.getReviewById(id);
            return CommonUtil.createBuildResponse(review, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> create(ReviewDTO reviewDTO) throws Exception {
            ReviewDTO created = reviewService.createReview(reviewDTO);
            return CommonUtil.createBuildResponse(created, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<?> update(Integer id, ReviewDTO reviewDTO) throws Exception {
            reviewDTO.setId(id);
            ReviewDTO updated = reviewService.updateReview(reviewDTO);
            return CommonUtil.createBuildResponse(updated, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> delete(Integer id) throws Exception {
            reviewService.deleteReview(id);
            return CommonUtil.createBuildResponseMessage("Review deleted successfully", HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> getByProductId(Integer productId) throws Exception {
            List<ReviewDTO> reviews = reviewService.getReviewsByProductId(productId);
            return CommonUtil.createBuildResponse(reviews, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> getByUserId(Integer userId) throws Exception {
            List<ReviewDTO> reviews = reviewService.getReviewsByUserId(userId);
            return CommonUtil.createBuildResponse(reviews, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> searchReviews(Integer productId, Integer userId, Integer minRating, Integer maxRating, String comment) {
        List<ReviewDTO> results = reviewService.searchReviews(productId, userId, minRating, maxRating, comment);
        return CommonUtil.createBuildResponse(results, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> getAllPaged(PaginationRequest paginationRequest) {
        Page<ReviewDTO> reviews = reviewService.getAllReviewsPaged(paginationRequest);
        PaginatedResponse<ReviewDTO> response = PaginatedResponse.fromPage(reviews);
        return CommonUtil.createBuildResponse(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> getByProductIdPaged(Integer productId, PaginationRequest paginationRequest) throws Exception {
            Page<ReviewDTO> reviews = reviewService.getReviewsByProductIdPaged(productId, paginationRequest);
            PaginatedResponse<ReviewDTO> response = PaginatedResponse.fromPage(reviews);
            return CommonUtil.createBuildResponse(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> getByUserIdPaged(Integer userId, PaginationRequest paginationRequest) throws Exception {
            Page<ReviewDTO> reviews = reviewService.getReviewsByUserIdPaged(userId, paginationRequest);
            PaginatedResponse<ReviewDTO> response = PaginatedResponse.fromPage(reviews);
            return CommonUtil.createBuildResponse(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> searchReviewsPaged(PaginationRequest paginationRequest, Integer productId, Integer userId, Integer minRating, Integer maxRating, String comment) {
        Page<ReviewDTO> results = reviewService.searchReviewsPaged(paginationRequest, productId, userId, minRating, maxRating, comment);
        PaginatedResponse<ReviewDTO> response = PaginatedResponse.fromPage(results);
        return CommonUtil.createBuildResponse(response, HttpStatus.OK);
    }
}