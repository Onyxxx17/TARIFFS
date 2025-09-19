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
    private CountryRepository countryRepository;
    private IndustryRepository industryRepository;
    
    public ProductServiceImpl(ProductRepository productRepository, 
                             CountryRepository countryRepository,
                             IndustryRepository industryRepository) {
        this.productRepository = productRepository;
        this.countryRepository = countryRepository;
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
    

    public List<Product> getProductsByCountryId(Long countryId) {
        if (!countryRepository.existsById(countryId)) {
            throw new CountryNotFoundException(countryId);
        }
        return productRepository.findByCountryId(countryId);
    }
    
    
    public List<Product> getProductsByIndustryId(Long industryId) {
        if (!industryRepository.existsById(industryId)) {
            throw new IndustryNotFoundException(industryId);
        }
        return productRepository.findByIndustryId(industryId);
    }
    
   
    public Product addProductByCountryAndIndustry(Long countryId, Long industryId, Product product) {
        return countryRepository.findById(countryId).map(country -> {
            return industryRepository.findById(industryId).map(industry -> {
                product.setCountry(country);
                product.setIndustry(industry);
                return productRepository.save(product);
            }).orElseThrow(() -> new IndustryNotFoundException(industryId));
        }).orElseThrow(() -> new CountryNotFoundException(countryId));
    }
    
   
    public Product addProduct(Product product) {
        return productRepository.save(product);
    }
    
    @Override
    public Product updateProduct(Long id, Product product) {
        return productRepository.findById(id).map(existingProduct -> {
            existingProduct.setName(product.getName());
            existingProduct.setHsCode(product.getHsCode());
            existingProduct.setDescription(product.getDescription());
            if (product.getCountry() != null) {
                existingProduct.setCountry(product.getCountry());
            }
            if (product.getIndustry() != null) {
                existingProduct.setIndustry(product.getIndustry());
            }
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