package com.evbooksministry.bibleandbookministry.config;


import com.evbooksministry.bibleandbookministry.models.Users;
import com.evbooksministry.bibleandbookministry.repositories.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

@Service
public class JWTService {
    private final UserRepository userRepository;


    private String secretKey = System.getenv("JWT_SECRET");

    public JWTService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    public String generateRefreshToken(String username, String role, UUID userID) {
        long refreshTokenExp = 15552000000L;
        return generateToken(username, refreshTokenExp, role, userID);
    }

    public String generateAccessToken(String username, String role, UUID userId) {
        long accessTokenExpirationTime = 15552000000L;
        return generateToken(username, accessTokenExpirationTime, role, userId);
    }

    public String generateToken(String username, long expirationTime, String role, UUID userId) {
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User with id " + userId + " not found"));

        Map<String, Object> claims = new HashMap<>();
        if ("ADMIN".equals(role)) {
            claims.put("adminId", user.getUserId().toString());
        } else if ("CUSTOMER".equals(role)) {
            claims.put("customerId", user.getUserId().toString());
        }
        claims.put("role", role);
        claims.put("username", user.getUserName());
        System.out.println("expiration time in jwt service: " + expirationTime);

        return Jwts.builder()
                .claims()
                .add(claims)
                .subject(username)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expirationTime))
                .and()
                .signWith(getKey())
                .compact();
    }

    public UUID extractAdminId(String token) {
        Claims claims = extractAllClaim(token);
        if (claims.containsKey("adminId")) {
            return UUID.fromString(claims.get("adminId", String.class));
        }
        return null;
    }

    public UUID extractCustomerId(String token) {
        Claims claims = extractAllClaim(token);
        if (claims.containsKey("customerId")) {
            return UUID.fromString(claims.get("customerId", String.class));
        }
        return null;
    }


    private SecretKey getKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String extractUsername(String token) {
        return Jwts.parser()
                .verifyWith(getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get("username", String.class);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaim(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaim(String token) {
        return Jwts.parser()
                .verifyWith(getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
    public boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public String extractRole(String token) {
        return Jwts.parser()
                .verifyWith(getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get("role", String.class);
    }

    public UUID getCustomerId(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        String userToken = authHeader.substring(7);
        return extractCustomerId(userToken);
    }

    public UUID getAdminId(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        String userToken = authHeader.substring(7);
        return extractAdminId(userToken);
    }

    public UUID extractUserId(HttpServletRequest request){
        String authToken = getTokenFromCookie(request.getCookies());
        return extractCustomerId(authToken);
    }

    public UUID extractAdminId(HttpServletRequest request){
        String authToken = getTokenFromCookie(request.getCookies());
        return extractAdminId(authToken);
    }

    private String getTokenFromCookie(Cookie[] cookies) {
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("JWTAccess_token".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
}
