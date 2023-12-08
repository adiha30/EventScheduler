package com.adiha.EventScheduler.repositories;

import com.adiha.EventScheduler.models.Event;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface EventRepository extends JpaRepository<Event, String>, JpaSpecificationExecutor<Event> {
    @Query(nativeQuery = true, value = "select e.event_id,e.creating_user_id,e.creation_time,e.end_time,e.location,e.name,e.start_time,e.venue from event_subscriptions es inner join events e on es.event_id = e.event_id group by e.event_id,creating_user_id,creation_time,end_time,location,name,start_time,venue order by count(*) desc")
    List<Event> findAllOrderByPopularityDesc();

    @Query(nativeQuery = true, value = "select e.event_id,e.creating_user_id,e.creation_time,e.end_time,e.location,e.name,e.start_time,e.venue from event_subscriptions es inner join events e on es.event_id = e.event_id group by e.event_id,creating_user_id,creation_time,end_time,location,name,start_time,venue order by count(*)")
    List<Event> findAllOrderByPopularityAsc();
}

