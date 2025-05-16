package com.tempagriproject2.service.impl;

import com.tempagriproject2.dto.ReviewDTO;
import com.tempagriproject2.entity.Product;
import com.tempagriproject2.entity.Review;
import com.tempagriproject2.entity.User;
import com.tempagriproject2.exception.ResourceNotFoundException;
import com.tempagriproject2.repository.ProductRepository;
import com.tempagriproject2.repository.ReviewRepository;
import com.tempagriproject2.repository.UserRepository;
import com.tempagriproject2.service.ReviewService;
import com.tempagriproject2.util.Constants;
import com.tempagriproject2.util.Validation;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
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
        return convertToDTO(review);
    }

    @Override
    public ReviewDTO createReview(ReviewDTO reviewDTO) throws Exception {
        validation.reviewValidate(reviewDTO);

        Product product = productRepository.findById(reviewDTO.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with ID: " + reviewDTO.getProductId()));

        User user = userRepository.findById(reviewDTO.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + reviewDTO.getUserId()));

        Optional<Review> existing = reviewRepository.findByProductIdAndUserId(
                reviewDTO.getProductId(), reviewDTO.getUserId());

        if (existing.isPresent()) {
            throw new ResourceNotFoundException(Constants.REVIEW_DUPLICATE);
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

        Review existing = reviewRepository.findById(reviewDTO.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Review not found with ID: " + reviewDTO.getId()));

        Product product = productRepository.findById(reviewDTO.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with ID: " + reviewDTO.getProductId()));

        User user = userRepository.findById(reviewDTO.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + reviewDTO.getUserId()));

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
        reviewRepository.delete(review);
    }

    @Override
    public List<ReviewDTO> getReviewsByProductId(Integer productId)throws Exception{
        productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with ID: " + productId));
        return reviewRepository.findByProductId(productId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    @Override
    public List<ReviewDTO> getReviewsByUserId(Integer userId) throws Exception {
        // Check if user exists
        userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));

        // Fetch reviews by userId
        return reviewRepository.findByUserId(userId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }


    private ReviewDTO convertToDTO(Review review) {
        return modelMapper.map(review, ReviewDTO.class);
    }
}
