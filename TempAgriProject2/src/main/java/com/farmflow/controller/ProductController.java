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
        String methodName = "getAll";
        log.debug("{} : {} :: Started", CLASS_NAME, methodName);
        List<ProductDTO> products = productService.getAllProducts();
        log.info("{} : {} :: Successfully retrieved {} products", CLASS_NAME, methodName, products.size());
        return CommonUtil.createBuildResponse(products, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> getAllAvailable() {
        String methodName = "getAllAvailable";
        log.debug("{} : {} :: Started", CLASS_NAME, methodName);
        List<ProductDTO> products = productService.getAllAvailableProducts();
        log.info("{} : {} :: Successfully retrieved {} available products", CLASS_NAME, methodName, products.size());
        return CommonUtil.createBuildResponse(products, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> getById(Integer id) throws Exception {
        String methodName = "getById";
        log.debug("{} : {} :: Started with id: {}", CLASS_NAME, methodName, id);
        try {
            ProductDTO product = productService.getProductById(id);
            log.info("{} : {} :: Successfully retrieved product for id: {}", CLASS_NAME, methodName, id);
            return CommonUtil.createBuildResponse(product, HttpStatus.OK);
        } catch (Exception e) {
            log.error("{} : {} :: Failed to retrieve product for id: {}, error: {}", CLASS_NAME, methodName, id, e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public ResponseEntity<?> create(ProductDTO productDTO) throws Exception {
        String methodName = "create";
        log.debug("{} : {} :: Started with productDTO: {}", CLASS_NAME, methodName, productDTO);
        try {
            ProductDTO created = productService.createProduct(productDTO);
            log.info("{} : {} :: Successfully created product", CLASS_NAME, methodName);
            return CommonUtil.createBuildResponse(created, HttpStatus.CREATED);
        } catch (Exception e) {
            log.error("{} : {} :: Failed to create product, error: {}", CLASS_NAME, methodName, e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public ResponseEntity<?> update(Integer id, ProductDTO productDTO) throws Exception {
        String methodName = "update";
        log.debug("{} : {} :: Started with id: {}, productDTO: {}", CLASS_NAME, methodName, id, productDTO);
        try {
            ProductDTO updated = productService.updateProduct(id, productDTO);
            log.info("{} : {} :: Successfully updated product for id: {}", CLASS_NAME, methodName, id);
            return CommonUtil.createBuildResponse(updated, HttpStatus.OK);
        } catch (Exception e) {
            log.error("{} : {} :: Failed to update product for id: {}, error: {}", CLASS_NAME, methodName, id, e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public ResponseEntity<?> delete(Integer id) throws Exception {
        String methodName = "delete";
        log.debug("{} : {} :: Started with id: {}", CLASS_NAME, methodName, id);
        try {
            productService.deleteProduct(id);
            log.info("{} : {} :: Successfully deleted product for id: {}", CLASS_NAME, methodName, id);
            return CommonUtil.createBuildResponseMessage("Product deleted successfully", HttpStatus.OK);
        } catch (Exception e) {
            log.error("{} : {} :: Failed to delete product for id: {}, error: {}", CLASS_NAME, methodName, id, e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public ResponseEntity<?> getByCategoryId(Integer categoryId) throws Exception {
        String methodName = "getByCategoryId";
        log.debug("{} : {} :: Started with categoryId: {}", CLASS_NAME, methodName, categoryId);
        try {
            List<ProductDTO> products = productService.getProductsByCategoryId(categoryId);
            log.info("{} : {} :: Successfully retrieved {} products for categoryId: {}", CLASS_NAME, methodName, products.size(), categoryId);
            return CommonUtil.createBuildResponse(products, HttpStatus.OK);
        } catch (Exception e) {
            log.error("{} : {} :: Failed to retrieve products for categoryId: {}, error: {}", CLASS_NAME, methodName, categoryId, e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public ResponseEntity<?> getByFarmerId(Integer farmerId) throws Exception {
        String methodName = "getByFarmerId";
        log.debug("{} : {} :: Started with farmerId: {}", CLASS_NAME, methodName, farmerId);
        try {
            List<ProductDTO> products = productService.getProductsByFarmerId(farmerId);
            log.info("{} : {} :: Successfully retrieved {} products for farmerId: {}", CLASS_NAME, methodName, products.size(), farmerId);
            return CommonUtil.createBuildResponse(products, HttpStatus.OK);
        } catch (Exception e) {
            log.error("{} : {} :: Failed to retrieve products for farmerId: {}, error: {}", CLASS_NAME, methodName, farmerId, e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public ResponseEntity<?> searchProducts(String name, Integer categoryId, Integer farmerId, Boolean available, Unit unit, Double priceMin, Double priceMax) {
        String methodName = "searchProducts";
        log.debug("{} : {} :: Started with name: {}, categoryId: {}, farmerId: {}, available: {}, unit: {}, priceMin: {}, priceMax: {}",
                CLASS_NAME, methodName, name, categoryId, farmerId, available, unit, priceMin, priceMax);
        List<ProductDTO> products = productService.searchProducts(name, categoryId, farmerId, available, unit, priceMin, priceMax);
        log.info("{} : {} :: Successfully retrieved {} products", CLASS_NAME, methodName, products.size());
        return CommonUtil.createBuildResponse(products, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> getAllPaged(PaginationRequest paginationRequest) {
        String methodName = "getAllPaged";
        log.debug("{} : {} :: Started with paginationRequest: {}", CLASS_NAME, methodName, paginationRequest);
        Page<ProductDTO> products = productService.getAllProductsPaged(paginationRequest);
        log.info("{} : {} :: Successfully retrieved paged products, page: {}, size: {}", CLASS_NAME, methodName, products.getNumber(), products.getSize());
        PaginatedResponse<ProductDTO> response = PaginatedResponse.fromPage(products);
        return CommonUtil.createBuildResponse(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> getAllAvailablePaged(PaginationRequest paginationRequest) {
        String methodName = "getAllAvailablePaged";
        log.debug("{} : {} :: Started with paginationRequest: {}", CLASS_NAME, methodName, paginationRequest);
        Page<ProductDTO> products = productService.getAllAvailableProductsPaged(paginationRequest);
        log.info("{} : {} :: Successfully retrieved paged available products, page: {}, size: {}", CLASS_NAME, methodName, products.getNumber(), products.getSize());
        PaginatedResponse<ProductDTO> response = PaginatedResponse.fromPage(products);
        return CommonUtil.createBuildResponse(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> getByCategoryIdPaged(Integer categoryId, PaginationRequest paginationRequest) throws Exception {
        String methodName = "getByCategoryIdPaged";
        log.debug("{} : {} :: Started with categoryId: {}, paginationRequest: {}", CLASS_NAME, methodName, categoryId, paginationRequest);
        try {
            Page<ProductDTO> products = productService.getProductsByCategoryIdPaged(categoryId, paginationRequest);
            log.info("{} : {} :: Successfully retrieved paged products for categoryId: {}, page: {}, size: {}",
                    CLASS_NAME, methodName, categoryId, products.getNumber(), products.getSize());
            PaginatedResponse<ProductDTO> response = PaginatedResponse.fromPage(products);
            return CommonUtil.createBuildResponse(response, HttpStatus.OK);
        } catch (Exception e) {
            log.error("{} : {} :: Failed to retrieve paged products for categoryId: {}, error: {}", CLASS_NAME, methodName, categoryId, e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public ResponseEntity<?> getByFarmerIdPaged(Integer farmerId, PaginationRequest paginationRequest) throws Exception {
        String methodName = "getByFarmerIdPaged";
        log.debug("{} : {} :: Started with farmerId: {}, paginationRequest: {}", CLASS_NAME, methodName, farmerId, paginationRequest);
        try {
            Page<ProductDTO> products = productService.getProductsByFarmerIdPaged(farmerId, paginationRequest);
            log.info("{} : {} :: Successfully retrieved paged products for farmerId: {}, page: {}, size: {}",
                    CLASS_NAME, methodName, farmerId, products.getNumber(), products.getSize());
            PaginatedResponse<ProductDTO> response = PaginatedResponse.fromPage(products);
            return CommonUtil.createBuildResponse(response, HttpStatus.OK);
        } catch (Exception e) {
            log.error("{} : {} :: Failed to retrieve paged products for farmerId: {}, error: {}", CLASS_NAME, methodName, farmerId, e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public ResponseEntity<?> searchProductsPaged(PaginationRequest paginationRequest, String name, Integer categoryId,
                                                 Integer farmerId, Boolean available, Unit unit, Double priceMin, Double priceMax) {
        String methodName = "searchProductsPaged";
        log.debug("{} : {} :: Started with paginationRequest: {}, name: {}, categoryId: {}, farmerId: {}, available: {}, unit: {}, priceMin: {}, priceMax: {}",
                CLASS_NAME, methodName, paginationRequest, name, categoryId, farmerId, available, unit, priceMin, priceMax);
        Page<ProductDTO> products = productService.searchProductsPaged(paginationRequest, name, categoryId, farmerId, available, unit, priceMin, priceMax);
        log.info("{} : {} :: Successfully retrieved paged products, page: {}, size: {}", CLASS_NAME, methodName, products.getNumber(), products.getSize());
        PaginatedResponse<ProductDTO> response = PaginatedResponse.fromPage(products);
        return CommonUtil.createBuildResponse(response, HttpStatus.OK);
    }
}