package com.tariff.controller;

import com.tariff.entity.Product;
import com.tariff.service.ProductService;

import io.swagger.v3.oas.annotations.Parameter;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    
    private ProductService productService;
    
    public ProductController(ProductService productService) {
        this.productService = productService;
    }
    
    @GetMapping
    public List<Product> getAllProducts() {
        return productService.listProduct();
    }
    
    @GetMapping("/{id}")
    public Product getProductById(
        @Parameter(description = "ID of Product", example = "10129")
        @PathVariable Long id) {
        return productService.getProduct(id);
    }
    
    @GetMapping("/category/{categoryId}")
    public List<Product> getProductsByCategory(
        @Parameter(description = "ID of Category", example = "1")
        @PathVariable Long categoryId) {
        return productService.getProductsByCategoryId(categoryId);
    }
    
    @PostMapping
    public Product createProduct(@RequestBody Product product) {
        return productService.addProduct(product);
    }
    
    @PostMapping("/category/{categoryId}")
    public Product createProductWithCategory(
            @PathVariable Long categoryId,
            @RequestBody Product product) {
        return productService.addProductByCategory(categoryId, product);
    }
    
    @PutMapping("/{id}")
    public Product updateProduct(@PathVariable Long id, @RequestBody Product product) {
        return productService.updateProduct(id, product);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.ok().build();
    }
}