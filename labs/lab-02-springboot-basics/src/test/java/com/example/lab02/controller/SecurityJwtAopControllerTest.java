package com.example.lab02.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class SecurityJwtAopControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void securityAdminShouldRejectAnonymousUser() throws Exception {
        mockMvc.perform(get("/api/security/admin"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("security authentication required"));
    }

    @Test
    void jwtPublicPingShouldBeAccessible() throws Exception {
        mockMvc.perform(get("/api/jwt/public/ping"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.message").value("jwt public endpoint"));
    }

    @Test
    void jwtMeShouldRejectAnonymousUser() throws Exception {
        mockMvc.perform(get("/api/jwt/me"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("security authentication required"));
    }

    @Test
    void jwtLoginShouldReturnBearerToken() throws Exception {
        mockMvc.perform(post("/api/jwt/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"alice\",\"password\":\"alice123\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.tokenType").value("Bearer"))
                .andExpect(jsonPath("$.data.username").value("alice"));
    }

    @Test
    void jwtAuthenticatedEndpointShouldAcceptValidToken() throws Exception {
        String token = loginAndGetJwt("alice", "alice123");

        mockMvc.perform(get("/api/jwt/me").header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.username").value("alice"))
                .andExpect(jsonPath("$.message").value("jwt authenticated demo"));
    }

    @Test
    void jwtAdminEndpointShouldRejectUserRole() throws Exception {
        String token = loginAndGetJwt("alice", "alice123");

        mockMvc.perform(get("/api/jwt/admin").header("Authorization", "Bearer " + token))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.message").value("security role not allowed"));
    }

    @Test
    void jwtAdminEndpointShouldAcceptAdminRole() throws Exception {
        String token = loginAndGetJwt("admin", "admin123");

        mockMvc.perform(get("/api/jwt/admin").header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.username").value("admin"))
                .andExpect(jsonPath("$.data.scope").value("jwt admin only"));
    }

    @Test
    void aopPublicPingShouldBeAccessible() throws Exception {
        mockMvc.perform(get("/api/aop/public/ping"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.message").value("aop public endpoint"));
    }

    @Test
    void aopAdminShouldRejectWithoutHeader() throws Exception {
        mockMvc.perform(get("/api/aop/admin-report"))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.message").value("aop access denied"));
    }

    @Test
    void aopAdminShouldAcceptAdminHeader() throws Exception {
        mockMvc.perform(get("/api/aop/admin-report").header("X-Demo-Role", "ADMIN"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.reportName").value("stage7-aop-demo"))
                .andExpect(jsonPath("$.data.scope").value("ADMIN_ONLY"));
    }

    private String loginAndGetJwt(String username, String password) throws Exception {
        MvcResult result = mockMvc.perform(post("/api/jwt/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"" + username + "\",\"password\":\"" + password + "\"}"))
                .andExpect(status().isOk())
                .andReturn();
        String responseBody = result.getResponse().getContentAsString();
        int tokenFieldIndex = responseBody.indexOf("\"token\":\"");
        int tokenStart = tokenFieldIndex + "\"token\":\"".length();
        int tokenEnd = responseBody.indexOf("\"", tokenStart);
        return responseBody.substring(tokenStart, tokenEnd);
    }
}
