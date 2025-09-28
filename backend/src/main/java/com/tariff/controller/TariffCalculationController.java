package com.tariff.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tariff.dto.request.TariffCalculationRequest;
import com.tariff.dto.response.TariffCalculationResponse;
import com.tariff.service.TariffCalculationService;

import io.swagger.v3.oas.annotations.Operation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/api/tariffs")
public class TariffCalculationController {

    @Autowired
    private TariffCalculationService tariffCalculationService;

    @Operation(summary = "Calculate tariff", 
               description = "Calculates tariff amount based on countries, product, unit cost, quantity, and year")
    @PostMapping("/calculate")
    public TariffCalculationResponse calculate(@RequestBody TariffCalculationRequest request) {
        return tariffCalculationService.calculateTariff(request);   
    }
}
