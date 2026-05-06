package com.example.lab02.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class SecurityDemoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void publicPingShouldReturnOk() throws Exception {
        mockMvc.perform(get("/api/security/public/ping"))
                .andExpect(status().isOk());
    }

    @Test
    void currentUserShouldRequireAuthentication() throws Exception {
        mockMvc.perform(get("/api/security/me"))
                .andExpect(status().isUnauthorized());
    }
}
