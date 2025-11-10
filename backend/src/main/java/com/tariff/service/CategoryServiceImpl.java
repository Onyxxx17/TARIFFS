package com.tariff.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tariff.entity.Category;
import com.tariff.exception.CategoryNotFoundException;
import com.tariff.repository.CategoryRepository;

@Service
@Transactional
public class CategoryServiceImpl implements CategoryService {
    
    private CategoryRepository categoryRepository;
    
    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }
    
    @Override
    public List<Category> listCategory() {
        return categoryRepository.findAll();
    }
    
    @Override
    public Category getCategory(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException(id));
    }
    
    @Override
    public Category addCategory(Category Category) {
        return categoryRepository.save(Category);
    }
    
    @Override
    public Category updateCategory(Long id, Category updatedCategory) {
        return categoryRepository.findById(id).map(existingCategory -> {
            existingCategory.setName(updatedCategory.getName());
            return categoryRepository.save(existingCategory);
        }).orElseThrow(() -> new CategoryNotFoundException(id));
    }
    
    @Override
    public void deleteCategory(Long id) {
        if (!categoryRepository.existsById(id)) {
            throw new CategoryNotFoundException(id);
        }
        categoryRepository.deleteById(id);
    }
}