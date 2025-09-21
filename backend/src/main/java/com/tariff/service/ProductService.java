package com.tariff.service;

import java.util.List;

import com.tariff.entity.Product;

public interface ProductService {

    List<Product> listProduct();

    Product getProduct(Long id);

    List<Product> getProductsByIndustryId(Long industryId);

    Product addProduct(Product product);

    Product addProductByIndustry(Long industryId, Product product);

    Product updateProduct(Long id, Product product);

    void deleteProduct(Long id);
}
