package com.tariff.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.tariff.entity.Industry;
import com.tariff.entity.Product;
import com.tariff.repository.IndustryRepository;
import com.tariff.repository.ProductRepository;

@Component
public class ProductLoader implements CommandLineRunner{
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private IndustryRepository industryRepository;

    @Override 
    public void run(String... args) throws Exception {


    // Sample data generation
    // Assuming you have Industry objects created, here are some sample products:

        // Sample Industries (you'll need to create these first)
        // Industry electronics = new Industry("Electronics & Technology", "Challenger");
        // Industry textiles = new Industry("Textiles & Apparel", "Chanel");
        // Industry automotive = new Industry("Automotive", "Tesla");
        // Industry food = new Industry("Food & Beverages", "A&W");
        // Industry machinery = new Industry("Industrial Machinery", "Macro's mechanics");

        // // SAVE all industries first - this is the crucial step you were missing!
        // electronics = industryRepository.save(electronics);
        // textiles = industryRepository.save(textiles);
        // automotive = industryRepository.save(automotive);
        // food = industryRepository.save(food);
        // machinery = industryRepository.save(machinery);

        // // Sample Products
        // Product product1 = new Product(
        //     "8517120000",
        //     "Wireless Bluetooth Headphones",
        //     "Premium over-ear wireless headphones with active noise cancellation, 30-hour battery life, and premium audio drivers. Compatible with all Bluetooth-enabled devices.",
        //     electronics,
        //     299.99
        // );

        // Product product2 = new Product(
        //     "6203420010", 
        //     "Men's Cotton Denim Jeans",
        //     "Classic fit men's denim jeans made from 100% cotton with reinforced stitching and traditional 5-pocket design. Available in various sizes and washes.",
        //     textiles,
        //     79.95
        // );

        // Product product3 = new Product(
        //     "8708801000",
        //     "Vehicle Shock Absorber Assembly",
        //     "Heavy-duty hydraulic shock absorber designed for passenger vehicles. Features adjustable dampening and corrosion-resistant coating for extended durability.",
        //     automotive,
        //     145.50
        // );

        // Product product4 = new Product(
        //     "2009900000",
        //     "Organic Apple Juice Concentrate",
        //     "100% organic apple juice concentrate made from premium Gala and Fuji apples. No added sugars or preservatives. Perfect for beverage manufacturing or direct consumption when diluted.",
        //     food,
        //     12.75
        // );

        // Product product5 = new Product(
        //     "8479820000",
        //     "Industrial Mixing Equipment",
        //     "High-capacity industrial mixer for food processing applications. Stainless steel construction with variable speed control and safety interlocks. Capacity: 500 liters.",
        //     machinery,
        //     15750.00
        // );

        // Product product6 = new Product(
        //     "8471300000",
        //     "Portable Data Processing Unit",
        //     "Compact portable computer designed for field data collection and processing. Ruggedized design with IP67 rating, extended battery life, and wireless connectivity options.",
        //     electronics,
        //     899.99
        // );

        // Product product7 = new Product(
        //     "6109100010",
        //     "Women's Cotton T-Shirt",
        //     "Soft cotton crew neck t-shirt for women. Pre-shrunk fabric with reinforced seams. Available in multiple colors and sizes. Made from sustainably sourced cotton.",
        //     textiles,
        //     24.99
        // );

        // Product product8 = new Product(
        //     "2103900000",
        //     "Gourmet BBQ Sauce",
        //     "Premium barbecue sauce with smoky flavor profile. Made with natural ingredients including tomatoes, molasses, and select spices. No artificial preservatives.",
        //     food,
        //     8.49
        // );

        // productRepository.save(product1);
        // productRepository.save(product2);
        // productRepository.save(product3);
        // productRepository.save(product4);
        // productRepository.save(product5);
        // productRepository.save(product6);
        // productRepository.save(product7);
        // productRepository.save(product8);
       
    }
}
