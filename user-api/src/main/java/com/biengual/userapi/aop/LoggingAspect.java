package com.biengual.userapi.aop;

import com.biengual.core.response.ApiCustomResponse;
import com.biengual.core.util.GlobalExceptionLogSchema;
import com.biengual.core.util.RestControllerSuccessLogSchema;
import com.biengual.userapi.oauth2.info.OAuth2UserPrincipal;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import static com.biengual.core.response.status.UserServiceStatus.USER_LOGIN_SUCCESS;

/**
 * 로그 남기는 것을 AOP로 관리하기 위한 클래스
 *
 * 현재 이 클래스에서 관리하는 로그
 * 1. ApiCustomResponse 형식으로 ResponseEntity를 반환하는 RestController의 정상 응답 로그
 * 2. 소셜 로그인 로그(OAuth2)
 * 3. RestController 동작에서 발생하는 에러 로그
 *
 * @author 문찬욱
 */
@Slf4j
@Aspect
@Component
public class LoggingAspect {
    @Value("${spring.profiles.active}")
    private String activeProfile;

    @Pointcut("@within(org.springframework.web.bind.annotation.RestController)")
    private void restController() {}

    @Pointcut("execution(* com.biengual.core.response.error.GlobalExceptionHandler.*(..))")
    public void globalException() {}

    @Pointcut("@annotation(com.biengual.core.annotation.LoginLogging)")
    private void login() {}

    // ApiCustomResponse 형식으로 ResponseEntity를 반환하는 RestController의 정상 응답에 대한 로그
    @Around("restController()")
    public Object logControllerAround(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        Object result = joinPoint.proceed(joinPoint.getArgs());
        long responseTime = System.currentTimeMillis() - startTime;

        if (result instanceof ResponseEntity<?> responseEntity) {
            Object responseBody = responseEntity.getBody();

            if (responseBody instanceof ApiCustomResponse apiResponse) {
                HttpServletRequest request =
                    ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();

                String user = this.getUserIdentifier();

                RestControllerSuccessLogSchema logSchema =
                    RestControllerSuccessLogSchema.of(activeProfile, user, responseTime, request, apiResponse);

                log.info(logSchema.toString());
            }
        }
        return result;
    }


    // 소셜 로그인 로그를 남기는 메서드
    @After(value = "login() && args(request, response, authentication)", argNames = "request, response, authentication")
    public void logLoginAfter(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        OAuth2UserPrincipal principal = (OAuth2UserPrincipal) authentication.getPrincipal();
        String email = principal.getEmail();
        String code = USER_LOGIN_SUCCESS.getServiceStatus();

        log.info("server: {}, user: {}, code: {}", activeProfile, email, code);
    }

    // GlobalException에 대한 로그를 남기는 메서드
    @AfterReturning(pointcut = "globalException()", returning = "result")
    public void globalExceptionAfterReturning(Object result) {
        if (result instanceof ResponseEntity<?> responseEntity) {
            Object responseBody = responseEntity.getBody();

            if (responseBody instanceof ApiCustomResponse apiResponse) {
                HttpServletRequest request =
                    ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();

                String user = this.getUserIdentifier();

                GlobalExceptionLogSchema logSchema =
                    GlobalExceptionLogSchema.of(activeProfile, user, request, apiResponse);

                log.error(logSchema.toString());
            }
        }
    }

    // Internal Method =================================================================================================

    // RestController에 대한 사용자 식별
    private String getUserIdentifier() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getPrincipal() != null) {
            Object principal = authentication.getPrincipal();

            if (principal instanceof OAuth2UserPrincipal oAuth2UserPrincipal) {
                return oAuth2UserPrincipal.getEmail();
            }
        }

        return "guest";
    }
}
