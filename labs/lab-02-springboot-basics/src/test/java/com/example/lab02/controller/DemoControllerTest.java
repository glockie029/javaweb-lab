package com.example.lab02.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.InvalidClassException;
import javax.servlet.ServletException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class DemoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void fileStorageInfoShouldReturnSampleFile() throws Exception {
        mockMvc.perform(get("/api/file/storage-info"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.samplePreview").value("stage8 file demo sample"));
    }

    @Test
    void fileSafeReadShouldReturnContentInsideBaseDirectory() throws Exception {
        mockMvc.perform(get("/api/file/safe/read").param("path", "notes/welcome.txt"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.content").value("stage8 file demo sample"));
    }

    @Test
    void fileSafeReadShouldRejectPathTraversal() throws Exception {
        mockMvc.perform(get("/api/file/safe/read").param("path", "../outside.txt"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("invalid request argument"));
    }

    @Test
    void fileWriteEndpointsShouldPersistContent() throws Exception {
        mockMvc.perform(post("/api/file/vuln/write")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"path\":\"notes/vuln.txt\",\"content\":\"vuln-content\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.inputPath").value("notes/vuln.txt"));

        mockMvc.perform(get("/api/file/vuln/read").param("path", "notes/vuln.txt"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.content").value("vuln-content"));

        mockMvc.perform(post("/api/file/safe/write")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"path\":\"notes/safe.txt\",\"content\":\"safe-content\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.inputPath").value("notes/safe.txt"));
    }

    @Test
    void expressionEndpointsShouldCoverSafeAndUnsafeBranches() throws Exception {
        mockMvc.perform(get("/api/expr/context-info"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.dangerousEndpoint").value("/api/expr/vuln/spel"));

        mockMvc.perform(post("/api/expr/vuln/spel")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"expression\":\"'spring'.toUpperCase()\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.value").value("SPRING"));

        mockMvc.perform(post("/api/expr/safe/spel")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"expression\":\"1 + 2 * 3\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.value").value(7));

        mockMvc.perform(post("/api/expr/safe/spel")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"expression\":\"T(java.lang.Runtime).getRuntime()\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("invalid request argument"));
    }

    @Test
    void commandEndpointsShouldCoverStorageAndAllowListValidation() throws Exception {
        mockMvc.perform(get("/api/cmd/storage-info"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.vulnerableMarker").exists());

        mockMvc.perform(post("/api/cmd/safe/run")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"command\":\"pwd\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.mode").value("safe-command-demo"));

        mockMvc.perform(post("/api/cmd/safe/run")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"command\":\"cat /etc/passwd\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("invalid request argument"));
    }

    @Test
    void commandVulnerableRunShouldExecuteCommand() throws Exception {
        mockMvc.perform(post("/api/cmd/vuln/run")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"command\":\"pwd\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.mode").value("vulnerable-command-demo"))
                .andExpect(jsonPath("$.data.inputCommand").value("pwd"));
    }

    @Test
    void deserializationEndpointsShouldCoverSafeAndVulnerableModes() throws Exception {
        String gadgetPayload = mockMvc.perform(get("/api/deser/sample-gadget"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.objectType").value("com.example.lab02.model.DemoHarmlessGadget"))
                .andReturn()
                .getResponse()
                .getContentAsString();

        String payload = extractJsonField(gadgetPayload, "payload");

        mockMvc.perform(post("/api/deser/gadget-reset"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.activated").value(false));

        mockMvc.perform(post("/api/deser/vuln/java")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"payload\":\"" + payload + "\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.deserializedClass").value("com.example.lab02.model.DemoHarmlessGadget"));

        mockMvc.perform(get("/api/deser/gadget-state"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.activated").value(true));

        InvalidClassException exception = Assertions.assertThrows(
                InvalidClassException.class,
                () -> mockMvc.perform(post("/api/deser/safe/java")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"payload\":\"" + payload + "\"}"))
                        .andReturn());
        Assertions.assertTrue(exception.getMessage().contains("REJECTED"));

        String notePayload = mockMvc.perform(get("/api/deser/sample-note"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        mockMvc.perform(post("/api/deser/safe/java")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"payload\":\"" + extractJsonField(notePayload, "payload") + "\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.deserializedClass").value("com.example.lab02.model.DemoSerializableNote"));
    }

    @Test
    void exceptionEndpointShouldReturnServerError() throws Exception {
        ServletException exception = Assertions.assertThrows(
                ServletException.class,
                () -> mockMvc.perform(post("/api/demo/exception")).andReturn());
        Assertions.assertTrue(exception.getCause() instanceof IllegalStateException);
    }

    private String extractJsonField(String json, String fieldName) {
        String prefix = "\"" + fieldName + "\":\"";
        int start = json.indexOf(prefix) + prefix.length();
        int end = json.indexOf("\"", start);
        return json.substring(start, end);
    }
}
