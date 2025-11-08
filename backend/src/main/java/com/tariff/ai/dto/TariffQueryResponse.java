package com.tariff.ai.dto;

import java.util.List;
import java.util.Map;

public record TariffQueryResponse(
    String response,
    String queryType,
    List<Map<String, Object>> data,
    String sqlQuery,
    boolean hasData
) {}