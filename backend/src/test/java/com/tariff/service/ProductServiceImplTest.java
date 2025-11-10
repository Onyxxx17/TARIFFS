package com.tariff.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.tariff.entity.Product;
import com.tariff.entity.Category;
import com.tariff.exception.ProductNotFoundException;
import com.tariff.exception.CategoryNotFoundException;
import com.tariff.repository.ProductRepository;
import com.tariff.repository.CategoryRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private ProductServiceImpl productService;

    private Product product;
    private Category category;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        category = new Category();
        category.setId(1L);
        category.setName("Electronics");

        product = new Product();
        product.setId(1L);
        product.setName("Laptop");
        product.setCategory(category);
    }

    // --- LIST / GET ---
    @Test
    void testListProduct() {
        when(productRepository.findAll()).thenReturn(Arrays.asList(product));
        List<Product> products = productService.listProduct();
        assertEquals(1, products.size());
        verify(productRepository).findAll();
    }

    @Test
    void testGetProduct_Success() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        Product result = productService.getProduct(1L);
        assertEquals(product, result);
    }

    @Test
    void testGetProduct_NotFound() {
        when(productRepository.findById(2L)).thenReturn(Optional.empty());
        assertThrows(ProductNotFoundException.class, () -> productService.getProduct(2L));
    }

    // --- GET BY CATEGORY ---
    @Test
    void testGetProductsByCategoryId_Success() {
        when(categoryRepository.existsById(1L)).thenReturn(true);
        when(productRepository.findByCategoryId(1L)).thenReturn(Arrays.asList(product));

        List<Product> products = productService.getProductsByCategoryId(1L);
        assertEquals(1, products.size());
    }

    @Test
    void testGetProductsByCategoryId_CategoryNotFound() {
        when(categoryRepository.existsById(1L)).thenReturn(false);
        assertThrows(CategoryNotFoundException.class, () -> productService.getProductsByCategoryId(1L));
    }

    // --- ADD ---
    @Test
    void testAddProduct_Success() {
        when(productRepository.save(product)).thenReturn(product);
        Product saved = productService.addProduct(product);
        assertEquals(product, saved);
    }

    // --- UPDATE ---
    @Test
    void testUpdateProduct_Success() {
        Product updatedProduct = new Product();
        updatedProduct.setName("Updated Laptop");

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(productRepository.save(product)).thenReturn(product);

        Product result = productService.updateProduct(1L, updatedProduct);
        assertEquals("Updated Laptop", result.getName());
    }

    @Test
    void testUpdateProduct_NotFound() {
        when(productRepository.findById(2L)).thenReturn(Optional.empty());
        assertThrows(ProductNotFoundException.class, () -> productService.updateProduct(2L, product));
    }

    // --- DELETE ---
    @Test
    void testDeleteProduct_Success() {
        when(productRepository.existsById(1L)).thenReturn(true);
        assertDoesNotThrow(() -> productService.deleteProduct(1L));
        verify(productRepository).deleteById(1L);
    }

    @Test
    void testDeleteProduct_NotFound() {
        when(productRepository.existsById(2L)).thenReturn(false);
        assertThrows(ProductNotFoundException.class, () -> productService.deleteProduct(2L));
    }
}
