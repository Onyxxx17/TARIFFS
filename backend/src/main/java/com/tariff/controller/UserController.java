package com.tariff.controller;

import com.tariff.entity.User;
import com.tariff.exception.UserAlreadyExistsException;
import com.tariff.service.UserService;

import io.swagger.v3.oas.annotations.Hidden;

import com.tariff.dto.SignupRequest;
import com.tariff.dto.LoginRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.Map;


@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:5173")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }
    
    // @GetMapping
    // public List<User> getAllUsers() {
    //     return userService.listUser();
    // }

    // --- Get user by ID ---
    // @GetMapping("/{id}")
    // public User getUserById(@PathVariable Long id) {
    //     return userService.getUser(id);
    // }

    // --- Get user by username ---
    // @GetMapping("/username/{username}")
    // public ResponseEntity<User> getUserByUsername(@PathVariable String username) {
    //     Optional<User> user = userService.getUserByUsername(username);
    //     return user.map(ResponseEntity::ok)
    //                .orElse(ResponseEntity.notFound().build());
    // }

//     // --- Create user ---
//     @Hidden
//     @PostMapping
//     public User createUser(@RequestBody User user) {
//         return userService.addUser(user);
//     }

//     // --- Update user ---
//     @Hidden
//     @PutMapping("/{id}")
//     public User updateUser(@PathVariable Long id, @RequestBody User user) {
//         return userService.updateUser(id, user);
//     }

    // --- Delete user ---
//     @Hidden
//     @DeleteMapping("/{id}")
//     public ResponseEntity<?> deleteUser(@PathVariable Long id) {
//         userService.deleteUser(id);
//         return ResponseEntity.ok().build();
//     }

    // --- Signup ---
    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody SignupRequest request) {

        Optional<User> existing = userService.findByEmail(request.email());
        if (existing.isPresent()) {
            throw new UserAlreadyExistsException(request.email());
        }

        // Map DTO to entity in correct order
        User user = new User(
            request.username(),   // username first
            request.email(),      // email
            request.password(),   // password
            request.role()        // role
        );

        userService.addUser(user);  // save to database
        return ResponseEntity.ok("User created");
    }

    // --- Login ---
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        Optional<User> userOpt = userService.findByEmail(request.email());

        if (userOpt.isEmpty() || !userOpt.get().getPassword().equals(request.password())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                                .body(Map.of("error", "Invalid credentials"));
        }

        // later, replace "dummy-jwt-token" with a real JWT
        return ResponseEntity.ok(Map.of(
            "token", "dummy-jwt-token",
            "message", "Login successful"
        ));
    }
}