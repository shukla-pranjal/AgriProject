package com.farmflow.service.impl;

import com.farmflow.dto.PaginationRequest;
import com.farmflow.dto.ReviewDTO;
import com.farmflow.entity.Product;
import com.farmflow.entity.Review;
import com.farmflow.entity.User;
import com.farmflow.exception.ResourceNotFoundException;
import com.farmflow.exception.ValidationException;
import com.farmflow.repository.ProductRepository;
import com.farmflow.repository.ReviewRepository;
import com.farmflow.repository.UserRepository;
import com.farmflow.service.AuthService;
import com.farmflow.service.ReviewService;
import com.farmflow.util.Constants;
import com.farmflow.util.Validation;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final Validation validation;
    private final ModelMapper modelMapper;
    private final AuthService authService;

    @Override
    public List<ReviewDTO> getAllReviews() {
        return reviewRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ReviewDTO getReviewById(Integer id) throws Exception {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Review not found with ID: " + id));
        authService.validateUser(review.getCreatedBy());

        return convertToDTO(review);
    }

    @Override
    public ReviewDTO createReview(ReviewDTO reviewDTO) throws Exception {
        validation.reviewValidate(reviewDTO);

        User user = userRepository.findById(reviewDTO.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + reviewDTO.getUserId()));
        authService.validateUser(user.getId());

        Product product = productRepository.findById(reviewDTO.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with ID: " + reviewDTO.getProductId()));


        Optional<Review> existing = reviewRepository.findByProductIdAndUserId(
                reviewDTO.getProductId(), reviewDTO.getUserId());

        if (existing.isPresent()) {
            throw new ValidationException(Constants.REVIEW_DUPLICATE);
        }


        Review review = modelMapper.map(reviewDTO, Review.class);
        review.setProduct(product);
        review.setUser(user);

        Review saved = reviewRepository.save(review);
        return convertToDTO(saved);
    }

    @Override
    public ReviewDTO updateReview(ReviewDTO reviewDTO) throws Exception {
        validation.reviewValidate(reviewDTO);

        User user = userRepository.findById(reviewDTO.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + reviewDTO.getUserId()));

        authService.validateUser(user.getId());


        Review existing = reviewRepository.findById(reviewDTO.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Review not found with ID: " + reviewDTO.getId()));

        Product product = productRepository.findById(reviewDTO.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with ID: " + reviewDTO.getProductId()));



        modelMapper.map(reviewDTO, existing);
        existing.setProduct(product);
        existing.setUser(user);

        Review updated = reviewRepository.save(existing);
        return convertToDTO(updated);
    }

    @Override
    public void deleteReview(Integer id) throws Exception {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Review not found with ID: " + id));
        authService.validateUser(review.getCreatedBy());

        reviewRepository.delete(review);
    }

    @Override
    public List<ReviewDTO> getReviewsByProductId(Integer productId)throws Exception{
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with ID: " + productId));
        authService.validateUser(product.getCreatedBy());
        return reviewRepository.findByProductId(productId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    @Override
    public List<ReviewDTO> getReviewsByUserId(Integer userId) throws Exception {
        // Check if user exists
        userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));

        authService.validateUser(userId);
        // Fetch reviews by userId
        return reviewRepository.findByUserId(userId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<ReviewDTO> searchReviews(Integer productId, Integer userId, Integer minRating, Integer maxRating, String comment) {

        if (userId ==null ||  authService.isOwnerOrAdmin(userId))
            throw new AccessDeniedException("Access Denied");


        return reviewRepository.searchReviews(productId,userId, minRating, maxRating, comment).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Page<ReviewDTO> getAllReviewsPaged(PaginationRequest paginationRequest) {
        Pageable pageable = paginationRequest.toPageable();
        Page<Review> page = reviewRepository.findAll(pageable);
        return page.map(this::convertToDTO);
    }

    @Override
    public Page<ReviewDTO> getReviewsByProductIdPaged(Integer productId, PaginationRequest paginationRequest) throws Exception{
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with ID: " + productId));
        authService.validateUser(product.getCreatedBy());

        Pageable pageable = paginationRequest.toPageable();
        Page<Review> page = reviewRepository.findByProductId(productId, pageable);
        return page.map(this::convertToDTO);
    }

    @Override
    public Page<ReviewDTO> getReviewsByUserIdPaged(Integer userId, PaginationRequest paginationRequest) throws Exception{
        userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));
        authService.validateUser(userId);

        Pageable pageable = paginationRequest.toPageable();
        Page<Review> page = reviewRepository.findByUserId(userId, pageable);
        return page.map(this::convertToDTO);
    }

    @Override
    public Page<ReviewDTO> searchReviewsPaged(PaginationRequest paginationRequest,
                                              Integer productId,
                                              Integer userId,
                                              Integer minRating,
                                              Integer maxRating,
                                              String comment) {
        if (userId ==null ||  authService.isOwnerOrAdmin(userId))
            throw new AccessDeniedException("Access Denied");
        Pageable pageable = paginationRequest.toPageable();
        Page<Review> page = reviewRepository.searchReviewsPaged(productId, userId, minRating, maxRating, comment, pageable);
        return page.map(this::convertToDTO);
    }



    private ReviewDTO convertToDTO(Review review) {
        return modelMapper.map(review, ReviewDTO.class);
    }
}
