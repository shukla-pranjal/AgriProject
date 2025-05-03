package com.tempagriproject2.controller;

import com.tempagriproject2.dto.ReviewDTO;
import com.tempagriproject2.endpoint.ReviewEndpoint;
import com.tempagriproject2.service.ReviewService;
import com.tempagriproject2.util.CommonUtil;
import lombok.RequiredArgsConstructor;
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
    public ResponseEntity<?> getByProductId(Integer productId)throws Exception {
        List<ReviewDTO> reviews = reviewService.getReviewsByProductId(productId);
        return CommonUtil.createBuildResponse(reviews, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> getByUserId(Integer userId) throws Exception {
        List<ReviewDTO> reviews = reviewService.getReviewsByUserId(userId);
        return CommonUtil.createBuildResponse(reviews, HttpStatus.OK);
    }
}
