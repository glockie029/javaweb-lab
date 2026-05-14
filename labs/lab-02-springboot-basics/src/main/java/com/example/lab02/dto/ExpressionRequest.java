package com.example.lab02.dto;

import javax.validation.constraints.NotBlank;

public class ExpressionRequest {

    @NotBlank
    private String expression;

    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }
}
