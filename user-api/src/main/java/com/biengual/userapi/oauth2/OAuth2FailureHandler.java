package com.biengual.userapi.oauth2;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

// TODO: GlobalException을 이용할 수 있는 방법이 있는지 생각해볼 것
@Slf4j
@Component
public class OAuth2FailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException {
        String errorCode;
        String errorMessage;

        if (exception instanceof OAuth2AuthenticationException) {
            OAuth2Error error = ((OAuth2AuthenticationException) exception).getError();
            errorCode = error.getErrorCode();
            errorMessage = error.getDescription();

            log.error("OAuth2 로그인에 실패했습니다. code: {}, message: {}", errorCode, errorMessage);

        } else {
            log.error("OAuth2 로그인 과정에 알 수 없는 에러가 발생했습니다. message: {}", exception.getMessage());
        }
    }
}
