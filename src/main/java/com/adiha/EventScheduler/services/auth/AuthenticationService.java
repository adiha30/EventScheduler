package com.adiha.EventScheduler.services.auth;

import com.adiha.EventScheduler.expections.UserAlreadyExistAuthenticationException;
import com.adiha.EventScheduler.models.User;
import com.adiha.EventScheduler.models.auth.AuthenticationRequest;
import com.adiha.EventScheduler.models.auth.AuthenticationResponse;
import com.adiha.EventScheduler.models.auth.RegisterRequest;
import com.adiha.EventScheduler.models.enums.Role;
import com.adiha.EventScheduler.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new UserAlreadyExistAuthenticationException(
                    String.format("User with username %s already exists", request.getUsername()));
        }

        var user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();

        userRepository.save(user);

        var jwtToken = jwtService.generateToken(user);

        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        var user = userRepository.findByUsername(request.getUsername())
                .orElseThrow();

        return AuthenticationResponse.builder()
                .token(jwtService.generateToken(user))
                .build();
    }
}
