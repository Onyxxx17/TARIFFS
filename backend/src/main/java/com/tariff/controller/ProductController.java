package com.tariff.controller;

import com.tariff.entity.Product;
import com.tariff.service.ProductService;
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
    public Product getProductById(@PathVariable Long id) {
        return productService.getProduct(id);
    }
    
    @GetMapping("/industry/{industryId}")
    public List<Product> getProductsByIndustry(@PathVariable Long industryId) {
        return productService.getProductsByIndustryId(industryId);
    }
    
    @PostMapping
    public Product createProduct(@RequestBody Product product) {
        return productService.addProduct(product);
    }
    
    @PostMapping("/industry/{industryId}")
    public Product createProductWithIndustry(
            @PathVariable Long industryId,
            @RequestBody Product product) {
        return productService.addProductByIndustry(industryId, product);
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