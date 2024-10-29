package com.biengual.userapi.core.oauth2;

import com.biengual.userapi.core.response.error.code.UserErrorCode;
import com.biengual.userapi.core.util.HttpServletResponseUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
public class AccessDeniedHandlerImpl implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException)
        throws IOException, ServletException {
        UserErrorCode userErrorCode = UserErrorCode.USER_PERMISSION_DENIED;

        log.error(userErrorCode.getCode() + " : " + userErrorCode.getMessage());

        HttpServletResponseUtil.createErrorResponse(response, userErrorCode);
    }
}
