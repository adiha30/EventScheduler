package com.adiha.EventScheduler.controllers;

import com.adiha.EventScheduler.services.Endpoints.SubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

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
    public void subscribe(@Validated String eventId) {
        subscriptionService.subscribe(eventId);
    }

    /**
     * This method is used to unsubscribe from an event.
     * It is mapped to the "/unsubscribe/{eventId}" URL path and responds to POST requests.
     *
     * @param eventId The ID of the event to unsubscribe from.
     */
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/unsubscribe/{eventId}")
    public void unsubscribe(@Validated String eventId) {
        subscriptionService.unsubscribe(eventId);
    }
}