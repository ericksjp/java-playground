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

import com.app.park_api.web.dto.SpotCreateDTO;
import com.app.park_api.web.dto.SpotResponseDTO;
import com.app.park_api.web.exception.ErrorMessage;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = "/sql/spots/spots-insert.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/sql/spots/spots-delete.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class SpotIT {

    private record User(Long id, String email, String password) {}
    private static Map<String, User> users = new HashMap<>();

    public SpotIT() {
        users.put("admin", new User(100L, "erick@mail.com", "123456"));
        users.put("client1", new User(101L, "maria@mail.com", "123456"));
        users.put("client2", new User(102L, "jorge@mail.com", "123456"));
    }

    @Autowired
    WebTestClient testClient;

    /* ----- Test to create parking spot ----- */

    @Test
    public void createSpot_WithValidData_ReturnCreatedSpotWith201Status() {
        var response = testClient
                .post()
                .uri("/api/v1/spots")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, users.get("admin").email, users.get("admin").password))
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new SpotCreateDTO("A120", "FREE"))
                .exchange()
                .expectStatus().isCreated()
                .returnResult(Void.class);

        HttpHeaders headers = response.getResponseHeaders();
        Assertions.assertThat(headers.getLocation()).isNotNull();
        Assertions.assertThat(headers.getLocation().getPath()).isEqualTo("/api/v1/spots/2");
    }

    @Test
    public void createSpot_WithDuplicateCode_ReturnErrorMessageWith409Status() {
        testClient
                .post()
                .uri("/api/v1/spots")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, users.get("admin").email, users.get("admin").password))
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new SpotCreateDTO("0002", "FREE"))
                .exchange()
                .expectStatus().isEqualTo(409)
                .expectBody(ErrorMessage.class);
    }

    @Test
    public void createSpot_WithInvalidData_ReturnErrorMessageWith422Status() {
        SpotCreateDTO[] invalidSpots = {
                new SpotCreateDTO("", "FEEE"),
                new SpotCreateDTO(null, "FREE"),
                new SpotCreateDTO("A1", ""),
                new SpotCreateDTO("A1", null),
        };

        for (SpotCreateDTO spot : invalidSpots) {
            ErrorMessage responseBody = testClient
                    .post()
                    .uri("/api/v1/spots")
                    .headers(JwtAuthentication.getHeaderAuthorization(testClient, users.get("admin").email, users.get("admin").password))
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(spot)
                    .exchange()
                    .expectStatus().isEqualTo(422)
                    .expectBody(ErrorMessage.class)
                    .returnResult().getResponseBody();

            Assertions.assertThat(responseBody).isNotNull();
            Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);
        }
    }

    @Test
    public void createSpot_WithoutAuthenticationToken_ReturnErrorMessageWith401Status() {
        var response = testClient
                .post()
                .uri("/api/v1/spots")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new SpotCreateDTO("A100", "FREE"))
                .exchange()
                .expectStatus().isEqualTo(401)
                .returnResult(Void.class);

        HttpHeaders headers = response.getResponseHeaders();
        Assertions.assertThat(headers.getFirst("www-authenticate")).isEqualTo("Bearer realm=/api/v1/auth");
    }

    @Test
    public void createSpot_WithClientUser_ReturnErrorMessageWith403Status() {
        testClient
            .post()
            .uri("/api/v1/spots")
            .headers(JwtAuthentication.getHeaderAuthorization(testClient, users.get("client1").email, users.get("client1").password))
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(new SpotCreateDTO("A100", "FREE"))
            .exchange()
            .expectStatus().isEqualTo(403)
            .expectBody(ErrorMessage.class);
    }

    /* ----- Test to retrieve parking spot by code ----- */

    @Test
    public void findSpotByCode_WithAuthenticationToken_ReturnSpotWithStatus200() {
        SpotResponseDTO responseBody = testClient
                .get()
                .uri("/api/v1/spots/0001")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, users.get("admin").email, users.get("admin").password))
                .exchange()
                .expectStatus().isOk()
                .expectBody(SpotResponseDTO.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(responseBody).isNotNull();
        Assertions.assertThat(responseBody.getCode()).isEqualTo("0001");
    }

    @Test
    public void findSpotByCode_WithoutAuthenticationToken_ReturnErrorMessageWith401Status() {
        var response = testClient
                .get()
                .uri("/api/v1/spots/A1")
                .exchange()
                .expectStatus().isEqualTo(401)
                .returnResult(Void.class);

        HttpHeaders headers = response.getResponseHeaders();
        Assertions.assertThat(headers.getFirst("www-authenticate")).isEqualTo("Bearer realm=/api/v1/auth");
    }

    @Test
    public void findSpotByCode_WithClientUser_ReturnErrorMessageWith403Status() {
        testClient
            .get()
            .uri("/api/v1/spots/A1")
            .headers(JwtAuthentication.getHeaderAuthorization(testClient, users.get("client1").email, users.get("client1").password))
            .exchange()
            .expectStatus().isEqualTo(403)
            .expectBody(ErrorMessage.class);
    }

    @Test
    public void findSpotByCode_WithNonExistentCode_ReturnErrorMessageWith404Status() {
        testClient
                .get()
                .uri("/api/v1/spots/1234")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, users.get("admin").email, users.get("admin").password))
                .exchange()
                .expectStatus().isEqualTo(404)
                .expectBody(ErrorMessage.class);
    }
}
