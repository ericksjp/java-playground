package com.app.park_api;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.app.park_api.web.dto.ClientResponseDTO;
import com.app.park_api.web.dto.PageableDTO;
import com.app.park_api.web.dto.ParkingCreateDTO;
import com.app.park_api.web.dto.ParkingResponseDTO;
import com.app.park_api.web.dto.SpotResponseDTO;
import com.app.park_api.web.exception.ErrorMessage;

import reactor.core.publisher.Flux;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = "/sql/parking-spots/parking_spots-insert.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/sql/parking-spots/parking_spots-delete.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class ParkingSpotIT {

    private record User(Long id, String email,String name, String password, String cpf, int visits) {}

    private Map<String, User> users = new HashMap<>();
    private Integer freeSpots = 2;

    public ParkingSpotIT() {
        users.put("admin", new User(100L, "erick@mail.com", null, "123456", null, 0));
        users.put("client1", new User(101L, "maria@mail.com","Maria Maria", "123456", "85015292066", 1));
        users.put("client2", new User(102L, "jorge@mail.com","Jorge Jorge", "123456", "23168131008", 1));
    }

    @Autowired
    WebTestClient testClient;

    /* ----- Check-In tests ----- */

    @Test
    public void checkIn_WithValidData_ReturnParkingResponseWith201Status() {
        User admin = users.get("admin");
        ParkingCreateDTO dto =  new ParkingCreateDTO("ABC-1234", "FordKa", "Ford", "Azul", users.get("client1").cpf);

        var response = testClient
                .post()
                .uri("/api/v1/parkings/check-in")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, admin.email, admin.password))
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(dto)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(ParkingResponseDTO.class)
                .returnResult();

        HttpHeaders headers = response.getResponseHeaders();
        ParkingResponseDTO responseBody = response.getResponseBody();

        Assertions.assertThat(responseBody.getClientCpf()).isEqualTo(users.get("client1").cpf);

        Assertions.assertThat(headers.getLocation()).isNotNull();
        Assertions.assertThat(headers.getLocation().getPath()).isEqualTo("/api/v1/parkings/" + responseBody.getReceipt());

        var spotResponse = testClient
                .get()
                .uri("/api/v1/spots/" + responseBody.getSpotCode())
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, admin.email, admin.password))
                .exchange()
                .expectStatus().isOk()
                .expectBody(SpotResponseDTO.class)
                .returnResult().getResponseBody();

        assert spotResponse != null;
        Assertions.assertThat(spotResponse.getStatus()).isEqualTo("OCCUPIED");
    }

    @Test
    public void checkIn_WithMalformedRequest_ReturnErrorMessageWith400Status() {
        User admin = users.get("admin");
        // Test with an empty body
        testClient
            .post()
            .uri("/api/v1/parkings/check-in")
            .headers(JwtAuthentication.getHeaderAuthorization(testClient, admin.email, admin.password))
            .contentType(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isEqualTo(400)
            .expectBody(ErrorMessage.class);
    }

    @Test
    public void checkIn_WithoutAuthenticationToken_ReturnErrorMessageWith401Status() {
        var response = testClient
                .get()
                .uri("/api/v1/parkings/check-in")
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
    public void checkIn_WithClientRole_ReturnErrorMessageWith403Status() {
        User client = users.get("client1");
        ParkingCreateDTO dto =  new ParkingCreateDTO("ABC-1234", "FordKa", "Ford", "Azul", users.get("client1").cpf); // Non-existent CPF

        testClient
            .post()
            .uri("/api/v1/parkings/check-in")
            .headers(JwtAuthentication.getHeaderAuthorization(testClient, client.email, client.password))
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(dto)
            .exchange()
            .expectStatus().isEqualTo(403)
            .expectBody(ErrorMessage.class);
    }

    @Test
    public void checkIn_WithUnavailableClient_ReturnErrorMessageWith404Status() {
        User admin = users.get("admin");
        ParkingCreateDTO dto =  new ParkingCreateDTO("ABC-1234", "FordKa", "Ford", "Azul", "78988828062"); // Non-existent CPF

        testClient
            .post()
            .uri("/api/v1/parkings/check-in")
            .headers(JwtAuthentication.getHeaderAuthorization(testClient, admin.email, admin.password))
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(dto)
            .exchange()
            .expectStatus().isNotFound()
            .expectBody(ErrorMessage.class);
    }

    @Test
    public void checkIn_WithFullParking_ReturnErrorMessageWith404Status() throws InterruptedException {
        User admin = users.get("admin");
        ParkingCreateDTO dto =  new ParkingCreateDTO("ABC-1234", "FordKa", "Ford", "Azul", users.get("client1").cpf);

        Consumer<HttpHeaders> headers = JwtAuthentication.getHeaderAuthorization(testClient, admin.email, admin.password);

        for (int i = 0; i < freeSpots; i++) {
            Thread.sleep(Duration.ofSeconds(1));
            testClient
                .post()
                .uri("/api/v1/parkings/check-in")
                .headers(headers)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(dto)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(ParkingResponseDTO.class);
        }

        // try to check in one more vehicle
        testClient
            .post()
            .uri("/api/v1/parkings/check-in")
            .headers(headers)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(dto)
            .exchange()
            .expectStatus().isNotFound()
            .expectBody(ErrorMessage.class);
    }

    @Test
    public void checkIn_WithInvalidData_ReturnErrorMessageWith422Status() {
        ParkingCreateDTO[] dtos = {
            new ParkingCreateDTO(null, "FordKa", "Ford", "Azul", users.get("client1").cpf), // null plate
            new ParkingCreateDTO("ABC-1234", null, "Ford", "Azul", users.get("client1").cpf), // null model
            new ParkingCreateDTO("ABC-1234", "FordKa", null, "Azul", users.get("client1").cpf), // null brand
            new ParkingCreateDTO("ABC-1234", "FordKa", "Ford", null, users.get("client1").cpf), // null color
            new ParkingCreateDTO("A4asdf", "FordKa", "Ford", "Azul", users.get("client1").cpf), // invalid plate
            new ParkingCreateDTO("ABC-1234", "FordKa", "Ford", null, "ksaljfsfaskld"), // invalid cpf
        };

        for (ParkingCreateDTO dto : dtos) {
            testClient
                    .post()
                    .uri("/api/v1/parkings/check-in")
                    .headers(JwtAuthentication.getHeaderAuthorization(testClient, users.get("admin").email, users.get("admin").password))
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(dto)
                    .exchange()
                    .expectStatus().isEqualTo(422)
                    .expectBody(ErrorMessage.class);
        }
    }

    /* ----- Check-Out tests ----- */

    @Test
    public void checkOut_WithValidReceipt_ReturnParkingResponseWith200Status() {
        User admin = users.get("admin");
        String receipt = "20230313-101300";

        ParkingResponseDTO responseDTO = testClient
            .put()
            .uri("/api/v1/parkings/check-out/{receipt}", receipt)
            .headers(JwtAuthentication.getHeaderAuthorization(testClient, admin.email, admin.password))
            .exchange()
            .expectStatus().isOk()
            .expectBody(ParkingResponseDTO.class)
            .returnResult().getResponseBody();

        assert responseDTO != null;
        Assertions.assertThat(responseDTO.getCheckIn()).isEqualTo("2024-03-13T10:15");

        Assertions.assertThat(responseDTO.getReceipt()).isEqualTo(receipt);
        Assertions.assertThat(responseDTO.getLicensePlate()).isEqualTo("FIT-1010");
        Assertions.assertThat(responseDTO.getBrand()).isEqualTo("FIAT");
        Assertions.assertThat(responseDTO.getModel()).isEqualTo("PALIO");
        Assertions.assertThat(responseDTO.getColor()).isEqualTo("VERDE");
        Assertions.assertThat(responseDTO.getSpotCode()).isEqualTo("0001");
        Assertions.assertThat(responseDTO.getClientCpf()).isEqualTo("23168131008");

        Assertions.assertThat(responseDTO.getPrice()).isNotNull();
        Assertions.assertThat(responseDTO.getDiscount()).isNotNull();
        Assertions.assertThat(responseDTO.getDiscount()).isGreaterThan(BigDecimal.ZERO);
        Assertions.assertThat(responseDTO.getCheckOut()).isNotNull();

        testClient.get().uri("/api/v1/spots/0001")
            .headers(JwtAuthentication.getHeaderAuthorization(testClient, admin.email, admin.password))
            .exchange()
            .expectStatus().isOk()
            .expectBody(SpotResponseDTO.class)
            .consumeWith(response -> {
                SpotResponseDTO spotResponse = response.getResponseBody();
                assert spotResponse != null;
                Assertions.assertThat(spotResponse.getStatus()).isEqualTo("FREE");
            });

        ClientResponseDTO userResponse = testClient.get()
            .uri("/api/v1/clients/" + users.get("client2").id)
            .headers(JwtAuthentication.getHeaderAuthorization(testClient, admin.email, admin.password))
            .exchange()
            .expectStatus().isOk()
            .expectBody(ClientResponseDTO.class)
            .returnResult().getResponseBody();

        assert userResponse != null;

        Assertions.assertThat(userResponse.getVisits()).isEqualTo(11);
    }

    @Test
    public void checkOut_WithInvalidReceipt_ReturnErrorMessageWith404Status() {
        User admin = users.get("admin");

        testClient
            .put()
            .uri("/api/v1/parkings/check-out/invalid-receipt")
            .headers(JwtAuthentication.getHeaderAuthorization(testClient, admin.email, admin.password))
            .exchange()
            .expectStatus().isNotFound()
            .expectBody(ErrorMessage.class);
    }

    @Test
    public void checkOut_WithoutAuthenticationToken_ReturnErrorMessageWith401Status() {
        testClient
            .put()
            .uri("/api/v1/parkings/check-otout/receipt")
            .exchange()
            .expectStatus().isUnauthorized()
            .expectBody(ErrorMessage.class);
    }

    @Test
    public void checkOut_WithClientRole_ReturnErrorMessageWith403Status() {
        User client = users.get("client1");

        testClient
            .put()
            .uri("/api/v1/parkings/check-out/client")
            .headers(JwtAuthentication.getHeaderAuthorization(testClient, client.email, client.password))
            .exchange()
            .expectStatus().isEqualTo(403)
            .expectBody(ErrorMessage.class);
    }

    /* ----- Get Parking Spots by Client CPF tests ----- */

    @Test
    public void getByClientCPF_WithValidCPF_ReturnPageableDTOWith200Status() {
        User admin = users.get("admin");
        String cpf = users.get("client1").cpf;

        testClient
            .get()
            .uri("/api/v1/parkings/cpf/{cpf}", cpf)
            .headers(JwtAuthentication.getHeaderAuthorization(testClient, admin.email, admin.password))
            .exchange()
            .expectStatus().isOk()
            .expectBody(PageableDTO.class)
            .consumeWith(response -> {
                PageableDTO responseBody = response.getResponseBody();
                Assertions.assertThat(responseBody).isNotNull();
                Assertions.assertThat(responseBody.getContent()).isNotEmpty();
            });
    }

    @Test
    public void getByClientCPF_WithInvalidCPF_ReturnErrorMessageWith404Status() {
        User admin = users.get("admin");
        String cpf = "invalid-cpf";

        testClient
            .get()
            .uri("/api/v1/parkings/cpf/{cpf}", cpf)
            .headers(JwtAuthentication.getHeaderAuthorization(testClient, admin.email, admin.password))
            .exchange()
            .expectStatus().isNotFound()
            .expectBody(ErrorMessage.class);
    }

    @Test
    public void getByClientCPF_WithoutAuthenticationToken_ReturnErrorMessageWith401Status() {
        String cpf = users.get("client1").cpf;

        testClient
            .get()
            .uri("/api/v1/parkings/cpf/{cpf}", cpf)
            .exchange()
            .expectStatus().isUnauthorized()
            .expectBody(ErrorMessage.class);
    }

    /* ----- Get All Parking Spots tests ----- */

    @Test
    public void getAll_WithAdminRole_ReturnPageableDTOWith200Status() {
        User admin = users.get("admin");

        testClient
            .get()
            .uri("/api/v1/parkings")
            .headers(JwtAuthentication.getHeaderAuthorization(testClient, admin.email, admin.password))
            .exchange()
            .expectStatus().isOk()
            .expectBody(PageableDTO.class)
            .consumeWith(response -> {
                PageableDTO responseBody = response.getResponseBody();
                Assertions.assertThat(responseBody).isNotNull();
                Assertions.assertThat(responseBody.getContent()).isNotEmpty();
            });
    }

    @Test
    public void getAll_WithClientRole_ReturnPageableDTOWith200Status() {
        User client = users.get("client1");

        testClient
            .get()
            .uri("/api/v1/parkings")
            .headers(JwtAuthentication.getHeaderAuthorization(testClient, client.email, client.password))
            .exchange()
            .expectStatus().isOk()
            .expectBody(PageableDTO.class)
            .consumeWith(response -> {
                PageableDTO responseBody = response.getResponseBody();
                Assertions.assertThat(responseBody).isNotNull();
                Assertions.assertThat(responseBody.getContent()).isNotEmpty();
            });
    }

    @Test
    public void getAll_WithoutAuthenticationToken_ReturnErrorMessageWith401Status() {
        testClient
            .get()
            .uri("/api/v1/parkings")
            .exchange()
            .expectStatus().isUnauthorized()
            .expectBody(ErrorMessage.class);
    }

    /* ----- Get Parking Spot by Receipt tests ----- */

    @Test
    public void getByReceipt_WithValidReceipt_ReturnParkingResponseWith200Status() {
        User admin = users.get("admin");
        String receipt = "20230313-101300";

        testClient
            .get()
            .uri("/api/v1/parkings/{receipt}", receipt)
            .headers(JwtAuthentication.getHeaderAuthorization(testClient, admin.email, admin.password))
            .exchange()
            .expectStatus().isOk()
            .expectBody(ParkingResponseDTO.class)
            .consumeWith(response -> {
                ParkingResponseDTO responseBody = response.getResponseBody();
                Assertions.assertThat(responseBody).isNotNull();
                Assertions.assertThat(responseBody.getReceipt()).isEqualTo(receipt);
            });
    }

    @Test
    public void getByReceipt_WithInvalidReceipt_ReturnErrorMessageWith404Status() {
        User admin = users.get("admin");
        String receipt = "invalid-receipt";

        testClient
            .get()
            .uri("/api/v1/parkings/{receipt}", receipt)
            .headers(JwtAuthentication.getHeaderAuthorization(testClient, admin.email, admin.password))
            .exchange()
            .expectStatus().isNotFound()
            .expectBody(ErrorMessage.class);
    }

    @Test
    public void getByReceipt_WithoutAuthenticationToken_ReturnErrorMessageWith401Status() {
        String receipt = "valid-receipt";

        testClient
            .get()
            .uri("/api/v1/parkings/{receipt}", receipt)
            .exchange()
            .expectStatus().isUnauthorized()
            .expectBody(ErrorMessage.class);
    }

    /* Get all parking Spots */

    @Test
    public void getAllParkingSpots_WithAdminRole_ReturnPageableDTOWith200Status() {
        User admin = users.get("admin");

        testClient
            .get()
            .uri("/api/v1/parkings")
            .headers(JwtAuthentication.getHeaderAuthorization(testClient, admin.email, admin.password))
            .exchange()
            .expectStatus().isOk()
            .expectBody(PageableDTO.class)
            .consumeWith(response -> {
                PageableDTO responseBody = response.getResponseBody();
                Assertions.assertThat(responseBody).isNotNull();
                Assertions.assertThat(responseBody.getContent().size()).isEqualTo(3);
            });
    }

    @Test 
    public void getAllParkingSpots_WithClientRole_ReturnPageableDTOWith200Status() {
        User client = users.get("client1");

        PageableDTO response = testClient
            .get()
            .uri("/api/v1/parkings")
            .headers(JwtAuthentication.getHeaderAuthorization(testClient, client.email, client.password))
            .exchange()
            .expectStatus().isOk()
            .expectBody(PageableDTO.class)
            .returnResult()
            .getResponseBody();

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getContent().size()).isEqualTo(1);
    }

    
    /* Get parking spot by receipt */

    @Test
    public void getParkingSpotByReceipt_WithValidReceipt_ReturnParkingResponseWith200Status() {
        User admin = users.get("admin");
        String receipt = "20230313-101300";

        ParkingResponseDTO response = testClient
            .get()
            .uri("/api/v1/parkings/{receipt}", receipt)
            .headers(JwtAuthentication.getHeaderAuthorization(testClient, admin.email, admin.password))
            .exchange()
            .expectStatus().isOk()
            .expectBody(ParkingResponseDTO.class)
            .returnResult()
            .getResponseBody();

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getReceipt()).isEqualTo(receipt);
    }

    @Test
    public void getParkingSpotByReceipt_WithInvalidReceipt_ReturnErrorMessageWith404Status() {
        // user trying to acess other user receipt and non existent receipt
        User user = users.get("client1");
        String[] receipts = { "20230313-101300", "20230313-101308" };

        for (String receipt : receipts) {
            testClient
                    .get()
                    .uri("/api/v1/parkings/{receipt}", receipt)
                    .headers(JwtAuthentication.getHeaderAuthorization(testClient, user.email, user.password))
                    .exchange()
                    .expectStatus().isNotFound()
                    .expectBody(ErrorMessage.class);
        }
    }

    @Test
    public void getParkingSpotByReceipt_WithoutAuthenticationToken_ReturnErrorMessageWith401Status() {
        String receipt = "20230313-101300";

        testClient
            .get()
            .uri("/api/v1/parkings/{receipt}", receipt)
            .exchange()
            .expectStatus().isUnauthorized()
            .expectBody(ErrorMessage.class);
    }

}

