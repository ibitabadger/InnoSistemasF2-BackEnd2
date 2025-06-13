package com.udea.innosistemas.service;

import java.lang.reflect.Field;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import io.jsonwebtoken.ExpiredJwtException;

class JwtServiceTest {

    private JwtService jwtService;

    // Llave secreta de prueba (base64 de 32 bytes)
    private static final String SECRET = "YmFzZTY0U2VjcmV0S2V5Rm9yVGVzdGluZ1B1cnBvc2VzMTIz";

    // Expiración en milisegundos (1 hora)
    private static final long EXPIRATION = 3600000;

    private UserDetails userDetails;

    @BeforeEach
    void setUp() throws Exception {
        jwtService = new JwtService();

        // Inyectamos las propiedades manualmente (porque @Value no funciona aquí)
        setField(jwtService, "jwtSecret", SECRET);
        setField(jwtService, "jwtExpiration", EXPIRATION);

        userDetails = new User("testuser", "password", Collections.emptyList());
    }
    

    @Test
    void testGenerateTokenAndExtractUsername() {
        String token = jwtService.generateToken(userDetails);
        assertNotNull(token);

        String username = jwtService.extractUsername(token);
        assertEquals(userDetails.getUsername(), username);
    }

    @Test
    void testTokenIsValid() {
        String token = jwtService.generateToken(userDetails);
        assertTrue(jwtService.isTokenValid(token, userDetails));
    }

    @Test
    void testTokenIsInvalidWhenUsernameDiffers() {
        String token = jwtService.generateToken(userDetails);
        UserDetails otherUser = new User("otheruser", "password", Collections.emptyList());
        assertFalse(jwtService.isTokenValid(token, otherUser));
    }

    @Test
    
    void testTokenExpired() throws Exception {
    setField(jwtService, "jwtExpiration", 1000L);
    String token = jwtService.generateToken(userDetails);
    Thread.sleep(1500);

    assertThrows(ExpiredJwtException.class, () -> {
        jwtService.isTokenValid(token, userDetails);
    });
    }


    // Método auxiliar para inyectar propiedades privadas usando reflexión
    private void setField(Object target, String fieldName, Object value) throws Exception {
        Field field = JwtService.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(target, value);
    }
}
