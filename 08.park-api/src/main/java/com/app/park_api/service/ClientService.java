package com.app.park_api.service;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.app.park_api.entity.Client;
import com.app.park_api.exception.ResourceNotFoundException;
import com.app.park_api.exception.ResourceUniqueViolationException;
import com.app.park_api.repository.ClientRepository;
import com.app.park_api.repository.projection.ClientProjection;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class ClientService {

    private final ClientRepository repository;

    @Transactional
    public Client save(Client client) {
        try {
            return repository.save(client);
        } catch (DataIntegrityViolationException e) {
            throw new ResourceUniqueViolationException("client", "cpf", client.getCpf());
        }
    }

    @Transactional(readOnly = true)
    public Client findById(Long id) {
        return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("client", "id" , id.toString()));
    }

    @Transactional(readOnly = true)
    public Page<ClientProjection> findAll(Pageable pageable) {
        return repository.findAllPageable(pageable);
    }

    @Transactional(readOnly = true)
    public Client findByUserId(Long id) {
        return repository.findByUserId(id);
    }

    @Transactional(readOnly = true)
    public Client findByCpf(String cpf) {
        return repository.findByCpf(cpf).orElseThrow(() -> new ResourceNotFoundException("client", "cpf", cpf));
    }
}
