package com.tariff.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tariff.dto.request.TariffCalculationRequest;
import com.tariff.dto.request.TariffPredictionRequest;
import com.tariff.dto.response.TariffCalculationResponse;
import com.tariff.dto.response.TariffPredictionResponse;
import com.tariff.service.TariffCalculationService;
import com.tariff.service.TariffPredictionService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/tariffs")
@Tag(name = "Tariff Calculations", description = "Tariff calculation and prediction endpoints")
public class TariffCalculationController {

    @Autowired
    private TariffCalculationService tariffCalculationService;

    @Autowired
    private TariffPredictionService tariffPredictionService;

    @PostMapping("/calculate")
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "Calculate tariff", 
               description = "Calculates tariff amount based on countries, product, unit cost, quantity, and year - requires authentication")
    public TariffCalculationResponse calculate(@RequestBody TariffCalculationRequest request) {
        return tariffCalculationService.calculateTariff(request);   
    }

    @PostMapping("/predict")
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "Predict future tariff rate",
        description = "Estimates a future tariff rate based on historical data - requires authentication")
    public TariffPredictionResponse predict(@RequestBody TariffPredictionRequest request) {
        return tariffPredictionService.predictFutureTariffRate(request);
    }
}
