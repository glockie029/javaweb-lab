package com.example.lab02.controller;

import com.example.lab02.common.ApiResponse;
import java.security.Principal;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/security")
public class SecurityDemoController {

    @GetMapping("/public/ping")
    public ApiResponse<Map<String, String>> publicPing() {
        Map<String, String> data = new LinkedHashMap<String, String>();
        data.put("message", "public endpoint");
        return ApiResponse.success("security public demo", data);
    }

    @GetMapping("/me")
    public ApiResponse<Map<String, Object>> currentUser(Principal principal, Authentication authentication) {
        Map<String, Object> data = new LinkedHashMap<String, Object>();
        data.put("username", principal.getName());
        data.put("authorities", authentication.getDetails());
        return ApiResponse.success("security authenticated demo", data);
    }

    @GetMapping("/admin")
    public ApiResponse<Map<String, Object>> adminOnly(Principal principal, Authentication authentication) {
        Map<String, Object> data = new LinkedHashMap<String, Object>();
        data.put("username", principal.getName());
        data.put("authorities", authentication.getAuthorities());
        data.put("scope", "admin only");
        return ApiResponse.success("security role demo", data);
    }
}
