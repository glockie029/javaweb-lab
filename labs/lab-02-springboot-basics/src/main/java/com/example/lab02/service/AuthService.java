package com.example.lab02.service;

import com.example.lab02.mapper.AppUserMapper;
import com.example.lab02.model.AppUserRecord;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final AppUserMapper appUserMapper;

    public AuthService(AppUserMapper appUserMapper) {
        this.appUserMapper = appUserMapper;
    }

    public boolean isValidUser(String username, String password) {
        if (username == null || password == null) {
            return false;
        }

        AppUserRecord user = appUserMapper.findByUsername(username);
        return user != null && password.equals(user.getPassword());
    }
}
