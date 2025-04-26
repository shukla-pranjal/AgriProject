package com.agriproject.controller;

import com.agriproject.dto.ReviewsDTO;
import com.agriproject.service.ReviewService;
import com.agriproject.util.CommonUtil;

import lombok.AllArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/reviews")
public class ReviewController {

    
    private final ReviewService reviewService;

    @GetMapping
    public ResponseEntity<?> getAll() {
        List<ReviewsDTO> reviews = reviewService.getAllReviews();
        return CommonUtil.createBuildResponse(reviews, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) throws  Exception {
        ReviewsDTO review = reviewService.getReviewById(id);
        return CommonUtil.createBuildResponse(review, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody ReviewsDTO reviewsDTO) {
        ReviewsDTO created = reviewService.createReview(reviewsDTO);
        return CommonUtil.createBuildResponse(created, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody ReviewsDTO reviewsDTO)  throws  Exception{
        ReviewsDTO updated = reviewService.updateReview(id, reviewsDTO);
        return CommonUtil.createBuildResponse(updated, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) throws  Exception {
        reviewService.deleteReview(id);
        return CommonUtil.createBuildResponseMessage("Review deleted successfully", HttpStatus.OK);
    }

    @GetMapping("/product")
    public ResponseEntity<?> getReviewsByProduct(@RequestParam Long productId) throws  Exception{
        List<ReviewsDTO> reviews = reviewService.getReviewsByProduct(productId);
        return CommonUtil.createBuildResponse(reviews, HttpStatus.OK);
    }

}
