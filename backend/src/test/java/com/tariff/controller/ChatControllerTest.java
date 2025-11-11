package com.tariff.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tariff.dto.request.ChatRequest;
import com.tariff.service.GeminiService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ChatController.class)
public class ChatControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GeminiService geminiService;

    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        objectMapper = new ObjectMapper();
    }

    @Test
    @WithMockUser
    public void testSendMessage_Success() throws Exception {
        ChatRequest request = new ChatRequest("Hello");
        String aiResponse = "Hi there!";

        when(geminiService.generateResponse("Hello")).thenReturn(aiResponse);

        mockMvc.perform(post("/api/chat/message")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value(aiResponse))
                .andExpect(jsonPath("$.error").value(false));
    }

    @Test
    @WithMockUser
    public void testSendMessage_EmptyMessage() throws Exception {
        ChatRequest request = new ChatRequest(" ");

        mockMvc.perform(post("/api/chat/message")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .with(csrf()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Please provide a message."))
                .andExpect(jsonPath("$.error").value(true));
    }

    @Test
    @WithMockUser
    public void testSendMessage_ServiceError() throws Exception {
        ChatRequest request = new ChatRequest("Hello");

        when(geminiService.generateResponse("Hello")).thenThrow(new RuntimeException("AI service is down"));

        mockMvc.perform(post("/api/chat/message")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("I'm sorry, I encountered an error. Please try again."))
                .andExpect(jsonPath("$.error").value(true));
    }

    @Test
    @WithMockUser
    public void testHealthCheck() throws Exception {
        mockMvc.perform(get("/api/chat/health")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().string("Chatbot is running"));
    }
}
