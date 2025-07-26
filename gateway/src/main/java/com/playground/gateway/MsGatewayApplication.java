package com.playground.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class MsGatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsGatewayApplication.class, args);
	}

    @Bean
    public RouteLocator routes(RouteLocatorBuilder builder) {
        // map all calls to /clients endpoint to the msclients service
        // utiliza o protocolo ld (Load Balancer) que permite balanceamento de
        // carga
        return builder
                .routes()
                    .route(r -> r.path("/clients/**").uri("lb://msclients"))
                    .route(r -> r.path("/cards/**").uri("lb://mscards"))
                    .route(r -> r.path("/credit-analizer/**").uri("lb://mscreditanalizer"))
                .build();
    }
}
