package com.app.park_api.jwt;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

// this filter will be applied to every incoming request (extends OncePerRequestFilter)

@Slf4j
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUserDetailsService detailsService;

    // this function will be executed for every incoming request
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // get authorization header
        final String token = request.getHeader(JwtUtils.JWT_AUTHORIZATION);

        // if token is null or dont start with 'Bearer ' continue filter chain without auth process
        if (token == null || !token.startsWith(JwtUtils.JWT_BEARER)) {
            log.info("JWT Token is missing or does not start with 'Bearer '");
            filterChain.doFilter(request, response);
            return;
        }

        // if token is invalid send an error response and stop the filter chain
        if (!JwtUtils.isTokenValid(token)) {
            log.warn("JWT Token is invalid or expired");
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        // gets the username from the token
        String username = JwtUtils.getUsernameFromToken(token);

        // authenticate the user in the security context of the filter chain
        toAuthenticate(request, username);

        // continue the filter chain with the authenticated user
        filterChain.doFilter(request, response);
    }

    private void toAuthenticate(HttpServletRequest request, String username) {
        // load user info as UserDetails to authenticate the user (or return 404 if user not found)
        UserDetails userDetails = detailsService.loadUserByUsername(username);

        // create authentication token with the user details
        UsernamePasswordAuthenticationToken authenticationToken = UsernamePasswordAuthenticationToken.authenticated(userDetails, null, userDetails.getAuthorities());

        // set the details of the request (like IP address, session ID, etc.)
        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        // set the authentication token in the security context of the current request
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
    }
    
}
