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
import com.app.park_api.web.dto.UserLoginDTO;
import com.app.park_api.web.exception.ErrorMessage;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Tag(name = "Auth", description = "Authentication operations")
@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/api/v1")
public class AuthController {

    @Autowired
    private JwtUserDetailsService detailsService;

    // for this to work the AuthenticationManager must have acess to the user service/repository
    // so, where spring get the AuthenticationManager implementation from ?
    @Autowired
    private AuthenticationManager authenticationManager;

    @Operation(
        summary = "Authenticate a user",
        description = "Endpoint to authenticate a user and return a JWT token.",
        responses = {
            @ApiResponse(
                responseCode = "200", description = "User authenticated successfully",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = JwtToken.class))
            ),
            @ApiResponse(
                responseCode = "400", description = "Invalid credentials",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))
            ),
            @ApiResponse(
                responseCode = "422", description = "Invalid input data",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))
            )
        }
    )
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
