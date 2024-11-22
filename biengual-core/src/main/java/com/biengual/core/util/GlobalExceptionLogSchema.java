package com.biengual.core.util;

import com.biengual.core.response.ApiCustomResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Builder;

@Builder
public record GlobalExceptionLogSchema(
    String server,
    String ip,
    String contentType,
    String userAgent,
    String user,
    String httpMethod,
    String uri,
    String params,
    Object requestBody,
    String code,
    String message
) {
    public static GlobalExceptionLogSchema of(
        String server, String user, HttpServletRequest request, ApiCustomResponse response
    ) {
        return GlobalExceptionLogSchema.builder()
            .server(server)
            .ip(HttpServletRequestUtil.getClientIp(request))
            .contentType(request.getContentType())
            .userAgent(request.getHeader("User-Agent"))
            .user(user)
            .httpMethod(request.getMethod())
            .uri(request.getRequestURI())
            .params(request.getQueryString())
            .requestBody(HttpServletRequestUtil.getRequestBody(request))
            .code(response.code())
            .message(response.message())
            .build();
    }

    @Override
    public String toString() {
        return """
            [GLOBAL EXCEPTION]
                server: %s,
                ip: %s,
                contentType: %s,
                userAgent: %s,
                user: %s,
                httpMethod: %s,
                uri: %s,
                params: %s,
                requestBody: %s,
                code: %s,
                message: %s
            """
            .formatted(
                this.server,
                this.ip,
                this.contentType,
                this.userAgent,
                this.user,
                this.httpMethod,
                this.uri,
                this.params,
                this.requestBody,
                this.code,
                this.message
            );
    }
}
