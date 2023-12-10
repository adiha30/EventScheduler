package com.adiha.EventScheduler.controllers;

import com.adiha.EventScheduler.models.auth.AuthenticationRequest;
import com.adiha.EventScheduler.models.auth.AuthenticationResponse;
import com.adiha.EventScheduler.models.auth.RegisterRequest;
import com.adiha.EventScheduler.services.auth.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody RegisterRequest request) {
        try {
            return ResponseEntity.ok(authenticationService.register(request));
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "User already exists", e);
        }
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request) {
        try {
            return ResponseEntity.ok(authenticationService.authenticate(request));
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Username or password are incorrect", e);
        }
    }
}
