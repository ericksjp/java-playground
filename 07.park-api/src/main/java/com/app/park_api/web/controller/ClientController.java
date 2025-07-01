package com.app.park_api.web.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.park_api.entity.Client;
import com.app.park_api.jwt.JwtUserDetails;
import com.app.park_api.service.ClientService;
import com.app.park_api.service.UserService;
import com.app.park_api.web.dto.ClientCreateDTO;
import com.app.park_api.web.dto.ClientResponseDTO;
import com.app.park_api.web.dto.mapper.ClientMapper;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@Tag(name = "Client", description = "Client operations")
@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/clients")
public class ClientController {

    private final ClientService clientService;
    private final UserService userService;

    @Operation( 
        summary = "Create a new client",
        description = "Endpoint to register a new client in the system.",
        security = @SecurityRequirement(name = "security"),
        responses = {
            @ApiResponse(
                responseCode = "201", description = "Client created successfully",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = ClientResponseDTO.class))
            ),
            @ApiResponse(
                responseCode = "401", description = "Unauthorized access. Bearer token is missing or invalid."
            ),
            @ApiResponse(
                responseCode = "403", description = "Client creation is only allowed for user witht CLIENT role",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = com.app.park_api.web.exception.ErrorMessage.class))
            ),
            @ApiResponse(
                responseCode = "409", description = "The CPF is already in use",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = com.app.park_api.web.exception.ErrorMessage.class))
            ),
            @ApiResponse(
                responseCode = "422", description = "Invalid input data",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = com.app.park_api.web.exception.ErrorMessage.class))
            )
        }
    )
    @PostMapping
    @PreAuthorize("hasRole('CLIENT')")
    public ResponseEntity<ClientResponseDTO> create(@RequestBody @Valid ClientCreateDTO createDTO, @AuthenticationPrincipal JwtUserDetails userDetails) {
        Client client = ClientMapper.toClient(createDTO);

        client.setUser(userService.findById(userDetails.getId()));

        client = clientService.save(client);

        return ResponseEntity.status(201).body(ClientMapper.toDTO(client));
    }

}
