package com.example.lab02.aop;

import java.util.Arrays;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class DemoAuditAspect {

    @Around("@annotation(demoAudit)")
    public Object logAudit(ProceedingJoinPoint joinPoint, DemoAudit demoAudit) throws Throwable {
        long startedAt = System.currentTimeMillis();
        try {
            return joinPoint.proceed();
        } finally {
            long durationMs = System.currentTimeMillis() - startedAt;
            System.out.println("[AOP-AUDIT] action=" + demoAudit.value()
                    + ", method=" + joinPoint.getSignature().toShortString()
                    + ", args=" + Arrays.toString(joinPoint.getArgs())
                    + ", durationMs=" + durationMs);
        }
    }
}
