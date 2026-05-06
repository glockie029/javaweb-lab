package com.example.lab02.service;

import org.springframework.stereotype.Service;

@Service
public class GreetingService {

    public String buildGreeting(String name) {
        String normalizedName = name == null || name.trim().isEmpty() ? "guest" : name.trim();
        return "hello, " + normalizedName;
    }
}
