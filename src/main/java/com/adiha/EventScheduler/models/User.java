package com.adiha.EventScheduler.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.hibernate.annotations.UuidGenerator;

/**
 * User entity class.
 * This class represents a user in the system.
 */
@Entity
@Table(name = "users")
public class User {
    /**
     * The unique identifier of the user.
     */
    @Id
    @UuidGenerator
    private String userId;
}