package com.farmflow.controller;

import com.farmflow.dto.PaginatedResponse;
import com.farmflow.dto.PaginationRequest;
import com.farmflow.dto.ProductDTO;
import com.farmflow.endpoint.ProductEndpoint;
import com.farmflow.enums.Unit;
import com.farmflow.service.ProductService;
import com.farmflow.util.CommonUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequiredArgsConstructor
@Slf4j
public class ProductController implements ProductEndpoint {

    private final ProductService productService;
    private static final String CLASS_NAME = ProductController.class.getSimpleName();

    @Override
    public ResponseEntity<?> getAll() {
        List<ProductDTO> products = productService.getAllProducts();
        return CommonUtil.createBuildResponse(products, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> getAllAvailable() {
        List<ProductDTO> products = productService.getAllAvailableProducts();
        return CommonUtil.createBuildResponse(products, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> getById(Integer id) throws Exception {
            ProductDTO product = productService.getProductById(id);
            return CommonUtil.createBuildResponse(product, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> create(ProductDTO productDTO) throws Exception {
            ProductDTO created = productService.createProduct(productDTO);
            return CommonUtil.createBuildResponse(created, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<?> update(Integer id, ProductDTO productDTO) throws Exception {
            ProductDTO updated = productService.updateProduct(id, productDTO);
            return CommonUtil.createBuildResponse(updated, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> delete(Integer id) throws Exception {
            productService.deleteProduct(id);
            return CommonUtil.createBuildResponseMessage("Product deleted successfully", HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> getByCategoryId(Integer categoryId) throws Exception {
            List<ProductDTO> products = productService.getProductsByCategoryId(categoryId);
            return CommonUtil.createBuildResponse(products, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> getByFarmerId(Integer farmerId) throws Exception {
            List<ProductDTO> products = productService.getProductsByFarmerId(farmerId);
            return CommonUtil.createBuildResponse(products, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> searchProducts(String name, Integer categoryId, Integer farmerId, Boolean available, Unit unit, Double priceMin, Double priceMax) {
        List<ProductDTO> products = productService.searchProducts(name, categoryId, farmerId, available, unit, priceMin, priceMax);
        return CommonUtil.createBuildResponse(products, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> getAllPaged(PaginationRequest paginationRequest) {
        Page<ProductDTO> products = productService.getAllProductsPaged(paginationRequest);
        PaginatedResponse<ProductDTO> response = PaginatedResponse.fromPage(products);
        return CommonUtil.createBuildResponse(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> getAllAvailablePaged(PaginationRequest paginationRequest) {
        Page<ProductDTO> products = productService.getAllAvailableProductsPaged(paginationRequest);
        PaginatedResponse<ProductDTO> response = PaginatedResponse.fromPage(products);
        return CommonUtil.createBuildResponse(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> getByCategoryIdPaged(Integer categoryId, PaginationRequest paginationRequest) throws Exception {
            Page<ProductDTO> products = productService.getProductsByCategoryIdPaged(categoryId, paginationRequest);
            PaginatedResponse<ProductDTO> response = PaginatedResponse.fromPage(products);
            return CommonUtil.createBuildResponse(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> getByFarmerIdPaged(Integer farmerId, PaginationRequest paginationRequest) throws Exception {
            Page<ProductDTO> products = productService.getProductsByFarmerIdPaged(farmerId, paginationRequest);
            PaginatedResponse<ProductDTO> response = PaginatedResponse.fromPage(products);
            return CommonUtil.createBuildResponse(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> searchProductsPaged(PaginationRequest paginationRequest, String name, Integer categoryId,
                                                 Integer farmerId, Boolean available, Unit unit, Double priceMin, Double priceMax) {
        Page<ProductDTO> products = productService.searchProductsPaged(paginationRequest, name, categoryId, farmerId, available, unit, priceMin, priceMax);
        PaginatedResponse<ProductDTO> response = PaginatedResponse.fromPage(products);
        return CommonUtil.createBuildResponse(response, HttpStatus.OK);
    }
}