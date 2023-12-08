package com.adiha.EventScheduler.repositories;

import com.adiha.EventScheduler.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * UserRepository interface.
 * This interface is used for CRUD operations on the User entity.
 */
public interface UserRepository extends JpaRepository<User, String> {
}