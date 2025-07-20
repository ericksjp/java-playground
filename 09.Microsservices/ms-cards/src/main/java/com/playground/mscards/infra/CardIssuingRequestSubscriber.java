package com.playground.mscards.infra;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.playground.mscards.entity.Card;
import com.playground.mscards.entity.ClientCard;
import com.playground.mscards.repository.CardRepository;
import com.playground.mscards.repository.ClientCardRepository;
import com.playground.mscards.web.dto.CardIssueDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

// receive message from RabbitMQ queue and process card issuing requests

@Slf4j
@RequiredArgsConstructor
@Component
public class CardIssuingRequestSubscriber {

    private final CardRepository cardRepository;
    private final ClientCardRepository clientCardRepository;

    @RabbitListener(queues = "${mq.queue.card-issuing-request}")
    public void receiveCardIssueRequest(@Payload String paylod) throws JsonMappingException, JsonProcessingException {
        try {

            CardIssueDto dto = new ObjectMapper().readValue(paylod, CardIssueDto.class);
            Card card = cardRepository.findById(dto.getIdCard()).orElseThrow();

            ClientCard clientCard = new ClientCard();
            clientCard.setCard(card);
            clientCard.setCpf(dto.getCpf());
            clientCard.setCreditLimit(dto.getCreditLimit());

            clientCardRepository.save(clientCard);
        } catch (Exception e) {
            log.error("Error processing card issue request: {}", e.getMessage());
        }
    }
}
