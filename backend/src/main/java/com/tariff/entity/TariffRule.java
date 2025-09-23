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
    
    
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    // @ManyToMany
    // @JoinTable(name="country_tariff_rules", joinColumns=@JoinColumn(name="country_id"),
    // inverseJoinColumns= @JoinColumn(name="tariff_rule_id"))
    // private Set<Country> countries = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "from_country_id")
    private Country fromCountry;

    @ManyToOne
    @JoinColumn(name = "to_country_id")
    private Country toCountry;

    
    @Column(name = "rate", precision = 10, scale = 4)
    private BigDecimal rate; // Percentage
    
    @Column(name = "additional_fee", precision = 15, scale = 2)
    private BigDecimal additionalFee; // Fixed amount, optional
    
    @Column(name = "effective_year", columnDefinition = "INTEGER")
    private int effectiveYear;
    

    // Constructor with all fields
    public TariffRule(BigDecimal rate, BigDecimal additionalFee,
                     int effectiveYear) {
        
        this.rate = rate;
        this.additionalFee = additionalFee;
        this.effectiveYear = effectiveYear;
       
    }

    // Constructor without optional fields
    public TariffRule(BigDecimal rate, int effectiveYear) {
        this.rate = rate;
        this.effectiveYear = effectiveYear;
    }


}