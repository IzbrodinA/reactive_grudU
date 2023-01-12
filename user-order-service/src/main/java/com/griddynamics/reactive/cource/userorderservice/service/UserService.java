package com.griddynamics.reactive.cource.userorderservice.service;

import com.griddynamics.reactive.cource.userorderservice.entity.User;
import com.griddynamics.reactive.cource.userorderservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import static com.griddynamics.reactive.cource.userorderservice.log.LogsUtils.logOnError;
import static com.griddynamics.reactive.cource.userorderservice.log.LogsUtils.logOnNext;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public Mono<User> getUser(String id) {
        return userRepository.findById(id)
                .doOnEach(logOnNext(v -> log.info("found  user {}", v)))
                .doOnEach(logOnError(e -> log.error("error when searching user", e)));
    }

}
