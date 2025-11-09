package com.tariff.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tariff.entity.Category;
import com.tariff.repository.CategoryRepository;

import java.io.*;
import java.util.*;

@Component
public class CategoryLoader {

    @Autowired
    private CategoryRepository categoryRepository;



    public void loadData() {
        try{
            File file = new File("src/main/java/com/tariff/csvs/categories.csv");
            Scanner fIn = new Scanner(file);

            fIn.nextLine();
            
            while (fIn.hasNext()) {
                String curLine = fIn.nextLine();
                System.out.println(curLine);
                String[] components = curLine.split(",");
                Long id = Long.parseLong(components[0]);
                String name = components[1];
                Category category = new Category(id, name);
                System.out.println(category);
                // categoryRepository.save(category);
                
            }	
            
            fIn.close();
        } 
        catch(IOException e) {	
            e.printStackTrace();
        }
    }
}
