package com.example.lab02.controller;

import com.example.lab02.common.ApiResponse;
import com.example.lab02.dto.ExpressionRequest;
import com.example.lab02.service.ExpressionDemoService;
import java.util.Map;
import javax.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/expr")
public class ExpressionDemoController {

    private final ExpressionDemoService expressionDemoService;

    public ExpressionDemoController(ExpressionDemoService expressionDemoService) {
        this.expressionDemoService = expressionDemoService;
    }

    @GetMapping("/context-info")
    public ApiResponse<Map<String, Object>> contextInfo() {
        return ApiResponse.success("spel demo context info", expressionDemoService.getContextInfo());
    }

    @PostMapping("/vuln/spel")
    public ApiResponse<Map<String, Object>> evaluateVulnerable(@Valid @RequestBody ExpressionRequest request) {
        return ApiResponse.success("vulnerable spel expression demo", expressionDemoService.evaluateVulnerable(request));
    }

    @PostMapping("/safe/spel")
    public ApiResponse<Map<String, Object>> evaluateSafe(@Valid @RequestBody ExpressionRequest request) {
        return ApiResponse.success("safe spel expression demo", expressionDemoService.evaluateSafe(request));
    }
}
