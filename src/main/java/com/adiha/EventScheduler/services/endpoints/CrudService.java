package com.adiha.EventScheduler.services.endpoints;

import com.adiha.EventScheduler.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.server.ResponseStatusException;

/**
 * This abstract class provides the basic CRUD operations for the services.
 * It uses the UserRepository to perform operations related to the User entity.
 */
@RequiredArgsConstructor
public abstract class CrudService {

    // The UserRepository to handle the database operations related to the User entity.
    private final UserRepository userRepository;

    /**
     * This static method throws a ResponseStatusException with a NOT_FOUND status and a custom message.
     * The message indicates that an event with the provided UUID was not found.
     *
     * @param eventId The UUID of the event that was not found.
     * @throws ResponseStatusException with a NOT_FOUND status and a custom message.
     */
    public static ResponseStatusException throwNotFoundException(String eventId) {
        throw new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                String.format("Event with uuid %s was not found", eventId));
    }

    /**
     * This private method retrieves the username of the currently authenticated user.
     *
     * @return The username of the currently authenticated user.
     */
    private static String getUsernameOfExecutor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        return authentication.getName();
    }

    /**
     * This public method retrieves the ID of the currently authenticated user.
     * It uses the getUsernameOfExecutor method to get the username of the currently authenticated user.
     * Then, it uses the UserRepository to find the User entity with the retrieved username.
     * If the User entity is not found, it throws a UsernameNotFoundException.
     *
     * @return The ID of the currently authenticated user.
     * @throws UsernameNotFoundException if the User entity is not found.
     */
    public String getActiveUserId() {
        String usernameOfExecutor = getUsernameOfExecutor();

        return userRepository
                .findByUsername(usernameOfExecutor)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"))
                .getUserId();
    }
}