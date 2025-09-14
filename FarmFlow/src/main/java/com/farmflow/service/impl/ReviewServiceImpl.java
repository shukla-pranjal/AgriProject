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
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@Service
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final Validation validation;
    private final ModelMapper modelMapper;
    private final AuthService authService;

    @Override
    @Cacheable(value = "reviewCacheAll", key = "'all'")
    public List<ReviewDTO> getAllReviews() {
        return reviewRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Cacheable(value = "reviewCacheById", key = "#id")
    public ReviewDTO getReviewById(Integer id) throws Exception {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(Constants.RESOURCE_NOT_FOUND, Constants.REVIEW, id)));
        authService.validateUser(review.getCreatedBy());

        return convertToDTO(review);
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = "reviewCacheAll", allEntries = true),
            @CacheEvict(value = "reviewCacheByProduct", key = "#reviewDTO.productId"),
            @CacheEvict(value = "reviewCacheByUser", key = "#reviewDTO.userId")
    })
    public ReviewDTO createReview(ReviewDTO reviewDTO) throws Exception {
        validation.reviewValidate(reviewDTO);

        User user = userRepository.findById(reviewDTO.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException(String.format(Constants.RESOURCE_NOT_FOUND, Constants.USER, reviewDTO.getUserId())));
        authService.validateUser(user.getId());

        Product product = productRepository.findById(reviewDTO.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException(String.format(Constants.RESOURCE_NOT_FOUND, Constants.PRODUCT, reviewDTO.getProductId())));

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
    @Caching(evict = {
            @CacheEvict(value = "reviewCacheAll", allEntries = true),
            @CacheEvict(value = "reviewCacheByProduct", key = "#reviewDTO.productId"),
            @CacheEvict(value = "reviewCacheByUser", key = "#reviewDTO.userId")
    }, put = {
            @CachePut(value = "reviewCacheById", key = "#result.id")
    })
    public ReviewDTO updateReview(ReviewDTO reviewDTO) throws Exception {
        validation.reviewValidate(reviewDTO);

        User user = userRepository.findById(reviewDTO.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException(String.format(Constants.RESOURCE_NOT_FOUND, Constants.USER, reviewDTO.getUserId())));
        authService.validateUser(user.getId());

        Review existing = reviewRepository.findById(reviewDTO.getId())
                .orElseThrow(() -> new ResourceNotFoundException(String.format(Constants.RESOURCE_NOT_FOUND, Constants.REVIEW, reviewDTO.getId())));

        Product product = productRepository.findById(reviewDTO.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException(String.format(Constants.RESOURCE_NOT_FOUND, Constants.PRODUCT, reviewDTO.getProductId())));

        modelMapper.map(reviewDTO, existing);
        existing.setProduct(product);
        existing.setUser(user);

        Review updated = reviewRepository.save(existing);
        return convertToDTO(updated);
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = "reviewCacheAll", allEntries = true),
            @CacheEvict(value = "reviewCacheById", key = "#id"),
            @CacheEvict(value = "reviewCacheByProduct", allEntries = true),
            @CacheEvict(value = "reviewCacheByUser", allEntries = true)
    })
    public void deleteReview(Integer id) throws Exception {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(Constants.RESOURCE_NOT_FOUND, Constants.REVIEW, id)));
        authService.validateUser(review.getCreatedBy());

        reviewRepository.delete(review);
    }

    @Override
    @Cacheable(value = "reviewCacheByProduct", key = "#productId")
    public List<ReviewDTO> getReviewsByProductId(Integer productId) throws Exception {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(Constants.RESOURCE_NOT_FOUND, Constants.PRODUCT, productId)));
        authService.validateUser(product.getCreatedBy());
        return reviewRepository.findByProductId(productId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Cacheable(value = "reviewCacheByUser", key = "#userId")
    public List<ReviewDTO> getReviewsByUserId(Integer userId) throws Exception {
        userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(Constants.RESOURCE_NOT_FOUND, Constants.USER, userId)));
        authService.validateUser(userId);
        return reviewRepository.findByUserId(userId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<ReviewDTO> searchReviews(Integer productId, Integer userId, Integer minRating, Integer maxRating, String comment) {
        // ✅ Fixed Access Control
        if (userId != null && !authService.isOwnerOrAdmin(userId)) {
            throw new AccessDeniedException(Constants.ACCESS_DENIED);
        }

        return reviewRepository.searchReviews(productId, userId, minRating, maxRating, comment).stream()
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
    public Page<ReviewDTO> getReviewsByProductIdPaged(Integer productId, PaginationRequest paginationRequest) throws Exception {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(Constants.RESOURCE_NOT_FOUND, Constants.PRODUCT, productId)));
        authService.validateUser(product.getCreatedBy());

        Pageable pageable = paginationRequest.toPageable();
        Page<Review> page = reviewRepository.findByProductId(productId, pageable);
        return page.map(this::convertToDTO);
    }

    @Override
    public Page<ReviewDTO> getReviewsByUserIdPaged(Integer userId, PaginationRequest paginationRequest) throws Exception {
        userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(Constants.RESOURCE_NOT_FOUND, Constants.USER, userId)));
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
        // ✅ Fixed Access Control
        if (userId != null && !authService.isOwnerOrAdmin(userId)) {
            throw new AccessDeniedException(Constants.ACCESS_DENIED);
        }
        Pageable pageable = paginationRequest.toPageable();
        Page<Review> page = reviewRepository.searchReviewsPaged(productId, userId, minRating, maxRating, comment, pageable);
        return page.map(this::convertToDTO);
    }

    private ReviewDTO convertToDTO(Review review) {
        return modelMapper.map(review, ReviewDTO.class);
    }
}
