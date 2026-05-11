package com.example.lab02.controller;

import com.example.lab02.common.ApiResponse;
import com.example.lab02.dto.LoginRequest;
import com.example.lab02.security.JwtTokenService;
import java.security.Principal;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.validation.Valid;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/jwt")
public class JwtDemoController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenService jwtTokenService;

    public JwtDemoController(AuthenticationManager authenticationManager, JwtTokenService jwtTokenService) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenService = jwtTokenService;
    }

    @GetMapping("/public/ping")
    public ApiResponse<Map<String, String>> publicPing() {
        Map<String, String> data = new LinkedHashMap<String, String>();
        data.put("message", "jwt public endpoint");
        return ApiResponse.success("jwt public demo", data);
    }

    @PostMapping("/login")
    public ApiResponse<Map<String, Object>> login(@Valid @RequestBody LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String token = jwtTokenService.generateToken(userDetails);

        Map<String, Object> data = new LinkedHashMap<String, Object>();
        data.put("tokenType", "Bearer");
        data.put("token", token);
        data.put("username", userDetails.getUsername());
        data.put("authorities", userDetails.getAuthorities());
        return ApiResponse.success("jwt login success", data);
    }

    @GetMapping("/me")
    public ApiResponse<Map<String, Object>> currentUser(Principal principal, Authentication authentication) {
        Map<String, Object> data = new LinkedHashMap<String, Object>();
        data.put("username", principal.getName());
        data.put("authorities", authentication.getAuthorities());
        data.put("authenticationType", "jwt");
        return ApiResponse.success("jwt authenticated demo", data);
    }

    @GetMapping("/admin")
    public ApiResponse<Map<String, Object>> adminOnly(Principal principal, Authentication authentication) {
        Map<String, Object> data = new LinkedHashMap<String, Object>();
        data.put("username", principal.getName());
        data.put("authorities", authentication.getAuthorities());
        data.put("scope", "jwt admin only");
        return ApiResponse.success("jwt role demo", data);
    }
}
