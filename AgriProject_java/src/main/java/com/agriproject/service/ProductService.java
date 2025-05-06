package com.agriproject.service;

import java.util.List;

import com.agriproject.dto.ProductDTO;



public  interface  ProductService {

    List<ProductDTO> getAllProducts();

    ProductDTO getProductById(Long id) throws Exception;


    ProductDTO updateProduct(Long id, ProductDTO productDTO) throws Exception;

    ProductDTO createProduct(ProductDTO productDTO);

    void deleteProduct(Long id) throws Exception;

    List<ProductDTO> searchProducts(String name, Double minPrice, Double maxPrice);

    void disableProduct(Long id) throws Exception;

    void enableProduct(Long id) throws Exception;
    
}