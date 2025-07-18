package com.playground.mscreditAnalizer.infra;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.core.Queue;

import lombok.RequiredArgsConstructor;

// publica o cartão na queue

@Component
@RequiredArgsConstructor
public class CardIssuingublisher {

    private final RabbitTemplate rabbitTemplate;
    private final Queue queueCardGeneration;

    public void publishCardGeneration(CardEmissionDto dto) {
        CardGeneration cardGeneration = new CardGeneration(cardNumber);
        rabbitTemplate.convertAndSend(queueCardGeneration.getName(), cardGeneration);
    }

    private String convertIntoJson(DadosSolicitacaoEmissaoCartao dados) throws JsonProcessingException {
        return new ObjectMapper().mapper.writeValueAsString(dados);
    }
}
