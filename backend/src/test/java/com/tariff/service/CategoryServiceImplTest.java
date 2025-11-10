package com.tariff.service;

import com.tariff.entity.Category;
import com.tariff.exception.CategoryNotFoundException;
import com.tariff.repository.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryServiceImplTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    private Category category1;
    private Category category2;

    @BeforeEach
    void setUp() {
        category1 = new Category(1L, "Electronics");
        category2 = new Category(2L, "Books");
    }

    @Test
    void testListCategory() {
        when(categoryRepository.findAll()).thenReturn(Arrays.asList(category1, category2));

        List<Category> result = categoryService.listCategory();

        assertEquals(2, result.size());
        verify(categoryRepository, times(1)).findAll();
    }

    @Test
    void testGetCategoryFound() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category1));

        Category result = categoryService.getCategory(1L);

        assertEquals("Electronics", result.getName());
        verify(categoryRepository, times(1)).findById(1L);
    }

    @Test
    void testGetCategoryNotFound() {
        when(categoryRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(CategoryNotFoundException.class, () -> categoryService.getCategory(99L));
        verify(categoryRepository).findById(99L);
    }

    @Test
    void testAddCategory() {
        when(categoryRepository.save(category1)).thenReturn(category1);

        Category result = categoryService.addCategory(category1);

        assertEquals(category1, result);
        verify(categoryRepository, times(1)).save(category1);
    }

    @Test
    void testUpdateCategoryFound() {
        Category updated = new Category(1L, "New Electronics");

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category1));
        when(categoryRepository.save(any(Category.class)))
            .thenAnswer(invocation -> invocation.getArgument(0)); // return updated object

        Category result = categoryService.updateCategory(1L, updated);

        assertEquals("New Electronics", result.getName());
        verify(categoryRepository).findById(1L);
        verify(categoryRepository).save(category1);
    }

    @Test
    void testUpdateCategoryNotFound() {
        when(categoryRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(CategoryNotFoundException.class,
                () -> categoryService.updateCategory(99L, category1));
        verify(categoryRepository).findById(99L);
        verify(categoryRepository, never()).save(any());
    }

    @Test
    void testDeleteCategoryFound() {
        when(categoryRepository.existsById(1L)).thenReturn(true);
        doNothing().when(categoryRepository).deleteById(1L);

        categoryService.deleteCategory(1L);

        verify(categoryRepository).existsById(1L);
        verify(categoryRepository).deleteById(1L);
    }

    @Test
    void testDeleteCategoryNotFound() {
        when(categoryRepository.existsById(99L)).thenReturn(false);

        assertThrows(CategoryNotFoundException.class, () -> categoryService.deleteCategory(99L));
        verify(categoryRepository).existsById(99L);
        verify(categoryRepository, never()).deleteById(anyLong());
    }
}
