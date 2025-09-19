package com.tariff.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tariff.entity.ImportRecord;
import com.tariff.exception.ImportRecordNotFoundException;
import com.tariff.exception.ProductNotFoundException;
import com.tariff.exception.UserNotFoundException;
import com.tariff.repository.ImportRecordRepository;
import com.tariff.repository.ProductRepository;
import com.tariff.repository.UserRepository;

@Service
@Transactional
public class ImportRecordServiceImpl implements ImportRecordService {
    
    private ImportRecordRepository importRecordRepository;
    private ProductRepository productRepository;
    private UserRepository userRepository;
    
    public ImportRecordServiceImpl(ImportRecordRepository importRecordRepository,
                                  ProductRepository productRepository,
                                  UserRepository userRepository) {
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
    
  
    public ImportRecord updateImportRecord(Long productId, Long userId, Long id, ImportRecord importRecord) {
        if (!productRepository.existsById(productId)) {
            throw new ProductNotFoundException(productId);
        }
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException(userId);
        }
        return importRecordRepository.findByIdAndProductIdAndUserId(id, productId, userId)
                .map(existingRecord -> {
                    existingRecord.setQuantity(importRecord.getQuantity());
                    existingRecord.setDate(importRecord.getDate());
                    existingRecord.setCalculatedTariffAmount(importRecord.getCalculatedTariffAmount());
                    return importRecordRepository.save(existingRecord);
                }).orElseThrow(() -> new ImportRecordNotFoundException(id));
    }
    
   
    public void deleteImportRecord(Long productId, Long userId, Long id) {
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

            existingRecord.setQuantity(importRecord.getQuantity());

            if (importRecord.getDate() != null) {
                existingRecord.setDate(importRecord.getDate());
            }
            
            existingRecord.setCalculatedTariffAmount(importRecord.getCalculatedTariffAmount());
           
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
}