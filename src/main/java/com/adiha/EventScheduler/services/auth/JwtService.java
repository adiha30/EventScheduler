package com.adiha.EventScheduler.services.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Service class for handling JWT (JSON Web Token) operations.
 */
@Service
public class JwtService {

    public static final int JWT_EXPIRATION = 1000 * 60 * 60;
    @Value("${application.security.jwt.secret-key}")
    private String secretKey;

    /**
     * Extracts the username from the JWT.
     *
     * @param token JWT as a string
     * @return Username as a string
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Extracts a claim from the JWT.
     *
     * @param token          JWT as a string
     * @param claimsResolver Function to resolve the claim
     * @return Claim as a generic type
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }


    /**
     * Generates a JWT for a user with no additional claims.
     * <p>
     * This method is a convenience method that calls the overloaded generateToken method with an empty map for the extraClaims parameter.
     *
     * @param userDetails User details
     * @return JWT as a string
     */
    public String generateToken(UserDetails userDetails) {
        return generateToken(userDetails, new HashMap<>());
    }

    /**
     * Generates a JWT for a user.
     *
     * @param userDetails User details
     * @param extraClaims Additional claims to include in the JWT
     * @return JWT as a string
     */
    public String generateToken(UserDetails userDetails, Map<String, Object> extraClaims) {
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + JWT_EXPIRATION))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Checks if a JWT is valid.
     * <p>
     * This method extracts the username from the token and checks if it matches the username from the provided UserDetails.
     * It also checks if the token has not expired.
     *
     * @param token       JWT as a string
     * @param userDetails User details
     * @return true if the token is valid, false otherwise
     */
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);

        return Keys.hmacShaKeyFor(keyBytes);
    }
}