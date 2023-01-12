package com.griddynamics.reactive.cource.userorderservice.repository;

import com.griddynamics.reactive.cource.userorderservice.entity.User;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface UserRepository
        extends ReactiveMongoRepository<User, String> {
}