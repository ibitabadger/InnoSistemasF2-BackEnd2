package com.udea.innosistemas.model.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

class CrearEquipoRequestTest {

    @Test
    void getNombre() {
        // Arrange
        String nombreEsperado = "Equipo A";
        CrearEquipoRequest request = new CrearEquipoRequest(nombreEsperado);

        // Act
        String nombreActual = request.getNombre();

        // Assert
        assertEquals(nombreEsperado, nombreActual);
    }

    @Test
    void setNombre() {
        // Arrange
        CrearEquipoRequest request = new CrearEquipoRequest();
        String nuevoNombre = "Equipo B";

        // Act
        request.setNombre(nuevoNombre);

        // Assert
        assertEquals(nuevoNombre, request.getNombre());
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

}