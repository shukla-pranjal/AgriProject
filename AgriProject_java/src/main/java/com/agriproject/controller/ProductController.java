package com.agriproject.controller;

import com.agriproject.dto.ProductDTO;
import com.agriproject.service.ProductService;
import com.agriproject.util.CommonUtil;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {

    
    private final ProductService productService;

    @GetMapping
    public ResponseEntity<?> getAll() {
        List<ProductDTO> productList = productService.getAllProducts();
        return CommonUtil.createBuildResponse(productList, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) throws Exception{
        ProductDTO product = productService.getProductById(id);
        return CommonUtil.createBuildResponse(product, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody ProductDTO productDTO) {
        ProductDTO created = productService.createProduct(productDTO);
        return CommonUtil.createBuildResponse(created, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody ProductDTO productDTO) throws Exception {
        ProductDTO updated = productService.updateProduct(id, productDTO);
        return CommonUtil.createBuildResponse(updated, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) throws Exception{
        productService.deleteProduct(id);
        return CommonUtil.createBuildResponseMessage("Product deleted successfully", HttpStatus.OK);
    }


    @GetMapping("/search")
public ResponseEntity<?> searchProducts(@RequestParam(required = false) String name,@RequestParam(required = false) Double minPrice,@RequestParam(required = false) Double maxPrice){

        List<ProductDTO> products = productService.searchProducts(name, minPrice, maxPrice);
        return CommonUtil.createBuildResponse(products, HttpStatus.OK);
    }


    @PatchMapping("/products/{id}/disable")
    public ResponseEntity<?> disableProduct(@PathVariable Long id) throws Exception{
        productService.disableProduct(id);
        return CommonUtil.createBuildResponseMessage("Product disabled successfully", HttpStatus.OK);
    }
    
    @PatchMapping("/products/{id}/enable")
    public ResponseEntity<?> enableProduct(@PathVariable Long id) throws Exception{
        productService.enableProduct(id);
        return CommonUtil.createBuildResponseMessage("Product enabled successfully", HttpStatus.OK);

    }
    

}
