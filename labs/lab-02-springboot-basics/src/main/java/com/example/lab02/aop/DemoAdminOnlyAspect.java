package com.example.lab02.aop;

import javax.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Aspect
@Component
public class DemoAdminOnlyAspect {

    @Around("@annotation(com.example.lab02.aop.DemoAdminOnly)")
    public Object requireAdminRole(ProceedingJoinPoint joinPoint) throws Throwable {
        HttpServletRequest request = currentRequest();
        String role = request == null ? null : request.getHeader("X-Demo-Role");
        if (!"ADMIN".equalsIgnoreCase(role)) {
            throw new DemoForbiddenException("aop demo requires X-Demo-Role: ADMIN");
        }
        return joinPoint.proceed();
    }

    private HttpServletRequest currentRequest() {
        RequestAttributes attributes = RequestContextHolder.getRequestAttributes();
        if (!(attributes instanceof ServletRequestAttributes)) {
            return null;
        }
        return ((ServletRequestAttributes) attributes).getRequest();
    }
}
