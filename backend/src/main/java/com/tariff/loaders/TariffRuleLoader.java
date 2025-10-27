package com.tariff.loaders;
// // package com.tariff.config;

// import java.io.BufferedReader;
// import java.io.FileReader;
// import java.io.IOException;
// import java.math.BigDecimal;
// import java.time.LocalDate;
// import java.util.ArrayList;
// import java.util.Optional;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.CommandLineRunner;
// import org.springframework.core.io.ClassPathResource;
// import org.springframework.stereotype.Component;

// import com.tariff.entity.Country;
// import com.tariff.entity.Product;
// import com.tariff.entity.TariffRule;
// import com.tariff.repository.CountryRepository;
// import com.tariff.repository.ProductRepository;
// import com.tariff.repository.TariffRuleRepository;

// @Component  // Uncomment this to enable the loader
// public class TariffRuleLoader implements CommandLineRunner {

// //     @Autowired
// //     private TariffRuleRepository tariffRuleRepository;

// //     @Autowired
// //     private CountryRepository countryRepository;

//     @Autowired
//     private ProductRepository productRepository;

//     @Override
//     public void run(String... args) throws Exception {
//         // Check if data already exists to avoid duplicates
//         // if (tariffRuleRepository.count() > 0) {
//         //     System.out.println("TariffRule data already exists. Skipping data loading.");
//         //     return;
//         // }

//         System.out.println("Loading tariff data from CSV...");
//         loadTariffDataFromCSV();
//         System.out.println("Tariff data loaded successfully!");
//     }

//     private void loadTariffDataFromCSV() {
//         try {
//             // Read CSV file from resources folder
//             // Read CSV file from csvs folder
//             ClassPathResource resource = new ClassPathResource("data.csv");
//             BufferedReader reader = new BufferedReader(new FileReader(resource.getFile()));

//             String line;
//             boolean isFirstLine = true;
//             int successCount = 0;
//             int errorCount = 0;

//             while ((line = reader.readLine()) != null) {
//                 // Skip header line
//                 if (isFirstLine) {
//                     isFirstLine = false;
//                     continue;
//                 }

//                 try {
//                     // Parse CSV line
//                     String[] data = line.split("\t"); // Your CSV uses tabs

//                     if (data.length >= 5) {
//                         String reporterCode = data[0].trim();       // to_country_id
//                         String reporterName = data[1].trim();      // not used
//                         int year = Integer.parseInt(data[2].trim()); // effective_year
//                         String productCode = data[3].trim();       // product_id
//                         BigDecimal rate = new BigDecimal(data[4].trim()); // rate

//                         // Create tariff rule
//                         if (createTariffRule(reporterCode, year, productCode, rate)) {
//                             successCount++;
//                         } else {
//                             errorCount++;
//                         }
//                     }
//                 } catch (Exception e) {
//                     System.err.println("Error processing line: " + line);
//                     System.err.println("Error: " + e.getMessage());
//                     errorCount++;
//                 }
//             }

//             reader.close();
//             System.out.println("CSV processing completed:");
//             System.out.println("Successfully processed: " + successCount + " records");
//             System.out.println("Errors: " + errorCount + " records");

//         } catch (IOException e) {
//             System.err.println("Error reading CSV file: " + e.getMessage());
//         }
//     }

//     private boolean createTariffRule(String countryCode, int year, String productCode, BigDecimal rate) {
//         try {
//             // Find or create country
//             Optional<Country> countryOpt = countryRepository.findById(countryCode);
//             Country country;

//             country = countryOpt.get();

//             // Find or create product
//             Optional<Product> productOpt = productRepository.findById(Long.parseLong(productCode));
//             Product product;
//             product = productOpt.get();
           

//             // Create tariff rule
//             TariffRule tariffRule = new TariffRule();
//             tariffRule.setToCountry(country);      // Set Country entity
//             tariffRule.setProduct(product);        // Set Product entity
//             tariffRule.setRate(rate);              // Use correct setter
//             tariffRule.setEffectiveYear(year);     // Use correct setter
//             tariffRule.setAdditionalFees(new ArrayList<>()); // Set empty list for now

//                 tariffRuleRepository.save(tariffRule);
//                 return true;

//         } catch (Exception e) {
//             System.err.println("Error creating tariff rule: " + e.getMessage());
//             return false;
//         }
//     }

// }
