package com.tariff.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tariff.dto.response.TariffComparisonDTO;
import com.tariff.dto.response.TariffRateOverTimeDTO;
import com.tariff.entity.Country;
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
    public Page<TariffRule> listTariffRule(Pageable pageable) {
        return tariffRuleRepository.findAll(pageable);
    }
    
    @Override
    public TariffRule getTariffRule(Long id) {
        return tariffRuleRepository.findById(id)
                .orElseThrow(() -> new TariffRuleNotFoundException(id));
    }
    
    @Override
    public Page<TariffRule> getTariffRulesByCountryCode(String countryCode, Pageable pageable) {
        if (!countryRepository.existsById(countryCode)) {
            throw new CountryNotFoundException(countryCode);
        }
        return tariffRuleRepository.findByFromCountryCountryCodeOrToCountryCountryCode(countryCode, countryCode, pageable);
    }
    
    @Override
    public Page<TariffRule> getTariffRulesByFromCountryCode(String fromCountryCode, Pageable pageable) {
        if (!countryRepository.existsById(fromCountryCode)) {
            throw new CountryNotFoundException(fromCountryCode);
        }
        return tariffRuleRepository.findByFromCountryCountryCode(fromCountryCode, pageable);
    }
    
    @Override
    public Page<TariffRule> getTariffRulesByToCountryCode(String toCountryCode, Pageable pageable) {
        if (!countryRepository.existsById(toCountryCode)) {
            throw new CountryNotFoundException(toCountryCode);
        }
        return tariffRuleRepository.findByToCountryCountryCode(toCountryCode, pageable);
    }
   
    @Override
    public Page<TariffRule> getTariffRulesByProductId(Long productId, Pageable pageable) {
        if (!productRepository.existsById(productId)) {
            throw new ProductNotFoundException(productId);
        }
        return tariffRuleRepository.findByProductId(productId, pageable);
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
    public List<TariffRateOverTimeDTO> getTariffRatesOverTime(String fromCountryCode, String toCountryCode, Long productId) {
        // Validate inputs
        if (!countryRepository.existsById(fromCountryCode)) {
            throw new CountryNotFoundException(fromCountryCode);
        }
        if (!countryRepository.existsById(toCountryCode)) {
            throw new CountryNotFoundException(toCountryCode);
        }
        if (!productRepository.existsById(productId)) {
            throw new ProductNotFoundException(productId);
        }
        
        // Get rates from database
        List<TariffRateOverTimeDTO> dbRates = tariffRuleRepository.findTariffRatesOverTime(fromCountryCode, toCountryCode, productId);
        
        // Create a map for quick lookup
        java.util.Map<Integer, java.math.BigDecimal> rateMap = new java.util.HashMap<>();
        for (TariffRateOverTimeDTO dto : dbRates) {
            rateMap.put(dto.getYear(), dto.getRate());
        }
        
        // Fill in all years from 1996 to 2025
        List<TariffRateOverTimeDTO> completeRates = new java.util.ArrayList<>();
        for (int year = 1996; year <= 2025; year++) {
            // Use existing rate or 0 if no data (no agreement = 0% tariff)
            java.math.BigDecimal rate = rateMap.getOrDefault(year, java.math.BigDecimal.ZERO);
            completeRates.add(new TariffRateOverTimeDTO(year, rate));
        }
        
        return completeRates;
    }
    
    @Override
    public TariffComparisonDTO compareTariffRates(String country1Code, String country2Code, Long productId) {
        // Validate inputs
        Country country1 = countryRepository.findById(country1Code)
            .orElseThrow(() -> new CountryNotFoundException(country1Code));
        Country country2 = countryRepository.findById(country2Code)
            .orElseThrow(() -> new CountryNotFoundException(country2Code));
        if (!productRepository.existsById(productId)) {
            throw new ProductNotFoundException(productId);
        }
        
        // Get rates for Country1 importing from Country2
        List<TariffRateOverTimeDTO> country1Rates = tariffRuleRepository.findTariffRatesOverTime(country2Code, country1Code, productId);
        
        // Get rates for Country2 importing from Country1
        List<TariffRateOverTimeDTO> country2Rates = tariffRuleRepository.findTariffRatesOverTime(country1Code, country2Code, productId);
        
        return new TariffComparisonDTO(
            country1Code,
            country1.getName(),
            country2Code,
            country2.getName(),
            productId,
            country1Rates,
            country2Rates
        );
    }
}