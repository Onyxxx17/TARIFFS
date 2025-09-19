package com.tariff.entity;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
public class Product {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "hs_code", length = 10)
    private String hsCode;

    
    private String name;

    @Column(columnDefinition = "TEXT") //Description will be optional
    private String description;

    @ManyToOne
    @JoinColumn(name = "industry_id")
    private Industry industry;

    @ManyToOne
    @JoinColumn(name = "country_id")
    private Country country;

    @OneToOne
    @JoinColumn(name = "import_record_id")
    private ImportRecord importRecord;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<TariffRule> tariffRules;

    private double basePrice;
    

    public Product(String hsCode, String name, String description, Industry industry, double basePrice){
        this.hsCode = hsCode;
        this.name = name;
        this.description = description;
        this.industry = industry;
        this.basePrice = basePrice;
    }

    

}
