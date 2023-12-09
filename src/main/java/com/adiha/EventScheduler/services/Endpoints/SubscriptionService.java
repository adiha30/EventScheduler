package com.adiha.EventScheduler.services.Endpoints;

import com.adiha.EventScheduler.models.Event;
import com.adiha.EventScheduler.repositories.EventRepository;
import com.adiha.EventScheduler.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

/**
 * This class is a Service class for handling operations related to subscriptions.
 * It extends the CrudService class and uses the UserRepository and EventRepository for database operations.
 */
@Service
public class SubscriptionService extends CrudService {
    private final Logger logger = LoggerFactory.getLogger(SubscriptionService.class);

    private final EventRepository eventRepository;

    /**
     * Constructor for the SubscriptionService class.
     * It initializes the userRepository and eventRepository.
     *
     * @param userRepository  The UserRepository to handle the database operations related to the User entity.
     * @param eventRepository The EventRepository to handle the database operations related to the Event entity.
     */
    public SubscriptionService(UserRepository userRepository, EventRepository eventRepository) {
        super(userRepository);
        this.eventRepository = eventRepository;
    }

    /**
     * This method is used to subscribe a user to an event.
     * It retrieves the event by its ID and adds the currently authenticated user to its subscribers.
     * Then, it saves the updated event to the database.
     *
     * @param eventId The ID of the event to subscribe to.
     * @throws ResponseStatusException if the event is not found.
     */
    public void subscribe(String eventId) {
        logger.info("Subscribing to event with id {}", eventId);

        Event eventToSub = eventRepository.findById(eventId)
                .orElseThrow(() -> throwNotFoundException(eventId));
        eventToSub.addToSubscribers(getActiveUserId());

        eventRepository.save(eventToSub);
    }

    /**
     * This method is used to unsubscribe a user from an event.
     * It retrieves the event by its ID and removes the currently authenticated user from its subscribers.
     * Then, it saves the updated event to the database.
     *
     * @param eventId The ID of the event to unsubscribe from.
     * @throws ResponseStatusException if the event is not found.
     */
    public void unsubscribe(String eventId) {
        logger.info("Unsubscribing from event with id {}", eventId);
        
        Event eventToSub = eventRepository.findById(eventId)
                .orElseThrow(() -> throwNotFoundException(eventId));

        eventToSub.removeFromSubscribers(getActiveUserId());

        eventRepository.save(eventToSub);
    }
}