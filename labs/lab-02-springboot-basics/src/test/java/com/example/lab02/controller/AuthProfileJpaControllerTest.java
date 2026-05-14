package com.example.lab02.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import javax.servlet.http.Cookie;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AuthProfileJpaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void authLoginShouldCreateSessionForValidUser() throws Exception {
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"alice\",\"password\":\"alice123\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.currentUser").value("alice"));
    }

    @Test
    void authLoginShouldRejectInvalidPassword() throws Exception {
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"alice\",\"password\":\"bad\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("invalid username or password"));
    }

    @Test
    void authLogoutShouldSucceedWithoutSession() throws Exception {
        mockMvc.perform(post("/api/auth/logout"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("logout success"));
    }

    @Test
    void profileEndpointsShouldRequireLogin() throws Exception {
        mockMvc.perform(get("/api/profile/me"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("login required"));
    }

    @Test
    void profileFlowShouldReadAndUpdateCurrentUser() throws Exception {
        MockHttpSession session = loginAndGetSession("alice", "alice123");

        mockMvc.perform(get("/api/profile/me").session(session))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.username").value("alice"))
                .andExpect(jsonPath("$.data.password").value("alice123"));

        mockMvc.perform(post("/api/profile/update")
                        .session(session)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nickname\":\"alice-profile\",\"email\":\"alice-profile@example.com\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.nickname").value("alice-profile"))
                .andExpect(jsonPath("$.data.email").value("alice-profile@example.com"));
    }

    @Test
    void profileUpdateShouldRejectInvalidEmail() throws Exception {
        MockHttpSession session = loginAndGetSession("alice", "alice123");

        mockMvc.perform(post("/api/profile/update")
                        .session(session)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nickname\":\"alice\",\"email\":\"bad-email\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void jpaUserByIdVulnerableShouldReturnFailureWhenMissing() throws Exception {
        mockMvc.perform(get("/api/jpa/users/99/vuln").session(loginAndGetSession("alice", "alice123")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("user not found"));
    }

    @Test
    void jpaUserByIdSafeShouldEnforceCurrentUserScope() throws Exception {
        MockHttpSession session = loginAndGetSession("alice", "alice123");

        mockMvc.perform(get("/api/jpa/users/3/safe").session(session))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("user not found or no permission"));
    }

    @Test
    void jpaProfileFlowShouldReturnAndUpdateCurrentUser() throws Exception {
        MockHttpSession session = loginAndGetSession("bob", "bob123");

        mockMvc.perform(get("/api/jpa/profile/me").session(session))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.username").value("bob"));

        mockMvc.perform(post("/api/jpa/profile/update")
                        .session(session)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nickname\":\"bob-jpa\",\"email\":\"bob-jpa@example.com\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.nickname").value("bob-jpa"))
                .andExpect(jsonPath("$.data.email").value("bob-jpa@example.com"));
    }

    private MockHttpSession loginAndGetSession(String username, String password) throws Exception {
        MvcResult result = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"" + username + "\",\"password\":\"" + password + "\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andReturn();
        return (MockHttpSession) result.getRequest().getSession(false);
    }
}
