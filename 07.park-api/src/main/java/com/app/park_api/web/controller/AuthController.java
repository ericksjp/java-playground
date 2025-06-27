package com.app.park_api.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.park_api.jwt.JwtToken;
import com.app.park_api.jwt.JwtUserDetailsService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

import com.app.park_api.web.dto.UserLoginDTO;
import com.app.park_api.web.exception.ErrorMessage;

@Slf4j
@RestController
@RequestMapping("/api/v1")
public class AuthController {

    @Autowired
    private JwtUserDetailsService detailsService;

    // for this to work the AuthenticationManager must have acess to the user service/repository
    // so, where spring get the AuthenticationManager implementation from ?
    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/auth")
    public ResponseEntity<?> auth(@RequestBody @Valid UserLoginDTO dto, HttpServletRequest request) {
        log.info("Authenticating user: {}", dto.getUsername());

        try {
            UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(dto.getUsername(), dto.getPassword());

            // black voodoo magic here
            authenticationManager.authenticate(authenticationToken);

            JwtToken token = detailsService.getTokenAuthenticated(dto.getUsername());
            return ResponseEntity.ok().body(token);
        } catch (AuthenticationException e) {
            log.error("Authentication failed for user: {}", dto.getUsername(), e);
        }

        return ResponseEntity
                .badRequest()
                .body(new ErrorMessage(request, HttpStatus.BAD_REQUEST, "Invalid credentials"));
    }

}
