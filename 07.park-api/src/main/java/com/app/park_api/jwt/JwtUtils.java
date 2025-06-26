package com.app.park_api.jwt;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import javax.crypto.SecretKey;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JwtUtils {

    public static String JWT_BEARER = "Bearer ";
    public static String JWT_AUTHORIZATION = "Authorization";
    public static String SECRET_KEY = "0123456789-0123456789-0123456789";

    public static final long EXPIRE_DAYS = 0;
    public static final long EXPIRE_HOURS = 0;
    public static final long EXPIRE_MINUTES = 2;

    private JwtUtils() {};

    // generates a SecretKey instance for HMAC-SHA algorithms based on the SECRET_KEY constant.
    private static SecretKey generateKey() {
        // Generates a SecretKey instance for HMAC-SHA algorithms based on the size of SECRET_KEY.
        // If SECRET_KEY length is 32 bytes, it corresponds to the HS256 algorithm.
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));
    }

    // converts the start date to an expiration date by adding the defined constants.
    private static Date toExpireDate(Date start) {
        // convert to datetime to add the constants
        LocalDateTime dateTime = start.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        LocalDateTime end = dateTime.plusDays(EXPIRE_DAYS).plusHours(EXPIRE_HOURS).plusMinutes(EXPIRE_MINUTES);
        // convert back to Date
        return Date.from(end.atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * Creates a JWT token with the given username and role in the payload.
     */
    public static JwtToken createToken(String username, String role) {
        Date issuedAt = new Date();
        Date limit = toExpireDate(issuedAt);

        String token = Jwts.builder()
                .header()
                    .type("JWT")
                    .and()
                .subject(username)
                .issuedAt(issuedAt)
                .expiration(limit)
                .signWith(generateKey())
                .claim("role", role)
                .compact();

        return new JwtToken(token);
    }

    // Extracts the claims (payload) from the JWT token.
    private static Claims getClaimsForToken(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(generateKey())
                    .build()
                    .parseSignedClaims(refactorToken(token))
                    .getPayload();
        } catch (Exception e) {
            log.error("Invalid token %s: ", e.getMessage());
        }

        return null;
    }
    
    public static String getUsernameFromToken(String token) {
        return getClaimsForToken(token).getSubject();
    }


    // Checks if the provided JWT token is valid.
    public static boolean isTokenValid(String token) {
        try {
            Jwts.parser()
                    .verifyWith(generateKey())
                    .build()
                    .parseSignedClaims(refactorToken(token));
            return true;
        } catch (Exception e) {
            log.error("Invalid token %s: ", e.getMessage());
        }

        return false;
    }

    // Removes the "Bearer " prefix from the token if it exists.
    private static String refactorToken(String token) {
        if (token.contains(JWT_BEARER)) {
            return token.substring(JWT_BEARER.length());
        }
        return token;
    }

}
