package com.adiha.EventScheduler.repositories;

import com.adiha.EventScheduler.models.Event;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface EventRepository extends JpaRepository<Event, UUID>, JpaSpecificationExecutor<Event> {
    @Query("SELECT e FROM Event e LEFT JOIN FETCH e.users u GROUP BY e ORDER BY COUNT(u) DESC")
    List<Event> findAllOrderByPopularity(Specification<Event> spec);

    @Query("SELECT e FROM Event e LEFT JOIN FETCH e.users u GROUP BY e ORDER BY COUNT(u) DESC")
    List<Event> findAllOrderByPopularity();
}
