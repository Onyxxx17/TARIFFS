package com.tariff.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tariff.entity.Product;
import com.tariff.exception.CountryNotFoundException;
import com.tariff.exception.IndustryNotFoundException;
import com.tariff.exception.ProductNotFoundException;
import com.tariff.repository.CountryRepository;
import com.tariff.repository.IndustryRepository;
import com.tariff.repository.ProductRepository;

@Service
@Transactional
public class ProductServiceImpl implements ProductService {
    
    private ProductRepository productRepository;
    private IndustryRepository industryRepository;
    
    public ProductServiceImpl(ProductRepository productRepository,
                            IndustryRepository industryRepository) {
        this.productRepository = productRepository;
        this.industryRepository = industryRepository;
    }
    
    @Override
    public List<Product> listProduct() {
        return productRepository.findAll();
    }
    
    @Override
    public Product getProduct(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
    }
    

    @Override
    public List<Product> getProductsByIndustryId(Long industryId) {
        if (!industryRepository.existsById(industryId)) {
            throw new IndustryNotFoundException(industryId);
        }
        return productRepository.findByIndustryId(industryId);
    }
    
    @Override
    public Product addProductByIndustry(Long industryId, Product product) {
        return industryRepository.findById(industryId).map(industry -> {
            product.setIndustry(industry);
            return productRepository.save(product);
        }).orElseThrow(() -> new IndustryNotFoundException(industryId));
    }
    
   
    @Override
    public Product addProduct(Product product) {
        return productRepository.save(product);
    }
    
    @Override
    public Product updateProduct(Long id, Product product) {
        return productRepository.findById(id).map(existingProduct -> {
            existingProduct.setName(product.getName());
            existingProduct.setDescription(product.getDescription());
            return productRepository.save(existingProduct);
        }).orElseThrow(() -> new ProductNotFoundException(id));
    }
    
    @Override
    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new ProductNotFoundException(id);
        }
        productRepository.deleteById(id);
    }

}