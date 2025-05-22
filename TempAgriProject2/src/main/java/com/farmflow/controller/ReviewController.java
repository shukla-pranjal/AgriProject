package com.farmflow.controller;

import com.farmflow.dto.PaginatedResponse;
import com.farmflow.dto.PaginationRequest;
import com.farmflow.dto.ReviewDTO;
import com.farmflow.endpoint.ReviewEndpoint;
import com.farmflow.service.ReviewService;
import com.farmflow.util.CommonUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

    @RestController
    @RequiredArgsConstructor
    @Slf4j
    public class ReviewController implements ReviewEndpoint {

        private final ReviewService reviewService;
        private static final String CLASS_NAME = ReviewController.class.getSimpleName();

        @Override
        public ResponseEntity<?> getAll() {
            String methodName = "getAll";
            log.debug("{} : {} :: Started", CLASS_NAME, methodName);
            List<ReviewDTO> reviews = reviewService.getAllReviews();
            log.info("{} : {} :: Successfully retrieved {} reviews", CLASS_NAME, methodName, reviews.size());
            return CommonUtil.createBuildResponse(reviews, HttpStatus.OK);
        }

        @Override
        public ResponseEntity<?> getById(Integer id) throws Exception {
            String methodName = "getById";
            log.debug("{} : {} :: Started with id: {}", CLASS_NAME, methodName, id);
            try {
                ReviewDTO review = reviewService.getReviewById(id);
                log.info("{} : {} :: Successfully retrieved review for id: {}", CLASS_NAME, methodName, id);
                return CommonUtil.createBuildResponse(review, HttpStatus.OK);
            } catch (Exception e) {
                log.error("{} : {} :: Failed to retrieve review for id: {}, error: {}", CLASS_NAME, methodName, id, e.getMessage());
                throw e;
            }
        }

        @Override
        public ResponseEntity<?> create(ReviewDTO reviewDTO) throws Exception {
            String methodName = "create";
            log.debug("{} : {} :: Started with reviewDTO: {}", CLASS_NAME, methodName, reviewDTO);
            try {
                ReviewDTO created = reviewService.createReview(reviewDTO);
                log.info("{} : {} :: Successfully created review", CLASS_NAME, methodName);
                return CommonUtil.createBuildResponse(created, HttpStatus.CREATED);
            } catch (Exception e) {
                log.error("{} : {} :: Failed to create review, error: {}", CLASS_NAME, methodName, e.getMessage());
                throw e;
            }
        }

        @Override
        public ResponseEntity<?> update(Integer id, ReviewDTO reviewDTO) throws Exception {
            String methodName = "update";
            log.debug("{} : {} :: Started with id: {}, reviewDTO: {}", CLASS_NAME, methodName, id, reviewDTO);
            try {
                reviewDTO.setId(id);
                ReviewDTO updated = reviewService.updateReview(reviewDTO);
                log.info("{} : {} :: Successfully updated review for id: {}", CLASS_NAME, methodName, id);
                return CommonUtil.createBuildResponse(updated, HttpStatus.OK);
            } catch (Exception e) {
                log.error("{} : {} :: Failed to update review for id: {}, error: {}", CLASS_NAME, methodName, id, e.getMessage());
                throw e;
            }
        }

        @Override
        public ResponseEntity<?> delete(Integer id) throws Exception {
            String methodName = "delete";
            log.debug("{} : {} :: Started with id: {}", CLASS_NAME, methodName, id);
            try {
                reviewService.deleteReview(id);
                log.info("{} : {} :: Successfully deleted review for id: {}", CLASS_NAME, methodName, id);
                return CommonUtil.createBuildResponseMessage("Review deleted successfully", HttpStatus.OK);
            } catch (Exception e) {
                log.error("{} : {} :: Failed to delete review for id: {}, error: {}", CLASS_NAME, methodName, id, e.getMessage());
                throw e;
            }
        }

        @Override
        public ResponseEntity<?> getByProductId(Integer productId) throws Exception {
            String methodName = "getByProductId";
            log.debug("{} : {} :: Started with productId: {}", CLASS_NAME, methodName, productId);
            try {
                List<ReviewDTO> reviews = reviewService.getReviewsByProductId(productId);
                log.info("{} : {} :: Successfully retrieved {} reviews for productId: {}", CLASS_NAME, methodName, reviews.size(), productId);
                return CommonUtil.createBuildResponse(reviews, HttpStatus.OK);
            } catch (Exception e) {
                log.error("{} : {} :: Failed to retrieve reviews for productId: {}, error: {}", CLASS_NAME, methodName, productId, e.getMessage());
                throw e;
            }
        }

        @Override
        public ResponseEntity<?> getByUserId(Integer userId) throws Exception {
            String methodName = "getByUserId";
            log.debug("{} : {} :: Started with userId: {}", CLASS_NAME, methodName, userId);
            try {
                List<ReviewDTO> reviews = reviewService.getReviewsByUserId(userId);
                log.info("{} : {} :: Successfully retrieved {} reviews for userId: {}", CLASS_NAME, methodName, reviews.size(), userId);
                return CommonUtil.createBuildResponse(reviews, HttpStatus.OK);
            } catch (Exception e) {
                log.error("{} : {} :: Failed to retrieve reviews for userId: {}, error: {}", CLASS_NAME, methodName, userId, e.getMessage());
                throw e;
            }
        }

        @Override
        public ResponseEntity<?> searchReviews(Integer productId, Integer userId, Integer minRating, Integer maxRating, String comment) {
            String methodName = "searchReviews";
            log.debug("{} : {} :: Started with productId: {}, userId: {}, minRating: {}, maxRating: {}, comment: {}", CLASS_NAME, methodName, productId, userId, minRating, maxRating, comment);
            List<ReviewDTO> results = reviewService.searchReviews(productId, userId, minRating, maxRating, comment);
            log.info("{} : {} :: Successfully retrieved {} reviews", CLASS_NAME, methodName, results.size());
            return CommonUtil.createBuildResponse(results, HttpStatus.OK);
        }

        @Override
        public ResponseEntity<?> getAllPaged(PaginationRequest paginationRequest) {
            String methodName = "getAllPaged";
            log.debug("{} : {} :: Started with paginationRequest: {}", CLASS_NAME, methodName, paginationRequest);
            Page<ReviewDTO> reviews = reviewService.getAllReviewsPaged(paginationRequest);
            log.info("{} : {} :: Successfully retrieved paged reviews, page: {}, size: {}", CLASS_NAME, methodName, reviews.getNumber(), reviews.getSize());
            PaginatedResponse<ReviewDTO> response = PaginatedResponse.fromPage(reviews);
            return CommonUtil.createBuildResponse(response, HttpStatus.OK);
        }

        @Override
        public ResponseEntity<?> getByProductIdPaged(Integer productId, PaginationRequest paginationRequest) throws Exception {
            String methodName = "getByProductIdPaged";
            log.debug("{} : {} :: Started with productId: {}, paginationRequest: {}", CLASS_NAME, methodName, productId, paginationRequest);
            try {
                Page<ReviewDTO> reviews = reviewService.getReviewsByProductIdPaged(productId, paginationRequest);
                log.info("{} : {} :: Successfully retrieved paged reviews for productId: {}, page: {}, size: {}", CLASS_NAME, methodName, productId, reviews.getNumber(), reviews.getSize());
                PaginatedResponse<ReviewDTO> response = PaginatedResponse.fromPage(reviews);
                return CommonUtil.createBuildResponse(response, HttpStatus.OK);
            } catch (Exception e) {
                log.error("{} : {} :: Failed to retrieve paged reviews for productId: {}, error: {}", CLASS_NAME, methodName, productId, e.getMessage());
                throw e;
            }
        }

        @Override
        public ResponseEntity<?> getByUserIdPaged(Integer userId, PaginationRequest paginationRequest) throws Exception {
            String methodName = "getByUserIdPaged";
            log.debug("{} : {} :: Started with userId: {}, paginationRequest: {}", CLASS_NAME, methodName, userId, paginationRequest);
            try {
                Page<ReviewDTO> reviews = reviewService.getReviewsByUserIdPaged(userId, paginationRequest);
                log.info("{} : {} :: Successfully retrieved paged reviews for userId: {}, page: {}, size: {}", CLASS_NAME, methodName, userId, reviews.getNumber(), reviews.getSize());
                PaginatedResponse<ReviewDTO> response = PaginatedResponse.fromPage(reviews);
                return CommonUtil.createBuildResponse(response, HttpStatus.OK);
            } catch (Exception e) {
                log.error("{} : {} :: Failed to retrieve paged reviews for userId: {}, error: {}", CLASS_NAME, methodName, userId, e.getMessage());
                throw e;
            }
        }

        @Override
        public ResponseEntity<?> searchReviewsPaged(PaginationRequest paginationRequest, Integer productId, Integer userId, Integer minRating, Integer maxRating, String comment) {
            String methodName = "searchReviewsPaged";
            log.debug("{} : {} :: Started with paginationRequest: {}, productId: {}, userId: {}, minRating: {}, maxRating: {}, comment: {}", CLASS_NAME, methodName, paginationRequest, productId, userId, minRating, maxRating, comment);
            Page<ReviewDTO> results = reviewService.searchReviewsPaged(paginationRequest, productId, userId, minRating, maxRating, comment);
            log.info("{} : {} :: Successfully retrieved paged reviews, page: {}, size: {}", CLASS_NAME, methodName, results.getNumber(), results.getSize());
            PaginatedResponse<ReviewDTO> response = PaginatedResponse.fromPage(results);
            return CommonUtil.createBuildResponse(response, HttpStatus.OK);
        }
    }