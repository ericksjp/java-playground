package com.playground.msclients.web.controller;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.playground.msclients.domain.Client;
import com.playground.msclients.service.ClientService;
import com.playground.msclients.web.dto.ClienteResponseDto;
import com.playground.msclients.web.dto.ClientCreateDto;
import com.playground.msclients.web.mappper.ClientMapper;
import com.playground.msclients.exception.ResourceNotFoundException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@RestController
@RequestMapping("/clients")
@Slf4j
public class ClientController {
    private final ClientService clienteService;

    @PostMapping
    public ResponseEntity<Void> save(@RequestBody ClientCreateDto dto) {

        Client cliente = ClientMapper.toEntity(dto);
        clienteService.save(cliente);

        URI locationHeader = ServletUriComponentsBuilder
            .fromCurrentRequest()
            .query("cpf={cpf}")
            .buildAndExpand(cliente.getCpf())
            .toUri();

        return ResponseEntity.created(locationHeader).build();
    }

    @GetMapping(params = "cpf")
    public ResponseEntity<?> findByCpf(@RequestParam(required = true) String cpf) {
        try {
            Client cliente = clienteService.findByCpf(cpf);
            ClienteResponseDto responseDto = ClientMapper.toDto(cliente);
            log.info("Cliente encontrado: {}", responseDto);
            return ResponseEntity.ok(responseDto);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
