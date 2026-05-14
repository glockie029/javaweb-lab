package com.example.lab02.controller;

import com.example.lab02.common.ApiResponse;
import com.example.lab02.dto.SerializedPayloadRequest;
import com.example.lab02.service.DeserializationDemoService;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/deser")
public class DeserializationDemoController {

    private final DeserializationDemoService deserializationDemoService;

    public DeserializationDemoController(DeserializationDemoService deserializationDemoService) {
        this.deserializationDemoService = deserializationDemoService;
    }

    @GetMapping("/sample-note")
    public ApiResponse<Map<String, Object>> sampleNotePayload() throws IOException {
        return ApiResponse.success("java serialization sample payload",
                deserializationDemoService.buildSamplePayload());
    }

    @GetMapping("/sample-gadget")
    public ApiResponse<Map<String, Object>> sampleHarmlessGadgetPayload() throws IOException {
        return ApiResponse.success("harmless gadget payload", deserializationDemoService.buildHarmlessGadgetPayload());
    }

    @GetMapping("/gadget-state")
    public ApiResponse<Map<String, Object>> gadgetState() {
        return ApiResponse.success("harmless gadget state", deserializationDemoService.getHarmlessGadgetState());
    }

    @PostMapping("/gadget-reset")
    public ApiResponse<Map<String, Object>> resetGadgetState() {
        return ApiResponse.success("harmless gadget state reset",
                deserializationDemoService.resetHarmlessGadgetState());
    }

    @PostMapping("/vuln/java")
    public ApiResponse<Map<String, Object>> deserializeVulnerable(@Valid @RequestBody SerializedPayloadRequest request)
            throws IOException, ClassNotFoundException {
        return ApiResponse.success("vulnerable java deserialization demo",
                deserializationDemoService.deserializeVulnerable(request.getPayload()));
    }

    @PostMapping("/safe/java")
    public ApiResponse<Map<String, Object>> deserializeSafe(@Valid @RequestBody SerializedPayloadRequest request)
            throws IOException, ClassNotFoundException {
        return ApiResponse.success("safe java deserialization demo",
                deserializationDemoService.deserializeWithAllowList(request.getPayload()));
    }
}
