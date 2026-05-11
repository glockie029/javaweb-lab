package com.example.lab02.controller;

import com.example.lab02.common.ApiResponse;
import com.example.lab02.service.AopDemoService;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/aop")
public class AopDemoController {

    private final AopDemoService aopDemoService;

    public AopDemoController(AopDemoService aopDemoService) {
        this.aopDemoService = aopDemoService;
    }

    @GetMapping("/public/ping")
    public ApiResponse<Map<String, String>> ping() {
        Map<String, String> data = new LinkedHashMap<String, String>();
        data.put("message", "aop public endpoint");
        return ApiResponse.success("aop public demo", data);
    }

    @GetMapping("/admin-report")
    public ApiResponse<Map<String, Object>> adminReport() {
        return ApiResponse.success("aop admin demo", aopDemoService.buildAdminReport());
    }
}
