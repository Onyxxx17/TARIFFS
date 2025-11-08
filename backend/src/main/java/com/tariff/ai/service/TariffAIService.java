package com.tariff.ai.service;

import com.tariff.ai.dto.TariffAnalysisRequest;
import com.tariff.ai.dto.TariffAnalysisResponse;
import com.tariff.ai.dto.TariffQueryRequest;
import com.tariff.ai.dto.TariffQueryResponse;
import com.tariff.repository.CountryRepository;
import com.tariff.repository.ProductRepository;
import com.tariff.repository.TariffRuleRepository;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class TariffAIService {

    private final ChatClient chatClient;
    private final TextToSqlService textToSqlService;
    private final DatabaseQueryService databaseQueryService;
    private final CountryRepository countryRepository;
    private final ProductRepository productRepository;
    private final TariffRuleRepository tariffRuleRepository;

    public TariffAIService(ChatClient.Builder chatClientBuilder,
                          TextToSqlService textToSqlService,
                          DatabaseQueryService databaseQueryService,
                          CountryRepository countryRepository,
                          ProductRepository productRepository,
                          TariffRuleRepository tariffRuleRepository) {
        this.chatClient = chatClientBuilder.build();
        this.textToSqlService = textToSqlService;
        this.databaseQueryService = databaseQueryService;
        this.countryRepository = countryRepository;
        this.productRepository = productRepository;
        this.tariffRuleRepository = tariffRuleRepository;
    }

    public TariffAnalysisResponse getTariffAnalysis(TariffAnalysisRequest request) {
        
        // First, try to get real data from database
        String realDataContext = getRealTariffData(request);
        
        String userMessage = """
                Analyze the import tariffs and additional fees for the following scenario:
                - Product: {product}
                - Exporting from: {origin}
                - Importing to: {destination}

                Real data from our database:
                {realData}

                Based on the real data above (if available) and your knowledge, provide the estimated tariff rate, 
                list any common additional fees (like VAT, customs processing fees, etc.), and give a brief summary.
                If real data is available, prioritize it over general knowledge.
                Also, include a disclaimer that this is an estimate and not legally binding financial advice.
                """; 

        return this.chatClient.prompt()
                .user(p -> p.text(userMessage)
                        .param("product", request.productDescription())
                        .param("origin", request.originCountry())
                        .param("destination", request.destinationCountry())
                        .param("realData", realDataContext))
                .call()
                .entity(TariffAnalysisResponse.class);
    }
    
    public TariffQueryResponse handleQuery(TariffQueryRequest request) {
        String query = request.query();
        String queryType = request.queryType() != null ? request.queryType() : "general";
        
        try {
            // Determine if this is a data query that needs database access
            if (isDataQuery(query)) {
                return handleDataQuery(query);
            } else {
                return handleGeneralQuery(query);
            }
        } catch (Exception e) {
            return new TariffQueryResponse(
                "I apologize, but I encountered an error processing your query: " + e.getMessage(),
                queryType,
                null,
                null,
                false
            );
        }
    }
    
    private String getRealTariffData(TariffAnalysisRequest request) {
        try {
            // Try to find countries by name
            var fromCountry = countryRepository.findByName(request.originCountry());
            var toCountry = countryRepository.findByName(request.destinationCountry());
            
            if (fromCountry.isEmpty() || toCountry.isEmpty()) {
                return "No specific tariff data found in our database for these countries.";
            }
            
            // For now, just return basic country information
            StringBuilder dataContext = new StringBuilder();
            dataContext.append("Found countries in database:\n");
            dataContext.append("From: ").append(fromCountry.get().getName()).append(" (").append(fromCountry.get().getCountryCode()).append(")\n");
            dataContext.append("To: ").append(toCountry.get().getName()).append(" (").append(toCountry.get().getCountryCode()).append(")\n");
            dataContext.append("Product search for: ").append(request.productDescription()).append("\n");
            
            return dataContext.toString();
            
        } catch (Exception e) {
            return "Error retrieving data from database: " + e.getMessage();
        }
    }
    
    private boolean isDataQuery(String query) {
        String lowerQuery = query.toLowerCase();
        return lowerQuery.contains("tariff rate") || 
               lowerQuery.contains("countries") || 
               lowerQuery.contains("products") || 
               lowerQuery.contains("show me") || 
               lowerQuery.contains("list") || 
               lowerQuery.contains("find") ||
               lowerQuery.contains("what are") ||
               lowerQuery.contains("data");
    }
    
    private TariffQueryResponse handleDataQuery(String query) {
        try {
            // Convert natural language to SQL
            String sqlQuery = textToSqlService.convertToSql(query);
            
            // Log the generated SQL for debugging
            System.out.println("Generated SQL: " + sqlQuery);
            
            // Execute the query
            List<Map<String, Object>> results = databaseQueryService.executeQuery(sqlQuery);
            
            // Format results for AI analysis
            String formattedResults = databaseQueryService.formatResultsAsText(results);
            
            // Get AI interpretation of the results
            String aiResponse = getAIInterpretation(query, formattedResults);
            
            return new TariffQueryResponse(
                aiResponse,
                "data",
                results,
                sqlQuery,
                !results.isEmpty()
            );
            
        } catch (Exception e) {
            // Log the full error for debugging
            System.err.println("Error in handleDataQuery: " + e.getMessage());
            e.printStackTrace();
            
            return new TariffQueryResponse(
                "I couldn't retrieve the specific data you requested. Error: " + e.getMessage() + 
                ". However, I can provide general information about tariffs if you'd like. You can try asking simpler questions like 'show me all countries' or 'list all products'.",
                "data",
                null,
                null,
                false
            );
        }
    }
    
    private TariffQueryResponse handleGeneralQuery(String query) {
        String systemPrompt = """
            You are a tariff and international trade expert. Answer questions about tariffs, 
            trade policies, customs procedures, and related topics. Provide accurate, helpful 
            information while noting when specific data would require database lookup.
            """;
            
        String response = this.chatClient.prompt()
                .system(systemPrompt)
                .user(query)
                .call()
                .content();
                
        return new TariffQueryResponse(
            response,
            "general",
            null,
            null,
            false
        );
    }
    
    private String getAIInterpretation(String originalQuery, String queryResults) {
        String prompt = """
            The user asked: "{query}"
            
            Here are the results from our database:
            {results}
            
            Please provide a clear, helpful interpretation of these results in response to the user's question.
            If the results are empty, explain that no data was found and suggest alternative queries.
            """;
            
        return this.chatClient.prompt()
                .user(p -> p.text(prompt)
                        .param("query", originalQuery)
                        .param("results", queryResults))
                .call()
                .content();
    }
}