package com.tempagriproject2.service.impl;

import com.tempagriproject2.dto.ProductDTO;
import com.tempagriproject2.entity.Category;
import com.tempagriproject2.entity.Farmer;
import com.tempagriproject2.entity.Product;
import com.tempagriproject2.exception.ResourceNotFoundException;
import com.tempagriproject2.repository.CategoryRepository;
import com.tempagriproject2.repository.FarmerRepository;
import com.tempagriproject2.repository.ProductRepository;
import com.tempagriproject2.service.ProductService;
import com.tempagriproject2.util.Validation;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor

public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final FarmerRepository farmerRepository;
    private final ModelMapper modelMapper;
    private final Validation validation;

    @Override
    public List<ProductDTO> getAllProducts() {
        return productRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ProductDTO getProductById(Integer id) throws Exception {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with ID: " + id));
        return convertToDTO(product);
    }

    @Override
    public ProductDTO createProduct(ProductDTO productDTO) throws Exception {
        // Validation logic for product (e.g., availability, quantity check)
        validation.productValidate(productDTO);

        Category category = categoryRepository.findById(productDTO.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with ID: " + productDTO.getCategoryId()));
        Farmer farmer = farmerRepository.findById(productDTO.getFarmerId())
                .orElseThrow(() -> new ResourceNotFoundException("Farmer not found with ID: " + productDTO.getFarmerId()));

        // Map DTO to Entity
        Product product = modelMapper.map(productDTO, Product.class);
        product.setCategory(category);
        product.setFarmer(farmer);

        // Save the new product to the repository
        Product savedProduct = productRepository.save(product);
        return convertToDTO(savedProduct);
    }

    @Override
    public ProductDTO updateProduct(Integer id, ProductDTO productDTO) throws Exception {
        // Validate input
        validation.productValidate(productDTO);

        // Fetch the product to update
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with ID: " + id));

        // Fetch related entities (category, farmer)
        Category category = categoryRepository.findById(productDTO.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with ID: " + productDTO.getCategoryId()));
        Farmer farmer = farmerRepository.findById(productDTO.getFarmerId())
                .orElseThrow(() -> new ResourceNotFoundException("Farmer not found with ID: " + productDTO.getFarmerId()));

        // Map the updated data into the existing product entity
        modelMapper.map(productDTO, existingProduct);
        existingProduct.setCategory(category);
        existingProduct.setFarmer(farmer);

        // Save and return the updated product
        Product updatedProduct = productRepository.save(existingProduct);
        return convertToDTO(updatedProduct);
    }

    @Override
    public void deleteProduct(Integer id) throws Exception {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with ID: " + id));
        productRepository.delete(product);
    }

    @Override
    public List<ProductDTO> getProductsByCategoryId(Integer categoryId) throws Exception {
        // Check if category exists
        categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with ID: " + categoryId));

        return productRepository.findByCategoryId(categoryId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductDTO> getProductsByFarmerId(Integer farmerId) throws Exception {
        // Check if farmer exists
        farmerRepository.findById(farmerId)
                .orElseThrow(() -> new ResourceNotFoundException("Farmer not found with ID: " + farmerId));

        return productRepository.findByFarmerId(farmerId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }



    private ProductDTO convertToDTO(Product product) {
        return modelMapper.map(product, ProductDTO.class);
    }
}
