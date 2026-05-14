package com.example.lab02.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class HelloControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void helloShouldUseGuestWhenNameMissing() throws Exception {
        mockMvc.perform(get("/api/hello"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.message").value("hello, guest"));
    }

    @Test
    void helloShouldTrimName() throws Exception {
        mockMvc.perform(get("/api/hello").param("name", "  alice  "))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.message").value("hello, alice"));
    }

    @Test
    void userByIdShouldReturnFailureWhenMissing() throws Exception {
        mockMvc.perform(get("/api/users/99"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("user not found"));
    }

    @Test
    void userByIdShouldReturnSummaryWhenPresent() throws Exception {
        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.username").value("admin"));
    }

    @Test
    void safeSearchShouldReturnEmptyWhenRoleMissing() throws Exception {
        mockMvc.perform(get("/api/users/safe-search").param("username", "alice"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.length()").value(0));
    }

    @Test
    void safeSearchShouldReturnMatchedUserWhenRolePresent() throws Exception {
        mockMvc.perform(get("/api/users/safe-search")
                        .param("username", "alice")
                        .param("role", "USER"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.length()").value(1))
                .andExpect(jsonPath("$.data[0].username").value("alice"));
    }

    @Test
    void safeListShouldFallbackToIdSort() throws Exception {
        mockMvc.perform(get("/api/users/safe-list").param("sortField", "bad-field"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].id").value(1))
                .andExpect(jsonPath("$.data[1].id").value(2));
    }

    @Test
    void updateVulnerableShouldModifyRoleAndNickname() throws Exception {
        mockMvc.perform(get("/api/users/vuln-update")
                        .param("username", "alice")
                        .param("nickname", "alice-new")
                        .param("email", "alice-new@example.com")
                        .param("role", "ADMIN"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.username").value("alice"))
                .andExpect(jsonPath("$.data.nickname").value("alice-new"))
                .andExpect(jsonPath("$.data.role").value("ADMIN"));
    }

    @Test
    void updateSafeShouldModifyOnlyNicknameAndEmail() throws Exception {
        mockMvc.perform(get("/api/users/safe-upda te")
                        .param("username", "bob")
                        .param("nickname", "bob-new")
                        .param("email", "bob-new@example.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.username").value("bob"))
                .andExpect(jsonPath("$.data.nickname").value("bob-new"))
                .andExpect(jsonPath("$.data.email").value("bob-new@example.com"))
                .andExpect(jsonPath("$.data.role").value("USER"));
    }
}
