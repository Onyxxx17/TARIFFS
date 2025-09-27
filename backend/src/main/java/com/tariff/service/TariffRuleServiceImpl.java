package com.tariff.service;

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
    
    @Override
    public List<TariffRule> getTariffRulesByCountryCode(String countryCode) {
        if (!countryRepository.existsById(countryCode)) {
            throw new CountryNotFoundException(countryCode);
        }
        return tariffRuleRepository.findByFromCountryCountryCodeOrToCountryCountryCode(countryCode, countryCode);
    }
    
    @Override
    public List<TariffRule> getTariffRulesByFromCountryCode(String fromCountryCode) {
        if (!countryRepository.existsById(fromCountryCode)) {
            throw new CountryNotFoundException(fromCountryCode);
        }
        return tariffRuleRepository.findByFromCountryCountryCode(fromCountryCode);
    }
    
    @Override
    public List<TariffRule> getTariffRulesByToCountryCode(String toCountryCode) {
        if (!countryRepository.existsById(toCountryCode)) {
            throw new CountryNotFoundException(toCountryCode);
        }
        return tariffRuleRepository.findByToCountryCountryCode(toCountryCode);
    }
   
    @Override
    public List<TariffRule> getTariffRulesByProductId(Long productId) {
        if (!productRepository.existsById(productId)) {
            throw new ProductNotFoundException(productId);
        }
        return tariffRuleRepository.findByProductId(productId);
    }
    
    
    public TariffRule addTariffRuleByCountryAndProduct(String countryCode, Long productId, TariffRule tariffRule) {
        return countryRepository.findById(countryCode).map(country -> {
            return productRepository.findById(productId).map(product -> {
                // Set both fromCountry and toCountry to the same country for backward compatibility
                tariffRule.setFromCountry(country);
                tariffRule.setToCountry(country);
                tariffRule.setProduct(product);
                return tariffRuleRepository.save(tariffRule);
            }).orElseThrow(() -> new ProductNotFoundException(productId));
        }).orElseThrow(() -> new CountryNotFoundException(countryCode));
    }
    
    @Override
    public TariffRule addTariffRuleByCountriesAndProduct(String fromCountryCode, String toCountryCode, Long productId, TariffRule tariffRule) {
        return countryRepository.findById(fromCountryCode).map(fromCountry -> {
            return countryRepository.findById(toCountryCode).map(toCountry -> {
                return productRepository.findById(productId).map(product -> {
                    tariffRule.setFromCountry(fromCountry);
                    tariffRule.setToCountry(toCountry);
                    tariffRule.setProduct(product);
                    return tariffRuleRepository.save(tariffRule);
                }).orElseThrow(() -> new ProductNotFoundException(productId));
            }).orElseThrow(() -> new CountryNotFoundException(toCountryCode));
        }).orElseThrow(() -> new CountryNotFoundException(fromCountryCode));
    }
    
    @Override
    public TariffRule addTariffRule(TariffRule tariffRule) {
        return tariffRuleRepository.save(tariffRule);
    }
 
  

    
    @Override
    public TariffRule updateTariffRule(String fromCountryCode, String toCountryCode, Long productId, Long id, TariffRule tariffRule) {
        // Verify countries and product exist
        if (!countryRepository.existsById(fromCountryCode)) {
            throw new CountryNotFoundException(fromCountryCode);
        }
        if (!countryRepository.existsById(toCountryCode)) {
            throw new CountryNotFoundException(toCountryCode);
        }
        if (!productRepository.existsById(productId)) {
            throw new ProductNotFoundException(productId);
        }
        
        // Find the tariff rule and verify it's associated with the countries and product
        return tariffRuleRepository.findById(id)
                .filter(tr -> tr.getProduct() != null && tr.getProduct().getId().equals(productId))
                .filter(tr -> tr.getFromCountry() != null && tr.getFromCountry().getCountryCode().equals(fromCountryCode))
                .filter(tr -> tr.getToCountry() != null && tr.getToCountry().getCountryCode().equals(toCountryCode))
                .map(existingRule -> {
                    // Update basic fields
                    if (tariffRule.getRate() != null) {
                        existingRule.setRate(tariffRule.getRate());
                    }
                    if (tariffRule.getAdditionalFee() != null) {
                        existingRule.setAdditionalFee(tariffRule.getAdditionalFee());
                    }
                    existingRule.setEffectiveYear(tariffRule.getEffectiveYear());
                    
                    return tariffRuleRepository.save(existingRule);
                }).orElseThrow(() -> new TariffRuleNotFoundException(id));
    }
 
    
    public void deleteTariffRule(String countryCode, Long productId, Long id) {
        // Verify country and product exist
        if (!countryRepository.existsById(countryCode)) {
            throw new CountryNotFoundException(countryCode);
        }
        if (!productRepository.existsById(productId)) {
            throw new ProductNotFoundException(productId);
        }
        
        // Find the tariff rule and verify it's associated with the product and has the country as either from or to
        tariffRuleRepository.findById(id)
                .filter(tr -> tr.getProduct() != null && tr.getProduct().getId().equals(productId))
                .filter(tr -> (tr.getFromCountry() != null && tr.getFromCountry().getCountryCode().equals(countryCode)) ||
                             (tr.getToCountry() != null && tr.getToCountry().getCountryCode().equals(countryCode)))
                .map(tariffRule -> {
                    tariffRuleRepository.delete(tariffRule);
                    return tariffRule;
                }).orElseThrow(() -> new TariffRuleNotFoundException(id));
    }
    
    @Override
    public void deleteTariffRule(String fromCountryCode, String toCountryCode, Long productId, Long id) {
        // Verify countries and product exist
        if (!countryRepository.existsById(fromCountryCode)) {
            throw new CountryNotFoundException(fromCountryCode);
        }
        if (!countryRepository.existsById(toCountryCode)) {
            throw new CountryNotFoundException(toCountryCode);
        }
        if (!productRepository.existsById(productId)) {
            throw new ProductNotFoundException(productId);
        }
        
        // Find the tariff rule and verify it's associated with the countries and product
        tariffRuleRepository.findById(id)
                .filter(tr -> tr.getProduct() != null && tr.getProduct().getId().equals(productId))
                .filter(tr -> tr.getFromCountry() != null && tr.getFromCountry().getCountryCode().equals(fromCountryCode))
                .filter(tr -> tr.getToCountry() != null && tr.getToCountry().getCountryCode().equals(toCountryCode))
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
            existingRule.setEffectiveYear(tariffRule.getEffectiveYear());
            if (tariffRule.getProduct() != null) {
                existingRule.setProduct(tariffRule.getProduct());
            }
            if (tariffRule.getFromCountry() != null) {
                existingRule.setFromCountry(tariffRule.getFromCountry());
            }
            if (tariffRule.getToCountry() != null) {
                existingRule.setToCountry(tariffRule.getToCountry());
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

    @Override
    public List<TariffRule> getTariffRulesByCriteria(String fromCountryName, String toCountryName, Integer effectiveYear, String productName, Long productId) {
        return tariffRuleRepository.findByMultipleCriteria(fromCountryName, toCountryName, effectiveYear, productName, productId);
    }
}