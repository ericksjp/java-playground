package com.app.park_api.web.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.park_api.entity.Client;
import com.app.park_api.jwt.JwtUserDetails;
import com.app.park_api.repository.projection.ClientProjection;
import com.app.park_api.service.ClientService;
import com.app.park_api.service.UserService;
import com.app.park_api.web.dto.ClientCreateDTO;
import com.app.park_api.web.dto.ClientResponseDTO;
import com.app.park_api.web.dto.PageableDTO;
import com.app.park_api.web.dto.mapper.ClientMapper;
import com.app.park_api.web.dto.mapper.PageableMapper;
import com.app.park_api.web.exception.ErrorMessage;

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

    @Operation(
        summary = "Retrieve client by ID",
        description = "Requires a Bearer token for authentication. Access restricted to ADMIN.",
        security = @SecurityRequirement(name = "security"),
        responses = {
            @ApiResponse(
                responseCode = "200", description = "Client details retrieved successfully.",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = ClientResponseDTO.class))
            ),
            @ApiResponse(
                responseCode = "401", description = "Unauthorized access. Bearer token is missing or invalid."
            ),
            @ApiResponse(
                responseCode = "403", description = "Access denied. Only ADMIN can retrieve client details.",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))
            ),
            @ApiResponse(
                responseCode = "404", description = "Client not found",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))
            )
        }
    )
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ClientResponseDTO> getById(@PathVariable Long id, @AuthenticationPrincipal JwtUserDetails userDetails) {
        Client client = clientService.findById(id);
        return ResponseEntity.ok(ClientMapper.toDTO(client));
    }

    @Operation(
        summary = "Retrieve all clients",
        description = "Requires a Bearer token for authentication. Access restricted to ADMIN.",
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
                responseCode = "200", description = "List of clients retrieved successfully.",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = PageableDTO.class))
            ),
            @ApiResponse(
                responseCode = "401", description = "Unauthorized access. Bearer token is missing or invalid."
            ),
            @ApiResponse(
                responseCode = "403", description = "Access denied. Only ADMIN can retrieve all clients.",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))
            )
        }
    )
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity <PageableDTO> getAll(@Parameter(hidden = true) @PageableDefault(size = 20, sort = {"name"})Pageable pageable) {
        Page<ClientProjection> clients = clientService.findAll(pageable);
        return ResponseEntity.ok(PageableMapper.toDTO(clients));
    }

    @Operation (
        summary = "Retrieve client details",
        description = "Requires a Bearer token for authentication. Access restricted to CLIENT.",
        security = @SecurityRequirement(name = "security"),
        responses = {
            @ApiResponse(
                responseCode = "200", description = "Client details retrieved successfully.",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = ClientResponseDTO.class))
            ),
            @ApiResponse(
                responseCode = "401", description = "Unauthorized access. Bearer token is missing or invalid."
            ),
            @ApiResponse(
                responseCode = "403", description = "Access denied. Only CLIENT can retrieve their own details.",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))
            )
        }
    )
    @GetMapping("/details")
    @PreAuthorize("hasRole('CLIENT')")
    public ResponseEntity <ClientResponseDTO> details(@AuthenticationPrincipal JwtUserDetails userDetails) {
        Client client = clientService.findByUserId(userDetails.getId());
        return ResponseEntity.ok(ClientMapper.toDTO(client));
    }
}
