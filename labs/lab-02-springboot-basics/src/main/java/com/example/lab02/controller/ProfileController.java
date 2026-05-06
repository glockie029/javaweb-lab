package com.example.lab02.controller;

import com.example.lab02.common.ApiResponse;
import com.example.lab02.dto.UpdateProfileRequest;
import com.example.lab02.service.ProfileService;
import java.util.Map;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/profile")
public class ProfileController {

    private final ProfileService profileService;

    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }

    @GetMapping("/me")
    public ApiResponse<Map<String, String>> currentProfile(HttpSession session) {
        String currentUser = (String) session.getAttribute("currentUser");
        return ApiResponse.success("profile query success", profileService.getProfile(currentUser));
    }

    @PostMapping("/update")
    public ApiResponse<Map<String, String>> updateProfile(@Valid @RequestBody UpdateProfileRequest request, HttpSession session) {
        String currentUser = (String) session.getAttribute("currentUser");
        return ApiResponse.success("profile update success", profileService.updateProfile(currentUser, request));
    }
}
