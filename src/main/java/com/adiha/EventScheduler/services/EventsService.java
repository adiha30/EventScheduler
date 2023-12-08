package com.adiha.EventScheduler.services;

import com.adiha.EventScheduler.models.Event;
import com.adiha.EventScheduler.repositories.EventRepository;
import com.adiha.EventScheduler.specifications.EventByLocation;
import com.adiha.EventScheduler.specifications.EventByVenue;
import com.adiha.EventScheduler.utils.mapper.EventMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static com.adiha.EventScheduler.utils.Constants.*;

@Service
@RequiredArgsConstructor
public class EventsService {
    private final Logger logger = LoggerFactory.getLogger(EventsService.class);

    private final EventRepository eventRepository;
    private final EventMapper eventMapper;


    public List<Event> getAll(String sort, String order) {
        logger.debug("Retrieving all events");

        if (POPULARITY.equals(sort)) {
            return getEventSortedBy(order);
        }

        Sort sortingParameters = getSortingParameters(sort, order);

        return eventRepository.findAll(sortingParameters);
    }

    public Event getEventById(String eventId) {
        logger.debug("Retrieving event with id: {}", eventId);

        return eventRepository.findById(eventId).orElseThrow(() -> throwNotFoundException(eventId));
    }

    public List<Event> getEventsByLocationAndVenue(
            String location,
            String venue,
            String sort,
            String order) {
        logger.debug("Retrieving events with location: {} and venue: {}", location, venue);

        Specification<Event> spec = getSpecification(location, venue);

        if (POPULARITY.equals(sort)) {
            return getEventSortedBy(order);
        }

        Sort sortingParameters = getSortingParameters(sort, order);

        return eventRepository.findAll(spec, sortingParameters);
    }

    public Event createEvent(Event event) {
        logger.debug("Creating event: {}", event);

        return eventRepository.save(event);
    }

    public List<Event> createEvents(List<Event> events) {
        return eventRepository.saveAll(events);
    }

    public Event updateEvent(String eventId, Event event) {
        logger.debug("Updating event with id: {}", eventId);

        Event updatedEvent = eventRepository.findById(eventId)
                .map((Event eventToUpdate) -> updateEvent(event, eventToUpdate))
                .orElseThrow(() -> throwNotFoundException(eventId));

        return eventRepository.save(updatedEvent);
    }

    public void deleteEvent(String eventId) {
        logger.debug("Deleting event with id: {}", eventId);

        eventRepository.deleteById(eventId);
    }

    public void deleteAll(List<String> eventIds) {
        logger.debug("Deleting events with ids: {}", eventIds);

        List<Event> eventsToDelete = eventRepository.findAllById(eventIds);

        eventRepository.deleteAll(eventsToDelete);
    }

    private List<Event> getEventSortedBy(String order) {
        return DESCENDING.equals(order)
                ? eventRepository.findAllOrderByPopularityDesc()
                : eventRepository.findAllOrderByPopularityAsc();
    }

    private static Specification<Event> getSpecification(String location, String venue) {
        return Specification
                .where(new EventByLocation(location))
                .and(new EventByVenue(venue));
    }

    private Sort getSortingParameters(String sort, String order) {
        if (sort == null || sortNotAllowed(sort)) {
            throw new IllegalArgumentException("Sorting can only be done on 'creationTime' or 'startDate'");
        }

        return Sort.by(Sort.Direction.fromString(order), sort);
    }

    private boolean sortNotAllowed(String sort) {
        return !sort.equals(CREATION_TIME)
                && !sort.equals(START_TIME)
                && !sort.equals(POPULARITY);
    }

    private static ResponseStatusException throwNotFoundException(String eventId) {
        return new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                String.format("Event with uuid %s was not found", eventId));
    }

    private Event updateEvent(Event event, Event eventToUpdate) {
        eventMapper.updateEventFromDto(event, eventToUpdate);

        return eventToUpdate;
    }
}
