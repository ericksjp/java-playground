package com.playground.mscards.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.playground.mscards.entity.ClientCard;
import com.playground.mscards.repository.ClientCardRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ClientCartService {

    private final ClientCardRepository clientCartRepository;

    List<ClientCard> getCardsByCpf(String cpf) {
        return clientCartRepository.findByCpf(cpf);
    }
    
}
