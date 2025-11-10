-- Check tariff rule structure
SELECT 
    COUNT(*) as total_rules,
    COUNT(DISTINCT from_country_id) as distinct_from_countries,
    COUNT(DISTINCT to_country_id) as distinct_to_countries,
    COUNT(DISTINCT product_id) as distinct_products,
    SUM(CASE WHEN from_country_id IS NULL THEN 1 ELSE 0 END) as null_from_country,
    SUM(CASE WHEN from_country_id IS NOT NULL THEN 1 ELSE 0 END) as specific_from_country
FROM tariff_rule;

-- Show sample rules
SELECT 
    tr.id,
    fc.name as from_country,
    tc.name as to_country,
    p.name as product,
    tr.rate,
    tr.effective_year
FROM tariff_rule tr
LEFT JOIN country fc ON tr.from_country_id = fc.country_code
LEFT JOIN country tc ON tr.to_country_id = tc.country_code
LEFT JOIN product p ON tr.product_id = p.id
LIMIT 10;
