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
    public List<TariffRule> getTariffRulesByCountryId(Long countryId) {
        if (!countryRepository.existsById(countryId)) {
            throw new CountryNotFoundException(countryId);
        }
        return tariffRuleRepository.findByFromCountryIdOrToCountryId(countryId, countryId);
    }
    
    @Override
    public List<TariffRule> getTariffRulesByFromCountryId(Long fromCountryId) {
        if (!countryRepository.existsById(fromCountryId)) {
            throw new CountryNotFoundException(fromCountryId);
        }
        return tariffRuleRepository.findByFromCountryId(fromCountryId);
    }
    
    @Override
    public List<TariffRule> getTariffRulesByToCountryId(Long toCountryId) {
        if (!countryRepository.existsById(toCountryId)) {
            throw new CountryNotFoundException(toCountryId);
        }
        return tariffRuleRepository.findByToCountryId(toCountryId);
    }
   
    @Override
    public List<TariffRule> getTariffRulesByProductId(Long productId) {
        if (!productRepository.existsById(productId)) {
            throw new ProductNotFoundException(productId);
        }
        return tariffRuleRepository.findByProductId(productId);
    }
    
    
    public TariffRule addTariffRuleByCountryAndProduct(Long countryId, Long productId, TariffRule tariffRule) {
        return countryRepository.findById(countryId).map(country -> {
            return productRepository.findById(productId).map(product -> {
                // Set both fromCountry and toCountry to the same country for backward compatibility
                tariffRule.setFromCountry(country);
                tariffRule.setToCountry(country);
                tariffRule.setProduct(product);
                return tariffRuleRepository.save(tariffRule);
            }).orElseThrow(() -> new ProductNotFoundException(productId));
        }).orElseThrow(() -> new CountryNotFoundException(countryId));
    }
    
    @Override
    public TariffRule addTariffRuleByCountriesAndProduct(Long fromCountryId, Long toCountryId, Long productId, TariffRule tariffRule) {
        return countryRepository.findById(fromCountryId).map(fromCountry -> {
            return countryRepository.findById(toCountryId).map(toCountry -> {
                return productRepository.findById(productId).map(product -> {
                    tariffRule.setFromCountry(fromCountry);
                    tariffRule.setToCountry(toCountry);
                    tariffRule.setProduct(product);
                    return tariffRuleRepository.save(tariffRule);
                }).orElseThrow(() -> new ProductNotFoundException(productId));
            }).orElseThrow(() -> new CountryNotFoundException(toCountryId));
        }).orElseThrow(() -> new CountryNotFoundException(fromCountryId));
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
        
        // Find the tariff rule and verify it's associated with the product and has the country as either from or to
        return tariffRuleRepository.findById(id)
                .filter(tr -> tr.getProduct() != null && tr.getProduct().getId().equals(productId))
                .filter(tr -> (tr.getFromCountry() != null && tr.getFromCountry().getId().equals(countryId)) ||
                             (tr.getToCountry() != null && tr.getToCountry().getId().equals(countryId)))
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
    
    @Override
    public TariffRule updateTariffRule(Long fromCountryId, Long toCountryId, Long productId, Long id, TariffRule tariffRule) {
        // Verify countries and product exist
        if (!countryRepository.existsById(fromCountryId)) {
            throw new CountryNotFoundException(fromCountryId);
        }
        if (!countryRepository.existsById(toCountryId)) {
            throw new CountryNotFoundException(toCountryId);
        }
        if (!productRepository.existsById(productId)) {
            throw new ProductNotFoundException(productId);
        }
        
        // Find the tariff rule and verify it's associated with the countries and product
        return tariffRuleRepository.findById(id)
                .filter(tr -> tr.getProduct() != null && tr.getProduct().getId().equals(productId))
                .filter(tr -> tr.getFromCountry() != null && tr.getFromCountry().getId().equals(fromCountryId))
                .filter(tr -> tr.getToCountry() != null && tr.getToCountry().getId().equals(toCountryId))
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
 
    
    public void deleteTariffRule(Long countryId, Long productId, Long id) {
        // Verify country and product exist
        if (!countryRepository.existsById(countryId)) {
            throw new CountryNotFoundException(countryId);
        }
        if (!productRepository.existsById(productId)) {
            throw new ProductNotFoundException(productId);
        }
        
        // Find the tariff rule and verify it's associated with the product and has the country as either from or to
        tariffRuleRepository.findById(id)
                .filter(tr -> tr.getProduct() != null && tr.getProduct().getId().equals(productId))
                .filter(tr -> (tr.getFromCountry() != null && tr.getFromCountry().getId().equals(countryId)) ||
                             (tr.getToCountry() != null && tr.getToCountry().getId().equals(countryId)))
                .map(tariffRule -> {
                    tariffRuleRepository.delete(tariffRule);
                    return tariffRule;
                }).orElseThrow(() -> new TariffRuleNotFoundException(id));
    }
    
    @Override
    public void deleteTariffRule(Long fromCountryId, Long toCountryId, Long productId, Long id) {
        // Verify countries and product exist
        if (!countryRepository.existsById(fromCountryId)) {
            throw new CountryNotFoundException(fromCountryId);
        }
        if (!countryRepository.existsById(toCountryId)) {
            throw new CountryNotFoundException(toCountryId);
        }
        if (!productRepository.existsById(productId)) {
            throw new ProductNotFoundException(productId);
        }
        
        // Find the tariff rule and verify it's associated with the countries and product
        tariffRuleRepository.findById(id)
                .filter(tr -> tr.getProduct() != null && tr.getProduct().getId().equals(productId))
                .filter(tr -> tr.getFromCountry() != null && tr.getFromCountry().getId().equals(fromCountryId))
                .filter(tr -> tr.getToCountry() != null && tr.getToCountry().getId().equals(toCountryId))
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
}