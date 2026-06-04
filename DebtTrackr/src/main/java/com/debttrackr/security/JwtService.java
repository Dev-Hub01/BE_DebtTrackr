package com.debttrackr.security;

import com.debttrackr.domain.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;

    @Service
//    @ConfigurationProperties
    public class JwtService {

        @Value("${spring.security.authentication.jwt.base64-secret}")
        private String secret;

        @Value("${spring.security.authentication.jwt.token-validity-in-seconds}")
        private long tokenValidity;

        private Key getSigningKey() {

            byte[] keyBytes = Decoders.BASE64.decode(secret);

            return Keys.hmacShaKeyFor(keyBytes);
        }

        public String generateToken(User user) {

            return Jwts.builder()

                    .setSubject(user.getEmail())
                    .claim("role", user.getRole().name())
                    .setIssuedAt(new Date())
                    .setExpiration(
                            new Date(System.currentTimeMillis() + tokenValidity * 1000))

                    .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                    .compact();
        }

        public String extractEmail(String token) {

            return Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();
        }
    }

