package com.udea.innosistemas.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.udea.innosistemas.model.entity.Usuario;
import com.udea.innosistemas.model.enums.RolSistema;
import com.udea.innosistemas.repository.UsuarioRepository;

class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private UsuarioService usuarioService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void buscarPorEmail_deberiaRetornarUsuarioDtoSiExiste() {
        // Arrange
        String email = "andrea@udea.edu.co";
        Usuario usuario = crearUsuario(1, email);
        when(usuarioRepository.findByEmail(email)).thenReturn(Optional.of(usuario));

        // Act
        var dto = usuarioService.buscarPorEmail(email);

        // Assert
        assertEquals(email, dto.getEmail());
        assertEquals("Andrea", dto.getNombre());
    }

    @Test
    void buscarPorEmail_deberiaLanzarExcepcionSiNoExiste() {
        // Arrange
        String email = "inexistente@udea.edu.co";
        when(usuarioRepository.findByEmail(email)).thenReturn(Optional.empty());

        // Assert
        assertThrows(UsernameNotFoundException.class, () -> usuarioService.buscarPorEmail(email));
    }

    @Test
    void buscarPorId_deberiaRetornarUsuarioDtoSiExiste() {
        // Arrange
        int id = 1;
        Usuario usuario = crearUsuario(id, "andrea@udea.edu.co");
        when(usuarioRepository.findById(id)).thenReturn(Optional.of(usuario));

        // Act
        var dto = usuarioService.buscarPorId(id);

        // Assert
        assertEquals(id, dto.getId());
        assertEquals("Andrea", dto.getNombre());
    }

    @Test
    void buscarPorId_deberiaLanzarExcepcionSiNoExiste() {
        // Arrange
        int id = 999;
        when(usuarioRepository.findById(id)).thenReturn(Optional.empty());

        // Assert
        assertThrows(UsernameNotFoundException.class, () -> usuarioService.buscarPorId(id));
    }

    @Test
    void listarUsuarios_deberiaRetornarListaDeUsuarios() {
        // Arrange
        Usuario usuario1 = crearUsuario(1, "uno@udea.edu.co");
        Usuario usuario2 = crearUsuario(2, "dos@udea.edu.co");

        when(usuarioRepository.findAll()).thenReturn(List.of(usuario1, usuario2));

        // Act
        var lista = usuarioService.listarUsuarios();

        // Assert
        assertEquals(2, lista.size());
        assertTrue(lista.stream().anyMatch(u -> u.getEmail().equals("uno@udea.edu.co")));
    }

    @Test
    void actualizarUltimoAcceso_deberiaActualizarYGuardarUsuario() {
        // Arrange
        String email = "andrea@udea.edu.co";
        Usuario usuario = crearUsuario(1, email);
        usuario.setUltimoAcceso(null);

        when(usuarioRepository.findByEmail(email)).thenReturn(Optional.of(usuario));

        // Act
        usuarioService.actualizarUltimoAcceso(email);

        // Assert
        assertNotNull(usuario.getUltimoAcceso());
        verify(usuarioRepository).save(usuario);
    }

    @Test
    void actualizarUltimoAcceso_deberiaLanzarExcepcionSiNoExiste() {
        // Arrange
        String email = "desconocido@udea.edu.co";
        when(usuarioRepository.findByEmail(email)).thenReturn(Optional.empty());

        // Assert
        assertThrows(UsernameNotFoundException.class, () -> usuarioService.actualizarUltimoAcceso(email));
    }

    private Usuario crearUsuario(int id, String email) {
        Usuario usuario = new Usuario();
        usuario.setId(id);
        usuario.setNombre("Andrea");
        usuario.setEmail(email);
        usuario.setRol(RolSistema.ESTUDIANTE);
        usuario.setFechaCreacion(LocalDateTime.now());
        return usuario;
    }
}
