package com.example.foodyshop.helper;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Date;
import java.util.Map;

import javax.crypto.SecretKey;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

public class JWT {

    private static final SecretKey PRIVATE_KEY = Keys.hmacShaKeyFor(Decoders.BASE64.decode("Rm9PZHlTaG9QX3NlY3JldEtleVl5KiMxMjA0JV4mNDUxODgxIyEhMjI0NWRzZEREIzJFZGVk"));

    public static String createToken() {
        return Jwts.builder()
                .setExpiration(new Date(System.currentTimeMillis() + 60 * 1000)) //a java.util.Date
                .setIssuedAt(new Date())
                .signWith(PRIVATE_KEY, SignatureAlgorithm.HS256)
                .compact();
    }

    public static String createToken(Map<String, Object> map, long timeExp) {
        return Jwts.builder()
                .setClaims(map)
                .setExpiration(new Date(timeExp)) //a java.util.Date
                .setIssuedAt(new Date())
                .signWith(PRIVATE_KEY, SignatureAlgorithm.HS256)
                .compact();
    }

    @Nullable
    public static Jws<Claims> decodeToken(String token) {
        try {
            return Jwts.parserBuilder().setSigningKey(PRIVATE_KEY).build().parseClaimsJws(token);
        } catch (RuntimeException e) {
            return null;
        }
    }

}
