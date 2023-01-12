package com.griddynamics.reactive.cource.userorderservice.service;

import com.griddynamics.reactive.cource.userorderservice.entity.Product;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.List;
import java.util.function.Function;

import static com.griddynamics.reactive.cource.userorderservice.log.LogsUtils.logOnError;
import static com.griddynamics.reactive.cource.userorderservice.log.LogsUtils.logOnNext;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductService {

    private final WebClient webClient;

    private static final String PRODUCT_SERVICE_PATH = "http://localhost:8082/productInfoService/product/names";

    public Mono<List<Product>> getOrderInfo(String productCode) {
        URI reqiestUtl = UriComponentsBuilder.fromUriString(PRODUCT_SERVICE_PATH)
                .queryParam("productCode", productCode)
                .encode()
                .build()
                .toUri();
        return webClient
                .get()
                .uri(reqiestUtl)
                .exchangeToMono(handelResponse())
                .doOnEach(logOnNext(v -> log.info("found  order product {}", v)))
                .doOnEach(logOnError(e -> log.error("error when searching product", e)))
                .onErrorResume(e -> Mono.just(List.of()));

    }

    private Function<ClientResponse, Mono<List<Product>>> handelResponse() {
        return response ->
                response.statusCode().equals(HttpStatus.OK) ?
                        response.bodyToMono(new ParameterizedTypeReference<List<Product>>() {
                        })
                        : Mono.just(List.of());
    }

}
