package com.example.lab02.controller;

import com.example.lab02.common.ApiResponse;
import com.example.lab02.dto.UpdateProfileRequest;
import com.example.lab02.service.JpaUserService;
import java.util.Map;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/jpa")
public class JpaUserController {

    private final JpaUserService jpaUserService;

    public JpaUserController(JpaUserService jpaUserService) {
        this.jpaUserService = jpaUserService;
    }

    @GetMapping("/users/{id}/vuln")
    public ApiResponse<Map<String, Object>> userByIdVulnerable(@PathVariable("id") Long id) {
        Map<String, Object> data = jpaUserService.getUserByIdVulnerable(id);
        if (data == null) {
            return ApiResponse.failure("user not found");
        }
        return ApiResponse.success("jpa findById vulnerable demo", data);
    }

    @GetMapping("/users/{id}/safe")
    public ApiResponse<Map<String, Object>> userByIdSafe(@PathVariable("id") Long id, HttpSession session) {
        String currentUser = (String) session.getAttribute("currentUser");
        Map<String, Object> data = jpaUserService.getUserByIdSafe(id, currentUser);
        if (data == null) {
            return ApiResponse.failure("user not found or no permission");
        }
        return ApiResponse.success("jpa findByIdAndUsername safe demo", data);
    }

    @GetMapping("/profile/me")
    public ApiResponse<Map<String, Object>> currentProfile(HttpSession session) {
        String currentUser = (String) session.getAttribute("currentUser");
        return ApiResponse.success("jpa profile query success", jpaUserService.getCurrentProfile(currentUser));
    }

    @PostMapping("/profile/update")
    public ApiResponse<Map<String, Object>> updateCurrentProfile(@Valid @RequestBody UpdateProfileRequest request, HttpSession session) {
        String currentUser = (String) session.getAttribute("currentUser");
        return ApiResponse.success("jpa profile update success", jpaUserService.updateCurrentProfile(currentUser, request));
    }
}
