package com.tariff.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tariff.entity.Product;
import com.tariff.repository.ProductRepository;


@RestController
@RequestMapping("api/products")
public class ProductController {
    @Autowired
    private ProductRepository productRepository;

   // GET all product
    @GetMapping
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    // GET Product by ID
    @GetMapping("/{id}")
    public Product getProductById(@PathVariable Long id) {
        return productRepository.findById(id).orElse(null);
    }

    // POST create new Product
    @PostMapping
    public Product createProduct(@RequestBody Product Product) {
        return productRepository.save(Product);
    }

    // PUT update Product
    // @PutMapping("/{id}")
    // public Product updateProduct(@PathVariable Long id, @RequestBody Product Product) {
    //     Product.setId(id);
    //     return productRepository.save(Product);
    // }

    // DELETE Product
    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable Long id) {
        productRepository.deleteById(id);
    }
}
