package com.example.lab02.service;

import com.example.lab02.aop.DemoAdminOnly;
import com.example.lab02.aop.DemoAudit;
import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public class AopDemoService {

    @DemoAudit("buildAdminReport")
    @DemoAdminOnly
    public Map<String, Object> buildAdminReport() {
        Map<String, Object> data = new LinkedHashMap<String, Object>();
        data.put("reportName", "stage7-aop-demo");
        data.put("scope", "ADMIN_ONLY");
        data.put("generatedAt", Instant.now().toString());
        return data;
    }
}
