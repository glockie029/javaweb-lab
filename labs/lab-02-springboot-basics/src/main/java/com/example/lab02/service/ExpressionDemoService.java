package com.example.lab02.service;

import com.example.lab02.dto.ExpressionRequest;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.stereotype.Service;

@Service
public class ExpressionDemoService {

    private final ExpressionParser parser = new SpelExpressionParser();

    public Map<String, Object> getContextInfo() {
        Map<String, Object> data = new LinkedHashMap<String, Object>();
        data.put("sink", "SpelExpressionParser.parseExpression(...).getValue(...)");
        data.put("dangerousEndpoint", "/api/expr/vuln/spel");
        data.put("safeEndpoint", "/api/expr/safe/spel");
        data.put("sampleExpressions", Arrays.asList(
                "'spring'.toUpperCase()",
                "'hello' + ' world'",
                "1 + 2 * 3",
                "100 / 5"));
        return data;
    }

    public Map<String, Object> evaluateVulnerable(ExpressionRequest request) {
        String expressionText = request.getExpression().trim();
        Expression expression = parser.parseExpression(expressionText);
        Object value = expression.getValue();
        return buildResult("vulnerable-spel-demo", expressionText, value);
    }

    public Map<String, Object> evaluateSafe(ExpressionRequest request) {
        String expressionText = request.getExpression().trim();
        if (!expressionText.matches("[0-9\\s+\\-*/%().]+")) {
            throw new IllegalArgumentException("safe spel demo only allows arithmetic expressions");
        }
        Expression expression = parser.parseExpression(expressionText);
        Object value = expression.getValue();
        return buildResult("safe-spel-demo", expressionText, value);
    }

    private Map<String, Object> buildResult(String mode, String evaluatedExpression, Object value) {
        Map<String, Object> data = new LinkedHashMap<String, Object>();
        data.put("mode", mode);
        data.put("evaluatedExpression", evaluatedExpression);
        data.put("valueType", value == null ? "null" : value.getClass().getSimpleName());
        data.put("value", value);
        return data;
    }
}
