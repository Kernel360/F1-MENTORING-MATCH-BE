package com.biengual.core.util;

import com.biengual.core.response.error.code.UserErrorCode;
import com.biengual.core.response.error.exception.CommonException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Map;

public class HttpServletResponseUtil {

    public static void createErrorResponse(HttpServletResponse response, CommonException e) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(e.getErrorCode().getStatus().value());

        ObjectMapper objectMapper = new ObjectMapper();

        String jsonResponse = objectMapper.writeValueAsString(
            Map.of("code", e.getErrorCode().getCode(),
                "message", e.getErrorCode().getMessage())
        );

        response.getWriter().write(jsonResponse);
        response.getWriter().flush();
    }

    public static void createErrorResponse(HttpServletResponse response, UserErrorCode userErrorCode) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(userErrorCode.getStatus().value());

        ObjectMapper objectMapper = new ObjectMapper();

        String jsonResponse = objectMapper.writeValueAsString(
            Map.of("code", userErrorCode.getCode(),
                "message", userErrorCode.getMessage())
        );

        response.getWriter().write(jsonResponse);
        response.getWriter().flush();
    }
}
