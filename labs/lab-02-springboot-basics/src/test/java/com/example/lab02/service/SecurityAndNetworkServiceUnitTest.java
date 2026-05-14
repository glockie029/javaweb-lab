package com.example.lab02.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.example.lab02.dto.UrlFetchRequest;
import com.example.lab02.security.JwtTokenService;
import java.util.Collections;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

class SecurityAndNetworkServiceUnitTest {

    @Test
    void jwtTokenServiceShouldGenerateAndValidateToken() {
        JwtTokenService jwtTokenService = new JwtTokenService(
                "lab02-jwt-demo-secret-key-please-change-2026",
                3_600_000L);
        UserDetails user = new User(
                "alice",
                "alice123",
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));

        String token = jwtTokenService.generateToken(user);

        assertEquals("alice", jwtTokenService.extractUsername(token));
        assertTrue(jwtTokenService.isTokenValid(token, user));
        assertFalse(jwtTokenService.isTokenValid(token, new User(
                "bob",
                "bob123",
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")))));
    }

    @Test
    void ssrfServiceShouldExposeContextAndRejectUnsafeTargets() {
        SsrfDemoService service = new SsrfDemoService();
        Map<String, Object> info = service.getContextInfo();

        assertEquals("/api/ssrf/vuln/fetch", info.get("dangerousEndpoint"));
        assertEquals("/api/ssrf/safe/fetch", info.get("safeEndpoint"));

        UrlFetchRequest localhost = new UrlFetchRequest();
        localhost.setUrl("http://127.0.0.1:8080");
        assertThrows(IllegalArgumentException.class, () -> service.fetchSafe(localhost));

        UrlFetchRequest invalidScheme = new UrlFetchRequest();
        invalidScheme.setUrl("file:///etc/passwd");
        assertThrows(IllegalArgumentException.class, () -> service.fetchSafe(invalidScheme));

        UrlFetchRequest invalidUrl = new UrlFetchRequest();
        invalidUrl.setUrl("  ");
        assertThrows(SsrfDemoFetchException.class, () -> service.fetchVulnerable(invalidUrl));
    }
}
