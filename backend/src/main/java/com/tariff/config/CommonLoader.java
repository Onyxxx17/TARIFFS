package com.tariff.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;


import java.io.*;
import java.util.*;

@Component
public class CommonLoader implements CommandLineRunner {

    @Autowired
    private CategoryLoader categoryLoader;

    public void run(String... args) {
        // categoryLoader.loadData();
    }

    
}
