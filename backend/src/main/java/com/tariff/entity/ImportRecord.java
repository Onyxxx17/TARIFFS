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
    private int quantity;
    private LocalDate date;
    private double calculatedTariffAmount;

    @OneToMany(mappedBy = "importRecord", cascade = CascadeType.ALL)
    private List<Country> countries;
    
    @ManyToOne
    @JoinColumn(name = "tariff_rule_id")
    private TariffRule tariffRule;

    @OneToOne(mappedBy = "importRecord", cascade = CascadeType.ALL) 
    private Product product;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    // constructor for ImportRecord
    public ImportRecord(int quantity, LocalDate date, double calculatedTariffAmount) {

        this.quantity = quantity;
        this.date = date;
        this.calculatedTariffAmount = calculatedTariffAmount;
    }

}
