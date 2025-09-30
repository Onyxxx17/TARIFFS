package com.tariff.controller;

import com.tariff.entity.Category;
import com.tariff.service.CategoryService;

import io.swagger.v3.oas.annotations.Parameter;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/industries")
public class CategoryController {
    
    private CategoryService categoryService;
    
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }
    
    @GetMapping
    public List<Category> getAllIndustries() {
        return categoryService.listCategory();
    }
    
    @GetMapping("/{id}")
    public Category getCategoryById(
        @Parameter(description = "ID of the Category", example = "1")
        @PathVariable Long id) {
        return categoryService.getCategory(id);
    }
    
    @PostMapping
    public Category createCategory(@RequestBody Category category) {
        return categoryService.addCategory(category);
    }
    
    @PutMapping("/{id}")
    public Category updateCategory(@PathVariable Long id, @RequestBody Category category) {
        return categoryService.updateCategory(id, category);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.ok().build();
    }
}