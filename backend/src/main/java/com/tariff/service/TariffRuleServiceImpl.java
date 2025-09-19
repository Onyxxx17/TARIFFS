package com.tariff.service;

import java.util.HashSet;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tariff.entity.TariffRule;
import com.tariff.exception.CountryNotFoundException;
import com.tariff.exception.ProductNotFoundException;
import com.tariff.exception.TariffRuleNotFoundException;
import com.tariff.repository.CountryRepository;
import com.tariff.repository.ProductRepository;
import com.tariff.repository.TariffRuleRepository;

@Service
@Transactional
public class TariffRuleServiceImpl implements TariffRuleService {
    
    private TariffRuleRepository tariffRuleRepository;
    private CountryRepository countryRepository;
    private ProductRepository productRepository;
    
    public TariffRuleServiceImpl(TariffRuleRepository tariffRuleRepository,
                                CountryRepository countryRepository,
                                ProductRepository productRepository) {
        this.tariffRuleRepository = tariffRuleRepository;
        this.countryRepository = countryRepository;
        this.productRepository = productRepository;
    }
    
    @Override
    public List<TariffRule> listTariffRule() {
        return tariffRuleRepository.findAll();
    }
    
    @Override
    public TariffRule getTariffRule(Long id) {
        return tariffRuleRepository.findById(id)
                .orElseThrow(() -> new TariffRuleNotFoundException(id));
    }
    
  
    public List<TariffRule> getTariffRulesByCountryId(Long countryId) {
        if (!countryRepository.existsById(countryId)) {
            throw new CountryNotFoundException(countryId);
        }
        return tariffRuleRepository.findByCountriesId(countryId);
    }
    
   
    public List<TariffRule> getTariffRulesByProductId(Long productId) {
        if (!productRepository.existsById(productId)) {
            throw new ProductNotFoundException(productId);
        }
        return tariffRuleRepository.findByProductId(productId);
    }
    
    
    public TariffRule addTariffRuleByCountryAndProduct(Long countryId, Long productId, TariffRule tariffRule) {
        return countryRepository.findById(countryId).map(country -> {
            return productRepository.findById(productId).map(product -> {
                // Initialize countries collection if null
                if (tariffRule.getCountries() == null) {
                    tariffRule.setCountries(new HashSet<>());
                }
                // Add the country to the countries collection
                tariffRule.getCountries().add(country);
                tariffRule.setProduct(product);
                return tariffRuleRepository.save(tariffRule);
            }).orElseThrow(() -> new ProductNotFoundException(productId));
        }).orElseThrow(() -> new CountryNotFoundException(countryId));
    }
    
    @Override
    public TariffRule addTariffRule(TariffRule tariffRule) {
        return tariffRuleRepository.save(tariffRule);
    }
    
 
    public TariffRule updateTariffRule(Long countryId, Long productId, Long id, TariffRule tariffRule) {
        // Verify country and product exist
        if (!countryRepository.existsById(countryId)) {
            throw new CountryNotFoundException(countryId);
        }
        if (!productRepository.existsById(productId)) {
            throw new ProductNotFoundException(productId);
        }
        
        // Find the tariff rule and verify it's associated with the product
        return tariffRuleRepository.findById(id)
                .filter(tr -> tr.getProduct() != null && tr.getProduct().getId().equals(productId))
                .filter(tr -> tr.getCountries().stream().anyMatch(c -> c.getId().equals(countryId)))
                .map(existingRule -> {
                    // Update basic fields
                    if (tariffRule.getRate() != null) {
                        existingRule.setRate(tariffRule.getRate());
                    }
                    if (tariffRule.getAdditionalFee() != null) {
                        existingRule.setAdditionalFee(tariffRule.getAdditionalFee());
                    }
                    if (tariffRule.getEffectiveDate() != null) {
                        existingRule.setEffectiveDate(tariffRule.getEffectiveDate());
                    }
                    if (tariffRule.getExpiryDate() != null) {
                        existingRule.setExpiryDate(tariffRule.getExpiryDate());
                    }
                    return tariffRuleRepository.save(existingRule);
                }).orElseThrow(() -> new TariffRuleNotFoundException(id));
    }
    
 
    public void deleteTariffRule(Long countryId, Long productId, Long id) {
        // Verify country and product exist
        if (!countryRepository.existsById(countryId)) {
            throw new CountryNotFoundException(countryId);
        }
        if (!productRepository.existsById(productId)) {
            throw new ProductNotFoundException(productId);
        }
        
        // Find the tariff rule and verify it's associated with both product and country
        tariffRuleRepository.findById(id)
                .filter(tr -> tr.getProduct() != null && tr.getProduct().getId().equals(productId))
                .filter(tr -> tr.getCountries().stream().anyMatch(c -> c.getId().equals(countryId)))
                .map(tariffRule -> {
                    tariffRuleRepository.delete(tariffRule);
                    return tariffRule;
                }).orElseThrow(() -> new TariffRuleNotFoundException(id));
    }

    @Override
    public TariffRule updateTariffRule(Long id, TariffRule tariffRule) {
            return tariffRuleRepository.findById(id).map(existingRule -> {
            if (tariffRule.getRate() != null) {
                existingRule.setRate(tariffRule.getRate());
            }
            if (tariffRule.getAdditionalFee() != null) {
                existingRule.setAdditionalFee(tariffRule.getAdditionalFee());
            }
            if (tariffRule.getEffectiveDate() != null) {
                existingRule.setEffectiveDate(tariffRule.getEffectiveDate());
            }
            if (tariffRule.getExpiryDate() != null) {
                existingRule.setExpiryDate(tariffRule.getExpiryDate());
            }
            if (tariffRule.getProduct() != null) {
                existingRule.setProduct(tariffRule.getProduct());
            }
            if (tariffRule.getCountries() != null && !tariffRule.getCountries().isEmpty()) {
                existingRule.setCountries(tariffRule.getCountries());
            }
            return tariffRuleRepository.save(existingRule);
        }).orElseThrow(() -> new TariffRuleNotFoundException(id));
    }

    @Override
    public void deleteTariffRule(Long id) {
        if (!tariffRuleRepository.existsById(id)) {
            throw new TariffRuleNotFoundException(id);
        }
        tariffRuleRepository.deleteById(id);
    }
}