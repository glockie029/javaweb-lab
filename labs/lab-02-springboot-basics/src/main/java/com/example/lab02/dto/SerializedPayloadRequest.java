package com.example.lab02.dto;

import javax.validation.constraints.NotBlank;

public class SerializedPayloadRequest {

    @NotBlank
    private String payload;

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }
}
