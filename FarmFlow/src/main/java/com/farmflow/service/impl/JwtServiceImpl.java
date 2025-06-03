package com.farmflow.service.impl;

import com.farmflow.entity.User;
import com.farmflow.exception.JwtTokenExpiredException;
import com.farmflow.service.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.farmflow.util.Constants.TOKEN_EXPIRES_IN_MILLISECOND;

@Slf4j
@Service
public class JwtServiceImpl implements JwtService {
    private String secretKey = "";

    public JwtServiceImpl() {
        log.info("JwtServiceImpl instantiated");
        try{
            KeyGenerator keyGenerator = KeyGenerator.getInstance("HmacSHA256");
            SecretKey sk = keyGenerator.generateKey();
            secretKey = Base64.getEncoder().encodeToString(sk.getEncoded());
        } catch (Exception e) {
            log.error("Error while generating secret key: {}", e.getMessage(), e);        }

    }

    @Override
    public String generateToken(User user) {

        Map<String, Object> claims = new HashMap<>();
        claims.put("id", user.getId());
        claims.put("role", user.getRoles());
        claims.put("status", user.getStatus().getIsActive());

        String token = Jwts.builder().claims().add(claims)
                .subject(user.getEmail())
                .issuedAt(new Date((System.currentTimeMillis())))
                .expiration(new Date((System.currentTimeMillis() + (TOKEN_EXPIRES_IN_MILLISECOND))))
                .and()
                .signWith(getKey())
                .compact();
        return token;
    }

    private Claims extractAllClaims(String token) {
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(decryptKey(secretKey))
                    .build().parseSignedClaims(token).getPayload();
            return claims;
        }
        catch (ExpiredJwtException e) {
            throw new JwtTokenExpiredException("Token is Expired");
        }
        catch (JwtException e) {
            throw new JwtTokenExpiredException("Invalid Jwt Token");
        }
        catch (Exception e) {
            throw e;
        }

    }

    private SecretKey decryptKey(String secretKey) {
        byte[] keyBytes= Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);

    }

    @Override
    public boolean  validateToken(String token, UserDetails userDetails) {
        String username = extractUsername(token);
        Boolean isExpired = isTokenExpired(token);

        return username.equalsIgnoreCase(userDetails.getUsername()) && !isExpired;
    }

    private Boolean isTokenExpired(String token) {
        Claims claims = extractAllClaims(token);
        Date expiration = claims.getExpiration();
        return expiration.before(new Date());
    }

    private Key getKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }


    @Override
    public String extractUsername(String token) {
        Claims claims = extractAllClaims(token);
        return claims.getSubject();
    }

    public String role(String token) {
        Claims claims = extractAllClaims(token);
        return claims.get("role", String.class);
    }

}

