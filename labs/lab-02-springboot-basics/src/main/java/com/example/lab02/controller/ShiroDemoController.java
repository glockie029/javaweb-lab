package com.example.lab02.controller;

import com.example.lab02.common.ApiResponse;
import com.example.lab02.dto.LoginRequest;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.validation.Valid;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/shiro")
public class ShiroDemoController {

    @GetMapping("/public/ping")
    public ApiResponse<Map<String, String>> publicPing() {
        Map<String, String> data = new LinkedHashMap<String, String>();
        data.put("message", "shiro public endpoint");
        return ApiResponse.success("shiro public demo", data);
    }

    @PostMapping("/login")
    public ApiResponse<Map<String, Object>> login(@Valid @RequestBody LoginRequest request) {
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(request.getUsername(), request.getPassword());
        subject.login(token);

        Session session = subject.getSession(false);
        Map<String, Object> data = new LinkedHashMap<String, Object>();
        data.put("username", subject.getPrincipal());
        data.put("authenticated", subject.isAuthenticated());
        data.put("sessionId", session == null ? null : String.valueOf(session.getId()));
        return ApiResponse.success("shiro login success", data);
    }

    @PostMapping("/logout")
    public ApiResponse<Void> logout() {
        SecurityUtils.getSubject().logout();
        return ApiResponse.success("shiro logout success", null);
    }

    @GetMapping("/me")
    @RequiresAuthentication
    public ApiResponse<Map<String, Object>> currentUser() {
        Subject subject = SecurityUtils.getSubject();
        Map<String, Object> data = new LinkedHashMap<String, Object>();
        data.put("username", subject.getPrincipal());
        data.put("authenticated", subject.isAuthenticated());
        data.put("roles", subject.hasRole("ADMIN") ? "ADMIN-or-USER-context" : "USER-context");
        return ApiResponse.success("shiro authenticated demo", data);
    }

    @GetMapping("/admin")
    @RequiresAuthentication
    @RequiresRoles("ADMIN")
    public ApiResponse<Map<String, Object>> adminOnly() {
        Subject subject = SecurityUtils.getSubject();
        Map<String, Object> data = new LinkedHashMap<String, Object>();
        data.put("username", subject.getPrincipal());
        data.put("scope", "admin only");
        return ApiResponse.success("shiro role demo", data);
    }
}
