package com.tariff.controller;

import com.tariff.entity.User;
import com.tariff.service.UserService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // GET all users - /api/users
    @GetMapping
    public List<User> getAllUsers() {
        return userService.listUser();
    }
}
