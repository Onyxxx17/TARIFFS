package com.tariff.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Product {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "hs_code", nullable = false, length = 10)
    private String hsCode;

    
    private String name;

    @Column(columnDefinition = "TEXT") //Description will be optional
    private String description;

    @ManyToOne
    @JoinColumn(name = "industry_id", nullable = false)
    private Industry industry;

    private double basePrice;
    // default constructor: required by JPA
    public Product() {
        
    }

    public Product(String hsCode, String name, String description, Industry industry, double basePrice){
        this.hsCode = hsCode;
        this.name = name;
        this.description = description;
        this.industry = industry;
        this.basePrice = basePrice;
    }

    // getters & setters

    public Long getId() {
        return id;
    }

    public String getHsCode() {
        return hsCode;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setHsCode(String hsCode) {
        this.hsCode = hsCode;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", hsCode='" + hsCode + '\'' +
                ", description='" + description + '\'' +
                ", industry=" + industry + '\'' +
                ", basePrice=" + basePrice +
                '}';
    }

}
