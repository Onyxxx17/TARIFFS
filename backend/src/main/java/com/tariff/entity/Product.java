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
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class Product {

    @Id 
    private Long id;

    // @Column(name = "hs_code", length = 10)
    // private String hsCode;
    
    private String name;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    // Constructor provided by Lombok's @AllArgsConstructor

    // public Product() {
    // }
    
    // public Long getId() {
    //     return id;
    // }

    // public void setId(Long id) {
    //     this.id = id;
    // }

    // public String getHsCode() {
    //     return hsCode;
    // }

    // public void setHsCode(String hsCode) {
    //     this.hsCode = hsCode;
    // }

    // public String getName() {
    //     return name;
    // }

    // public void setName(String name) {
    //     this.name = name;
    // }

    // public String getDescription() {
    //     return description;
    // }

    // public void setDescription(String description) {
    //     this.description = description;
    // }

    // public Category getCategoryy() {
    //     return category;
    // }

    // public void setCategory(Category category) {
    //     this.category = category;
    // }

    // public List<TariffRule> getTariffRules() {
    //     return tariffRules;
    // }

    // public void setTariffRules(List<TariffRule> tariffRules) {
    //     this.tariffRules = tariffRules;
    // }

    // public List<ImportRecord> getImportRecords() {
    //     return importRecords;
    // }

    // public void setImportRecords(List<ImportRecord> importRecords) {
    //     this.importRecords = importRecords;
    // }
}
