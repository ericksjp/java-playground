package com.app.park_api;

import java.util.Objects;
import java.util.function.Consumer;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.app.park_api.jwt.JwtToken;
import com.app.park_api.web.dto.UserLoginDTO;

public class JwtAuthentication {

    // helper function to get the jwt token in a authorization request
    public static Consumer<HttpHeaders> getHeaderAuthorization(WebTestClient client, String username, String password) {
        String token = Objects.requireNonNull(client
                .post()
                .uri("api/v1/auth")
                .bodyValue(new UserLoginDTO(username, password))
                .exchange()
                .expectStatus().isOk()
                .expectBody(JwtToken.class)
                .returnResult().getResponseBody()).getToken();

        return headers -> headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + token);
    }
}
