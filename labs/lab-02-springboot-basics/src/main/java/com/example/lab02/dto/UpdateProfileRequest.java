package com.example.lab02.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

public class UpdateProfileRequest {

    @NotBlank
    private String nickname;

    @NotBlank
    @Email
    private String email;

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
