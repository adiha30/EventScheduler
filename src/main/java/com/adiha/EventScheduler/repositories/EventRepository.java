package com.adiha.EventScheduler.repositories;

import com.adiha.EventScheduler.models.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

/**
 * EventRepository interface.
 * This interface is used for CRUD operations and custom queries on the Event entity.
 */
public interface EventRepository extends JpaRepository<Event, String>, JpaSpecificationExecutor<Event> {

    List<Event> findByStartTimeBetweenAndReminderSentIsFalse(LocalDateTime start, LocalDateTime end);

    /**
     * Custom query to find all events ordered by popularity in descending order.
     * Popularity is determined by the number of subscriptions to an event.
     *
     * @return a list of all events ordered by popularity in descending order
     */
    @Query(nativeQuery = true, value = "select e.event_id,e.creating_user_id,e.creation_time,e.end_time,e.location,e.name,e.start_time,e.venue from event_subscriptions es inner join events e on es.event_id = e.event_id group by e.event_id,creating_user_id,creation_time,end_time,location,name,start_time,venue order by count(*) desc")
    List<Event> findAllOrderByPopularityDesc();

    /**
     * Custom query to find all events ordered by popularity in ascending order.
     * Popularity is determined by the number of subscriptions to an event.
     *
     * @return a list of all events ordered by popularity in ascending order
     */
    @Query(nativeQuery = true, value = "select e.event_id,e.creating_user_id,e.creation_time,e.end_time,e.location,e.name,e.start_time,e.venue from event_subscriptions es inner join events e on es.event_id = e.event_id group by e.event_id,creating_user_id,creation_time,end_time,location,name,start_time,venue order by count(*)")
    List<Event> findAllOrderByPopularityAsc();
}