package com.tariff.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class HelloController {

    @GetMapping("/hello")
    public String hello() {
        return "Hello World from Tariffs API!";
    }

    @GetMapping("/hello/{name}")
    public String helloWithName(@PathVariable String name) {
        return "Hello " + name + " from Tariffs API!";
    }

    @GetMapping("/status")
    public String status() {
        return "Tariffs Backend is running successfully!";
    }
}
