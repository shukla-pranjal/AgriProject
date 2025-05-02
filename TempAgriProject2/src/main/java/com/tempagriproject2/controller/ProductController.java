package com.tempagriproject2.controller;

import com.tempagriproject2.dto.ProductDTO;
import com.tempagriproject2.endpoint.ProductEndpoint;
import com.tempagriproject2.service.ProductService;
import com.tempagriproject2.util.CommonUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequiredArgsConstructor
public class ProductController implements ProductEndpoint {

    private final ProductService productService;

    @Override
    public ResponseEntity<?> getAll() {
        List<ProductDTO> products = productService.getAllProducts();
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
}
