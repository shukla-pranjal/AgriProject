package com.farmflow.service;

import com.farmflow.entity.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public interface JwtService {
    String generateToken(User user);
    String extractUsername(String token);
    boolean validateToken(String token, UserDetails userDetails);
}
