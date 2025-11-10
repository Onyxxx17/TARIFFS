package com.tariff.service;

import com.tariff.entity.Country;
import com.tariff.exception.CountryNotFoundException;
import com.tariff.exception.DuplicateCountryException;
import com.tariff.repository.CountryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CountryServiceImplTest {

    @Mock
    private CountryRepository countryRepository;

    @InjectMocks
    private CountryServiceImpl countryService;

    private Country singapore;
    private Country malaysia;

    @BeforeEach
    void setUp() {
        singapore = new Country("SG", "Singapore");
        malaysia = new Country("MY", "Malaysia");
    }

    @Test
    void testListCountry() {
        when(countryRepository.findAll()).thenReturn(List.of(singapore, malaysia));

        List<Country> result = countryService.listCountry();

        assertEquals(2, result.size());
        verify(countryRepository).findAll();
    }

    @Test
    void testGetCountryFound() {
        when(countryRepository.findById("SG")).thenReturn(Optional.of(singapore));

        Country result = countryService.getCountry("SG");

        assertEquals("Singapore", result.getName());
        verify(countryRepository).findById("SG");
    }

    @Test
    void testGetCountryNotFound() {
        when(countryRepository.findById("XX")).thenReturn(Optional.empty());

        assertThrows(CountryNotFoundException.class, () -> countryService.getCountry("XX"));
        verify(countryRepository).findById("XX");
    }

    @Test
    void testGetCountryByNameFound() {
        when(countryRepository.findByName("Singapore")).thenReturn(Optional.of(singapore));

        Optional<Country> result = countryService.getCountryByName("Singapore");

        assertTrue(result.isPresent());
        assertEquals("SG", result.get().getCountryCode());
    }

    @Test
    void testAddCountrySuccess() {
        when(countryRepository.existsByName("Singapore")).thenReturn(false);
        when(countryRepository.save(singapore)).thenReturn(singapore);

        Country result = countryService.addCountry(singapore);

        assertEquals("Singapore", result.getName());
        verify(countryRepository).save(singapore);
    }

    @Test
    void testAddCountryDuplicate() {
        when(countryRepository.existsByName("Singapore")).thenReturn(true);

        assertThrows(DuplicateCountryException.class, () -> countryService.addCountry(singapore));
        verify(countryRepository, never()).save(any());
    }

    @Test
    void testUpdateCountrySuccess() {
        when(countryRepository.existsById("SG")).thenReturn(true);
        when(countryRepository.findByName("Singapore")).thenReturn(Optional.of(singapore));
        when(countryRepository.save(any(Country.class))).thenReturn(singapore);

        Country updated = new Country("SG", "Singapore");
        Country result = countryService.updateCountry("SG", updated);

        assertEquals("SG", result.getCountryCode());
        verify(countryRepository).save(updated);
    }

    @Test
    void testUpdateCountryNotFound() {
        when(countryRepository.existsById("XX")).thenReturn(false);

        assertThrows(CountryNotFoundException.class, () -> countryService.updateCountry("XX", singapore));
        verify(countryRepository, never()).save(any());
    }

    @Test
    void testUpdateCountryDuplicateName() {
        when(countryRepository.existsById("MY")).thenReturn(true);
        when(countryRepository.findByName("Singapore")).thenReturn(Optional.of(singapore));

        Country updated = new Country("MY", "Singapore"); // duplicate name
        assertThrows(DuplicateCountryException.class, () -> countryService.updateCountry("MY", updated));
    }

    @Test
    void testDeleteCountrySuccess() {
        when(countryRepository.existsById("SG")).thenReturn(true);
        doNothing().when(countryRepository).deleteById("SG");

        countryService.deleteCountry("SG");

        verify(countryRepository).deleteById("SG");
    }

    @Test
    void testDeleteCountryNotFound() {
        when(countryRepository.existsById("XX")).thenReturn(false);

        assertThrows(CountryNotFoundException.class, () -> countryService.deleteCountry("XX"));
        verify(countryRepository, never()).deleteById(anyString());
    }

    @Test
    void testExistsByName() {
        when(countryRepository.existsByName("Singapore")).thenReturn(true);

        boolean result = countryService.existsByName("Singapore");

        assertTrue(result);
        verify(countryRepository).existsByName("Singapore");
    }
}
