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
@Slf4j
public class CategoryController implements CategoryEndpoint {

    private final CategoryService categoryService;
    private static final String CLASS_NAME = CategoryController.class.getSimpleName();

    @Override
    public ResponseEntity<?> getAll() {
        String methodName = "getAll";
        log.debug("{} : {} :: Started", CLASS_NAME, methodName);
        List<CategoryDTO> categories = categoryService.getAllCategories();
        log.info("{} : {} :: Successfully retrieved {} categories", CLASS_NAME, methodName, categories.size());
        return CommonUtil.createBuildResponse(categories, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> getById(Integer id) throws Exception {
        String methodName = "getById";
        log.debug("{} : {} :: Started with id: {}", CLASS_NAME, methodName, id);
        try {
            CategoryDTO category = categoryService.getCategoryById(id);
            log.info("{} : {} :: Successfully retrieved category for id: {}", CLASS_NAME, methodName, id);
            return CommonUtil.createBuildResponse(category, HttpStatus.OK);
        } catch (Exception e) {
            log.error("{} : {} :: Failed to retrieve category for id: {}, error: {}", CLASS_NAME, methodName, id, e.getMessage());
            throw e;
        }
    }

    @Override
    public ResponseEntity<?> create(CategoryDTO categoryDTO) throws Exception {
        String methodName = "create";
        log.debug("{} : {} :: Started with categoryDTO: {}", CLASS_NAME, methodName, categoryDTO);
        try {
            CategoryDTO created = categoryService.createCategory(categoryDTO);
            log.info("{} : {} :: Successfully created category", CLASS_NAME, methodName);
            return CommonUtil.createBuildResponse(created, HttpStatus.CREATED);
        } catch (Exception e) {
            log.error("{} : {} :: Failed to create category, error: {}", CLASS_NAME, methodName, e.getMessage());
            throw e;
        }
    }

    @Override
    public ResponseEntity<?> update(Integer id, CategoryDTO categoryDTO) throws Exception {
        String methodName = "update";
        log.debug("{} : {} :: Started with id: {}, categoryDTO: {}", CLASS_NAME, methodName, id, categoryDTO);
        try {
            CategoryDTO updated = categoryService.updateCategory(id, categoryDTO);
            log.info("{} : {} :: Successfully updated category for id: {}", CLASS_NAME, methodName, id);
            return CommonUtil.createBuildResponse(updated, HttpStatus.OK);
        } catch (Exception e) {
            log.error("{} : {} :: Failed to update category for id: {}, error: {}", CLASS_NAME, methodName, id, e.getMessage());
            throw e;
        }
    }

    @Override
    public ResponseEntity<?> delete(Integer id) throws Exception {
        String methodName = "delete";
        log.debug("{} : {} :: Started with id: {}", CLASS_NAME, methodName, id);
        try {
            categoryService.deleteCategory(id);
            log.info("{} : {} :: Successfully deleted category for id: {}", CLASS_NAME, methodName, id);
            return CommonUtil.createBuildResponseMessage("Category deleted successfully", HttpStatus.OK);
        } catch (Exception e) {
            log.error("{} : {} :: Failed to delete category for id: {}, error: {}", CLASS_NAME, methodName, id, e.getMessage());
            throw e;
        }
    }

    @Override
    public ResponseEntity<?> searchCategories(String name, String description) {
        String methodName = "searchCategories";
        log.debug("{} : {} :: Started with name: {}, description: {}", CLASS_NAME, methodName, name, description);
        List<CategoryDTO> categories = categoryService.searchCategories(name, description);
        log.info("{} : {} :: Successfully retrieved {} categories", CLASS_NAME, methodName, categories.size());
        return CommonUtil.createBuildResponse(categories, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> getAllCategoriesPaged(PaginationRequest paginationRequest) {
        String methodName = "getAllCategoriesPaged";
        log.debug("{} : {} :: Started with paginationRequest: {}", CLASS_NAME, methodName, paginationRequest);
        Page<CategoryDTO> page = categoryService.getAllCategoriesPaged(paginationRequest);
        log.info("{} : {} :: Successfully retrieved paged categories, page: {}, size: {}", CLASS_NAME, methodName, page.getNumber(), page.getSize());
        PaginatedResponse<CategoryDTO> paginatedResponse = PaginatedResponse.fromPage(page);
        return CommonUtil.createBuildResponse(paginatedResponse, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> searchCategoriesPaged(PaginationRequest paginationRequest, String name, String description) {
        String methodName = "searchCategoriesPaged";
        log.debug("{} : {} :: Started with paginationRequest: {}, name: {}, description: {}", CLASS_NAME, methodName, paginationRequest, name, description);
        Page<CategoryDTO> page = categoryService.searchCategoriesPaged(paginationRequest, name, description);
        log.info("{} : {} :: Successfully retrieved paged categories, page: {}, size: {}", CLASS_NAME, methodName, page.getNumber(), page.getSize());
        PaginatedResponse<CategoryDTO> paginatedResponse = PaginatedResponse.fromPage(page);
        return CommonUtil.createBuildResponse(paginatedResponse, HttpStatus.OK);
    }
}