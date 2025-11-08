# Tariff AI System Documentation

## Overview

The Tariff AI System integrates Ollama AI with the tariff database to provide intelligent querying and analysis capabilities. Users can ask natural language questions about countries, tariff rates, products, and trade data, and the AI will extract real data from the SQL database when possible.

## Features

### 1. Natural Language to SQL Conversion
- Converts user questions into SQL queries
- Safely executes queries against the database
- Supports complex joins across countries, products, categories, and tariff rules

### 2. Real Data Integration
- Extracts actual tariff rates, additional fees, and trade data
- Provides context-aware responses based on database content
- Falls back to general AI knowledge when specific data isn't available

### 3. Security Features
- SQL injection protection through query validation
- Whitelist of allowed SQL operations (SELECT only)
- Automatic query limits to prevent large result sets
- Blacklist of dangerous SQL keywords

## API Endpoints

### 1. Tariff Analysis
```
POST /api/ai/analyze-tariffs
```

**Request Body:**
```json
{
  "originCountry": "China",
  "destinationCountry": "United States of America", 
  "productDescription": "Electronics"
}
```

**Response:**
```json
{
  "tariffRate": "25%",
  "additionalFees": [
    {
      "feeName": "Processing Fee",
      "amount": "$50",
      "description": "Standard customs processing fee"
    }
  ],
  "summary": "Analysis based on real database data...",
  "disclaimer": "This is an estimate and not legally binding financial advice."
}
```

### 2. Natural Language Query
```
POST /api/ai/query
```

**Request Body:**
```json
{
  "query": "What are the tariff rates for China?",
  "queryType": "data"
}
```

**Response:**
```json
{
  "response": "Based on our database, here are the tariff rates for China...",
  "queryType": "data",
  "data": [
    {
      "country": "China",
      "rate": "25.0",
      "effective_year": 2025
    }
  ],
  "sqlQuery": "SELECT c.name, tr.rate, tr.effective_year FROM tariff_rule tr JOIN countries c ON tr.to_country_id = c.country_code WHERE c.name ILIKE '%China%' LIMIT 10;",
  "hasData": true
}
```

## Example Queries

### Data Queries (Extract from Database)
- "What are the tariff rates for China?"
- "Show me products in the agriculture category"
- "List countries with tariff data"
- "Find tariff rates between US and Germany"
- "What products have the highest tariff rates?"

### General Queries (AI Knowledge)
- "What is a tariff?"
- "How do trade wars affect global economics?"
- "What are the benefits of free trade agreements?"
- "Explain customs procedures"

## Database Schema

The AI system understands the following database structure:

### Countries Table
- `country_code` (VARCHAR, PRIMARY KEY) - e.g., 'US', 'CN', 'DE'
- `name` (VARCHAR) - e.g., 'United States of America', 'China', 'Germany'

### Products Table
- `id` (BIGINT, PRIMARY KEY)
- `name` (VARCHAR) - product name
- `category_id` (BIGINT) - foreign key to categories

### Categories Table
- `id` (BIGINT, PRIMARY KEY)
- `name` (VARCHAR) - category name like 'Agriculture', 'Electronics'

### Tariff Rules Table
- `id` (BIGINT, PRIMARY KEY)
- `from_country_id` (VARCHAR) - foreign key to countries (can be NULL for MFN rates)
- `to_country_id` (VARCHAR) - foreign key to countries
- `product_id` (BIGINT) - foreign key to products
- `rate` (DECIMAL) - tariff rate as percentage
- `additional_fees` (List<DECIMAL>) - additional fee rates
- `effective_year` (INTEGER) - year when tariff is effective

## Frontend Integration

### AI Chat Interface
Access the AI chat interface at `/ai-chat` in the frontend application.

**Features:**
- Real-time chat interface
- Display of SQL queries used
- Tabular display of database results
- Message history
- Loading states and error handling

### Navigation
The AI Assistant is accessible through:
- Main navigation menu under "Features" dropdown
- Direct URL: `/ai-chat`

## Configuration

### Environment Variables
Make sure the following environment variables are set in your `.env` file:

```
# Database Configuration
DB_URL=your_database_url
DB_USERNAME=your_db_username
DB_PASSWORD=your_db_password

# AI Configuration (if using external AI service)
OLLAMA_BASE_URL=http://localhost:11434
```

### Ollama Setup
1. Install Ollama on your system
2. Pull a suitable model (e.g., `ollama pull llama2`)
3. Ensure Ollama is running on the default port (11434)

## Security Considerations

### SQL Injection Prevention
- Only SELECT statements are allowed
- Dangerous keywords (DROP, DELETE, UPDATE, etc.) are blocked
- Query results are limited to prevent resource exhaustion
- Input validation and sanitization

### Authentication
- All AI endpoints require valid authentication
- Uses the same JWT-based authentication as other API endpoints

## Troubleshooting

### Common Issues

1. **"Query contains unsafe operations"**
   - The AI generated a query with forbidden SQL operations
   - Try rephrasing your question to be more specific

2. **"No results found"**
   - The database doesn't contain data matching your query
   - Try broader search terms or check if the data exists

3. **"Error executing query"**
   - SQL syntax error in generated query
   - Database connection issues
   - Check logs for detailed error information

### Debugging
- Check backend logs for SQL query generation and execution
- Verify database connectivity
- Ensure Ollama service is running and accessible

## Future Enhancements

- Support for more complex analytical queries
- Integration with external trade data sources
- Caching of frequently asked questions
- Multi-language support
- Voice input/output capabilities
- Export functionality for query results