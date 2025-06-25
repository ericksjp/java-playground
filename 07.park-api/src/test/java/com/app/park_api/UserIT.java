package com.app.park_api;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.app.park_api.web.dto.UserCreateDTO;
import com.app.park_api.web.dto.UserResponseDTO;
import com.app.park_api.web.dto.UserPasswordDTO;
import com.app.park_api.web.exception.ErrorMessage;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = "/sql/users/users-insert.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/sql/users/users-delete.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class UserIT {

    @Autowired
    WebTestClient testClient;

    @Test
    public void createUser_WithValidUsernameAndPassword_ReturnCreatedUserWith201Status() {
        UserResponseDTO responseBody = testClient
                .post()
                .uri("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UserCreateDTO("wild@mail.com", "123456"))
                .exchange()
                .expectStatus().isCreated()
                .expectBody(UserResponseDTO.class)
                .returnResult().getResponseBody();


        Assertions.assertThat(responseBody).isNotNull();
        Assertions.assertThat(responseBody.getId()).isNotNull();
        Assertions.assertThat(responseBody.getUsername()).isEqualTo("wild@mail.com");
        Assertions.assertThat(responseBody.getRole()).isEqualTo("CLIENT");
    }

    @Test
    public void createUser_WithInvalidUsername_ReturnErrorMessageWith422Status() {
        UserCreateDTO[] invalidUsers = {
                new UserCreateDTO("", "123456"),
                new UserCreateDTO("user@", "123456"),
                new UserCreateDTO("user@.com", "123456"),
                new UserCreateDTO("useruseruser", "123456"),
                new UserCreateDTO(null, "123456"),
        };

        for (UserCreateDTO user : invalidUsers) {
            ErrorMessage responseBody = testClient
                    .post()
                    .uri("/api/v1/users")
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(user)
                    .exchange()
                    .expectStatus().isEqualTo(422)
                    .expectBody(ErrorMessage.class)
                    .returnResult().getResponseBody();

            Assertions.assertThat(responseBody).isNotNull();
            Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);
        }
    }

    @Test
    public void createUser_WithInvalidPassword_ReturnErrorMessageWith422Status() {
        UserCreateDTO[] invalidUsers = {
                new UserCreateDTO("user@mail.com", ""),
                new UserCreateDTO("user@mail.com", "123"),
                new UserCreateDTO("user@mail.com", "1234567"),
                new UserCreateDTO("user@mail.com", null),
        };

        for (UserCreateDTO user : invalidUsers) {
            ErrorMessage responseBody = testClient
                    .post()
                    .uri("/api/v1/users")
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(user)
                    .exchange()
                    .expectStatus().isEqualTo(422)
                    .expectBody(ErrorMessage.class)
                    .returnResult().getResponseBody();

            Assertions.assertThat(responseBody).isNotNull();
            Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);
        }
    }

    @Test
    public void createUser_WithDuplicateUsername_ReturnErrorMessageWith409Status() {
        UserCreateDTO user = new UserCreateDTO("erick@mail.com", "123456");

        ErrorMessage responseBody = testClient
                .post()
                .uri("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(user)
                .exchange()
                .expectStatus().isEqualTo(409)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(responseBody).isNotNull();
        Assertions.assertThat(responseBody.getStatus()).isEqualTo(409);
    }

    @Test
    public void findUserById_WithRegisteredId_ReturnUserWithStatus200() {
        Long id = 100L;

        UserResponseDTO responseBody = testClient
                .get()
                .uri("/api/v1/users/" + id)
                .exchange()
                .expectStatus().isOk()
                .expectBody(UserResponseDTO.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(responseBody).isNotNull();
        Assertions.assertThat(responseBody.getId()).isEqualTo(id);
        Assertions.assertThat(responseBody.getUsername()).isEqualTo("erick@mail.com");
        Assertions.assertThat(responseBody.getRole()).isEqualTo("ADMIN");
    }

    @Test
    public void findUserById_WithNonRegisteredId_ReturnErrorMessageWithStatus404() {

        ErrorMessage responseBody = testClient
                .get()
                .uri("/api/v1/users/99")
                .exchange()
                .expectStatus().isEqualTo(404)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(responseBody).isNotNull();
        Assertions.assertThat(responseBody.getStatus()).isEqualTo(404);
    }

    @Test
    public void updatePassword_WithValidData_ReturnVoidWithStatus204() {
        Long id = 100L;
        UserPasswordDTO updateDTO = new UserPasswordDTO("123456", "101010", "101010");

        HttpStatusCode response = testClient
                .patch()
                .uri("/api/v1/users/" + id)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(updateDTO)
                .exchange()
                .expectStatus().isEqualTo(204)
                .expectBody(Void.class)
                .returnResult().getStatus();

        Assertions.assertThat(response.value()).isEqualTo(204);
    }

    @Test
    public void updatePassword_WithNonRegisteredId_ReturnErrorMessageWithStatus404() {
        Long id = 99L;
        UserPasswordDTO updateDTO = new UserPasswordDTO("123456", "101010", "101010");

        ErrorMessage responseBody = testClient
                .patch()
                .uri("/api/v1/users/" + id)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(updateDTO)
                .exchange()
                .expectStatus().isEqualTo(404)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(responseBody).isNotNull();
        Assertions.assertThat(responseBody.getStatus()).isEqualTo(404);
    }

    @Test
    public void updatePassword_WithPasswordMismatch_ReturnErrorMessageWithStatus400() {
        UserPasswordDTO[] data = {
            new UserPasswordDTO("123457", "101010", "101010"),
            new UserPasswordDTO("123456", "101010", "101011"),
        };

        for (UserPasswordDTO dto : data) {
            ErrorMessage responseBody = testClient
                    .patch()
                    .uri("/api/v1/users/101")
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(dto)
                    .exchange()
                    .expectStatus().isEqualTo(400)
                    .expectBody(ErrorMessage.class)
                    .returnResult().getResponseBody();

            Assertions.assertThat(responseBody).isNotNull();
            Assertions.assertThat(responseBody.getStatus()).isEqualTo(400);
        }
    }

    @Test
    public void updatePassword_WithInvalidData_ReturnErrorMessageWithStatus422() {
        UserPasswordDTO[] data = {
            new UserPasswordDTO("12345782", "101010", "101010"),
            new UserPasswordDTO("123456", "100", "100"),
            new UserPasswordDTO("123456", null, null),
            new UserPasswordDTO(null, null, null),
            new UserPasswordDTO("123456", "1001101", "1001101"),
        };

        for (UserPasswordDTO dto : data) {
            ErrorMessage responseBody = testClient
                    .patch()
                    .uri("/api/v1/users/101")
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(dto)
                    .exchange()
                    .expectStatus().isEqualTo(422)
                    .expectBody(ErrorMessage.class)
                    .returnResult().getResponseBody();

            Assertions.assertThat(responseBody).isNotNull();
            Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);
            
        }
    }

    @Test
    public void getUsers_WithNoParametes_ReturnUsersWith200Status() {
        UserResponseDTO[] users = {
            new UserResponseDTO(100L, "erick@mail.com", "ADMIN"),
            new UserResponseDTO(101L, "maria@mail.com", "CLIENT"),
            new UserResponseDTO(102L, "jorge@mail.com", "CLIENT"),
        };

        UserResponseDTO[] responseBody = testClient
                .get()
                .uri("/api/v1/users")
                .exchange()
                .expectStatus().isOk()
                .expectBody(UserResponseDTO[].class)
                .returnResult().getResponseBody();

        Assertions.assertThat(responseBody).isNotNull();
        Assertions.assertThat(responseBody).hasSize(users.length);

        for (int i = 0; i < users.length; i++) {
            UserResponseDTO response = responseBody[i];
            UserResponseDTO expected = users[i];

            Assertions.assertThat(response).isNotNull();
            Assertions.assertThat(response.getId()).isEqualTo(expected.getId());
            Assertions.assertThat(response.getUsername()).isEqualTo(expected.getUsername());
            Assertions.assertThat(response.getRole()).isEqualTo(expected.getRole());
        }
    }
}
