package com.tariff.entity;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class TariffRule {
    
    @Id
    @GeneratedValue
    private Long id;
    
    @Column(name = "from_country", nullable = false)
    private String fromCountry; // Exporter country
    
    @Column(name = "to_country", nullable = false)
    private String toCountry; // Importer country
    
    @Column(name = "industry", nullable = false)
    private String industry;
    
    @Column(name = "product")
    private String product; // Optional, if specific
    
    @Column(name = "rate", precision = 10, scale = 4, nullable = false)
    private BigDecimal rate; // Percentage
    
    @Column(name = "additional_fee", precision = 15, scale = 2)
    private BigDecimal additionalFee; // Fixed amount, optional
    
    @Column(name = "effective_date", nullable = false)
    private LocalDate effectiveDate;
    
    @Column(name = "expiry_date")
    private LocalDate expiryDate; // Optional

    // Default constructor
    public TariffRule() {}

    // Constructor with all fields
    public TariffRule(String fromCountry, String toCountry, String industry, 
                     String product, BigDecimal rate, BigDecimal additionalFee,
                     LocalDate effectiveDate, LocalDate expiryDate) {
        this.fromCountry = fromCountry;
        this.toCountry = toCountry;
        this.industry = industry;
        this.product = product;
        this.rate = rate;
        this.additionalFee = additionalFee;
        this.effectiveDate = effectiveDate;
        this.expiryDate = expiryDate;
    }

    // Constructor without optional fields
    public TariffRule(String fromCountry, String toCountry, String industry, 
                     BigDecimal rate, LocalDate effectiveDate) {
        this.fromCountry = fromCountry;
        this.toCountry = toCountry;
        this.industry = industry;
        this.rate = rate;
        this.effectiveDate = effectiveDate;
    }

    // GETTERS AND SETTERS

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFromCountry() {
        return fromCountry;
    }

    public void setFromCountry(String fromCountry) {
        this.fromCountry = fromCountry;
    }

    public String getToCountry() {
        return toCountry;
    }

    public void setToCountry(String toCountry) {
        this.toCountry = toCountry;
    }

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    public BigDecimal getAdditionalFee() {
        return additionalFee;
    }

    public void setAdditionalFee(BigDecimal additionalFee) {
        this.additionalFee = additionalFee;
    }

    public LocalDate getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(LocalDate effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDate expiryDate) {
        this.expiryDate = expiryDate;
    }

    @Override
    public String toString() {
        return "TariffRule{" +
                "id=" + id +
                ", fromCountry='" + fromCountry + '\'' +
                ", toCountry='" + toCountry + '\'' +
                ", industry='" + industry + '\'' +
                ", product='" + product + '\'' +
                ", rate=" + rate +
                ", additionalFee=" + additionalFee +
                ", effectiveDate=" + effectiveDate +
                ", expiryDate=" + expiryDate +
                '}';
    }
}