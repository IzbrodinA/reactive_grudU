package com.griddynamics.reactive.cource.userorderservice.controller;

import com.griddynamics.reactive.cource.userorderservice.entity.UserOrder;
import com.griddynamics.reactive.cource.userorderservice.service.UserOrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.util.context.Context;

import static com.griddynamics.reactive.cource.userorderservice.log.LogsUtils.logOnError;
import static com.griddynamics.reactive.cource.userorderservice.log.LogsUtils.logOnNext;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
@Slf4j
public class UserOrderController {

    private final UserOrderService userOrderService;

    @GetMapping("/{id}")
    public Flux<UserOrder> getUserOrderById(@PathVariable String id, @RequestHeader(required = false, name = "requestId", defaultValue = "") String requestId) {
        return userOrderService.getUserInfo(id)
                .doOnEach(logOnNext(v -> log.info("found search order {}", v)))
                .doOnEach(logOnError(e -> log.error("error when searching user order", e)))
                .contextWrite(Context.of("CONTEXT_KEY", requestId));

    }
}
