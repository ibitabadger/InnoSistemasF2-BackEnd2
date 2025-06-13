package com.udea.innosistemas.model.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

import com.udea.innosistemas.model.enums.RolEquipo;

public class AgregarMiembroRequestTest {

    @Test
    void getEmail() {
        AgregarMiembroRequest request = new AgregarMiembroRequest("test@udea.edu.co", RolEquipo.MIEMBRO);
        assertEquals("test@udea.edu.co", request.getEmail());
    }

    @Test
    void getRol() {
        AgregarMiembroRequest request = new AgregarMiembroRequest("test@udea.edu.co", RolEquipo.MIEMBRO);
        assertEquals(RolEquipo.MIEMBRO, request.getRol());
    }

    @Test
    void setEmail() {
        AgregarMiembroRequest request = new AgregarMiembroRequest();
        request.setEmail("new@udea.edu.co");
        assertEquals("new@udea.edu.co", request.getEmail());
    }

    @Test
    void setRol() {
        AgregarMiembroRequest request = new AgregarMiembroRequest();
        request.setRol(RolEquipo.LIDER);
        assertEquals(RolEquipo.LIDER, request.getRol());
    }

    @Test
    void testEquals() {
        AgregarMiembroRequest request1 = new AgregarMiembroRequest("a@udea.edu.co", RolEquipo.MIEMBRO);
        AgregarMiembroRequest request2 = new AgregarMiembroRequest("a@udea.edu.co", RolEquipo.MIEMBRO);
        AgregarMiembroRequest request3 = new AgregarMiembroRequest("b@udea.edu.co", RolEquipo.LIDER);
        
        assertEquals(request1, request2);
        assertNotEquals(request1, request3);
    }

    @Test
    void canEqual() {
        AgregarMiembroRequest request1 = new AgregarMiembroRequest("a@udea.edu.co", RolEquipo.MIEMBRO);
        AgregarMiembroRequest request2 = new AgregarMiembroRequest("a@udea.edu.co", RolEquipo.MIEMBRO);
        assertTrue(request1.canEqual(request2));
    
    }

    @Test
    void testHashCode() {
        AgregarMiembroRequest request1 = new AgregarMiembroRequest("a@udea.edu.co", RolEquipo.MIEMBRO);
        AgregarMiembroRequest request2 = new AgregarMiembroRequest("a@udea.edu.co", RolEquipo.MIEMBRO);
        
        assertEquals(request1.hashCode(), request2.hashCode());
    }

    @Test
    void testToString() {
        AgregarMiembroRequest request = new AgregarMiembroRequest("test@udea.edu.co", RolEquipo.MIEMBRO);
        String expected = "AgregarMiembroRequest(email=test@udea.edu.co, rol=MIEMBRO)";
        assertEquals(expected, request.toString());
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