package com.udea.innosistemas.model.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

import com.udea.innosistemas.model.enums.RolEquipo;

public class AgregarMiembroRequestTest {

    @Test
    void getEmail() {
    }

    @Test
    void getRol() {
    }

    @Test
    void setEmail() {
    }

    @Test
    void setRol() {
    }

    @Test
    void testEquals() {
    }

    @Test
    void canEqual() {
    }

    @Test
    void testHashCode() {
    }

    @Test
    void testToString() {
    }

    @Test
    void testGettersAndSetters() {
        AgregarMiembroRequest request = new AgregarMiembroRequest();

        // Test setters y getters para email
        String email = "test@udea.edu.co";
        request.setEmail(email);
        assertEquals(email, request.getEmail());

        // Test setters y getters para rol
        RolEquipo rol = RolEquipo.MIEMBRO;
        request.setRol(rol);
        assertEquals(rol, request.getRol());
    }

    @Test
    void testAllArgsConstructor() {
        String email = "test@udea.edu.co";
        RolEquipo rol = RolEquipo.LIDER;

        AgregarMiembroRequest request = new AgregarMiembroRequest(email, rol);

        assertEquals(email, request.getEmail());
        assertEquals(rol, request.getRol());
    }
}