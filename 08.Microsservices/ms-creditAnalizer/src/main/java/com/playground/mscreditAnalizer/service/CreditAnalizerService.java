package com.playground.mscreditAnalizer.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.playground.mscreditAnalizer.client.CardResourceClient;
import com.playground.mscreditAnalizer.client.ClientResourceClient;
import com.playground.mscreditAnalizer.domain.ClientStatus;
import com.playground.mscreditAnalizer.exception.CommunicationException;
import com.playground.mscreditAnalizer.exception.ResourceNotFoundException;
import com.playground.mscreditAnalizer.web.dto.ApprovedCardDto;
import com.playground.mscreditAnalizer.web.dto.CardDto;
import com.playground.mscreditAnalizer.web.dto.ClientCardDto;
import com.playground.mscreditAnalizer.web.dto.ClientDataDto;

import feign.FeignException.FeignClientException;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class CreditAnalizerService {
    private final ClientResourceClient clientResourceClient;
    private final CardResourceClient cardResourceClient;

    public ClientStatus getClientStatus(String cpf) {

        try {
            ResponseEntity<ClientDataDto> clientData = clientResourceClient.findByCpf(cpf);
            ResponseEntity<List<ClientCardDto>> cards = cardResourceClient.findByCpf(cpf);

            return ClientStatus.builder()
                    .clientData(clientData.getBody())
                    .clientCards(cards.getBody())
                    .build();

        } catch (FeignClientException e) {
            if (e.status() == 404) {
                throw new ResourceNotFoundException(String.format("Client with CPF %s not found", cpf));
            }

            throw new CommunicationException(e.status(), e.getMessage());
        }
    }

    public List<ApprovedCardDto> avaliateClient(String cpf, Double renda) {

        try {
            ClientDataDto client = clientResourceClient.findByCpf(cpf).getBody();
            List<CardDto> cards = cardResourceClient.findByIncomeGreatherThan(renda).getBody(); 

            List<ApprovedCardDto> approvedCards = cards.stream().map(card -> {
                Double x = client.getAge() / 10.0;
                Double approvedLimit = card.getCreditLimit() * x;

                return new ApprovedCardDto(card.getName(), card.getBrand(), approvedLimit);

            }).collect(Collectors.toList());

            return approvedCards; 
        } catch (FeignClientException e) {
            if (e.status() == 404) {
                throw new ResourceNotFoundException(String.format("Client with CPF %s not found", cpf));
            }

            throw new CommunicationException(e.status(), e.getMessage());
        }

    }
}
