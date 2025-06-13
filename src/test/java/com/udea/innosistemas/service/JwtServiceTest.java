package com.udea.innosistemas.service;

import java.util.Base64;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;

import io.jsonwebtoken.Claims;

class JwtServiceTest {

    private JwtService jwtService;
    private final String secretKey = Base64.getEncoder().encodeToString("clave-secreta-segura-para-tests".getBytes());
    private final long expirationMillis = 1000 * 60 * 60; // 1 hora

    private UserDetails userDetails;

    @BeforeEach
    void setUp() {
        jwtService = new JwtService();
        ReflectionTestUtils.setField(jwtService, "jwtSecret", secretKey);
        ReflectionTestUtils.setField(jwtService, "jwtExpiration", expirationMillis);

        userDetails = User.withUsername("andrea@udea.edu.co")
                .password("password123")
                .authorities("USER")
                .build();
    }

    @Test
    void generateToken_deberiaGenerarTokenValido() {
        // Act
        String token = jwtService.generateToken(userDetails);

        // Assert
        assertNotNull(token);
        String usernameExtraido = jwtService.extractUsername(token);
        assertEquals(userDetails.getUsername(), usernameExtraido);
    }

    @Test
    void extractClaim_deberiaExtraerElSubject() {
        String token = jwtService.generateToken(userDetails);

        String subject = jwtService.extractClaim(token, Claims::getSubject);
        assertEquals(userDetails.getUsername(), subject);
    }

    @Test
    void isTokenValid_deberiaRetornarTrueParaTokenValido() {
        String token = jwtService.generateToken(userDetails);

        boolean esValido = jwtService.isTokenValid(token, userDetails);
        assertTrue(esValido);
    }

    @Test
    void isTokenValid_deberiaRetornarFalseSiUsuarioEsDiferente() {
        String token = jwtService.generateToken(userDetails);

        UserDetails otroUsuario = User.withUsername("otro@udea.edu.co")
                .password("1234")
                .authorities("USER")
                .build();

        boolean esValido = jwtService.isTokenValid(token, otroUsuario);
        assertFalse(esValido);
    }

    @Test
    void isTokenExpired_deberiaRetornarTrueSiTokenEstaExpirado() throws InterruptedException {
        // Cambiar expiración a -1 segundo para forzar expiración
        ReflectionTestUtils.setField(jwtService, "jwtExpiration", -1000L);

        String tokenExpirado = jwtService.generateToken(userDetails);

        boolean esValido = jwtService.isTokenValid(tokenExpirado, userDetails);
        assertFalse(esValido);
    }

    @Test
    void extractExpiration_deberiaRetornarFechaValida() {
        String token = jwtService.generateToken(userDetails);
        Date expiration = ReflectionTestUtils.invokeMethod(jwtService, "extractExpiration", token);

        assertNotNull(expiration);
        assertTrue(expiration.after(new Date()));
    }
}
