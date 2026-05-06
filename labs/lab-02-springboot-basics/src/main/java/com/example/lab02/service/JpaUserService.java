package com.example.lab02.service;

import com.example.lab02.dto.UpdateProfileRequest;
import com.example.lab02.entity.JpaUserEntity;
import com.example.lab02.repository.JpaUserRepository;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public class JpaUserService {

    private final JpaUserRepository jpaUserRepository;

    public JpaUserService(JpaUserRepository jpaUserRepository) {
        this.jpaUserRepository = jpaUserRepository;
    }

    public Map<String, Object> getUserByIdVulnerable(Long id) {
        return jpaUserRepository.findById(id)
                .map(this::toSummaryMap)
                .orElse(null);
    }

    public Map<String, Object> getUserByIdSafe(Long id, String currentUser) {
        return jpaUserRepository.findByIdAndUsername(id, currentUser)
                .map(this::toSummaryMap)
                .orElse(null);
    }

    public Map<String, Object> getCurrentProfile(String currentUser) {
        return jpaUserRepository.findByUsername(currentUser)
                .map(this::toSummaryMap)
                .orElse(null);
    }

    public Map<String, Object> updateCurrentProfile(String currentUser, UpdateProfileRequest request) {
        JpaUserEntity user = jpaUserRepository.findByUsername(currentUser).orElse(null);
        if (user == null) {
            return null;
        }

        user.setNickname(request.getNickname());
        user.setEmail(request.getEmail());
        return toSummaryMap(jpaUserRepository.save(user));
    }

    private Map<String, Object> toSummaryMap(JpaUserEntity user) {
        Map<String, Object> data = new LinkedHashMap<String, Object>();
        data.put("id", user.getId());
        data.put("username", user.getUsername());
        data.put("nickname", user.getNickname());
        data.put("email", user.getEmail());
        data.put("role", user.getRole());
        return data;
    }
}
