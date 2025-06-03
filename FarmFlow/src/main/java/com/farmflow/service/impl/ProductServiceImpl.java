package com.farmflow.service.impl;

import com.farmflow.dto.PaginationRequest;
import com.farmflow.dto.ProductDTO;
import com.farmflow.entity.Category;
import com.farmflow.entity.Farmer;
import com.farmflow.entity.Product;
import com.farmflow.enums.Unit;
import com.farmflow.exception.ResourceNotFoundException;
import com.farmflow.exception.ValidationException;
import com.farmflow.repository.CategoryRepository;
import com.farmflow.repository.FarmerRepository;
import com.farmflow.repository.ProductRepository;
import com.farmflow.service.AuthService;
import com.farmflow.service.ProductService;
import com.farmflow.util.Validation;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
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
    private final AuthService authService;

    @Override
    public List<ProductDTO> getAllProducts() {
        return productRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductDTO> getAllAvailableProducts() {

        return productRepository.findByQuantityGreaterThanAndAvailableTrue(0.0).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }


    @Override
    public ProductDTO getProductById(Integer id) throws Exception {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with ID: " + id));
        if (authService.isOwnerOrAdmin(product.getCreatedBy()))
            throw new AccessDeniedException("Access Denied");


        return convertToDTO(product);
    }

    @Override
    public ProductDTO createProduct(ProductDTO productDTO) throws Exception {
        validation.productValidate(productDTO);

        if (productDTO.getCategoryId() == null) {
            throw new ValidationException("Category ID is required.");
        }
        if (productDTO.getFarmerId() == null) {
            throw new ValidationException("Farmer ID is required.");
        }

        Category category = categoryRepository.findById(productDTO.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with ID: " + productDTO.getCategoryId()));
        Farmer farmer = farmerRepository.findById(productDTO.getFarmerId())
                .orElseThrow(() -> new ResourceNotFoundException("Farmer not found with ID: " + productDTO.getFarmerId()));

        Product product = modelMapper.map(productDTO, Product.class);
        product.setCategory(category);
        product.setFarmer(farmer);

        Product savedProduct = productRepository.save(product);
        return convertToDTO(savedProduct);
    }

    @Override
    public ProductDTO updateProduct(Integer id, ProductDTO productDTO) throws Exception {
        validation.productValidate(productDTO);

        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with ID: " + id));

        if (productDTO.getCategoryId() == null || productDTO.getFarmerId() == null) {
            throw new ValidationException("Category ID and Farmer ID are required.");
        }

        Category category = categoryRepository.findById(productDTO.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with ID: " + productDTO.getCategoryId()));
        Farmer farmer = farmerRepository.findById(productDTO.getFarmerId())
                .orElseThrow(() -> new ResourceNotFoundException("Farmer not found with ID: " + productDTO.getFarmerId()));

        modelMapper.map(productDTO, existingProduct);
        existingProduct.setCategory(category);
        existingProduct.setFarmer(farmer);

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
        categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with ID: " + categoryId));

        return productRepository.findByCategoryId(categoryId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductDTO> getProductsByFarmerId(Integer farmerId) throws Exception {
        Farmer farmer = farmerRepository.findById(farmerId)
                .orElseThrow(() -> new ResourceNotFoundException("Farmer not found with ID: " + farmerId));
        authService.validateUser(farmer.getCreatedBy());

        return productRepository.findByFarmerId(farmerId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductDTO> searchProducts(String name, Integer categoryId, Integer farmerId, Boolean available, Unit unit, Double priceMin, Double priceMax) {
        return productRepository.searchProducts(name, categoryId, farmerId, available, unit, priceMin, priceMax).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());    }



    @Override
    public Page<ProductDTO> searchProductsPaged(PaginationRequest paginationRequest,
                                                String name,
                                                Integer categoryId,
                                                Integer farmerId,
                                                Boolean available,
                                                Unit unit,
                                                Double priceMin,
                                                Double priceMax) {
        Pageable pageable = paginationRequest.toPageable();
        Page<Product> page = productRepository.searchProductsPaged(name, categoryId, farmerId, available, unit, priceMin, priceMax, pageable);
        return page.map(this::convertToDTO);
    }

    @Override
    public Page<ProductDTO> getProductsByFarmerIdPaged(Integer farmerId, PaginationRequest paginationRequest) throws Exception {
        farmerRepository.findById(farmerId)
                .orElseThrow(() -> new ResourceNotFoundException("Farmer not found with ID: " + farmerId));

        Pageable pageable = paginationRequest.toPageable();
        Page<Product> page = productRepository.findByFarmerId(farmerId, pageable);
        return page.map(this::convertToDTO);
    }

    @Override
    public Page<ProductDTO> getProductsByCategoryIdPaged(Integer categoryId, PaginationRequest paginationRequest) throws Exception {
        categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with ID: " + categoryId));

        Pageable pageable = paginationRequest.toPageable();
        Page<Product> page = productRepository.findByCategoryId(categoryId, pageable);
        return page.map(this::convertToDTO);
    }

    @Override
    public Page<ProductDTO> getAllAvailableProductsPaged(PaginationRequest paginationRequest) {
        Pageable pageable = paginationRequest.toPageable();
        Page<Product> page = productRepository.findByQuantityGreaterThanAndAvailableTrue(0.0, pageable);
        return page.map(this::convertToDTO);
    }

    @Override
    public Page<ProductDTO> getAllProductsPaged(PaginationRequest paginationRequest) {
        Pageable pageable = paginationRequest.toPageable();
        Page<Product> page = productRepository.findAll(pageable);
        return page.map(this::convertToDTO);
    }



    private ProductDTO convertToDTO(Product product) {
        return modelMapper.map(product, ProductDTO.class);
    }
}
