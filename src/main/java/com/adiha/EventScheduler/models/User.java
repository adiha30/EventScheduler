package com.adiha.EventScheduler.models;


import jakarta.persistence.*;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

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
