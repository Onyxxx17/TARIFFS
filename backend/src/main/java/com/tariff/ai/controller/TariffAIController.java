package com.tariff.ai.controller;

import com.tariff.ai.dto.TariffAnalysisRequest;
import com.tariff.ai.dto.TariffAnalysisResponse;
import com.tariff.ai.dto.TariffQueryRequest;
import com.tariff.ai.dto.TariffQueryResponse;
import com.tariff.ai.service.TariffAIService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ai")
@Tag(name = "Tariff AI", description = "AI-powered tariff analysis and queries")
public class TariffAIController {

    private final TariffAIService tariffAIService;

    public TariffAIController(TariffAIService tariffAIService) {
        this.tariffAIService = tariffAIService;
    }

    @PostMapping("/analyze-tariffs")
    @Operation(summary = "Analyze tariffs for specific trade scenario", 
               description = "Get AI-powered analysis of tariffs and fees for importing specific products between countries")
    public TariffAnalysisResponse analyzeTariffs(@Valid @RequestBody TariffAnalysisRequest request) {
        return tariffAIService.getTariffAnalysis(request);
    }
    
    @PostMapping("/query")
    @Operation(summary = "Ask questions about tariffs and trade data", 
               description = "Ask natural language questions about countries, tariff rates, products, and trade policies. The AI will extract data from the database when possible.")
    public TariffQueryResponse queryTariffs(@Valid @RequestBody TariffQueryRequest request) {
        return tariffAIService.handleQuery(request);
    }
    
    @PostMapping("/test")
    @Operation(summary = "Test AI system with simple queries", 
               description = "Test endpoint to verify AI system is working with simple database queries")
    public TariffQueryResponse testAI() {
        TariffQueryRequest testRequest = new TariffQueryRequest("show me all countries", "data");
        return tariffAIService.handleQuery(testRequest);
    }
}
