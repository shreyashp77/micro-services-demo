package com.example.auth_service.service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private long jwtExpiration;

    /**
     * Generates a JWT for the given user details.
     * @param userDetails The user details to create the token for.
     * @return A signed JWT string.
     */
    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    /**
     * Generates a JWT with extra claims.
     * @param extraClaims Additional claims to include in the token.
     * @param userDetails The user details.
     * @return A signed JWT string.
     */
    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        return Jwts.builder()
                .claims(extraClaims)
                .subject(userDetails.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .signWith(getSignInKey())
                .compact();
    }

    /**
     * Extracts the username from a JWT.
     * @param token The JWT string.
     * @return The username (subject) from the token.
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Checks if a token is valid for a given user.
     * @param token The JWT to validate.
     * @param userDetails The user details to validate against.
     * @return true if the token is valid, false otherwise.
     */
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    /**
     * Checks if the provided JWT token has expired.
     *
     * @param token the JWT token to check
     * @return {@code true} if the token is expired, {@code false} otherwise
     */
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     * Extracts the expiration date from the provided JWT token.
     *
     * @param token the JWT token from which to extract the expiration date
     * @return the expiration {@link Date} of the token
     */
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * Generic method to extract a single claim from a token.
     * @param token The JWT.
     * @param claimsResolver A function to extract the desired claim.
     * @return The claim value.
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Extracts all claims from the provided JWT token.
     *
     * @param token the JWT token from which to extract claims
     * @return the {@link Claims} object containing all claims present in the token
     * @throws io.jsonwebtoken.security.SecurityException if the token signature is invalid
     * @throws io.jsonwebtoken.MalformedJwtException if the token is malformed
     * @throws io.jsonwebtoken.ExpiredJwtException if the token is expired
     * @throws io.jsonwebtoken.UnsupportedJwtException if the token is unsupported
     * @throws IllegalArgumentException if the token is null or empty
     */
    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSignInKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    /**
     * Creates a SecretKey instance from the base64 encoded secret string.
     * @return A SecretKey for signing and verification.
     */
    private SecretKey getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}