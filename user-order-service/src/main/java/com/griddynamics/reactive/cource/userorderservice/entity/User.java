package com.griddynamics.reactive.cource.userorderservice.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Data
@AllArgsConstructor
public class User {

    @Id
    private String id;
    private String name;
    private String phone;

}