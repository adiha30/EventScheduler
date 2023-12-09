package com.adiha.EventScheduler.services.Endpoints;

import com.adiha.EventScheduler.models.Event;
import com.adiha.EventScheduler.repositories.EventRepository;
import com.adiha.EventScheduler.repositories.UserRepository;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import static com.adiha.EventScheduler.services.Endpoints.EventsService.throwNotFoundException;

@Service
public class SubscriptionService extends CrudService {
    private final Logger logger = LoggerFactory.getLogger(SubscriptionService.class);

    private final EventRepository eventRepository;

    public SubscriptionService(UserRepository userRepository, EventRepository eventRepository) {
        super(userRepository);
        this.eventRepository = eventRepository;
    }

    public void subscribe(String eventId) {
        Event eventToSub = eventRepository.findById(eventId)
                .orElseThrow(() -> throwNotFoundException(eventId));

        eventToSub.addToSubscribers(getActiveUser().getUserId());

        eventRepository.save(eventToSub);
    }

    public void unsubscribe(String eventId) {
        Event eventToSub = eventRepository.findById(eventId)
                .orElseThrow(() -> throwNotFoundException(eventId));

        eventToSub.removeFromSubscribers(getActiveUser().getUserId());

        eventRepository.save(eventToSub);
    }
}
