package com.tariff.entity;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Product {

    @Id 
    private Long id;

    // @Column(name = "hs_code", length = 10)
    // private String hsCode;
    
    private String name;

    @Column(columnDefinition = "TEXT") //Description will be optional
    private String description;

    @ManyToOne
    @JoinColumn(name = "industry_id")
    private Industry industry;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<TariffRule> tariffRules;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<ImportRecord> importRecords;

    public Product() {
    }
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    // public String getHsCode() {
    //     return hsCode;
    // }

    // public void setHsCode(String hsCode) {
    //     this.hsCode = hsCode;
    // }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Industry getIndustry() {
        return industry;
    }

    public void setIndustry(Industry industry) {
        this.industry = industry;
    }

    public List<TariffRule> getTariffRules() {
        return tariffRules;
    }

    public void setTariffRules(List<TariffRule> tariffRules) {
        this.tariffRules = tariffRules;
    }

    public List<ImportRecord> getImportRecords() {
        return importRecords;
    }

    public void setImportRecords(List<ImportRecord> importRecords) {
        this.importRecords = importRecords;
    }

    public Product(String name, String description) {
        this.name = name;
        this.description = description;
    }
    

}
