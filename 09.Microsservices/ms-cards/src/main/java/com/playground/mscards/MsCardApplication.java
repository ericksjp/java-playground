package com.playground.mscards;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableRabbit
public class MsCardApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsCardApplication.class, args);
	}
}
