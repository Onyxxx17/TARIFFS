package com.tariff.service;

import java.util.List;
import java.util.stream.*;
import java.util.ArrayList;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tariff.entity.ImportRecord;
import com.tariff.exception.CountryNotFoundException;
import com.tariff.exception.ImportRecordNotFoundException;
import com.tariff.exception.ProductNotFoundException;
import com.tariff.exception.UserNotFoundException;
import com.tariff.repository.ImportRecordRepository;
import com.tariff.repository.ProductRepository;
import com.tariff.repository.CountryRepository;
import com.tariff.repository.UserRepository;

@Service
@Transactional
public class ImportRecordServiceImpl implements ImportRecordService {
    
    private ImportRecordRepository importRecordRepository;
    private ProductRepository productRepository;
    private UserRepository userRepository;
    private CountryRepository countryRepository;
    
    public ImportRecordServiceImpl(ImportRecordRepository importRecordRepository,
                                  ProductRepository productRepository,
                                  UserRepository userRepository,CountryRepository countryRepository ) {
        this.importRecordRepository = importRecordRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }
    
    @Override
    public List<ImportRecord> listImportRecord() {
        return importRecordRepository.findAll();
    }
    
    @Override
    public ImportRecord getImportRecord(Long id) {
        return importRecordRepository.findById(id)
                .orElseThrow(() -> new ImportRecordNotFoundException(id));
    }

    @Override
    public List<ImportRecord> getImportRecordsByFromCountryId(Long fromCountryId) {
        if (!countryRepository.existsById(fromCountryId)) {
            throw new CountryNotFoundException(fromCountryId);
        }
        return importRecordRepository.findByFromCountryId(fromCountryId);
    }
    
    @Override
    public List<ImportRecord> getImportRecordsByToCountryId(Long toCountryId) {
        if (!countryRepository.existsById(toCountryId)) {
            throw new CountryNotFoundException(toCountryId);
        }
        return importRecordRepository.findByToCountryId(toCountryId);
    }

    @Override
    public List<ImportRecord> getImportRecordsByFromCountryIdAndToCountryId(Long fromCountryId, Long toCountryId) {
        List<ImportRecord> recordsByFromCountryId = getImportRecordsByFromCountryId(fromCountryId);
        List<ImportRecord> recordsByToCountryId = getImportRecordsByFromCountryId(toCountryId);
        return Stream.concat(recordsByFromCountryId.stream(), recordsByToCountryId.stream())
                                    .collect(Collectors.toList());
    }
    

    public List<ImportRecord> getImportRecordsByProductId(Long productId) {
        if (!productRepository.existsById(productId)) {
            throw new ProductNotFoundException(productId);
        }
        return importRecordRepository.findByProductId(productId);
    }
    
    
    public List<ImportRecord> getImportRecordsByUserId(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException(userId);
        }
        return importRecordRepository.findByUserId(userId);
    }
    

    public ImportRecord addImportRecordByProductAndUser(Long productId, Long userId, ImportRecord importRecord) {
        return productRepository.findById(productId).map(product -> {
            return userRepository.findById(userId).map(user -> {
                importRecord.setProduct(product);
                importRecord.setUser(user);
                return importRecordRepository.save(importRecord);
            }).orElseThrow(() -> new UserNotFoundException(userId));
        }).orElseThrow(() -> new ProductNotFoundException(productId));
    }
    
    @Override
    public ImportRecord addImportRecord(ImportRecord importRecord) {
        return importRecordRepository.save(importRecord);
    }

    @Override
    public ImportRecord addImportRecordByCountryPair(Long fromCountryId, Long toCountryId, ImportRecord importRecord) {
        return countryRepository.findById(fromCountryId).map(fromCountry -> {
            return countryRepository.findById(toCountryId).map(toCountry -> {
                importRecord.setFromCountry(fromCountry);
                importRecord.setToCountry(toCountry);
                return importRecordRepository.save(importRecord);
            }).orElseThrow(() -> new CountryNotFoundException(toCountryId));
        }).orElseThrow(() -> new CountryNotFoundException(fromCountryId));
    }
    
  
    public ImportRecord updateImportRecordByProductAndUser(Long productId, Long userId, Long id, ImportRecord importRecord) {
        if (!productRepository.existsById(productId)) {
            throw new ProductNotFoundException(productId);
        }
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException(userId);
        }
        return importRecordRepository.findByIdAndProductIdAndUserId(id, productId, userId)
                .map(existingRecord -> {
                    existingRecord.setValue(importRecord.getValue());
                    existingRecord.setYear(importRecord.getYear());
                    return importRecordRepository.save(existingRecord);
                }).orElseThrow(() -> new ImportRecordNotFoundException(id));
    }

    @Override
    public ImportRecord updateImportRecordByCountries(Long fromCountryId, Long toCountryId, ImportRecord importRecord, Long id) {
        if (!countryRepository.existsById(fromCountryId)) {
            throw new CountryNotFoundException(fromCountryId);
        }
        if (!countryRepository.existsById(toCountryId)) {
            throw new CountryNotFoundException(toCountryId);
        }
        return importRecordRepository.findByFromCountryIdAndToCountryId(fromCountryId, toCountryId)
                .map(existingRecord -> {
                    existingRecord.setValue(importRecord.getValue());
                    existingRecord.setYear(importRecord.getYear());
                    return importRecordRepository.save(existingRecord);
                }).orElseThrow(() -> new ImportRecordNotFoundException(id));
    }
    
   
    public void deleteImportRecordByProductAndUser(Long productId, Long userId, Long id) {
        if (!productRepository.existsById(productId)) {
            throw new ProductNotFoundException(productId);
        }
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException(userId);
        }
        importRecordRepository.findByIdAndProductIdAndUserId(id, productId, userId)
                .map(importRecord -> {
                    importRecordRepository.delete(importRecord);
                    return importRecord;
                }).orElseThrow(() -> new ImportRecordNotFoundException(id));
    }

    @Override
    public ImportRecord updateImportRecord(Long id, ImportRecord importRecord) {
        return importRecordRepository.findById(id).map(existingRecord -> {

            existingRecord.setValue(importRecord.getValue());

            existingRecord.setYear(importRecord.getYear());

            
            // existingRecord.setCalculatedTariffAmount(importRecord.getCalculatedTariffAmount());
           
            if (importRecord.getProduct() != null) {
                existingRecord.setProduct(importRecord.getProduct());
            }
            if (importRecord.getUser() != null) {
                existingRecord.setUser(importRecord.getUser());
            }
            return importRecordRepository.save(existingRecord);
        }).orElseThrow(() -> new ImportRecordNotFoundException(id));
    }

    @Override
    public void deleteImportRecord(Long id) {
        if (!importRecordRepository.existsById(id)) {
            throw new ImportRecordNotFoundException(id);
        }
        importRecordRepository.deleteById(id);
       
    }

    @Override
    public void deleteImportRecordByCountries(Long fromCountryId, Long toCountryId, Long id) {
        if (!countryRepository.existsById(fromCountryId)) {
            throw new CountryNotFoundException(fromCountryId);
        }
        if (!countryRepository.existsById(toCountryId)) {
            throw new CountryNotFoundException(toCountryId);
        }
        importRecordRepository.findByFromCountryIdAndToCountryId(fromCountryId, toCountryId)
                .map(importRecord -> {
                    importRecordRepository.delete(importRecord);
                    return importRecord;
                }).orElseThrow(() -> new ImportRecordNotFoundException(id));
    }
}