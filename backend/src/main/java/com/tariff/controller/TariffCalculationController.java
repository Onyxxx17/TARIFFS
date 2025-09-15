package com.tariff.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;


@RestController
@RequestMapping("/api/tariffs")
public class TariffCalculationController {
    @GetMapping("/calculate")
    public String calculate() {
        return "Calculation!";
    }
    
}
