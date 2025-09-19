package com.tariff.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class TariffRule {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(optional = false)
    @JoinColumn(name = "country_id", referencedColumnName = "id")
    private Country country;

    
    @ManyToOne
    @JoinColumn(name = "industry_id", nullable = false)
    private Industry industry;
    
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @OneToMany(mappedBy = "tariffRule", cascade = CascadeType.ALL)
    private List<ImportRecord> importRecord;

    @ManyToMany
    @JoinTable(name="country_tariff_rules", joinColumns=@JoinColumn(name="country_id"),
    inverseJoinColumns= @JoinColumn(name="tariff_rule_id"))
    private Set<Country> countries = new HashSet<>();

    
    @Column(name = "rate", precision = 10, scale = 4, nullable = false)
    private BigDecimal rate; // Percentage
    
    @Column(name = "additional_fee", precision = 15, scale = 2)
    private BigDecimal additionalFee; // Fixed amount, optional
    
    @Column(name = "effective_date", nullable = false)
    private LocalDate effectiveDate;
    
    @Column(name = "expiry_date")
    private LocalDate expiryDate; // Optional


    // Constructor with all fields
    public TariffRule(Country country, Industry industry, 
                     Product product, BigDecimal rate, BigDecimal additionalFee,
                     LocalDate effectiveDate, LocalDate expiryDate) {
        this.country = country;
        this.industry = industry;
        this.product = product;
        this.rate = rate;
        this.additionalFee = additionalFee;
        this.effectiveDate = effectiveDate;
        this.expiryDate = expiryDate;
    }

    // Constructor without optional fields
    public TariffRule(Country country, Industry industry, 
                     BigDecimal rate, LocalDate effectiveDate) {
        this.country = country;
        this.industry = industry;
        this.rate = rate;
        this.effectiveDate = effectiveDate;
    }


}