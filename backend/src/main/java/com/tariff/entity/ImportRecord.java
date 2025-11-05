package com.tariff.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    
    private int value;
    private int year;
    private double tariffRate;
    private double calculatedTariff;
    private double additionalFee; // stores the percentage rate
    private double totalCost;
    private String calculationType; // WEIGHT or QUANTITY

    @ManyToOne
    @JoinColumn(name = "product_id")
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

    public ImportRecord(int value, int year, double tariffRate, double calculatedTariff, double additionalFee, double totalCost, Product product) {
        this.value = value;
        this.year = year;
        this.tariffRate = tariffRate;
        this.calculatedTariff = calculatedTariff;
        this.additionalFee = additionalFee;
        this.totalCost = totalCost;
        this.product = product;
        this.calculationType = "QUANTITY"; // default value
    }

}
