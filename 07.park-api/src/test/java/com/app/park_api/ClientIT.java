package com.app.park_api;

import java.util.HashMap;
import java.util.Map;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.app.park_api.web.dto.ClientCreateDTO;
import com.app.park_api.web.dto.ClientResponseDTO;
import com.app.park_api.web.dto.PageableDTO;
import com.app.park_api.web.exception.ErrorMessage;

import reactor.core.publisher.Flux;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = "/sql/clients/clients-insert.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/sql/clients/clients-delete.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class ClientIT {

    @Autowired
    WebTestClient testClient;

    private record User(Long id, String email, String password, String role) {}

    private static Map<String, User> users = new HashMap<>();

    public ClientIT() {
        users.put("erick", new User(100L, "erick@mail.com", "123456", "ADMIN"));
        users.put("maria", new User(101L, "maria@mail.com", "123456", "CLIENT"));
        users.put("jorge", new User(102L, "jorge@mail.com", "123456", "CLIENT"));
        users.put("bob", new User(102L, "bob@mail.com", "123456", "CLIENT"));
    }

    /* ----- Test to create client ----- */

    @Test
    public void createClient_WithValidData_ReturnClientWith201Status() {
        String email = users.get("bob").email;
        String password = users.get("bob").password;

        ClientCreateDTO clientCreateDTO = new ClientCreateDTO("bob", "92197598023");

        ClientResponseDTO responseBody = testClient
                .post()
                .uri("/api/v1/clients")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, email, password))
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(clientCreateDTO)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(ClientResponseDTO.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(responseBody).isNotNull();
        Assertions.assertThat(responseBody.getCpf()).isEqualTo(clientCreateDTO.getCpf());
        Assertions.assertThat(responseBody.getName()).isEqualTo(clientCreateDTO.getName());
    }

    @Test
    public void createClient_WithoutAuthenticationToken_ReturnErrorMessageWith401Status() {
        var response = testClient
                .post()
                .uri("/api/v1/clients")
                .contentType(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isEqualTo(401)
                .returnResult(Void.class);

        HttpHeaders headers = response.getResponseHeaders();
        Assertions.assertThat(headers).isNotNull();
        Assertions.assertThat(headers.getFirst("www-authenticate")).isEqualTo("Bearer realm=/api/v1/auth");

        Assertions.assertThat(response.getResponseBody()).isInstanceOf(Flux.class);
        Assertions.assertThat(response.getResponseBody().hasElements().block()).isFalse();
    }

    @Test
    public void createClient_WithAdminUser_ReturnErrorMessageWith403Status() {
        String email = users.get("erick").email;
        String password = users.get("erick").password;

        ClientCreateDTO clientCreateDTO = new ClientCreateDTO("erick", "25924624064");

        testClient
                .post()
                .uri("/api/v1/clients")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, email, password))
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(clientCreateDTO)
                .exchange()
                .expectStatus().isEqualTo(403)
                .expectBody(ErrorMessage.class);
    }

    @Test
    public void createClient_WithDuplicateCPF_ReturnErrorMessageWith409Status() {
        String email = users.get("maria").email;
        String password = users.get("maria").password;

        ClientCreateDTO clientCreateDTO = new ClientCreateDTO("maria", "53065874024");

        testClient
                .post()
                .uri("/api/v1/clients")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, email, password))
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(clientCreateDTO)
                .exchange()
                .expectStatus().isEqualTo(409)
                .expectBody(ErrorMessage.class);
    }

    @Test
    public void createClient_WithInvalidData_ReturnErrorMessageWith422Status() {
        String email = users.get("maria").email;
        String password = users.get("maria").password;

        ClientCreateDTO[] dtos = {
                new ClientCreateDTO("", "53065874024"),
                new ClientCreateDTO("maria", ""),
                new ClientCreateDTO(null, null),
                new ClientCreateDTO("rithmy", "530658a4e24"),
                new ClientCreateDTO("ragatanga", "10000874099"),
                new ClientCreateDTO("ragatanga", "174099"),
                new ClientCreateDTO("ragatanga", "17409909128421"),
        };

        for (ClientCreateDTO client : dtos) {
            testClient
                    .post()
                    .uri("/api/v1/clients")
                    .headers(JwtAuthentication.getHeaderAuthorization(testClient, email, password))
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(client)
                    .exchange()
                    .expectStatus().isEqualTo(422)
                    .expectBody(ErrorMessage.class);
        }
    }

    /* ----- Test find client by ID ----- */

    @Test
    public void getClientById_WithAdminUser_ReturnClientWith200Status() {
        String email = users.get("erick").email;
        String password = users.get("erick").password;

        Long clientId = 102L;

        ClientResponseDTO responseBody = testClient
                .get()
                .uri("/api/v1/clients/" + clientId)
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, email, password))
                .exchange()
                .expectStatus().isOk()
                .expectBody(ClientResponseDTO.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(responseBody).isNotNull();
        Assertions.assertThat(responseBody.getId()).isEqualTo(clientId);
        Assertions.assertThat(responseBody.getName()).isNotEmpty();
        Assertions.assertThat(responseBody.getCpf()).isNotEmpty();
    }

    @Test
    public void getClientById_WithoutAuthenticationToken_ReturnErrorMessageWith401Status() {
        var response = testClient
                .get()
                .uri("/api/v1/clients/100")
                .exchange()
                .expectStatus().isEqualTo(401)
                .returnResult(Void.class);

        HttpHeaders headers = response.getResponseHeaders();
        Assertions.assertThat(headers).isNotNull();
        Assertions.assertThat(headers.getFirst("www-authenticate")).isEqualTo("Bearer realm=/api/v1/auth");

        Assertions.assertThat(response.getResponseBody()).isInstanceOf(Flux.class);
        Assertions.assertThat(response.getResponseBody().hasElements().block()).isFalse();
    }

    @Test
    public void getClientById_WithClientRole_ReturnErrorMessageWith403Status() {
        String email = users.get("maria").email;
        String password = users.get("maria").password;

        testClient
                .get()
                .uri("/api/v1/clients/" + users.get("jorge").id)
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, email, password))
                .exchange()
                .expectStatus().isEqualTo(403)
                .expectBody(ErrorMessage.class);
    }

    @Test
    public void getClientById_WithAdminUser_ClientNotFound_ReturnErrorMessageWith404Status() {
        String email = users.get("erick").email;
        String password = users.get("erick").password;

        testClient
                .get()
                .uri("/api/v1/clients/999")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, email, password))
                .exchange()
                .expectStatus().isEqualTo(404)
                .expectBody(ErrorMessage.class);
    }

    /* ----- Test find all clients ----- */

    @Test
    public void getAllClients_WithAdminUser_ReturnClientsWith200Status() {
        String email = users.get("erick").email; // Admin user
        String password = users.get("erick").password;

        PageableDTO responseBody = testClient
                .get()
                .uri("/api/v1/clients")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, email, password))
                .exchange()
                .expectStatus().isOk()
                .expectBody(PageableDTO.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(responseBody).isNotNull();
        Assertions.assertThat(responseBody.getContent()).isNotEmpty();
        Assertions.assertThat(responseBody.getTotalElements()).isGreaterThan(0);
    }

    @Test
    public void getAllClients_WithoutAuthenticationToken_ReturnErrorMessageWith401Status() {
        var response = testClient
                .get()
                .uri("/api/v1/clients")
                .exchange()
                .expectStatus().isEqualTo(401)
                .returnResult(Void.class);

        HttpHeaders headers = response.getResponseHeaders();
        Assertions.assertThat(headers).isNotNull();
        Assertions.assertThat(headers.getFirst("www-authenticate")).isEqualTo("Bearer realm=/api/v1/auth");

        Assertions.assertThat(response.getResponseBody()).isInstanceOf(Flux.class);
        Assertions.assertThat(response.getResponseBody().hasElements().block()).isFalse();
    }

    @Test
    public void getAllClients_WithClientRole_ReturnErrorMessageWith403Status() {
        String email = users.get("maria").email;
        String password = users.get("maria").password;

        testClient
                .get()
                .uri("/api/v1/clients")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, email, password))
                .exchange()
                .expectStatus().isEqualTo(403)
                .expectBody(ErrorMessage.class);
    }
}
