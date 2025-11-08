package com.tariff.ai.service;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

@Service
public class DatabaseQueryService {

    private final JdbcTemplate jdbcTemplate;
    
    // Whitelist of allowed SQL operations for security
    private static final Pattern ALLOWED_QUERY_PATTERN = Pattern.compile(
        "^\\s*SELECT\\s+.*", 
        Pattern.CASE_INSENSITIVE | Pattern.DOTALL
    );
    
    // Blacklist of dangerous SQL keywords
    private static final Pattern DANGEROUS_KEYWORDS = Pattern.compile(
        "\\b(DROP|DELETE|UPDATE|INSERT|ALTER|CREATE|TRUNCATE|EXEC|EXECUTE)\\b",
        Pattern.CASE_INSENSITIVE
    );

    public DatabaseQueryService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Map<String, Object>> executeQuery(String sql) {
        // Security validation
        if (!isQuerySafe(sql)) {
            throw new SecurityException("Query contains unsafe operations");
        }
        
        try {
            // Clean and validate SQL
            String cleanSql = cleanSql(sql);
            String limitedSql = addLimitIfMissing(cleanSql);
            
            return jdbcTemplate.queryForList(limitedSql);
        } catch (Exception e) {
            // Log the error for debugging
            System.err.println("SQL Error: " + e.getMessage());
            System.err.println("SQL Query: " + sql);
            
            // Provide a more user-friendly error message
            String errorMsg = "Database query failed";
            if (e.getMessage().contains("syntax error")) {
                errorMsg = "SQL syntax error - the query could not be parsed";
            } else if (e.getMessage().contains("does not exist")) {
                errorMsg = "Referenced table or column does not exist";
            } else if (e.getMessage().contains("permission")) {
                errorMsg = "Insufficient permissions to execute query";
            }
            
            throw new RuntimeException(errorMsg + ": " + e.getMessage(), e);
        }
    }
    
    private String cleanSql(String sql) {
        if (sql == null) return "";
        
        // Remove common formatting issues
        sql = sql.trim();
        
        // Remove markdown code blocks if present
        if (sql.startsWith("```sql")) {
            sql = sql.substring(6);
        }
        if (sql.startsWith("```")) {
            sql = sql.substring(3);
        }
        if (sql.endsWith("```")) {
            sql = sql.substring(0, sql.length() - 3);
        }
        
        // Ensure it ends with semicolon
        sql = sql.trim();
        if (!sql.endsWith(";")) {
            sql += ";";
        }
        
        return sql;
    }
    
    private boolean isQuerySafe(String sql) {
        if (sql == null || sql.trim().isEmpty()) {
            return false;
        }
        
        // Must be a SELECT statement
        if (!ALLOWED_QUERY_PATTERN.matcher(sql).matches()) {
            return false;
        }
        
        // Must not contain dangerous keywords
        if (DANGEROUS_KEYWORDS.matcher(sql).find()) {
            return false;
        }
        
        return true;
    }
    
    private String addLimitIfMissing(String sql) {
        String upperSql = sql.toUpperCase();
        if (!upperSql.contains("LIMIT")) {
            // Add a reasonable limit to prevent large result sets
            return sql + " LIMIT 50";
        }
        return sql;
    }
    
    public String formatResultsAsText(List<Map<String, Object>> results) {
        if (results.isEmpty()) {
            return "No results found.";
        }
        
        StringBuilder sb = new StringBuilder();
        sb.append("Query Results:\n");
        sb.append("=============\n\n");
        
        for (int i = 0; i < results.size(); i++) {
            Map<String, Object> row = results.get(i);
            sb.append("Result ").append(i + 1).append(":\n");
            
            for (Map.Entry<String, Object> entry : row.entrySet()) {
                sb.append("  ").append(entry.getKey()).append(": ")
                  .append(entry.getValue() != null ? entry.getValue().toString() : "null")
                  .append("\n");
            }
            sb.append("\n");
        }
        
        return sb.toString();
    }
}