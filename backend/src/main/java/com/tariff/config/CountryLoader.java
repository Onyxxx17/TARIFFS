package com.tariff.config;

import java.util.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;
import org.w3c.dom.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.tariff.entity.Country;
import com.tariff.repository.CountryRepository;

@Component
public class CountryLoader implements CommandLineRunner {

    @Autowired
    private CountryRepository countryRepository;

    @Override
    public void run(String... args) throws Exception {
        String url = "https://wits.worldbank.org/API/V1/wits/datasource/trn/country/ALL";
        RestTemplate restTemplate = new RestTemplate();

        try {
            String xmlResponse = restTemplate.getForObject(url, String.class);
            
            // Remove BOM if present
            if (xmlResponse.startsWith("\uFEFF")) {
                xmlResponse = xmlResponse.substring(1);
            }
            // This handles the BOM characters which was causing errors
            if (xmlResponse.startsWith("ï»¿")) {
                xmlResponse = xmlResponse.substring(3);
            }
            
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new ByteArrayInputStream(xmlResponse.getBytes("UTF-8")));
            
            NodeList countryNodes = document.getElementsByTagName("wits:country");
            
            for (int i = 0; i < countryNodes.getLength(); i++) {
                Element countryElement = (Element) countryNodes.item(i);
                
                String name = countryElement.getElementsByTagName("wits:name").item(0).getTextContent();
                String iso3Code = countryElement.getElementsByTagName("wits:iso3Code").item(0).getTextContent();
                
                if (name != null && !name.trim().isEmpty()) {
                    Country existingCountry = countryRepository.findByName(name).orElse(null);
                    
                    if (existingCountry == null) {
                        Country country = new Country(name, iso3Code, "USD");
                        countryRepository.save(country);
                        System.out.println("Saved country: " + name);
                    }
                }
            }
            
        } catch (Exception e) {
            System.err.println("Error loading countries: " + e.getMessage());
            e.printStackTrace();
        }
    }
}


