package com.tariff.service;

import java.util.List;

import com.tariff.entity.Category;

public interface CategoryService {
    List<Category> listCategory();
    Category getCategory(Long id);
    Category addCategory(Category category);
    Category updateCategory(Long id, Category category);
    void deleteCategory(Long category);
}
