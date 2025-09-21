package com.tariff.entity;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
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
@EqualsAndHashCode
public class Country {

    @Id
    private String countryCode;

    @Column
    private String name;
    public Country(String countryCode, String name) {
        this.countryCode = countryCode;
        this.name = name;
    }

    public Country(){

    }

    @OneToMany(mappedBy = "fromCountry", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<TariffRule> tariffRules;

    @OneToMany(mappedBy = "fromCountry", cascade = CascadeType.ALL) 
    @JsonIgnore
    private List<ImportRecord> importRecords;

}
