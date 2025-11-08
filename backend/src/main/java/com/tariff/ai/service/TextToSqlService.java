package com.tariff.ai.service;

import com.tariff.service.GeminiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TextToSqlService {

    private final GeminiService geminiService;

    @Autowired
    public TextToSqlService(GeminiService geminiService) {
        this.geminiService = geminiService;
    }

    public String convertToSql(String naturalLanguageQuery) {
        String systemPrompt = """
            You are a SQL expert for a tariff database system. Convert natural language queries to VALID PostgreSQL SQL.
            
            CRITICAL: Return ONLY the SQL query, no explanations, no markdown, no extra text.
            
            Database Schema:
            
            1. country table:
               - country_code (VARCHAR, PRIMARY KEY) - e.g., 'US', 'CN', 'DE'
               - name (VARCHAR) - e.g., 'United States of America', 'China', 'Germany'
            
            2. product table:
               - id (BIGINT, PRIMARY KEY)
               - name (VARCHAR) - product name
               - category_id (BIGINT) - foreign key to category
            
            3. category table:
               - id (BIGINT, PRIMARY KEY)
               - name (VARCHAR) - category name like 'Agriculture', 'Electronics'
            
            4. tariff_rule table:
               - id (BIGINT, PRIMARY KEY)
               - from_country_id (VARCHAR) - foreign key to country.country_code (can be NULL for MFN rates)
               - to_country_id (VARCHAR) - foreign key to country.country_code
               - product_id (BIGINT) - foreign key to product.id
               - rate (DECIMAL) - tariff rate as percentage
               - additional_fee (DECIMAL) - additional fee
               - effective_year (INTEGER) - year when tariff is effective
            
            STRICT RULES:
            1. Use EXACT table and column names as shown above
            2. Always use proper JOIN syntax: JOIN table_name alias ON condition
            3. Use UPPER() function for case-insensitive text matching: UPPER(c.name) LIKE UPPER('%China%')
            4. Always add LIMIT 20 to prevent large result sets
            5. Use proper table aliases (single letters: c, p, tr, cat)
            6. Return ONLY the SQL query, nothing else
            7. For country searches, try both full names and common abbreviations
            8. Don't filter by specific years unless explicitly requested
            9. Use LEFT JOIN for optional relationships, especially for from_country_id which can be NULL
            10. Start with simple queries - avoid complex multi-table joins unless necessary
            
            CORRECT Examples:
            
            Query: "tariff rates for China"
            SQL: SELECT c.name, tr.rate, tr.effective_year FROM tariff_rule tr JOIN country c ON tr.to_country_id = c.country_code WHERE UPPER(c.name) LIKE UPPER('%China%') LIMIT 20;
            
            Query: "products from agriculture category"  
            SQL: SELECT p.name FROM product p JOIN category cat ON p.category_id = cat.id WHERE UPPER(cat.name) LIKE UPPER('%agriculture%') LIMIT 20;
            
            Query: "tariff rates between US and Germany"
            SQL: SELECT c2.name as to_country, tr.rate, tr.effective_year FROM tariff_rule tr JOIN country c2 ON tr.to_country_id = c2.country_code WHERE UPPER(c2.name) LIKE UPPER('%Germany%') LIMIT 20;
            
            Query: "show me all countries"
            SQL: SELECT country_code, name FROM country LIMIT 20;
            
            Query: "list all tariff rules"
            SQL: SELECT tr.rate, tr.effective_year, c.name as country FROM tariff_rule tr LEFT JOIN country c ON tr.to_country_id = c.country_code LIMIT 20;
            
            Query: "all countries"
            SQL: SELECT country_code, name FROM country LIMIT 20;
            
            Query: "all products"
            SQL: SELECT id, name FROM product LIMIT 20;
            """;

        try {
            String sqlQuery = this.geminiService.generateContent(systemPrompt, naturalLanguageQuery);
            
            // Clean up the response - remove any markdown or extra text
            sqlQuery = sqlQuery.trim();
            if (sqlQuery.startsWith("```sql")) {
                sqlQuery = sqlQuery.substring(6);
            }
            if (sqlQuery.startsWith("```")) {
                sqlQuery = sqlQuery.substring(3);
            }
            if (sqlQuery.endsWith("```")) {
                sqlQuery = sqlQuery.substring(0, sqlQuery.length() - 3);
            }
            
            return sqlQuery.trim();
            
        } catch (Exception e) {
            // Fallback to simple queries based on keywords
            return generateFallbackQuery(naturalLanguageQuery);
        }
    }
    
    private String generateFallbackQuery(String query) {
        String lowerQuery = query.toLowerCase();
        
        if (lowerQuery.contains("countries") || lowerQuery.contains("country")) {
            return "SELECT country_code, name FROM country LIMIT 20;";
        }
        
        if (lowerQuery.contains("products") || lowerQuery.contains("product")) {
            return "SELECT id, name FROM product LIMIT 20;";
        }
        
        if (lowerQuery.contains("categories") || lowerQuery.contains("category")) {
            return "SELECT id, name FROM category LIMIT 20;";
        }
        
        if (lowerQuery.contains("tariff") || lowerQuery.contains("rate")) {
            return "SELECT rate, effective_year FROM tariff_rule LIMIT 20;";
        }
        
        // Default fallback - simple query that should always work
        return "SELECT COUNT(*) as total_countries FROM country;";
    }
}