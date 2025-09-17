package com.tariff.entity;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class TariffRule {
    
    @Id
    @GeneratedValue
    private Long id;
    
    @ManyToOne(optional = false)
    @JoinColumn(name = "from_country_id", referencedColumnName = "id")
    private Country fromCountry;

    @ManyToOne(optional = false)
    @JoinColumn(name = "to_country_id", referencedColumnName = "id")
    private Country toCountry;
    
    @ManyToOne
    @JoinColumn(name = "industry_id", nullable = false)
    private Industry industry;
    
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
    
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
    public TariffRule(Country fromCountry, Country toCountry, Industry industry, 
                     Product product, BigDecimal rate, BigDecimal additionalFee,
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
    public TariffRule(Country fromCountry, Country toCountry, Industry industry, 
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

    public Country getFromCountry() {
        return fromCountry;
    }

    public void setFromCountry(Country fromCountry) {
        this.fromCountry = fromCountry;
    }

    public Country getToCountry() {
        return toCountry;
    }

    public void setToCountry(Country toCountry) {
        this.toCountry = toCountry;
    }

    public Industry getIndustry() {
        return industry;
    }

    public void setIndustry(Industry industry) {
        this.industry = industry;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
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