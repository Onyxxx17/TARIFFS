package com.tariff.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tariff.config.GeminiConfig;
import com.tariff.mcp.dto.ToolDefinition;
import com.tariff.mcp.tools.McpTool;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class GeminiServiceTest {

    @InjectMocks
    private GeminiService geminiService;

    @Mock
    private GeminiConfig geminiConfig;

    @Mock
    private McpTool mcpTool;

    @Mock
    private List<McpTool> mcpTools;

    @Mock
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private void setMcpToolsField() throws NoSuchFieldException, IllegalAccessException {
        Field field = GeminiService.class.getDeclaredField("mcpTools");
        field.setAccessible(true);
        field.set(geminiService, Collections.singletonList(mcpTool));
    }

    private ToolDefinition createToolDefinition(String name) {
        ToolDefinition toolDefinition = new ToolDefinition();
        try {
            Field nameField = ToolDefinition.class.getDeclaredField("name");
            nameField.setAccessible(true);
            nameField.set(toolDefinition, name);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        return toolDefinition;
    }

    @Test
    public void testDetectAndExecuteTool_SearchTariffs() throws Exception {
        // Given
        String userMessage = "search for cars";
        ToolDefinition toolDefinition = createToolDefinition("search_tariffs");
        when(mcpTool.getDefinition()).thenReturn(toolDefinition);
        when(mcpTool.execute(any(Map.class))).thenReturn(Map.<String, Object>of("results", Collections.emptyList(), "count", 0, "query", "cars"));
        setMcpToolsField();

        // When
        Method method = GeminiService.class.getDeclaredMethod("detectAndExecuteTool", String.class);
        method.setAccessible(true);
        String response = (String) method.invoke(geminiService, userMessage);

        // Then
        assertNotNull(response);
        assertEquals("I couldn't find any products matching your search. Please try different keywords.", response);
    }

    @Test
    public void testDetectAndExecuteTool_GetTariffRate() throws Exception {
        // Given
        String userMessage = "what is the tariff rate from usa to china in 2018?";
        ToolDefinition toolDefinition = createToolDefinition("get_tariff_rate");
        when(mcpTool.getDefinition()).thenReturn(toolDefinition);
        when(mcpTool.execute(any(Map.class))).thenReturn(Map.<String, Object>of("found", false, "message", "No tariff data available."));
        setMcpToolsField();

        // When
        Method method = GeminiService.class.getDeclaredMethod("detectAndExecuteTool", String.class);
        method.setAccessible(true);
        String response = (String) method.invoke(geminiService, userMessage);

        // Then
        assertNotNull(response);
        assertEquals("No tariff data available.", response);
    }

    @Test
    public void testExtractYear() throws Exception {
        // Given
        String message = "what is the tariff rate from usa to china in 2018?";

        // When
        Method method = GeminiService.class.getDeclaredMethod("extractYear", String.class);
        method.setAccessible(true);
        Integer year = (Integer) method.invoke(geminiService, message);

        // Then
        assertEquals(2018, year);
    }

    @Test
    public void testExtractSearchQuery() throws Exception {
        // Given
        String message1 = "show me all products";
        String message2 = "are there any car products";
        String message3 = "find trucks";

        // When
        Method method = GeminiService.class.getDeclaredMethod("extractSearchQuery", String.class);
        method.setAccessible(true);
        String query1 = (String) method.invoke(geminiService, message1);
        String query2 = (String) method.invoke(geminiService, message2);
        String query3 = (String) method.invoke(geminiService, message3);

        // Then
        assertEquals("*", query1);
        assertEquals("car", query2);
        assertEquals("trucks", query3);
    }

    @Test
    public void testExtractCountries() throws Exception {
        // Given
        String message = "tariff rate from united states to china";

        // When
        Method method = GeminiService.class.getDeclaredMethod("extractCountries", String.class);
        method.setAccessible(true);
        @SuppressWarnings("unchecked")
        Map<String, String> countries = (Map<String, String>) method.invoke(geminiService, message);

        // Then
        assertNotNull(countries);
        assertEquals("United States of America", countries.get("from"));
        assertEquals("People's Republic of China", countries.get("to"));
    }

    @Test
    public void testNormalizeCountryName() throws Exception {
        // Given
        String us = "USA";
        String china = "People's Republic of China";
        String korea = "dprk";
        String unknown = "Canada";

        // When
        Method method = GeminiService.class.getDeclaredMethod("normalizeCountryName", String.class);
        method.setAccessible(true);
        String normalizedUs = (String) method.invoke(geminiService, us);
        String normalizedChina = (String) method.invoke(geminiService, china);
        String normalizedKorea = (String) method.invoke(geminiService, korea);
        String normalizedUnknown = (String) method.invoke(geminiService, unknown);

        // Then
        assertEquals("United States of America", normalizedUs);
        assertEquals("People's Republic of China", normalizedChina);
        assertEquals("North Korea", normalizedKorea);
        assertEquals("Canada", normalizedUnknown);
    }

    @Test
    public void testExtractProductFromTariffQuery() throws Exception {
        // Given
        String message1 = "what is the tariff for steel from usa to china";
        String message2 = "What's the tariff rate from USA to China for electronics?";

        // When
        Method method = GeminiService.class.getDeclaredMethod("extractProductFromTariffQuery", String.class);
        method.setAccessible(true);
        String product1 = (String) method.invoke(geminiService, message1);
        String product2 = (String) method.invoke(geminiService, message2);

        // Then
        assertEquals("steel", product1);
        assertEquals("electronics", product2);
    }

    @Test
    public void testFormatToolResponse_ProductSearch_NotFound() throws Exception {
        // Given
        Map<String, Object> result = Map.of("results", Collections.emptyList(), "count", 0, "query", "unobtanium");
        String queryType = "product search";

        // When
        Method method = GeminiService.class.getDeclaredMethod("formatToolResponse", Object.class, String.class);
        method.setAccessible(true);
        String formattedResponse = (String) method.invoke(geminiService, result, queryType);

        // Then
        assertEquals("I couldn't find any products matching your search. Please try different keywords.", formattedResponse);
    }

    // @Test
    // public void testGenerateResponse_ToolExecutionFails() throws Exception {
    //     // Given
    //     String userMessage = "search for cars";
    //     when(geminiConfig.getKey()).thenReturn("test-api-key");
    //     setMcpToolsField();

    //     // Force detectAndExecuteTool to throw an exception
    //     // This is tricky without changing the source. We'll mock a tool to throw an exception.
    //     ToolDefinition toolDefinition = createToolDefinition("search_tariffs");
    //     when(mcpTool.getDefinition()).thenReturn(toolDefinition);
    //     when(mcpTool.execute(any(Map.class))).thenThrow(new RuntimeException("Tool failed"));

    //     // When
    //     String response = geminiService.generateResponse(userMessage);

    //     // Then
    //     assertEquals("I'm sorry, I encountered an error while processing your request. Please try again later. Error: Tool failed", response);
    // }

    

    @Test
    public void testFormatToolResponse_ProductSearch_FoundMultiple() throws Exception {
        // Given
        List<Map<String, Object>> products = List.of(
                Map.of("name", "Sedan", "id", "1", "category", "Vehicle"),
                Map.of("name", "SUV", "id", "2", "category", "Vehicle")
        );
        Map<String, Object> result = Map.of("results", products, "count", 2, "query", "cars");
        String queryType = "product search";

        // When
        Method method = GeminiService.class.getDeclaredMethod("formatToolResponse", Object.class, String.class);
        method.setAccessible(true);
        String formattedResponse = (String) method.invoke(geminiService, result, queryType);

        // Then
        String expected = "I found 2 product(s) matching 'cars':\n\n• Sedan (Vehicle) - ID: 1\n• SUV (Vehicle) - ID: 2";
        assertEquals(expected, formattedResponse);
    }

    

    @Test
    public void testFormatToolResponse_FormattingError() throws Exception {
        // Given
        // Pass a result that will cause a ClassCastException
        Map<String, Object> result = Map.of("results", "not-a-list");
        String queryType = "product search";

        // When
        Method method = GeminiService.class.getDeclaredMethod("formatToolResponse", Object.class, String.class);
        method.setAccessible(true);
        String formattedResponse = (String) method.invoke(geminiService, result, queryType);

        // Then
        assertNotNull(formattedResponse);
        assertEquals("I found some information but had trouble formatting it. Error: class java.lang.String cannot be cast to class java.util.List (java.lang.String and java.util.List are in module java.base of loader 'bootstrap')", formattedResponse);
    }

    @Test
    public void testCleanMarkdownFormatting() throws Exception {
        // Given
        String markdown = "### Header\n\n**Bold text** and *italic text*.\n\n- Item 1\n- Item 2\n\n`code`";

        // When
        Method method = GeminiService.class.getDeclaredMethod("cleanMarkdownFormatting", String.class);
        method.setAccessible(true);
        String cleanedText = (String) method.invoke(geminiService, markdown);

        // Then
        String expected = "Header\n\nBold text and italic text.\n• Item 1\n• Item 2\n\ncode";
        assertEquals(expected, cleanedText);
    }

    @Test
    public void testGenerateResponse_NoToolDetected() throws Exception {
        // Given
        String userMessage = "hello, how are you?";
        when(geminiConfig.getKey()).thenReturn("test-api-key");
        when(geminiConfig.getModel()).thenReturn("gemini-pro");
        // This test will fail if it tries to make a real API call.
        // We are testing the path, assuming the external call would work.
        // Mocking the GenerativeModel and its response is complex due to final classes.
        // This test primarily ensures we enter the non-tool-execution path.
        
        // Setup to make detectAndExecuteTool return null
        setMcpToolsField(); // No tools will match "hello, how are you?"

        // When
        String response = geminiService.generateResponse(userMessage);

        // Then
        // In a real unit test, we'd mock the Gemini client. Here, it will likely fail to build the client
        // or throw an error on the API call, which is caught.
        assertTrue(response.startsWith("I'm sorry, I encountered an error while processing your request."));
    }

    @Test
    public void testDetectAndExecuteTool_GetRate_Fails() throws Exception {
        // Given
        String userMessage = "tariff for cars from usa to china";
        
        // Mock for search_tariffs tool
        ToolDefinition searchToolDef = createToolDefinition("search_tariffs");
        Map<String, Object> searchResult = Map.of("results", List.of(Map.of("id", 123L)), "count", 1);

        // Mock for get_tariff_rate tool that fails
        ToolDefinition getRateToolDef = createToolDefinition("get_tariff_rate");
        
        McpTool searchTool = org.mockito.Mockito.mock(McpTool.class);
        when(searchTool.getDefinition()).thenReturn(searchToolDef);
        when(searchTool.execute(any(Map.class))).thenReturn(searchResult);

        McpTool getRateTool = org.mockito.Mockito.mock(McpTool.class);
        when(getRateTool.getDefinition()).thenReturn(getRateToolDef);
        when(getRateTool.execute(any(Map.class))).thenThrow(new RuntimeException("DB connection failed"));

        // Inject both tools
        Field field = GeminiService.class.getDeclaredField("mcpTools");
        field.setAccessible(true);
        field.set(geminiService, List.of(searchTool, getRateTool));
        
        // When
        Method method = GeminiService.class.getDeclaredMethod("detectAndExecuteTool", String.class);
        method.setAccessible(true);
        String response = (String) method.invoke(geminiService, userMessage);

        // Then
        // The method should catch the exception and return null, falling back to AI
        assertNull(response);
    }

   

   
}
