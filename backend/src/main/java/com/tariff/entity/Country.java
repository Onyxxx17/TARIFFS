package com.tariff.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;

import jakarta.persistence.Id;


@Entity
public class Country {
    @Id @GeneratedValue
    private Long id;

    private String name;

    private String isoCode;

    private String currency;

    // default constructor: required by JPA
    public Country() {
        
    }

    public Country(String name, String isoCode, String currency) {
        this.name = name;
        this.isoCode = isoCode;
        this.currency = currency;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIsoCode() {
        return isoCode;
    }

    public void setIsoCode(String isoCode) {
        this.isoCode = isoCode;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    // Optional: toString method for debugging
    @Override
    public String toString() {
        return "Country{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", isoCode='" + isoCode + '\'' +
                ", currency='" + currency + '\'' +
                '}';
    }
}
