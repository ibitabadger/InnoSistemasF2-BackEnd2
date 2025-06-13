package com.udea.innosistemas.model.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

public class LoginRequestTest {

    private static Validator validator;

    @BeforeAll
    static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void getEmail_DevuelveEmailCorrecto() {
        // Arrange
        String emailEsperado = "usuario@udea.edu.co";
        LoginRequest request = new LoginRequest(emailEsperado, "password123");

        // Act
        String emailActual = request.getEmail();

        // Assert
        assertEquals(emailEsperado, emailActual);
    }

    @Test
    void getPassword_DevuelvePasswordCorrecto() {
        // Arrange
        String passwordEsperado = "securePassword123";
        LoginRequest request = new LoginRequest("test@udea.edu.co", passwordEsperado);

        // Act
        String passwordActual = request.getPassword();

        // Assert
        assertEquals(passwordEsperado, passwordActual);
    }

    @Test
    void setEmail_ActualizaCorrectamente() {
        // Arrange
        LoginRequest request = new LoginRequest();
        String nuevoEmail = "nuevo@udea.edu.co";

        // Act
        request.setEmail(nuevoEmail);

        // Assert
        assertEquals(nuevoEmail, request.getEmail());
    }

    @Test
    void setPassword_ActualizaCorrectamente() {
        // Arrange
        LoginRequest request = new LoginRequest();
        String nuevoPassword = "newPassword456";

        // Act
        request.setPassword(nuevoPassword);

        // Assert
        assertEquals(nuevoPassword, request.getPassword());
    }
}