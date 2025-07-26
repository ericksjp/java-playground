package com.app.park_api;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.app.park_api.jwt.JwtToken;
import com.app.park_api.web.dto.UserLoginDTO;
import com.app.park_api.web.exception.ErrorMessage;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = "/sql/users/users-insert.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/sql/users/users-delete.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class AuthIT {

    @Autowired
    WebTestClient testClient;

    @Test
    public void authenticateUser_WithValidCredentials_ReturnTokenWith200Status() {

        UserLoginDTO userLogin = new UserLoginDTO("erick@mail.com", "123456");

        JwtToken responseBody = testClient
                .post()
                .uri("/api/v1/auth")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(userLogin)
                .exchange()
                .expectStatus().isOk()
                .expectBody(JwtToken.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(responseBody).isNotNull();
        Assertions.assertThat(responseBody.getToken()).isNotBlank();
    }

    @Test
    public void authenticateUser_WithInvalidCredentials_ReturnErrorWith400Status() {
        UserLoginDTO[] users = {
            new UserLoginDTO("erick@mail.com", "121212"),
            new UserLoginDTO("erick@gmail.com", "123456"),
            new UserLoginDTO("erick@outlook.com", "101010")
        };

        for (UserLoginDTO userLogin : users) {
            ErrorMessage error = testClient
                    .post()
                    .uri("/api/v1/auth")
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(userLogin)
                    .exchange()
                    .expectStatus().isEqualTo(400)
                    .expectBody(ErrorMessage.class)
                    .returnResult().getResponseBody();

            Assertions.assertThat(error).isNotNull();
            Assertions.assertThat(error.getMessage()).isNotNull();
            Assertions.assertThat(error.getMessage()).contains("Invalid credentials");
        }
    }

    @Test
    public void authenticateUser_WithInvalidInputData_ReturnErrorWith422Status() {
        UserLoginDTO[] users = {
            new UserLoginDTO(null, "123456"),
            new UserLoginDTO("erick@mail.com", null),
            new UserLoginDTO("lsfjddsfjslk", "123456"),
            new UserLoginDTO("erick@mail.com", "1234567")
        };

        for (UserLoginDTO userLogin : users) {
            ErrorMessage error = testClient
                .post()
                .uri("/api/v1/auth")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(userLogin)
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

            Assertions.assertThat(error).isNotNull();
            Assertions.assertThat(error.getMessage()).isNotNull();
            Assertions.assertThat(error.getMessage()).contains("invalid fields");
        }
    }
}
