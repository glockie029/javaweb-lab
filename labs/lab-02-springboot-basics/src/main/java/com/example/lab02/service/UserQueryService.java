package com.example.lab02.service;

import com.example.lab02.mapper.AppUserMapper;
import com.example.lab02.model.AppUserRecord;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public class UserQueryService {

    private final AppUserMapper appUserMapper;

    public UserQueryService(AppUserMapper appUserMapper) {
        this.appUserMapper = appUserMapper;
    }

    public Map<String, Object> getUserSummaryById(Long id) {
        AppUserRecord user = appUserMapper.findById(id);
        if (user == null) {
            return null;
        }

        Map<String, Object> data = new LinkedHashMap<String, Object>();
        data.put("id", user.getId());
        data.put("username", user.getUsername());
        data.put("nickname", user.getNickname());
        data.put("email", user.getEmail());
        return data;
    }

    public List<Map<String, Object>> listUserSummariesVulnerable(String sortField) {
        return toUserSummaryList(appUserMapper.listUsersOrderByVulnerable(sortField));
    }

    public List<Map<String, Object>> listUserSummariesSafe(String sortField) {
        return toUserSummaryList(appUserMapper.listUsersOrderBySafe(sortField));
    }

    public List<Map<String, Object>> searchUserSummariesVulnerable(String username, String role) {
        return toUserSummaryList(appUserMapper.searchUsersVulnerable(username, role));
    }

    public List<Map<String, Object>> searchUserSummariesSafe(String username, String role) {
        if (role == null || role.trim().isEmpty()) {
            return new ArrayList<Map<String, Object>>();
        }
        return toUserSummaryList(appUserMapper.searchUsersSafe(username, role));
    }

    public Map<String, Object> updateUserSelectiveVulnerable(String username, String nickname, String email, String role) {
        appUserMapper.updateUserSelectiveVulnerable(username, nickname, email, role);
        return getUserSummaryByUsername(username);
    }

    public Map<String, Object> updateUserSelectiveSafe(String username, String nickname, String email) {
        appUserMapper.updateUserSelectiveSafe(username, nickname, email);
        return getUserSummaryByUsername(username);
    }

    private List<Map<String, Object>> toUserSummaryList(List<AppUserRecord> users) {
        List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
        for (AppUserRecord user : users) {
            Map<String, Object> item = new LinkedHashMap<String, Object>();
            item.put("id", user.getId());
            item.put("username", user.getUsername());
            item.put("nickname", user.getNickname());
            item.put("email", user.getEmail());
            data.add(item);
        }
        return data;
    }

    private Map<String, Object> getUserSummaryByUsername(String username) {
        AppUserRecord user = appUserMapper.findByUsername(username);
        if (user == null) {
            return null;
        }

        Map<String, Object> data = new LinkedHashMap<String, Object>();
        data.put("id", user.getId());
        data.put("username", user.getUsername());
        data.put("nickname", user.getNickname());
        data.put("email", user.getEmail());
        data.put("role", user.getRole());
        return data;
    }
}
