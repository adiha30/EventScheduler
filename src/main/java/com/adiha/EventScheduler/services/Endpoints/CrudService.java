package com.adiha.EventScheduler.services.Endpoints;

import com.adiha.EventScheduler.models.User;
import com.adiha.EventScheduler.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.server.ResponseStatusException;

@RequiredArgsConstructor
public abstract class CrudService {

    private final UserRepository userRepository;

    public static ResponseStatusException throwNotFoundException(String eventId) {
        throw new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                String.format("Event with uuid %s was not found", eventId));
    }

    private static String getUsernameOfExecutor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        return authentication.getName();
    }

    public User getActiveUser() {
        String usernameOfExecutor = getUsernameOfExecutor();

        return userRepository
                .findByUsername(usernameOfExecutor)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}
