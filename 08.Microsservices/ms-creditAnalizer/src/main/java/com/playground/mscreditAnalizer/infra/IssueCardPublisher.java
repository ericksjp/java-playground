package com.playground.mscreditAnalizer.infra;

import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.playground.mscreditAnalizer.web.dto.CardIssueDto;

import lombok.RequiredArgsConstructor;

// send message to RabbitMQ queue for card issuing

@Component
@RequiredArgsConstructor
public class IssueCardPublisher {

    private final RabbitTemplate rabbitTemplate;
    private final Queue cardIssuingQueue;

    public void publishCardIssueRequest(CardIssueDto cardIssueDto) throws JsonProcessingException, AmqpException {
        String jsonMessage = convertToJson(cardIssueDto);
        rabbitTemplate.convertAndSend(cardIssuingQueue.getName(), jsonMessage);
    }

    private String convertToJson(CardIssueDto cardIssueDto) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(cardIssueDto);
    }
    
}
