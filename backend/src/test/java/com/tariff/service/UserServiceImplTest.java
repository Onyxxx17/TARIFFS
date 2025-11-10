package com.tariff.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.tariff.entity.User;
import com.tariff.exception.UserNotFoundException;
import com.tariff.repository.UserRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    private User user1;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        user1 = new User();
        user1.setId(1L);
        user1.setUsername("john");
        user1.setPassword("pass123");
        user1.setRole("ADMIN");
    }

    @Test
    void testListUser() {
        when(userRepository.findAll()).thenReturn(Arrays.asList(user1));

        List<User> result = userService.listUser();
        assertEquals(1, result.size());
        verify(userRepository).findAll();
    }

    @Test
    void testGetUserFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user1));

        User result = userService.getUser(1L);
        assertEquals("john", result.getUsername());
    }

    @Test
    void testGetUserNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.getUser(1L));
    }

    @Test
    void testGetUserByUsernameFound() {
        when(userRepository.findByUsername("john")).thenReturn(Optional.of(user1));

        Optional<User> result = userService.getUserByUsername("john");
        assertTrue(result.isPresent());
        assertEquals("john", result.get().getUsername());
    }

    @Test
    void testGetUserByUsernameNotFound() {
        when(userRepository.findByUsername("alice")).thenReturn(Optional.empty());

        Optional<User> result = userService.getUserByUsername("alice");
        assertFalse(result.isPresent());
    }

    @Test
    void testAddUser() {
        when(userRepository.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));

        User result = userService.addUser(user1);
        assertEquals("john", result.getUsername());
        verify(userRepository).save(user1);
    }

    @Test
    void testUpdateUserFound() {
        User updatedUser = new User();
        updatedUser.setUsername("johnny");
        updatedUser.setPassword("newpass");
        updatedUser.setRole("USER");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user1));
        when(userRepository.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));

        User result = userService.updateUser(1L, updatedUser);
        assertEquals("johnny", result.getUsername());
        assertEquals("newpass", result.getPassword());
        assertEquals("USER", result.getRole());
    }

    @Test
    void testUpdateUserNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.updateUser(1L, user1));
    }

    @Test
    void testDeleteUserFound() {
        when(userRepository.existsById(1L)).thenReturn(true);

        userService.deleteUser(1L);
        verify(userRepository).deleteById(1L);
    }

    @Test
    void testDeleteUserNotFound() {
        when(userRepository.existsById(1L)).thenReturn(false);

        assertThrows(UserNotFoundException.class, () -> userService.deleteUser(1L));
    }
}