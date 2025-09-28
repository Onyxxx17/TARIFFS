// package com.tariff.config;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.CommandLineRunner;
// import org.springframework.stereotype.Component;

// import com.tariff.entity.Category;
// import com.tariff.entity.Product;
// import com.tariff.repository.CategoryRepository;
// import com.tariff.repository.ProductRepository;

// import java.util.*;
// import java.io.*;

// @Component
// public class ProductLoader implements CommandLineRunner{
//     @Autowired
//     private ProductRepository productRepository;

//     @Autowired
//     private CategoryRepository categoryRepository;

//     private List<Category> categories = new ArrayList<>();

//     @Override 
//     public void run(String... args) throws Exception {      
        
    //     try{
    //         File file = new File("src/main/java/com/tariff/csvs/categories.csv");
    //         Scanner fIn = new Scanner(file);

    //         fIn.nextLine();
            
    //         while (fIn.hasNext()) {
    //             String curLine = fIn.nextLine();
    //             System.out.println(curLine);
    //             String[] components = curLine.split(",");
    //             Long id = Long.parseLong(components[0]);
    //             String name = components[1];
    //             Category category = new Category(id, name);
    //             categoryRepository.save(category);
               
                
    //         }	
            
    //         fIn.close();
    //     } 
    //     catch(IOException e) {	
    //         e.printStackTrace();
    //     }
    //     System.out.println(categories);
    //    try{
    //         File file = new File("src/main/java/com/tariff/csvs/products.csv");
    //         Scanner fIn = new Scanner(file);

    //         fIn.nextLine();
            
    //         while (fIn.hasNext()) {
    //             String curLine = fIn.nextLine();
    //             Long id = Long.parseLong(curLine.substring(0, curLine.indexOf(",")));
    //             String name = curLine.substring(curLine.indexOf(",") + 1, curLine.lastIndexOf(","));
    //             // System.out.println(name.startsWith("\"") && name.endsWith("\""));
    //             if (name.startsWith("\"") && name.endsWith("\"")) {
    //                name = name.substring(1, name.length() - 1);
    //             } else if (name.startsWith("\"")) {
    //                 name = name.substring(1);
    //             } else if (name.endsWith("\"")) {
    //                 name = name.substring(0, name.length() - 1);
    //             }
    //             Long productId = Long.parseLong(curLine.substring(curLine.lastIndexOf(",") + 1));
                
    //             Category category = categoryRepository.findById(productId).get();
                
    //             Product product = new Product(id, name, category);

    //             productRepository.save(product);

                // System.out.println(categoryRepository.findById(productId).get());

                // System.out.println(id);
                // System.out.println(name);
                // System.out.println(productId);


             
                

                // System.out.println(id + name + productId);

                // first, identify that particular category
                // Category category = categoryRepository.findById(productId).get();

                // System.out.print(productId + ",");
                // System.out.print(category.getId());
                // System.out.println();
                
        //     }	
            
        //     fIn.close();
        // } 
        // catch(IOException e) {	
        //     e.printStackTrace();
//         // }
//     }
// }
