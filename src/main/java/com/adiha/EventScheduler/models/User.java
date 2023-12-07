package com.adiha.EventScheduler.models;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.hibernate.annotations.UuidGenerator;

@Entity
@Table(name = "users")
public class User {
    @Id
    @UuidGenerator
    private String userId;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;
}
