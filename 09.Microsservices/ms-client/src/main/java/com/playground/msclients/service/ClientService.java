package com.playground.msclients.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.playground.msclients.domain.Client;
import com.playground.msclients.exception.ResourceNotFoundException;
import com.playground.msclients.repository.ClientRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ClientService {
    private final ClientRepository clienteRepository;

    @Transactional
    public Client save(Client cliente) {
        return clienteRepository.save(cliente);
    }

    public Client findByCpf(String cpf) {
        return clienteRepository.findByCpf(cpf)
            .orElseThrow(() -> new ResourceNotFoundException("Client not found with CPF: " + cpf));
    }
    
}
