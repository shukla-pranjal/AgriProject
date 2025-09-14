package com.farmflow.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.farmflow.service.JwtService;
import com.farmflow.util.CommonUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;

import static com.farmflow.util.Constants.PUBLIC_PATHS;

@Slf4j
@Component
public class JwtFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final ObjectMapper objectMapper;
    private final UserDetailsServiceImpl userDetailsService;


    public JwtFilter(JwtService jwtService, ObjectMapper objectMapper, UserDetailsServiceImpl userDetailsService) {
        this.jwtService = jwtService;
        this.objectMapper = objectMapper;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.info("JwtFilter : doFilterInternal() : Execution Start");
        try {

            String requestPath = request.getRequestURI();
            log.info("Message :{}", requestPath);
            if (Arrays.stream(PUBLIC_PATHS).anyMatch(requestPath::startsWith))
            {

                log.info("Skipping JWT Filter for: {}", requestPath);
                filterChain.doFilter(request, response);
                return;
            }


            String authHeader = request.getHeader("Authorization");
            String token = null;
            String username = null;
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                token = authHeader.substring(7);  // Extract token
                username = jwtService.extractUsername(token);
                log.info("Extracted username: {}", username);
            }

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                if (jwtService.validateToken(token, userDetails)) {
                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(
                                    userDetails, null, userDetails.getAuthorities()
                            );


                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    log.info("Authentication set for user: {}", username);
                } else {
                    log.warn("JWT validation failed for user: {}", username);
                }
            }
        } catch (Exception e) {
            log.error("Error during token authentication: {}", e.getMessage());
            generateResponseError(response, e);
            return;
        }
        log.info("JwtFilter : doFilterInternal() : Execution End");
        filterChain.doFilter(request, response);
    }


    private void generateResponseError(HttpServletResponse response, Exception e) throws IOException {
        response.setContentType("application/json");
        response.setStatus(HttpStatus.UNAUTHORIZED.value());

        ResponseEntity<?> errorResponse = CommonUtil.createErrorResponseMessage(
                e.getMessage(),
                HttpStatus.UNAUTHORIZED
        );

        objectMapper.writeValue(response.getWriter(), errorResponse.getBody());
    }

}