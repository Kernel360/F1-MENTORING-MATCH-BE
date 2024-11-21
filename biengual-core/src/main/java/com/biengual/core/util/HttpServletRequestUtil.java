package com.biengual.core.util;

import jakarta.servlet.http.HttpServletRequest;

public class HttpServletRequestUtil {

    // 로깅에 남길 RequestBody를 얻는 메서드
    public static Object getRequestBody(HttpServletRequest request) {
        Object optionalRequestBody = request.getAttribute("requestBody");
        boolean existsRequestBody = optionalRequestBody.toString().isEmpty();

        return existsRequestBody ? null : optionalRequestBody;
    }

    // 로깅에 남길 클라이언트 IP를 얻는 메서드
    public static String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");

        if (ip != null && !ip.isEmpty()) {
            ip = ip.split(",")[0].trim();
        } else {
            ip = request.getRemoteAddr();
        }

        return ip;
    }
}
