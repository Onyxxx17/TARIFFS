# AI System Troubleshooting Guide

## Current Status
✅ **Backend compiles successfully**  
✅ **Frontend compiles successfully**  
✅ **AI endpoints are available**  
✅ **SQL generation and execution system is in place**  

## Fixed Issues

### 1. SQL Syntax Errors
**Problem:** AI was generating SQL with bad syntax like `SELECT tr.rate, tr.effective_year FROM tariff_rule tr JOIN countries c1 ON tr.from_country_id = c1.country_code`

**Solution:** 
- Enhanced TextToSqlService with better examples and stricter rules
- Added SQL cleaning and validation in DatabaseQueryService
- Added fallback queries for common requests
- Improved error handling and logging

### 2. Product Entity Method Issues
**Problem:** Lombok-generated methods (`getId()`, `getName()`) were not being recognized

**Solution:** 
- Simplified getRealTariffData method to avoid Product entity for now
- Focus on basic country and tariff rule queries first
- Can be enhanced later once Lombok issues are resolved

## How to Test the AI System

### 1. Backend Test Endpoint
```bash
POST /api/ai/test
```
This endpoint tests the AI with a simple "show me all countries" query.

### 2. Frontend AI Chat
Visit `/ai-chat` in your frontend application to test the interactive chat interface.

### 3. Simple Test Queries
Try these queries to verify the system is working:

**Basic Data Queries:**
- "show me all countries"
- "list all products" 
- "what countries are in the database"
- "show me tariff rates"

**General Knowledge Queries:**
- "what is a tariff?"
- "how do trade wars work?"
- "explain customs procedures"

## Current Limitations

1. **Product Entity Integration:** Currently simplified due to Lombok method recognition issues
2. **Complex Joins:** Some complex SQL queries may still fail - the system will fall back to simpler queries
3. **AI Model Dependency:** Requires Ollama to be running and accessible

## Debugging Tips

### 1. Check Backend Logs
The system now logs generated SQL queries and errors:
```
Generated SQL: SELECT country_code, name FROM countries LIMIT 20;
```

### 2. SQL Query Issues
If you see SQL errors, the system will:
- Try to clean the SQL (remove markdown, fix syntax)
- Fall back to simpler predefined queries
- Provide helpful error messages

### 3. Frontend Error Handling
The frontend chat interface shows:
- SQL queries that were executed
- Database results in table format
- Error messages with suggestions

## Next Steps for Enhancement

1. **Fix Lombok Issues:** Resolve Product entity method recognition
2. **Enhanced SQL Generation:** Add more sophisticated query patterns
3. **Caching:** Add query result caching for better performance
4. **Advanced Analytics:** Add support for complex analytical queries

## Common Error Messages and Solutions

### "Query contains unsafe operations"
**Cause:** SQL contains forbidden keywords (DROP, DELETE, etc.)  
**Solution:** Rephrase query to use only SELECT statements

### "SQL syntax error - the query could not be parsed"
**Cause:** AI generated invalid SQL syntax  
**Solution:** Try simpler queries or use fallback queries like "show me all countries"

### "Referenced table or column does not exist"
**Cause:** SQL references non-existent database objects  
**Solution:** Check database schema and try basic queries first

### "I couldn't retrieve the specific data you requested"
**Cause:** General query execution failure  
**Solution:** Try simpler queries like:
- "list countries"
- "show products" 
- "what tariff rates are available"

## Success Indicators

✅ Backend compiles without errors  
✅ Frontend builds successfully  
✅ AI chat interface loads at `/ai-chat`  
✅ Simple queries like "show me all countries" work  
✅ Error messages are helpful and suggest alternatives  
✅ SQL queries are logged for debugging  

The AI system is now functional with basic query capabilities and robust error handling. You can start testing with simple queries and gradually try more complex ones as the system learns and improves.