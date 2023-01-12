package com.griddynamics.reactive.cource.userorderservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;

@SpringBootApplication
@EnableReactiveMongoRepositories
public class UserOrderServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(UserOrderServiceApplication.class, args);
    }
}