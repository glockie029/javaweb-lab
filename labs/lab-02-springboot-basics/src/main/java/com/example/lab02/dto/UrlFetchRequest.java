package com.example.lab02.dto;

import javax.validation.constraints.NotBlank;

public class UrlFetchRequest {

    @NotBlank
    private String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
