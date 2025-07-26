package com.app.park_api.web.controller;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.app.park_api.entity.Spot;
import com.app.park_api.service.SpotService;
import com.app.park_api.web.dto.SpotCreateDTO;
import com.app.park_api.web.dto.SpotResponseDTO;
import com.app.park_api.web.dto.mapper.SpotMapper;
import com.app.park_api.web.exception.ErrorMessage;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/spots")
public class SpotController {

    @Autowired
    SpotService spotService;

    @Operation(
        summary = "Create a new parking spot",
        description = "Endpoint to register a new parking spot in the system.",
        security = @SecurityRequirement(name = "security"),
        responses = {
            @ApiResponse(
                responseCode = "201", description = "Spot created successfully",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = SpotCreateDTO.class))
            ),
            @ApiResponse(
                responseCode = "401", description = "Unauthorized access. Bearer token is missing or invalid."
            ),
            @ApiResponse(
                responseCode = "403", description = "User does not have permission to view this resource.",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))
            ),
            @ApiResponse(
                responseCode = "409", description = "The spot code is already in use",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))
            ),
            @ApiResponse(
                responseCode = "422", description = "Invalid input data",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))
            )
        }
    )
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> create(@RequestBody @Valid SpotCreateDTO dto) {
        Spot spot = spotService.save(SpotMapper.toSpot(dto));
        URI uri = ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/v1/spots/{id}").buildAndExpand(spot.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @Operation(
        summary = "Retrieve a parking spot by code",
        description = "Requires a Bearer token for authentication. Access restricted to ADMIN.",
        security = @SecurityRequirement(name = "security"),
        responses = {
            @ApiResponse(
                responseCode = "200", description = "Spot details retrieved successfully.",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = SpotResponseDTO.class))
            ),
            @ApiResponse(
                responseCode = "401", description = "Unauthorized access. Bearer token is missing or invalid."
            ),
            @ApiResponse(
                responseCode = "403", description = "User does not have permission to view this resource.",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))
            ),
            @ApiResponse(
                responseCode = "404", description = "Spot not found",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))
            )
        }
    )
    @GetMapping("/{code}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SpotResponseDTO> findByCode(@PathVariable String code) {
        Spot spot = spotService.findByCode(code);
        return ResponseEntity.ok(SpotMapper.toDTO(spot));
    }
    
}
