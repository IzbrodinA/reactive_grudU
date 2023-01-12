package com.griddynamics.reactive.cource.userorderservice.service;

import com.griddynamics.reactive.cource.userorderservice.entity.OrderInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;

import java.net.URI;

import static com.griddynamics.reactive.cource.userorderservice.log.LogsUtils.logOnError;
import static com.griddynamics.reactive.cource.userorderservice.log.LogsUtils.logOnNext;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderService {

    private final WebClient webClient;

    private static final String ORDER_SEARCH_SERVICE_PATH = "http://localhost:8083/orderSearchService/order/phone";

    public Flux<OrderInfo> getOrderInfo(String phone) {
        URI reqiestUtl = UriComponentsBuilder.fromUriString(ORDER_SEARCH_SERVICE_PATH)
                .queryParam("phoneNumber", phone)
                .encode()
                .build()
                .toUri();
        return webClient
                .get()
                .uri(reqiestUtl)
                .retrieve()
                .bodyToFlux(OrderInfo.class)
                .doOnEach(logOnNext(v -> log.info("found  order {}", v)))
                .doOnEach(logOnError(e -> log.error("error when searching order", e)));
    }

}
