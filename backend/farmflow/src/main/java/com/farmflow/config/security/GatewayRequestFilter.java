package com.farmflow.config.security;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Filter to ensure all requests come through the Eureka Gateway
 * Blocks direct external access to the service
 */
@Slf4j
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class GatewayRequestFilter implements Filter {

    @Value("${gateway.validation.enabled:true}")
    private boolean gatewayValidationEnabled;

    @Value("${gateway.header.name:X-Gateway-Request}")
    private String gatewayHeaderName;

    @Value("${gateway.header.secret:gateway-secret-key-12345}")
    private String gatewayHeaderSecret;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        // Skip validation if disabled (for testing/development)
        if (!gatewayValidationEnabled) {
            chain.doFilter(request, response);
            return;
        }

        // Allow actuator endpoints for health checks
        String requestURI = httpRequest.getRequestURI();
        if (requestURI.startsWith("/actuator/")) {
            chain.doFilter(request, response);
            return;
        }

        // Check for gateway header
        String gatewayHeader = httpRequest.getHeader(gatewayHeaderName);

        if (gatewayHeader == null || !gatewayHeader.equals(gatewayHeaderSecret)) {
            log.warn("Direct access attempt blocked from: {} to: {}",
                    httpRequest.getRemoteAddr(), requestURI);

            httpResponse.setStatus(HttpServletResponse.SC_FORBIDDEN);
            httpResponse.setContentType("application/json");
            httpResponse.getWriter().write(
                    "{\"timestamp\":\"" + java.time.Instant.now() + "\"," +
                            "\"status\":403," +
                            "\"error\":\"Forbidden\"," +
                            "\"message\":\"Direct access not allowed. Please use the API Gateway.\"," +
                            "\"path\":\"" + requestURI + "\"}");
            return;
        }

        // Valid gateway request, proceed
        chain.doFilter(request, response);
    }
}
