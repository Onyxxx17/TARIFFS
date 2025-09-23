// package com.tariff.config;

// import java.io.BufferedReader;
// import java.io.File;
// import java.io.FileReader;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.CommandLineRunner;
// import org.springframework.stereotype.Component;

// import com.tariff.entity.Country;
// import com.tariff.repository.CountryRepository;

// @Component
// public class CountryLoader implements CommandLineRunner {

//     @Autowired
//     private CountryRepository countryRepository;

//     @Override
//     public void run(String... args) throws Exception {
//         File file = new File("src/main/resources/data/countries.csv");
        
//         try (BufferedReader br = new BufferedReader(new FileReader(file))) {
//             // Skip the header line
//             String line = br.readLine();
            
//             while ((line = br.readLine()) != null) {
//                 String[] data = line.split(",");
//                 if (data.length >= 2) {
//                     String countryCode = data[0];
//                     String name = data[1];
                    
//                     Country existingCountry = countryRepository.findById(countryCode).orElse(null);
                    
//                     if (existingCountry == null) {
//                         Country country = new Country(countryCode, name);
//                         countryRepository.save(country);
//                         System.out.println("Saved country: " + name + " (" + countryCode + ")");
//                     }
//                 }
//             }
//         } catch (Exception e) {
//             System.err.println("Error loading countries: " + e.getMessage());
//             e.printStackTrace();
//         }
//     }
// }


