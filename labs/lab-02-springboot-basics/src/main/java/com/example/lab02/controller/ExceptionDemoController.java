package com.example.lab02.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/demo")
public class ExceptionDemoController {

    @PostMapping("/exception")
    public void throwException() {
        throw new IllegalStateException("demo exception from controller");
    }
}
