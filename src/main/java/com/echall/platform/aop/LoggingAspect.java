package com.echall.platform.aop;

import com.echall.platform.message.ApiCustomResponse;
import com.echall.platform.oauth2.domain.info.OAuth2UserPrincipal;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import static com.echall.platform.message.status.UserServiceStatus.USER_LOGIN_SUCCESS;

@Slf4j
@Aspect
@Component
public class LoggingAspect {
    @Value("${spring.profiles.active}")
    private String activeProfile;

    @Pointcut("@within(org.springframework.web.bind.annotation.RestController)")
    private void restController() {}

    @Pointcut("@within(org.springframework.stereotype.Service)")
    private void service() {}

    @Pointcut("@annotation(com.echall.platform.annotation.LoginLogging)")
    private void login() {}

    @Around("restController()")
    public Object logControllerAround(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        String methodName = joinPoint.getSignature().toShortString();
        Object result = joinPoint.proceed(joinPoint.getArgs());
        long executionTime = System.currentTimeMillis() - startTime;

        if (result instanceof ResponseEntity<?>) {
            ResponseEntity<?> responseEntity = (ResponseEntity<?>) result;
            Object body = responseEntity.getBody();

            ApiCustomResponse<?> apiResponse = (ApiCustomResponse<?>) body;
            String customCode = apiResponse.code();

            log.info("server: {}, controller: {}, responseTime: {}ms, code: {}",
                activeProfile, methodName, executionTime, customCode);
        }
        return result;
    }

    @After(value = "login() && args(request, response, authentication)")
    public void logLoginAfter(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        OAuth2UserPrincipal principal = (OAuth2UserPrincipal) authentication.getPrincipal();
        String email = principal.getEmail();
        String code = USER_LOGIN_SUCCESS.getServiceStatus();

        log.info("server: {}, user: {}, code: {}", activeProfile, email, code);
    }

    @AfterThrowing(pointcut = "restController()", throwing = "e")
    public void logException(JoinPoint joinPoint, Exception e) {
        String className = joinPoint.getSignature().getDeclaringTypeName();
        String methodName = joinPoint.getSignature().getName();
        String errorMessage = e.getMessage();

        log.error("server: {}, class: {}, method: {}, message: {}", activeProfile, className, methodName, errorMessage);
    }
}
