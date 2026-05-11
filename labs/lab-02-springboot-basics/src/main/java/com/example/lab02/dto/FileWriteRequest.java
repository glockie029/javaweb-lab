package com.example.lab02.dto;

import javax.validation.constraints.NotBlank;

public class FileWriteRequest {

    @NotBlank
    private String path;

    @NotBlank
    private String content;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
