package com.biengual.userapi.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Profile;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;

@Profile({"local", "dev"})
public class ServerTimingFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
        throws ServletException, IOException {
        long startTime = System.currentTimeMillis();
        ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(response);
        try {
            filterChain.doFilter(request, responseWrapper);
        } finally {
            long duration = System.currentTimeMillis() - startTime;
            response.addHeader("Server-Timing", String.format("app;dur=%d", duration));
            responseWrapper.copyBodyToResponse();
        }
    }
}
