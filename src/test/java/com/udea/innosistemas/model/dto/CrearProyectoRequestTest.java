package com.udea.innosistemas.model.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

class CrearProyectoRequestTest {

    @Test
    void getNombre() {
        CrearProyectoRequest request = new CrearProyectoRequest("Proyecto ABC", "Descripción del proyecto", 1);
        assertEquals("Proyecto ABC", request.getNombre());
    }

    @Test
    void getDescripcion() {
        CrearProyectoRequest request = new CrearProyectoRequest("Proyecto ABC", "Descripción detallada", 1);
        assertEquals("Descripción detallada", request.getDescripcion());
    }

    @Test
    void getEquipoId() {
        CrearProyectoRequest request = new CrearProyectoRequest("Proyecto ABC", "Descripción", 5);
        assertEquals(5, request.getEquipoId());
    }

    @Test
    void setNombre() {
        CrearProyectoRequest request = new CrearProyectoRequest();
        request.setNombre("Nuevo Proyecto");
        assertEquals("Nuevo Proyecto", request.getNombre());
    }

    @Test
    void setDescripcion() {
        CrearProyectoRequest request = new CrearProyectoRequest();
        request.setDescripcion("Nueva descripción");
        assertEquals("Nueva descripción", request.getDescripcion());
    }

    @Test
    void setEquipoId() {
        CrearProyectoRequest request = new CrearProyectoRequest();
        request.setEquipoId(10);
        assertEquals(10, request.getEquipoId());
    }

    @Test
    void testEquals() {
        CrearProyectoRequest request1 = new CrearProyectoRequest("Proyecto 1", "Desc 1", 1);
        CrearProyectoRequest request2 = new CrearProyectoRequest("Proyecto 1", "Desc 1", 1);
        CrearProyectoRequest request3 = new CrearProyectoRequest("Proyecto 2", "Desc 2", 2);
        
        assertEquals(request1, request2);
        assertNotEquals(request1, request3);
    }

    @Test
    void canEqual() {
        CrearProyectoRequest request1 = new CrearProyectoRequest("Proyecto", "Desc", 1);
        CrearProyectoRequest request2 = new CrearProyectoRequest("Proyecto", "Desc", 1);
        
        assertTrue(request1.canEqual(request2));
    }

    @Test
    void testHashCode() {
        CrearProyectoRequest request1 = new CrearProyectoRequest("Proyecto", "Desc", 1);
        CrearProyectoRequest request2 = new CrearProyectoRequest("Proyecto", "Desc", 1);
        
        assertEquals(request1.hashCode(), request2.hashCode());
    }

    @Test
    void testToString() {
        CrearProyectoRequest request = new CrearProyectoRequest("Mi Proyecto", "Descripción larga", 3);
        String expected = "CrearProyectoRequest(nombre=Mi Proyecto, descripcion=Descripción larga, equipoId=3)";
        
        assertEquals(expected, request.toString());
    }

    @Test
    void noArgsConstructor() {
        CrearProyectoRequest request = new CrearProyectoRequest();
        assertNull(request.getNombre());
        assertNull(request.getDescripcion());
        assertNull(request.getEquipoId());
    }

    @Test
    void allArgsConstructor() {
        CrearProyectoRequest request = new CrearProyectoRequest("Test", "Test Desc", 99);
        assertEquals("Test", request.getNombre());
        assertEquals("Test Desc", request.getDescripcion());
        assertEquals(99, request.getEquipoId());
    }
}