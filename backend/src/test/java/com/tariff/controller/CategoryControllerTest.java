package com.tariff.controller;

import com.tariff.entity.Category;
import com.tariff.service.CategoryService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class CategoryControllerTest {

    private MockMvc mockMvc;

    @Mock
    private CategoryService categoryService;

    @InjectMocks
    private CategoryController categoryController;

    private ObjectMapper objectMapper = new ObjectMapper();
    private Category category1;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(categoryController).build();

        category1 = new Category();
        category1.setId(1L);
        category1.setName("Electronics");
    }

    @Test
    void testGetAllIndustries() throws Exception {
        List<Category> categories = Arrays.asList(category1);
        when(categoryService.listCategory()).thenReturn(categories);

        mockMvc.perform(get("/api/industries"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Electronics"));

        verify(categoryService).listCategory();
    }

    @Test
    void testGetCategoryById() throws Exception {
        when(categoryService.getCategory(1L)).thenReturn(category1);

        mockMvc.perform(get("/api/industries/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Electronics"));

        verify(categoryService).getCategory(1L);
    }

    @Test
    void testCreateCategory() throws Exception {
        when(categoryService.addCategory(any(Category.class))).thenReturn(category1);

        mockMvc.perform(post("/api/industries")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(category1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Electronics"));

        verify(categoryService).addCategory(any(Category.class));
    }

    @Test
    void testUpdateCategory() throws Exception {
        Category updatedCategory = new Category();
        updatedCategory.setId(1L);
        updatedCategory.setName("New Electronics");

        when(categoryService.updateCategory(eq(1L), any(Category.class))).thenReturn(updatedCategory);

        mockMvc.perform(put("/api/industries/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedCategory)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("New Electronics"));

        verify(categoryService).updateCategory(eq(1L), any(Category.class));
    }

    @Test
    void testDeleteCategory() throws Exception {
        doNothing().when(categoryService).deleteCategory(1L);

        mockMvc.perform(delete("/api/industries/1"))
                .andExpect(status().isOk());

        verify(categoryService).deleteCategory(1L);
    }
}