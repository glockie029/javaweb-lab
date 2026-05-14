package com.example.lab02.controller;

import com.example.lab02.common.ApiResponse;
import com.example.lab02.dto.UrlFetchRequest;
import com.example.lab02.service.SsrfDemoService;
import java.util.Map;
import javax.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ssrf")
public class SsrfDemoController {

    private final SsrfDemoService ssrfDemoService;

    public SsrfDemoController(SsrfDemoService ssrfDemoService) {
        this.ssrfDemoService = ssrfDemoService;
    }

    @GetMapping("/context-info")
    public ApiResponse<Map<String, Object>> contextInfo() {
        return ApiResponse.success("ssrf demo context info", ssrfDemoService.getContextInfo());
    }

    @PostMapping("/vuln/fetch")
    public ApiResponse<Map<String, Object>> fetchVulnerable(@Valid @RequestBody UrlFetchRequest request) {
        return ApiResponse.success("vulnerable ssrf demo", ssrfDemoService.fetchVulnerable(request));
    }

    @PostMapping("/safe/fetch")
    public ApiResponse<Map<String, Object>> fetchSafe(@Valid @RequestBody UrlFetchRequest request) {
        return ApiResponse.success("safe ssrf demo", ssrfDemoService.fetchSafe(request));
    }
}
