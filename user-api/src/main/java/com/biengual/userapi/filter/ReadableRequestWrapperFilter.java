package com.biengual.userapi.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class ReadableRequestWrapperFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
        throws ServletException, IOException {
        try {
            ReadableRequestWrapper wrapper = new ReadableRequestWrapper(request);
            wrapper.setAttribute("requestBody", wrapper.getRequestBody());
            filterChain.doFilter(wrapper, response);
        } catch (Exception e) {
            filterChain.doFilter(request, response);
        }
    }
}
