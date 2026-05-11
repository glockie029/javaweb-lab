package com.example.lab02;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Lab02SpringbootBasicsApplication {

    public static void main(String[] args) {
        System.setProperty("org.apache.commons.collections.enableUnsafeSerialization", "true");
        SpringApplication.run(Lab02SpringbootBasicsApplication.class, args);
    }
}
