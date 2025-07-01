package com.app.park_api.service;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.app.park_api.entity.Client;
import com.app.park_api.exception.CPFUniqueViolationException;
import com.app.park_api.exception.ResourceNotFoundException;
import com.app.park_api.repository.ClientRepository;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class ClientService {

    private final ClientRepository repository;

    @Transactional(readOnly = true)
    public Client save(Client client) {
        try {
            return repository.save(client);
        } catch (DataIntegrityViolationException e) {
            throw new CPFUniqueViolationException(String.format("Cpf '%s' already in use", client.getCpf()));
        }
    }
}

