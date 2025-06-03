package com.farmflow.service.impl;

import com.farmflow.dto.CategoryDTO;
import com.farmflow.dto.PaginationRequest;
import com.farmflow.entity.Category;
import com.farmflow.exception.DuplicateResourceException;
import com.farmflow.exception.ResourceNotFoundException;
import com.farmflow.exception.ValidationException;
import com.farmflow.repository.CategoryRepository;
import com.farmflow.repository.ProductRepository;
import com.farmflow.service.CategoryService;
import com.farmflow.util.Validation;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;
    private final Validation validation;

    @Override
    public List<CategoryDTO> getAllCategories() {
        return categoryRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public CategoryDTO getCategoryById(Integer id) throws Exception{
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with ID: " + id));
        return convertToDTO(category);
    }

    @Override
    public CategoryDTO createCategory(CategoryDTO categoryDTO) {
        // Custom validation logic
        try {
            validation.categoryValidate(categoryDTO);
        } catch (Exception ex) {
            throw new ValidationException("Invalid category data: " + ex.getMessage());
        }

        // Check if category already exists
        if (categoryRepository.existsByNameIgnoreCase(categoryDTO.getName())) {
            throw new DuplicateResourceException("Category with name '" + categoryDTO.getName() + "' already exists.");
        }

        Category category = modelMapper.map(categoryDTO, Category.class);
        Category savedCategory = categoryRepository.save(category);
        return convertToDTO(savedCategory);
    }

    @Override
    public CategoryDTO updateCategory(Integer id, CategoryDTO categoryDTO) throws Exception {
        try {
            validation.categoryValidate(categoryDTO);
        } catch (Exception ex) {
            throw new ValidationException("Invalid category data: " + ex.getMessage());
        }

        Category existingCategory = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with ID: " + id));

        // Prevent duplicate category name (only if name is changed)
        if (!existingCategory.getName().equalsIgnoreCase(categoryDTO.getName()) &&
                categoryRepository.existsByNameIgnoreCase(categoryDTO.getName())) {
            throw new DuplicateResourceException("Category with name '" + categoryDTO.getName() + "' already exists.");
        }

        modelMapper.map(categoryDTO, existingCategory);
        Category updatedCategory = categoryRepository.save(existingCategory);
        return convertToDTO(updatedCategory);
    }

    @Override
    public List<CategoryDTO> searchCategories(String name, String description) {
        List<Category> categories = categoryRepository.searchCategories(name, description);
        return categories.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Page<CategoryDTO> getAllCategoriesPaged(PaginationRequest paginationRequest) {
        Pageable pageable = paginationRequest.toPageable();
        return categoryRepository.findAll(pageable).map(this::convertToDTO);
    }

    @Override
    public Page<CategoryDTO> searchCategoriesPaged(PaginationRequest paginationRequest, String name, String description) {
        Pageable pageable = paginationRequest.toPageable();
        Page<Category> page = categoryRepository.searchCategoriesPaged(name, description, pageable);
        return page.map(this::convertToDTO);
    }

    @Override
    public void deleteCategory(Integer id) throws Exception{
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with ID: " + id));
        if (!productRepository.findByCategoryId(category.getId()).isEmpty()){
            throw new ResourceNotFoundException("Cannot delete category; it is used by existing products");
        }
        categoryRepository.delete(category);
    }

    private CategoryDTO convertToDTO(Category category) {
        return modelMapper.map(category, CategoryDTO.class);
    }
}
