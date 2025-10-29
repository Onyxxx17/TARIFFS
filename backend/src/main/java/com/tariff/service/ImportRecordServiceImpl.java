package com.tariff.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tariff.entity.ImportRecord;
import com.tariff.entity.User;
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

    private final ImportRecordRepository importRecordRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final CountryRepository countryRepository;

    public ImportRecordServiceImpl(ImportRecordRepository importRecordRepository,
            ProductRepository productRepository,
            UserRepository userRepository,
            CountryRepository countryRepository) {
        this.importRecordRepository = importRecordRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.countryRepository = countryRepository;
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
    public List<ImportRecord> getImportRecordsByFromCountryCode(String fromCountryCode) {
        if (!countryRepository.existsById(fromCountryCode)) {
            throw new CountryNotFoundException(fromCountryCode);
        }
        return importRecordRepository.findByFromCountryCountryCode(fromCountryCode);
    }

    @Override
    public List<ImportRecord> getImportRecordsByToCountryCode(String toCountryCode) {
        if (!countryRepository.existsById(toCountryCode)) {
            throw new CountryNotFoundException(toCountryCode);
        }
        return importRecordRepository.findByToCountryCountryCode(toCountryCode);
    }

    @Override
    public List<ImportRecord> getImportRecordsByFromCountryCodeAndToCountryCode(String fromCountryCode, String toCountryCode) {
        return importRecordRepository.findByFromCountryCountryCodeAndToCountryCountryCode(fromCountryCode, toCountryCode);
    }

    @Override
    public List<ImportRecord> getImportRecordsByProductId(Long productId) {
        if (!productRepository.existsById(productId)) {
            throw new ProductNotFoundException(productId);
        }
        return importRecordRepository.findByProductId(productId);
    }

    @Override
    public List<ImportRecord> getImportRecordsByUserId(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException(userId);
        }
        return importRecordRepository.findByUserId(userId);
    }

    @Override
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
    public ImportRecord addImportRecordByCountryPair(String fromCountryCode, String toCountryCode, ImportRecord importRecord) {
        return countryRepository.findById(fromCountryCode).map(fromCountry -> {
            return countryRepository.findById(toCountryCode).map(toCountry -> {
                importRecord.setFromCountry(fromCountry);
                importRecord.setToCountry(toCountry);
                return importRecordRepository.save(importRecord);
            }).orElseThrow(() -> new CountryNotFoundException(toCountryCode));
        }).orElseThrow(() -> new CountryNotFoundException(fromCountryCode));
    }

    @Override
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
    public ImportRecord updateImportRecordByCountries(String fromCountryCode, String toCountryCode, ImportRecord importRecord, Long id) {
        if (!countryRepository.existsById(fromCountryCode)) {
            throw new CountryNotFoundException(fromCountryCode);
        }
        if (!countryRepository.existsById(toCountryCode)) {
            throw new CountryNotFoundException(toCountryCode);
        }
        return importRecordRepository.findByFromCountryCountryCodeAndToCountryCountryCode(fromCountryCode, toCountryCode)
                .stream()
                .filter(record -> record.getId().equals(id))
                .findFirst()
                .map(existingRecord -> {
                    existingRecord.setValue(importRecord.getValue());
                    existingRecord.setYear(importRecord.getYear());
                    return importRecordRepository.save(existingRecord);
                }).orElseThrow(() -> new ImportRecordNotFoundException(id));
    }

    @Override
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
            existingRecord.setProduct(importRecord.getProduct());
            existingRecord.setUser(importRecord.getUser());
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
    public void deleteImportRecordByCountries(String fromCountryCode, String toCountryCode, Long id) {
        if (!countryRepository.existsById(fromCountryCode)) {
            throw new CountryNotFoundException(fromCountryCode);
        }
        if (!countryRepository.existsById(toCountryCode)) {
            throw new CountryNotFoundException(toCountryCode);
        }
        List<ImportRecord> records = importRecordRepository.findByFromCountryCountryCodeAndToCountryCountryCode(fromCountryCode, toCountryCode);
        if (records.isEmpty()) {
            throw new ImportRecordNotFoundException(id);
        }
        records.stream()
                .filter(record -> record.getId().equals(id))
                .findFirst()
                .ifPresentOrElse(
                        importRecordRepository::delete,
                        () -> {
                            throw new ImportRecordNotFoundException(id);
                        }
                );
    }

    @Override
    public Long getUserIdFromAuthentication(Authentication authentication) {
        String email = authentication.getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
        return user.getId();
    }

    @Override
    public Page<ImportRecord> getUserCalculationHistory(Long userId, Pageable pageable) {
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException(userId);
        }
        return importRecordRepository.findByUserId(userId, pageable);
    }

    @Override
    public Page<ImportRecord> getAllCalculationHistory(Pageable pageable) {
        return importRecordRepository.findAll(pageable);
    }

    @Override
    public void deleteCalculationHistory(Long id, Long userId) {
        ImportRecord record = importRecordRepository.findById(id)
                .orElseThrow(() -> new ImportRecordNotFoundException(id));

        // Verify the record belongs to this user
        if (!record.getUser().getId().equals(userId)) {
            throw new RuntimeException("You can only delete your own calculation history");
        }

        importRecordRepository.deleteById(id);
    }

    public String getCountryCodeByCountryName(String countryName) {
        var country = countryRepository.findCountryByName(countryName);
        if (country.isPresent()) {
            return country.get().getCountryCode();
        }
        throw new RuntimeException("Country not found: " + countryName);
    }
}
