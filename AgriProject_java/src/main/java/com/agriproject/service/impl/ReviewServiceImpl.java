package com.agriproject.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.agriproject.dto.ReviewsDTO;
import com.agriproject.enitity.Product;
import com.agriproject.enitity.Reviews;
import com.agriproject.exception.ResourceNotFoundException;
import com.agriproject.repository.ProductRepository;
import com.agriproject.repository.ReviewsRepository;
import com.agriproject.service.ReviewService;

@Service
public class ReviewServiceImpl implements ReviewService {

    @Autowired
    private ReviewsRepository reviewsRepository;
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public List<ReviewsDTO> getAllReviews() {
        return reviewsRepository.findAll().stream()
                .map(review -> modelMapper.map(review, ReviewsDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public ReviewsDTO getReviewById(Long id) throws  Exception {
        Reviews review = reviewsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Review not found with ID: " + id));
        return modelMapper.map(review, ReviewsDTO.class);
    }

    @Override
    public ReviewsDTO createReview(ReviewsDTO reviewsDTO) {
        Reviews review = modelMapper.map(reviewsDTO, Reviews.class);
        Reviews savedReview = reviewsRepository.save(review);
        return modelMapper.map(savedReview, ReviewsDTO.class);
    }

    @Override
    public ReviewsDTO updateReview(Long id, ReviewsDTO reviewsDTO) throws  Exception {
        Reviews existingReview = reviewsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Review not found with ID: " + id));

        modelMapper.map(reviewsDTO, existingReview);
        Reviews updatedReview = reviewsRepository.save(existingReview);
        return modelMapper.map(updatedReview, ReviewsDTO.class);
    }

    @Override
    public void deleteReview(Long id) throws  Exception{
        Reviews existingReview = reviewsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Review not found with ID: " + id));
        reviewsRepository.delete(existingReview);
    }

    @Override
    public List<ReviewsDTO> getReviewsByProduct(Long productId)throws  Exception {
        Product product = productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Product not found with ID: " + productId));
        return reviewsRepository.findByProduct(product).stream()
                .map(review -> modelMapper.map(review, ReviewsDTO.class))
                .collect(Collectors.toList());
    }
}
