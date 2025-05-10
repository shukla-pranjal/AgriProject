package com.tempagriproject2.controller;

import com.tempagriproject2.dto.CategoryDTO;
import com.tempagriproject2.endpoint.CategoryEndpoint;
import com.tempagriproject2.service.CategoryService;
import com.tempagriproject2.util.CommonUtil;
import lombok.RequiredArgsConstructor;
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
}
