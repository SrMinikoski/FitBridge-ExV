package com.fitbridge.util;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.Map;

/**
 * Utilitário para validação de autenticação baseada em headers.
 * A aplicação Angular envia X-User-ID e X-User-Type nos headers.
 */
public class AuthValidationUtil {

    public static ResponseEntity<?> validateUserAuth(
            @RequestHeader(value = "X-User-ID", required = false) String userId,
            @RequestHeader(value = "X-User-Type", required = false) String userType) {
        
        if (userId == null || userId.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "X-User-ID header is required for authenticated requests"));
        }
        
        if (userType == null || userType.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "X-User-Type header is required for authenticated requests"));
        }
        
        return null; // Validação passou
    }

    public static Long getUserIdFromHeader(String userId) {
        try {
            return Long.parseLong(userId);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
