package com.tempagriproject2.service;

import com.tempagriproject2.dto.ProductDTO;

import java.util.List;

public interface ProductService {

    List<ProductDTO> getAllProducts();

    ProductDTO getProductById(Integer id) throws Exception;

    ProductDTO createProduct(ProductDTO productDTO) throws Exception;

    ProductDTO updateProduct(Integer id, ProductDTO productDTO) throws Exception;

    void deleteProduct(Integer id) throws Exception;

    List<ProductDTO> getProductsByCategoryId(Integer categoryId) throws Exception;

    List<ProductDTO> getProductsByFarmerId(Integer farmerId) throws Exception;
}

