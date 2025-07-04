package com.app.park_api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.app.park_api.entity.ClientSpot;
import com.app.park_api.exception.ResourceNotFoundException;
import com.app.park_api.repository.ClientSpotRepository;
import com.app.park_api.repository.projection.ClientSpotProjection;

@Service
public class ClientSpotService {
    @Autowired
    private ClientSpotRepository repository;
    @Autowired
    private ClientService clientService;

    @Transactional
    public ClientSpot save(ClientSpot clientSpot) {
         return repository.save(clientSpot);
    }

    @Transactional(readOnly = true)
    public ClientSpot getByReceipt(String receipt) {
        return repository.findByReceiptAndCheckOutIsNull(receipt).orElseThrow(() -> new ResourceNotFoundException(String.format("Parking with receipt '%s' not available for checkout", receipt)));
    }

    @Transactional(readOnly = true)
    public Page<ClientSpotProjection> getAll(Pageable pageable) {
        return repository.findAllPageable(pageable);
    }

    @Transactional(readOnly = true)
    public Page<ClientSpotProjection> getByClientId(Long id, Pageable pageable) {
        clientService.findById(id);
        return repository.findAllByClientId(id, pageable);
    }

    @Transactional(readOnly = true)
    public Page<ClientSpotProjection> getByClientCpf(String cpf, Pageable pageable) {
        clientService.findByCpf(cpf);
        return repository.findAllByClientCpf(cpf, pageable);
    }

}
