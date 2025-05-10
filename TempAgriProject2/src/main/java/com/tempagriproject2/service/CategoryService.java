package com.tempagriproject2.service;

import com.tempagriproject2.dto.CategoryDTO;

import java.util.List;

public interface CategoryService {

    List<CategoryDTO> getAllCategories();

    CategoryDTO getCategoryById(Integer id) throws Exception;

    CategoryDTO createCategory(CategoryDTO categoryDTO) throws Exception;

    CategoryDTO updateCategory(Integer id, CategoryDTO categoryDTO) throws Exception;

    void deleteCategory(Integer id) throws Exception;
}
