package com.agriproject.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.agriproject.dto.CategoryDTO;
import com.agriproject.service.CategoryService;
import com.agriproject.util.CommonUtil;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/category")
public class CategoryController {
    
    
    private final CategoryService categoryService;



    @GetMapping
    public ResponseEntity<?> getAll() {
        List<CategoryDTO> categoryList = categoryService.getAllCategories();
        return CommonUtil.createBuildResponse(categoryList, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) throws Exception {
        CategoryDTO category = categoryService.getCategoryById(id);
        return CommonUtil.createBuildResponse(category, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody CategoryDTO categoryDTO) throws Exception{
        CategoryDTO saved = categoryService.createCategory(categoryDTO);
        return CommonUtil.createBuildResponse(saved, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody CategoryDTO categoryDTO)throws Exception {
        CategoryDTO updated = categoryService.updateCategory(id, categoryDTO);
        return CommonUtil.createBuildResponse(updated, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id)throws Exception {
        categoryService.deleteCategory(id);
        return CommonUtil.createBuildResponseMessage("Category deleted successfully", HttpStatus.OK);
    }
}

