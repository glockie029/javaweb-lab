package com.example.lab02.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.lab02.dto.UpdateProfileRequest;
import com.example.lab02.entity.JpaUserEntity;
import com.example.lab02.mapper.AppUserMapper;
import com.example.lab02.model.AppUserRecord;
import com.example.lab02.repository.JpaUserRepository;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CoreServiceUnitTest {

    @Mock
    private AppUserMapper appUserMapper;

    @Mock
    private JpaUserRepository jpaUserRepository;

    @InjectMocks
    private AuthService authService;

    @InjectMocks
    private ProfileService profileService;

    @InjectMocks
    private UserQueryService userQueryService;

    @InjectMocks
    private JpaUserService jpaUserService;

    private final GreetingService greetingService = new GreetingService();

    @Test
    void greetingServiceShouldNormalizeBlankInput() {
        assertEquals("hello, guest", greetingService.buildGreeting(null));
        assertEquals("hello, guest", greetingService.buildGreeting("   "));
        assertEquals("hello, dev", greetingService.buildGreeting(" dev "));
    }

    @Test
    void authServiceShouldHandleNullAndValidPasswordBranches() {
        assertFalse(authService.isValidUser(null, "x"));
        assertFalse(authService.isValidUser("alice", null));

        AppUserRecord user = buildUserRecord(2L, "alice", "alice123", "alice-demo", "alice@example.com", "USER");
        when(appUserMapper.findByUsername("alice")).thenReturn(user);

        assertTrue(authService.isValidUser("alice", "alice123"));
        assertFalse(authService.isValidUser("alice", "bad"));
    }

    @Test
    void profileServiceShouldReturnNullWhenUserMissing() {
        when(appUserMapper.findByUsername("ghost")).thenReturn(null);
        assertNull(profileService.getProfile("ghost"));
    }

    @Test
    void profileServiceShouldUpdateAndReturnProfile() {
        UpdateProfileRequest request = new UpdateProfileRequest();
        request.setNickname("alice-new");
        request.setEmail("alice-new@example.com");

        AppUserRecord updated = buildUserRecord(2L, "alice", "alice123", "alice-new", "alice-new@example.com", "USER");
        when(appUserMapper.findByUsername("alice")).thenReturn(updated);

        Map<String, String> result = profileService.updateProfile("alice", request);

        verify(appUserMapper).updateProfileByUsername("alice", "alice-new", "alice-new@example.com");
        assertEquals("alice-new", result.get("nickname"));
        assertEquals("alice123", result.get("password"));
    }

    @Test
    void userQueryServiceShouldCoverNullAndCollectionBranches() {
        when(appUserMapper.findById(99L)).thenReturn(null);
        assertNull(userQueryService.getUserSummaryById(99L));

        AppUserRecord alice = buildUserRecord(2L, "alice", "alice123", "alice-demo", "alice@example.com", "USER");
        when(appUserMapper.listUsersOrderBySafe("id")).thenReturn(List.of(alice));
        when(appUserMapper.searchUsersVulnerable("alice", "USER")).thenReturn(List.of(alice));

        assertEquals(1, userQueryService.listUserSummariesSafe("id").size());
        assertEquals(1, userQueryService.searchUserSummariesVulnerable("alice", "USER").size());
        assertTrue(userQueryService.searchUserSummariesSafe("alice", "").isEmpty());
    }

    @Test
    void userQueryServiceShouldCoverUpdateBranches() {
        AppUserRecord bob = buildUserRecord(3L, "bob", "bob123", "bob-new", "bob-new@example.com", "ADMIN");
        when(appUserMapper.findByUsername("bob")).thenReturn(bob);

        Map<String, Object> vuln = userQueryService.updateUserSelectiveVulnerable(
                "bob", "bob-new", "bob-new@example.com", "ADMIN");
        Map<String, Object> safe = userQueryService.updateUserSelectiveSafe(
                "bob", "bob-safe", "bob-safe@example.com");

        verify(appUserMapper).updateUserSelectiveVulnerable("bob", "bob-new", "bob-new@example.com", "ADMIN");
        verify(appUserMapper).updateUserSelectiveSafe("bob", "bob-safe", "bob-safe@example.com");
        assertEquals("ADMIN", vuln.get("role"));
        assertEquals("bob", safe.get("username"));
    }

    @Test
    void jpaUserServiceShouldCoverNullAndSaveBranches() {
        when(jpaUserRepository.findById(1L)).thenReturn(Optional.empty());
        when(jpaUserRepository.findByIdAndUsername(1L, "alice")).thenReturn(Optional.empty());
        when(jpaUserRepository.findByUsername("ghost")).thenReturn(Optional.empty());

        assertNull(jpaUserService.getUserByIdVulnerable(1L));
        assertNull(jpaUserService.getUserByIdSafe(1L, "alice"));
        assertNull(jpaUserService.updateCurrentProfile("ghost", buildUpdateRequest("n", "e@example.com")));

        JpaUserEntity entity = buildEntity(2L, "alice", "alice123", "alice-demo", "alice@example.com", "USER");
        when(jpaUserRepository.findByUsername("alice")).thenReturn(Optional.of(entity));
        when(jpaUserRepository.save(entity)).thenReturn(entity);

        Map<String, Object> updated = jpaUserService.updateCurrentProfile(
                "alice", buildUpdateRequest("alice-jpa", "alice-jpa@example.com"));
        assertNotNull(updated);
        assertEquals("alice-jpa", updated.get("nickname"));
        assertEquals("alice-jpa@example.com", updated.get("email"));
    }

    private AppUserRecord buildUserRecord(
            Long id, String username, String password, String nickname, String email, String role) {
        AppUserRecord user = new AppUserRecord();
        user.setId(id);
        user.setUsername(username);
        user.setPassword(password);
        user.setNickname(nickname);
        user.setEmail(email);
        user.setRole(role);
        return user;
    }

    private JpaUserEntity buildEntity(
            Long id, String username, String password, String nickname, String email, String role) {
        JpaUserEntity entity = new JpaUserEntity();
        entity.setId(id);
        entity.setUsername(username);
        entity.setPassword(password);
        entity.setNickname(nickname);
        entity.setEmail(email);
        entity.setRole(role);
        return entity;
    }

    private UpdateProfileRequest buildUpdateRequest(String nickname, String email) {
        UpdateProfileRequest request = new UpdateProfileRequest();
        request.setNickname(nickname);
        request.setEmail(email);
        return request;
    }
}
