package com.tariff.entity;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
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
public class Country {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String isoCode;

    private String currency;

    // one-to-many with product
    @OneToMany(mappedBy = "country", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Product> products;

    @ManyToMany(mappedBy = "countries")
    @JsonIgnore
    private Set<TariffRule> tariffRules = new HashSet<>();

    public Country(String name, String isoCode, String currency) {
        this.name = name;
        this.isoCode = isoCode;
        this.currency = currency;
    }

    
}
