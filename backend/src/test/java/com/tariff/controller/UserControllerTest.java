package com.tariff.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tariff.entity.User;
import com.tariff.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class UserControllerTest {

    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private ObjectMapper objectMapper = new ObjectMapper();

    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();

        user = new User();
        user.setId(1L);
        user.setUsername("john");
        user.setPassword("secret");
        user.setRole("ADMIN");
    }

    @Test
    void testGetAllUsers() throws Exception {
        when(userService.listUser()).thenReturn(List.of(user));

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].username").value("john"))
                .andExpect(jsonPath("$[0].role").value("ADMIN"));

        verify(userService).listUser();
    }

    @Test
    void testGetUserById() throws Exception {
        when(userService.getUser(1L)).thenReturn(user);

        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.username").value("john"));

        verify(userService).getUser(1L);
    }

    @Test
    void testGetUserByUsernameFound() throws Exception {
        when(userService.getUserByUsername("john")).thenReturn(Optional.of(user));

        mockMvc.perform(get("/api/users/username/john"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("john"));

        verify(userService).getUserByUsername("john");
    }

    @Test
    void testGetUserByUsernameNotFound() throws Exception {
        when(userService.getUserByUsername("jane")).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/users/username/jane"))
                .andExpect(status().isNotFound());

        verify(userService).getUserByUsername("jane");
    }

    @Test
    void testCreateUser() throws Exception {
        when(userService.addUser(any(User.class))).thenReturn(user);

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("john"));

        verify(userService).addUser(any(User.class));
    }

    @Test
    void testUpdateUser() throws Exception {
        when(userService.updateUser(eq(1L), any(User.class))).thenReturn(user);

        mockMvc.perform(put("/api/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("john"));

        verify(userService).updateUser(eq(1L), any(User.class));
    }

    @Test
    void testDeleteUser() throws Exception {
        doNothing().when(userService).deleteUser(1L);

        mockMvc.perform(delete("/api/users/1"))
                .andExpect(status().isOk());

        verify(userService).deleteUser(1L);
    }
}