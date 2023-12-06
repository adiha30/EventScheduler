package com.adiha.EventScheduler.repositories;

import com.adiha.EventScheduler.models.Event;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface EventRepository extends JpaRepository<Event, UUID> {
}
