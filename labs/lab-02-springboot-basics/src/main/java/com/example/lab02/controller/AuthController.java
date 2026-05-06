package com.example.lab02.controller;

import com.example.lab02.common.ApiResponse;
import com.example.lab02.dto.LoginRequest;
import com.example.lab02.service.AuthService;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ApiResponse<Map<String, String>> login(@Valid @RequestBody LoginRequest request, HttpServletRequest httpRequest) {
        if (!authService.isValidUser(request.getUsername(), request.getPassword())) {
            return ApiResponse.failure("invalid username or password");
        }

        HttpSession session = httpRequest.getSession(true);
        session.setAttribute("currentUser", request.getUsername());

        Map<String, String> data = new LinkedHashMap<String, String>();
        data.put("currentUser", request.getUsername());
        return ApiResponse.success("login success", data);
    }

    @PostMapping("/logout")
    public ApiResponse<Void> logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        return ApiResponse.success("logout success", null);
    }
}
