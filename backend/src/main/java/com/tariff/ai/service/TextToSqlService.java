package com.tariff.ai.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
public class TextToSqlService {

    private final ChatClient chatClient;

    public TextToSqlService(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
    }

    public String convertToSql(String naturalLanguageQuery) {
        String systemPrompt = """
            You are a SQL expert for a tariff database system. Convert natural language queries to VALID PostgreSQL SQL.
            
            CRITICAL: Return ONLY the SQL query, no explanations, no markdown, no extra text.
            
            Database Schema:
            
            1. countries table:
               - country_code (VARCHAR, PRIMARY KEY) - e.g., 'US', 'CN', 'DE'
               - name (VARCHAR) - e.g., 'United States of America', 'China', 'Germany'
            
            2. products table:
               - id (BIGINT, PRIMARY KEY)
               - name (VARCHAR) - product name
               - category_id (BIGINT) - foreign key to categories
            
            3. categories table:
               - id (BIGINT, PRIMARY KEY)
               - name (VARCHAR) - category name like 'Agriculture', 'Electronics'
            
            4. tariff_rule table:
               - id (BIGINT, PRIMARY KEY)
               - from_country_id (VARCHAR) - foreign key to countries.country_code (can be NULL for MFN rates)
               - to_country_id (VARCHAR) - foreign key to countries.country_code
               - product_id (BIGINT) - foreign key to products.id
               - rate (DECIMAL) - tariff rate as percentage
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
            
            CORRECT Examples:
            
            Query: "tariff rates for China"
            SQL: SELECT c.name, tr.rate, tr.effective_year FROM tariff_rule tr JOIN countries c ON tr.to_country_id = c.country_code WHERE UPPER(c.name) LIKE UPPER('%China%') LIMIT 20;
            
            Query: "products from agriculture category"  
            SQL: SELECT p.name FROM products p JOIN categories cat ON p.category_id = cat.id WHERE UPPER(cat.name) LIKE UPPER('%agriculture%') LIMIT 20;
            
            Query: "tariff rates between US and Germany"
            SQL: SELECT c1.name as from_country, c2.name as to_country, tr.rate, tr.effective_year FROM tariff_rule tr JOIN countries c1 ON tr.from_country_id = c1.country_code JOIN countries c2 ON tr.to_country_id = c2.country_code WHERE (UPPER(c1.name) LIKE UPPER('%United States%') OR UPPER(c1.name) LIKE UPPER('%US%')) AND (UPPER(c2.name) LIKE UPPER('%Germany%')) LIMIT 20;
            
            Query: "all countries"
            SQL: SELECT country_code, name FROM countries LIMIT 20;
            
            Query: "all products"
            SQL: SELECT id, name FROM products LIMIT 20;
            """;

        try {
            String sqlQuery = this.chatClient.prompt()
                    .system(systemPrompt)
                    .user(naturalLanguageQuery)
                    .call()
                    .content();
            
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
            return "SELECT country_code, name FROM countries LIMIT 20;";
        }
        
        if (lowerQuery.contains("products") || lowerQuery.contains("product")) {
            return "SELECT id, name FROM products LIMIT 20;";
        }
        
        if (lowerQuery.contains("categories") || lowerQuery.contains("category")) {
            return "SELECT id, name FROM categories LIMIT 20;";
        }
        
        if (lowerQuery.contains("tariff") || lowerQuery.contains("rate")) {
            return "SELECT tr.rate, tr.effective_year, c.name as country FROM tariff_rule tr JOIN countries c ON tr.to_country_id = c.country_code LIMIT 20;";
        }
        
        // Default fallback
        return "SELECT country_code, name FROM countries LIMIT 10;";
    }
}