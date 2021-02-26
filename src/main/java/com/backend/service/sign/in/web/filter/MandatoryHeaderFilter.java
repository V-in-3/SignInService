package com.backend.service.sign.in.web.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j
public class MandatoryHeaderFilter extends OncePerRequestFilter {

    public static final String X_REQUEST_ID_NAME = "X-RequestId";

    private final ObjectMapper objectMapper;

    public MandatoryHeaderFilter(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        final String requestId = request.getHeader(X_REQUEST_ID_NAME);
        if (requestId == null) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            String errorMessage = String.format("Mandatory header %s does not exist in the request", X_REQUEST_ID_NAME);
            addResponseBody(request, response, errorMessage);
            log.error(errorMessage);
        } else {
            response.setHeader(X_REQUEST_ID_NAME, requestId);
            chain.doFilter(request, response);
        }
    }

    @SneakyThrows
    protected void addResponseBody(HttpServletRequest request, HttpServletResponse response, String errorMessage) {
        Map<String, Object> errorBody = new LinkedHashMap<>();
        errorBody.put("timestamp", Instant.now());
        errorBody.put("status", HttpStatus.BAD_REQUEST.value());
        errorBody.put("error", HttpStatus.BAD_REQUEST.getReasonPhrase());
        errorBody.put("message", errorMessage);
        errorBody.put("path", request.getRequestURI());
        byte[] body = objectMapper.writeValueAsBytes(errorBody);
        response.getOutputStream().write(body);
        response.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
    }
}