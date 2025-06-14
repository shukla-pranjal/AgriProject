package com.farmflow.controller;

import com.farmflow.dto.CategoryDTO;
import com.farmflow.dto.PaginatedResponse;
import com.farmflow.dto.PaginationRequest;
import com.farmflow.endpoint.CategoryEndpoint;
import com.farmflow.service.CategoryService;
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
public class CategoryController implements CategoryEndpoint {

    private final CategoryService categoryService;

    @Override
    public ResponseEntity<?> getAll() {
        List<CategoryDTO> categories = categoryService.getAllCategories();
        return CommonUtil.createBuildResponse(categories, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> getById(Integer id) throws Exception {
        CategoryDTO category = categoryService.getCategoryById(id);
        return CommonUtil.createBuildResponse(category, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> create(CategoryDTO categoryDTO) throws Exception {
        CategoryDTO created = categoryService.createCategory(categoryDTO);
        return CommonUtil.createBuildResponse(created, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<?> update(Integer id, CategoryDTO categoryDTO) throws Exception {
        CategoryDTO updated = categoryService.updateCategory(id, categoryDTO);
        return CommonUtil.createBuildResponse(updated, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> delete(Integer id) throws Exception {
        categoryService.deleteCategory(id);
        return CommonUtil.createBuildResponseMessage("Category deleted successfully", HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> searchCategories(String name, String description) {
        List<CategoryDTO> categories = categoryService.searchCategories(name, description);
        return CommonUtil.createBuildResponse(categories, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> getAllCategoriesPaged(PaginationRequest paginationRequest) {
        Page<CategoryDTO> page = categoryService.getAllCategoriesPaged(paginationRequest);
        PaginatedResponse<CategoryDTO> paginatedResponse = PaginatedResponse.fromPage(page);
        return CommonUtil.createBuildResponse(paginatedResponse, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> searchCategoriesPaged(PaginationRequest paginationRequest, String name, String description) {
        Page<CategoryDTO> page = categoryService.searchCategoriesPaged(paginationRequest, name, description);
        PaginatedResponse<CategoryDTO> paginatedResponse = PaginatedResponse.fromPage(page);
        return CommonUtil.createBuildResponse(paginatedResponse, HttpStatus.OK);
    }
}