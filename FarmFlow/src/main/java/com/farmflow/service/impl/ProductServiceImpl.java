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
import com.farmflow.service.email.EmailComposerService;
import com.farmflow.util.Constants;
import com.farmflow.util.Validation;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final FarmerRepository farmerRepository;
    private final ModelMapper modelMapper;
    private final Validation validation;
    private final AuthService authService;
    private final EmailComposerService emailComposerService;

    @Override
    @Cacheable(value = "productCache", key = "'all'")
    public List<ProductDTO> getAllProducts() {
        return productRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Cacheable(value = "productCache", key = "'available'")
    public List<ProductDTO> getAllAvailableProducts() {
        return productRepository.findByQuantityGreaterThanAndAvailableTrue(0.0).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Cacheable(value = "productCache", key = "#id")
    public ProductDTO getProductById(Integer id) throws Exception {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(Constants.RESOURCE_NOT_FOUND, Constants.PRODUCT, id)));
        if (authService.isOwnerOrAdmin(product.getCreatedBy()))
            throw new AccessDeniedException(Constants.ACCESS_DENIED);

        return convertToDTO(product);
    }

    @Override
    @CacheEvict(value = "productCache", allEntries = true)
    public ProductDTO createProduct(ProductDTO productDTO) throws Exception {
        validation.productValidate(productDTO);

        if (productDTO.getCategoryId() == null) {
            throw new ValidationException(Constants.CATEGORY_ID_REQUIRED);
        }
        if (productDTO.getFarmerId() == null) {
            throw new ValidationException(Constants.FARMER_ID_REQUIRED);
        }

        Category category = categoryRepository.findById(productDTO.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException(String.format(Constants.RESOURCE_NOT_FOUND, Constants.CATEGORY, productDTO.getCategoryId())));
        Farmer farmer = farmerRepository.findById(productDTO.getFarmerId())
                .orElseThrow(() -> new ResourceNotFoundException(String.format(Constants.RESOURCE_NOT_FOUND, Constants.FARMER, productDTO.getFarmerId())));

        Product product = modelMapper.map(productDTO, Product.class);
        product.setCategory(category);
        product.setFarmer(farmer);

        emailComposerService.newProductCreated(farmer, product);

        Product savedProduct = productRepository.save(product);
        return convertToDTO(savedProduct);
    }

    @Override
    @CacheEvict(value = "productCache", key = "#id")
    @CachePut(value = "productCache", key = "#result.id")
    public ProductDTO updateProduct(Integer id, ProductDTO productDTO) throws Exception {
        validation.productValidate(productDTO);

        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(Constants.RESOURCE_NOT_FOUND, Constants.PRODUCT, id)));

        if (productDTO.getCategoryId() == null || productDTO.getFarmerId() == null) {
            throw new ValidationException(Constants.CATEGORY_AND_FARMER_REQUIRED);
        }

        Category category = categoryRepository.findById(productDTO.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException(String.format(Constants.RESOURCE_NOT_FOUND, Constants.CATEGORY, productDTO.getCategoryId())));
        Farmer farmer = farmerRepository.findById(productDTO.getFarmerId())
                .orElseThrow(() -> new ResourceNotFoundException(String.format(Constants.RESOURCE_NOT_FOUND, Constants.FARMER, productDTO.getFarmerId())));

        modelMapper.map(productDTO, existingProduct);
        existingProduct.setCategory(category);
        existingProduct.setFarmer(farmer);

        Product updatedProduct = productRepository.save(existingProduct);

        emailComposerService.productUpdated(farmer, updatedProduct);
        return convertToDTO(updatedProduct);
    }

    @Override
    @CacheEvict(value = "productCache", key = "#id")
    public void deleteProduct(Integer id) throws Exception {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(Constants.RESOURCE_NOT_FOUND, Constants.PRODUCT, id)));
        productRepository.delete(product);
    }

    @Override
    @Cacheable(value = "productCache", key = "#categoryId")
    public List<ProductDTO> getProductsByCategoryId(Integer categoryId) throws Exception {
        categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(Constants.RESOURCE_NOT_FOUND, Constants.CATEGORY, categoryId)));

        return productRepository.findByCategoryId(categoryId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Cacheable(value = "productCache", key = "#farmerId")
    public List<ProductDTO> getProductsByFarmerId(Integer farmerId) throws Exception {
        Farmer farmer = farmerRepository.findById(farmerId)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(Constants.RESOURCE_NOT_FOUND, Constants.FARMER, farmerId)));
        authService.validateUser(farmer.getCreatedBy());

        return productRepository.findByFarmerId(farmerId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductDTO> searchProducts(String name, Integer categoryId, Integer farmerId, Boolean available, Unit unit, Double priceMin, Double priceMax) {
        return productRepository.searchProducts(name, categoryId, farmerId, available, unit, priceMin, priceMax).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

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
                .orElseThrow(() -> new ResourceNotFoundException(String.format(Constants.RESOURCE_NOT_FOUND, Constants.FARMER, farmerId)));

        Pageable pageable = paginationRequest.toPageable();
        Page<Product> page = productRepository.findByFarmerId(farmerId, pageable);
        return page.map(this::convertToDTO);
    }

    @Override
    public Page<ProductDTO> getProductsByCategoryIdPaged(Integer categoryId, PaginationRequest paginationRequest) throws Exception {
        categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(Constants.RESOURCE_NOT_FOUND, Constants.CATEGORY, categoryId)));

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