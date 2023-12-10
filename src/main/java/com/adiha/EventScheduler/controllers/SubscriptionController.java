package com.adiha.EventScheduler.controllers;

import com.adiha.EventScheduler.services.Endpoints.SubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

/**
 * This class is a Rest Controller for handling subscription related requests.
 * It is mapped to "/api/v1" URL path.
 */
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    /**
     * This method is used to subscribe to an event.
     * It is mapped to the "/subscribe/{eventId}" URL path and responds to POST requests.
     *
     * @param eventId The ID of the event to subscribe to.
     */
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/subscribe/{eventId}")
    public void subscribe(@Validated @PathVariable String eventId) {
        try {
            subscriptionService.subscribe(eventId);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Failed to subscribe to event " + eventId, e);
        }
    }

    /**
     * This method is used to unsubscribe from an event.
     * It is mapped to the "/unsubscribe/{eventId}" URL path and responds to POST requests.
     *
     * @param eventId The ID of the event to unsubscribe from.
     */
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/unsubscribe/{eventId}")
    public void unsubscribe(@Validated @PathVariable String eventId) {
        try {
            subscriptionService.unsubscribe(eventId);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Failed to unsubscribe from event " + eventId, e);
        }
    }
}