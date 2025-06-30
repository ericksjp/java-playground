package com.app.park_api;

import java.util.HashMap;
import java.util.Map;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.app.park_api.web.dto.UserCreateDTO;
import com.app.park_api.web.dto.UserPasswordDTO;
import com.app.park_api.web.dto.UserResponseDTO;
import com.app.park_api.web.exception.ErrorMessage;

import reactor.core.publisher.Flux;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = "/sql/users/users-insert.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/sql/users/users-delete.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class UserIT {

    private record User(Long id, String email, String password, String role) {}
    private static Map<String, User> users = new HashMap<>();

    public UserIT() {
        users.put("erick", new User(100L, "erick@mail.com", "123456", "ADMIN"));
        users.put("maria", new User(101L, "maria@mail.com", "123456", "CLIENT"));
        users.put("jorge", new User(102L, "jorge@mail.com", "123456", "CLIENT"));
    }

    @Autowired
    WebTestClient testClient;

    /*  ----- Test to create user ----- */

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

    /*  ----- Test to find user by id ----- */
    
    @Test
    public void findUserById_WithAuthenticationToken_ReturnUserWithStatus200() {
        for (User u : users.values()) {
            UserResponseDTO responseBody = testClient
                    .get()
                    .uri("/api/v1/users/" + u.id)
                    .headers(JwtAuthentication.getHeaderAuthorization(testClient, u.email, "123456")) // admin
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody(UserResponseDTO.class)
                    .returnResult().getResponseBody();

            Assertions.assertThat(responseBody).isNotNull();
            Assertions.assertThat(responseBody.getId()).isEqualTo(u.id);
            Assertions.assertThat(responseBody.getUsername()).isEqualTo(u.email);
            Assertions.assertThat(responseBody.getRole()).isEqualTo(u.role);
        }
    }

    @Test
    public void findUserById_WithoutAuthenticationToken_ReturnErrorMessageWithStatus401() {
            var response = testClient
                .patch()
                .uri("/api/v1/users/" + users.get("jorge").id)
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
    public void findUserById_WhenAccessingOtherUser_ReturnsErrorWithStatus403() {
        ErrorMessage responseBody = testClient
                .get()
                .uri("/api/v1/users/" + users.get("jorge").id)
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, users.get("maria").email, "123456"))
                .exchange()
                .expectStatus().isEqualTo(403)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(responseBody).isNotNull();
        Assertions.assertThat(responseBody.getStatus()).isEqualTo(403);
    }

    /*  ----- Test to update user password ----- */

    @Test
    public void updatePassword_WithValidData_ReturnVoidWithStatus204() {
        UserPasswordDTO updateDTO = new UserPasswordDTO("123456", "101010", "101010");

        HttpStatusCode response = testClient
                .patch()
                .uri("/api/v1/users/" + users.get("erick").id)
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, users.get("erick").email, "123456"))
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(updateDTO)
                .exchange()
                .expectStatus().isEqualTo(204)
                .expectBody(Void.class)
                .returnResult().getStatus();

        Assertions.assertThat(response.value()).isEqualTo(204);
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
                    .uri("/api/v1/users/" + users.get("erick").id)
                    .headers(JwtAuthentication.getHeaderAuthorization(testClient, users.get("erick").email, "123456"))
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
    public void updatePassword_WithoutAuthenticationToken_ReturnErrorMessageWithStatus401() {
        UserPasswordDTO updateDTO = new UserPasswordDTO("123456", "101010", "101010");

        var response = testClient
                .patch()
                .uri("/api/v1/users/" + users.get("jorge").id)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(updateDTO)
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
    public void updatePassword_WhenAccessingOtherUser_ReturnErrorMessageWithStatus403() {
        UserPasswordDTO updateDTO = new UserPasswordDTO("123456", "101010", "101010");

        ErrorMessage responseBody = testClient
                .patch()
                .uri("/api/v1/users/" + users.get("jorge").id)
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, users.get("erick").email, "123456"))
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(updateDTO)
                .exchange()
                .expectStatus().isEqualTo(403)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(responseBody).isNotNull();
        Assertions.assertThat(responseBody.getStatus()).isEqualTo(403);
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
                    .uri("/api/v1/users/" + users.get("erick").id)
                    .headers(JwtAuthentication.getHeaderAuthorization(testClient, users.get("erick").email, "123456"))
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

    /*  ----- Test to get all users ----- */

    @Test
    public void getUsers_WithAdminUser_ReturnUsersWithStatus200() {
        UserResponseDTO[] responseBody = testClient
                .get()
                .uri("/api/v1/users")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, users.get("erick").email, users.get("erick").password))
                .exchange()
                .expectStatus().isOk()
                .expectBody(UserResponseDTO[].class)
                .returnResult().getResponseBody();

        Assertions.assertThat(responseBody).isNotNull();
        Assertions.assertThat(responseBody).hasSize(users.size());
    }

    @Test
    public void getUsers_WithoutAuthenticationToken_ReturnErrorMessageWithStatus401() {
        var response = testClient
                .patch()
                .uri("/api/v1/users" + users.get("jorge").id)
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
    public void getUsers_WithClientUser_ReturnErrorMessageWithStatus403() {
        ErrorMessage response = testClient
                .get()
                .uri("/api/v1/users")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, users.get("maria").email, users.get("maria").password))
                .exchange()
                .expectStatus().isEqualTo(403)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStatus()).isEqualTo(403);
    }
}
