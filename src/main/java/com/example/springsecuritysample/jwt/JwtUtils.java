package com.example.springsecuritysample.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Service;

import java.sql.Date;

@Service
public class JwtUtils {

    private final String SECRET = "mysecret";

    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setExpiration(new Date(System.currentTimeMillis() + (60 * 60 * 24 * 1000)))
                .signWith(SignatureAlgorithm.ES256, SECRET)
                .compact();
    }

    public String getUsername(String token) {
        return Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token).getBody().getSubject();
    }
}
