package com.tariff.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.tariff.entity.Product;
import com.tariff.repository.ProductRepository;

@Component
public class ProductLoader implements CommandLineRunner{
    @Autowired
    private ProductRepository productRepository;

    @Override 
    public void run(String... args) throws Exception {

    }
}
