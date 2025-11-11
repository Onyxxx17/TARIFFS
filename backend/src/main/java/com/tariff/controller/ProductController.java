package com.tariff.controller;

import com.tariff.entity.Product;
import com.tariff.service.ProductService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@Tag(name = "Products", description = "Product management endpoints")
public class ProductController {
    
    private ProductService productService;
    
    public ProductController(ProductService productService) {
        this.productService = productService;
    }
    
    @GetMapping
    @Operation(summary = "Get all products", description = "Returns a list of all products")
    public List<Product> getAllProducts() {
        return productService.listProduct();
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Get product by ID", description = "Returns a specific product by its ID")
    public Product getProductById(
        @Parameter(description = "ID of Product", example = "10129")
        @PathVariable Long id) {
        return productService.getProduct(id);
    }
    
    @GetMapping("/category/{categoryId}")
    @Operation(summary = "Get products by category", description = "Returns products filtered by category ID")
    public List<Product> getProductsByCategory(
        @Parameter(description = "ID of Category", example = "1")
        @PathVariable Long categoryId) {
        return productService.getProductsByCategoryId(categoryId);
    }
    
    @PostMapping
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "Create a new product (Admin only)", description = "Creates a new product - requires ADMIN role")
    public Product createProduct(@RequestBody Product product) {
        return productService.addProduct(product);
    }
    
    @PutMapping("/{id}")
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "Update a product (Admin only)", description = "Updates an existing product - requires ADMIN role")
    public Product updateProduct(@PathVariable Long id, @RequestBody Product product) {
        return productService.updateProduct(id, product);
    }
    
    @DeleteMapping("/{id}")
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "Delete a product (Admin only)", description = "Deletes a product by ID - requires ADMIN role")
    public ResponseEntity<?> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.ok().build();
    }
}