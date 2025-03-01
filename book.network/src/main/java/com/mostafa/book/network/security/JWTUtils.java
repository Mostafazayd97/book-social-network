package com.mostafa.book.network.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.function.Function;

@Service
public class JWTUtils {

    private final String secretKey;
    private final Long jwtExpirationMs;

    public JWTUtils(@Value("${application.security.jwt.secret-key}") String secretKey, @Value("${application.security.jwt.expiration}") Long jwtExpirationMs) {
        this.secretKey = secretKey;
        this.jwtExpirationMs = jwtExpirationMs;
    }


    public String generateJwtToken(UserDetails userDetails, HashMap<String, Object> claims) {
        String token = buildTokens(claims, userDetails, jwtExpirationMs);
        return token;
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String extractUsername(String jwt) {

        return extractClaims(jwt, Claims::getSubject);

    }

    public <T> T extractClaims(String jwt, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(jwt);
        return claimsResolver.apply(claims);


    }

    private Claims extractAllClaims(String jwt) {
        return Jwts.parserBuilder().setSigningKey(getSignInKey()).build().parseClaimsJws(jwt).getBody();
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String userEmail = extractUsername(token);
        return (userEmail.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new java.util.Date());
    }

    private Date extractExpiration(String token) {
        return extractClaims(token, Claims::getExpiration);
    }

    private String generateJwtToken(HashMap<String, Object> claims, UserDetails userDetails) {
        return buildTokens(claims, userDetails, jwtExpirationMs);
    }

    private String buildTokens(HashMap<String, Object> claims, UserDetails userDetails, Long jwtExpirationMs) {
        var authorities = userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();

        return Jwts.builder().setClaims(claims).setSubject(userDetails.getUsername()).setIssuedAt(new java.util.Date(System.currentTimeMillis())).setExpiration(new java.util.Date(System.currentTimeMillis() + jwtExpirationMs)).claim("authorities", authorities).signWith(getSignInKey()).compact();


    }


}
