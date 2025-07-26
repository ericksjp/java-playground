package com.app.park_api.web.controller;

import java.net.URI;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.app.park_api.entity.ClientSpot;
import com.app.park_api.entity.User.Role;
import com.app.park_api.exception.ResourceNotFoundException;
import com.app.park_api.jwt.JwtUserDetails;
import com.app.park_api.repository.projection.ClientSpotProjection;
import com.app.park_api.service.ClientSpotService;
import com.app.park_api.service.ParkingService;
import com.app.park_api.web.dto.PageableDTO;
import com.app.park_api.web.dto.ParkingCreateDTO;
import com.app.park_api.web.dto.ParkingResponseDTO;
import com.app.park_api.web.dto.mapper.ClientSpotMapper;
import com.app.park_api.web.dto.mapper.PageableMapper;
import com.app.park_api.web.exception.ErrorMessage;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@Tag(name = "Parking", description = "Parking management operations")
@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/parkings")
public class ParkingController {

    private final ParkingService parkingService;
    private final ClientSpotService clientSpotService;

    @Operation (
        summary = "Check-in a vehicle",
        description = "Allows an admin to check in a vehicle into the parking system.",
        security = @SecurityRequirement(name = "security"),
        responses = {
            @ApiResponse(
                responseCode = "201", description = "Vehicle checked in successfully",
                content = @Content( mediaType = "application/json", schema = @Schema(implementation = ParkingResponseDTO.class))
            ),
            @ApiResponse(
                responseCode = "400", description = "Request is malformed or contains invalid parameters.",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))
            ),
            @ApiResponse(
                responseCode = "401", description = "Unauthorized access. Bearer token is missing or invalid."
            ),
            @ApiResponse(
                responseCode = "403", description = "ADMIN role is required to acess this resource.",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = com.app.park_api.web.exception.ErrorMessage.class))
            ),
            @ApiResponse(
                responseCode = "404", description = "No available parking spots or client not found",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))
            ),
            @ApiResponse(
                responseCode = "422", description = "Request contains semantically invalid data.",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))
            )
        }
    )
    @PostMapping("/check-in")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ParkingResponseDTO> checkIn(@RequestBody @Valid ParkingCreateDTO dto) {
        ClientSpot clientSpot = parkingService.CheckIn(ClientSpotMapper.toClient(dto));
        URI uri = ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/v1/parkings/{receipt}").buildAndExpand(clientSpot.getReceipt()).toUri();
        return ResponseEntity.created(uri).body(ClientSpotMapper.toDTO(clientSpot));
    }

    @Operation (
        summary = "Check-out a vehicle",
        description = "Allows an admin to check out a vehicle out of the parking system.",
        security = @SecurityRequirement(name = "security"),
        responses = {
            @ApiResponse(
                responseCode = "200", description = "Vehicle checked out successfully",
                content = @Content( mediaType = "application/json", schema = @Schema(implementation = ParkingResponseDTO.class))
            ),
            @ApiResponse(
                responseCode = "400", description = "Request is malformed or contains invalid parameters.",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))
            ),
            @ApiResponse(
                responseCode = "401", description = "Unauthorized access. Bearer token is missing or invalid."
            ),
            @ApiResponse(
                responseCode = "403", description = "ADMIN role is required to acess this resource.",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = com.app.park_api.web.exception.ErrorMessage.class))
            ),
            @ApiResponse(
                responseCode = "404", description = "Receipt not found or already checked out",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))
            )
        }
    )
    @PutMapping("/check-out/{receipt}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ParkingResponseDTO> checkOut(@PathVariable String receipt) {
        ClientSpot clientSpot = parkingService.CheckOut(receipt);
        return ResponseEntity.ok(ClientSpotMapper.toDTO(clientSpot));
    }

    @Operation (
        summary = "Get parking spots by client CPF",
        description = "Allows an admin to retrieve parking spots associated with a client's CPF.",
        security = @SecurityRequirement(name = "security"),
        parameters =  {
            @Parameter(
                in = ParameterIn.QUERY, name = "page",
                content = @Content(schema = @Schema(type = "integer", defaultValue = "0")),
                description = "Page number to retrieve (0-based index)"
            ),
            @Parameter(
                in = ParameterIn.QUERY, name = "size",
                content = @Content(schema = @Schema(type = "integer", defaultValue = "20")),
                description = "Number of elements per page"
            ),
            @Parameter(
                in = ParameterIn.QUERY, name = "sort", hidden = true,
                content = @Content(schema = @Schema(type = "string", defaultValue = "id,asc")),
                description = "Sorting criteria in the format: property,(asc|desc). Default sort order is ascending. Multiple sort criteria can be specified by separating them with commas."
            )
        },
        responses = {
            @ApiResponse(
                responseCode = "200", description = "Parking spots retrieved successfully",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = PageableDTO.class))
            ),
            @ApiResponse(
                responseCode = "400", description = "Request is malformed or contains invalid parameters.",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))
            ),
            @ApiResponse(
                responseCode = "401", description = "Unauthorized access. Bearer token is missing or invalid."
            ),
            @ApiResponse(
                responseCode = "403", description = "ADMIN role is required to acess this resource.",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = com.app.park_api.web.exception.ErrorMessage.class))
            ),
            @ApiResponse(
                responseCode = "404", description = "Client not found",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))
            )
        }
    )
    @GetMapping("/cpf/{cpf}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PageableDTO> getByClientCPF(
        @Parameter(hidden = true) @PageableDefault(size = 20) Pageable pageable,
        @PathVariable String cpf
    ) {
        Page<ClientSpotProjection> clientSpots = clientSpotService.getByClientCpf(cpf, pageable);

        return ResponseEntity.ok(PageableMapper.toDTO(clientSpots));
    }

    @Operation (
        summary = "Get all parking spots",
        description = "Allows an admin to retrieve all parking spots and a client to retrieve their own parking spots.",
        security = @SecurityRequirement(name = "security"),
        parameters =  {
            @Parameter(
                in = ParameterIn.QUERY, name = "page",
                content = @Content(schema = @Schema(type = "integer", defaultValue = "0")),
                description = "Page number to retrieve (0-based index)"
            ),
            @Parameter(
                in = ParameterIn.QUERY, name = "size",
                content = @Content(schema = @Schema(type = "integer", defaultValue = "20")),
                description = "Number of elements per page"
            ),
            @Parameter(
                in = ParameterIn.QUERY, name = "sort", hidden = true,
                content = @Content(schema = @Schema(type = "string", defaultValue = "id,asc")),
                description = "Sorting criteria in the format: property,(asc|desc). Default sort order is ascending. Multiple sort criteria can be specified by separating them with commas."
            )
        },
        responses = {
            @ApiResponse(
                responseCode = "200", description = "Parking spots retrieved successfully",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = PageableDTO.class))
            ),
            @ApiResponse(
                responseCode = "401", description = "Unauthorized access. Bearer token is missing or invalid."
            )
        }
    )
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'CLIENT')")
    public ResponseEntity<PageableDTO> getAll(
        @Parameter(hidden = true) @PageableDefault(size = 20) Pageable pageable,
        @AuthenticationPrincipal JwtUserDetails user
    ) {
        Page<ClientSpotProjection> clientSpots;

        if (user.getRole().equals(Role.ROLE_CLIENT.name())) {
            clientSpots = clientSpotService.getByClientId(user.getId(), pageable);
        } else {
            clientSpots = clientSpotService.getAll(pageable);
        }

        return ResponseEntity.ok(PageableMapper.toDTO(clientSpots));
    }

    @Operation (
        summary = "Get parking spot by receipt",
        description = "Allows an admin or client to retrieve a parking spot by its receipt.",
        security = @SecurityRequirement(name = "security"),
        responses = {
            @ApiResponse(
                responseCode = "200", description = "Parking spot retrieved successfully",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = ParkingResponseDTO.class))
            ),
            @ApiResponse(
                responseCode = "401", description = "Unauthorized access. Bearer token is missing or invalid."
            ),
            @ApiResponse(
                responseCode = "404", description = "Receipt not found or client does not own the parking spot",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))
            )
        }
    )
    @GetMapping("/{receipt}")
    @PreAuthorize("hasAnyRole('ADMIN', 'CLIENT')")
    public ResponseEntity<ParkingResponseDTO> getByReceipt(@PathVariable String receipt, @AuthenticationPrincipal JwtUserDetails user) {
        ClientSpot clientSpot = clientSpotService.getByReceipt(receipt);

        // throw a 404 for safety
        if (user.getRole().equals(Role.ROLE_CLIENT.name()) && user.getId() != clientSpot.getClient().getId()) {
            throw new ResourceNotFoundException("Parking", "receipt", receipt);
        }

        return ResponseEntity.ok(ClientSpotMapper.toDTO(clientSpot));
    }
}
