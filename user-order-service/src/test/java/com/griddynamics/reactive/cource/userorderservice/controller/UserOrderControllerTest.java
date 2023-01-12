package com.griddynamics.reactive.cource.userorderservice.controller;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import com.griddynamics.reactive.cource.userorderservice.entity.User;
import com.griddynamics.reactive.cource.userorderservice.entity.UserOrder;
import com.griddynamics.reactive.cource.userorderservice.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.time.Duration;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@WireMockTest(httpPort = 8083)
class UserOrderControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private UserRepository userRepository;


    @BeforeEach
    public void setUp() {
        webTestClient = webTestClient.mutate()
                .responseTimeout(Duration.ofMillis(30000))
                .build();
    }

    @Test
    void testSuccess() {
        when(userRepository.findById("user2")).thenReturn(Mono.just(new User("user2", "Bill", "987654321")));
        stubFor(get(urlPathEqualTo("/orderSearchService/order/phone"))
                .withQueryParam("phoneNumber", equalTo("987654321"))
                .willReturn(WireMock.aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .withBodyFile("order.json")));

        webTestClient
                // Create a GET request to test an endpoint
                .get().uri("/orders/user2")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(UserOrder.class)
                .hasSize(4)
                .contains(UserOrder.builder()
                        .orderNumber("Order_1")
                        .userName("Bill")
                        .phoneNumber("987654321")
                        .productCode("5256")
                        .productName("")
                        .productId("")
                        .build());
    }


}