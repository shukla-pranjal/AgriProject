package com.farmflow.service;

import com.farmflow.dto.CategoryDTO;
import com.farmflow.dto.PaginationRequest;
import org.springframework.data.domain.Page;

import java.util.List;

public interface CategoryService {

    List<CategoryDTO> getAllCategories();

    CategoryDTO getCategoryById(Integer id) throws Exception;

    CategoryDTO createCategory(CategoryDTO categoryDTO) throws Exception;

    CategoryDTO updateCategory(Integer id, CategoryDTO categoryDTO) throws Exception;

    void deleteCategory(Integer id) throws Exception;

    List<CategoryDTO> searchCategories(String name, String description);

    Page<CategoryDTO> getAllCategoriesPaged(PaginationRequest paginationRequest);

    Page<CategoryDTO> searchCategoriesPaged(PaginationRequest paginationRequest, String name, String description);
}
