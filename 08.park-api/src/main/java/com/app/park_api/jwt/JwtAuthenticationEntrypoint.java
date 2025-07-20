package com.app.park_api.jwt;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

// class implements the AuthenticationEntryPoint interface to handle unauthorized access attempts.

@Slf4j
public class JwtAuthenticationEntrypoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        int status = HttpStatus.UNAUTHORIZED.value();
        log.info("http Status {} {}", status, authException.getMessage());
        response.setHeader("www-authenticate", "Bearer realm=/api/v1/auth");
        response.sendError(status);
    }
    
}
