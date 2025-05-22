package com.farmflow.service;

import com.farmflow.dto.PaginationRequest;
import com.farmflow.dto.ProductDTO;
import com.farmflow.enums.Unit;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ProductService {

    List<ProductDTO> getAllProducts();
    List<ProductDTO> getAllAvailableProducts();

    ProductDTO getProductById(Integer id) throws Exception;

    ProductDTO createProduct(ProductDTO productDTO) throws Exception;

    ProductDTO updateProduct(Integer id, ProductDTO productDTO) throws Exception;

    void deleteProduct(Integer id) throws Exception;

    List<ProductDTO> getProductsByCategoryId(Integer categoryId) throws Exception;

    List<ProductDTO> getProductsByFarmerId(Integer farmerId) throws Exception;

    List<ProductDTO> searchProducts(String name, Integer categoryId, Integer farmerId, Boolean available, Unit unit, Double priceMin, Double priceMax);

    Page<ProductDTO> searchProductsPaged(PaginationRequest paginationRequest, String name, Integer categoryId, Integer farmerId, Boolean available, Unit unit, Double priceMin, Double priceMax);

    Page<ProductDTO> getProductsByFarmerIdPaged(Integer farmerId, PaginationRequest paginationRequest) throws Exception;

    Page<ProductDTO> getProductsByCategoryIdPaged(Integer categoryId, PaginationRequest paginationRequest) throws Exception;

    Page<ProductDTO> getAllAvailableProductsPaged(PaginationRequest paginationRequest);

    Page<ProductDTO> getAllProductsPaged(PaginationRequest paginationRequest);
}

