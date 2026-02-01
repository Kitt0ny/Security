package org.example.Security.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;

@Service
public class JwtService {

    private static final long ACCESS_EXP = 1000 * 60 * 15;      // 15 мин
    private static final long REFRESH_EXP = 1000 * 60 * 60 * 24; // 24 часа
    //по идее секретный ключ нужно хранить в программе Hashicorp Vault откуда при запуске она будет подтягиваться из БД или иного места
    // и никогда случайно не попадет в github или не стащится декомпилятором твоего приложения
//<dependency>
//<groupId>org.springframework.cloud</groupId>
//<artifactId>spring-cloud-starter-vault-config</artifactId>
//</dependency>
    private final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    public String generateAccessToken(String username, String role) {
        return Jwts.builder()
                .setSubject(username)
                .claim("role", role)
                .setExpiration(new Date(System.currentTimeMillis() + ACCESS_EXP))
                .signWith(key)
                .compact();
    }

    public String generateRefreshToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setExpiration(new Date(System.currentTimeMillis() + REFRESH_EXP))
                .signWith(key)
                .compact();
    }

    public Claims parseToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
