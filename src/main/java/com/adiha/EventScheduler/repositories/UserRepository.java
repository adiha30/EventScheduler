package com.adiha.EventScheduler.repositories;

import com.adiha.EventScheduler.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserRepository extends JpaRepository<User, String> {
}
