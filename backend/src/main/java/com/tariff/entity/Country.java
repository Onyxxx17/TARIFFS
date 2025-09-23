package com.tariff.entity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
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
public class Country {

    @Id
    private String countryCode;

    @Column
    private String name;
    public Country(String countryCode, String name) {
        this.countryCode = countryCode;
        this.name = name;
    }


    @OneToMany(mappedBy = "fromCountry", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<TariffRule> tariffRules;

    @OneToMany(mappedBy = "fromCountry", cascade = CascadeType.ALL) 
    @JsonIgnore
    private List<ImportRecord> importRecords;

}
