package com.playground.mscreditAnalizer.config;

import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MQConfig {

    @Value("${mq.queue.card-issuing-request}")
    private String cardIssuingQueue;

    @Bean
    public Queue cardIssuingQueue() {
        return new Queue(cardIssuingQueue, true);
    }
}
