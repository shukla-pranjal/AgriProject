package com.agriproject.service;

import java.util.List;

import com.agriproject.dto.CategoryDTO;


public interface CategoryService {

    List<CategoryDTO> getAllCategories();

    CategoryDTO getCategoryById(Long id) throws Exception;

    CategoryDTO createCategory(CategoryDTO categoryDTO);

    CategoryDTO updateCategory(Long id, CategoryDTO categoryDTO) throws Exception;

    void deleteCategory(Long id) throws Exception;
    
}