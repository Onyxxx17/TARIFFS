package com.tariff.entity;

import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.CascadeType;
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

public class ImportRecord {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int value;
    private int year;
    private double calculatedTariffAmount;

    @JsonIgnore
    @OneToOne(mappedBy = "importRecord", cascade = CascadeType.ALL) 
    private Product product;

    @ManyToOne
    @JoinColumn(name = "from_country_id")
    private Country fromCountry;

    @ManyToOne
    @JoinColumn(name = "to_country_id")
    private Country toCountry;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    // constructor for ImportRecord
    public ImportRecord(int value, int year, double calculatedTariffAmount) {

        this.value = value;
        this.year = year;
        this.calculatedTariffAmount = calculatedTariffAmount;
    }

}
