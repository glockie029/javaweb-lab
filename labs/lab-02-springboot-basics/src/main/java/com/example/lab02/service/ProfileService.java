package com.example.lab02.service;

import com.example.lab02.dto.UpdateProfileRequest;
import com.example.lab02.mapper.AppUserMapper;
import com.example.lab02.model.AppUserRecord;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public class ProfileService {

    private final AppUserMapper appUserMapper;

    public ProfileService(AppUserMapper appUserMapper) {
        this.appUserMapper = appUserMapper;
    }

    public Map<String, String> getProfile(String username) {
        AppUserRecord user = appUserMapper.findByUsername(username);
        if (user == null) {
            return null;
        }
        return toProfileMap(user);
    }

    public Map<String, String> updateProfile(String username, UpdateProfileRequest request) {
        appUserMapper.updateProfileByUsername(username, request.getNickname(), request.getEmail());
        return getProfile(username);
    }

    private Map<String, String> toProfileMap(AppUserRecord user) {
        Map<String, String> profile = new LinkedHashMap<String, String>();
        profile.put("username", user.getUsername());
        profile.put("nickname", user.getNickname());
        profile.put("email", user.getEmail());
        profile.put("role", user.getRole());
        profile.put("password",user.getPassword());
        return profile;
    }
}
