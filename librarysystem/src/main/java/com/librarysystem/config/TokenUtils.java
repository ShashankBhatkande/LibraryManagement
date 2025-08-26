package com.librarysystem.config;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;

public class TokenUtils {
    public static String getCurrentToken() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null) {
            // Case 1: Token stored in credentials
            Object credentials = authentication.getCredentials();
            if (credentials instanceof String token && !token.isBlank()) {
                return token;
            }

            // Case 2: Token stored in details
            Object details = authentication.getDetails();
            if (details instanceof String token && !token.isBlank()) {
                return token;
            }
        }

        // Case 3: Fallback to Authorization header
        ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attrs != null) {
            HttpServletRequest request = attrs.getRequest();
            String header = request.getHeader("Authorization");
            if (header != null && header.startsWith("Bearer ")) {
                return header.substring(7);
            }
        }

        return null;
    }
}
