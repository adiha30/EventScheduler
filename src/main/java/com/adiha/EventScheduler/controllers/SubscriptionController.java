package com.adiha.EventScheduler.controllers;

import com.adiha.EventScheduler.services.Endpoints.SubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/subscribe/{eventId}")
    public void subscribe(@Validated String eventId) {
        subscriptionService.subscribe(eventId);
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/unsubscribe/{eventId}")
    public void unsubscribe(@Validated String eventId) {
        subscriptionService.unsubscribe(eventId);
    }
}
