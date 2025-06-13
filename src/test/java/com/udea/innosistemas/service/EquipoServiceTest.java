package com.udea.innosistemas.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

import com.udea.innosistemas.exception.ResourceNotFoundException;
import com.udea.innosistemas.model.dto.CrearEquipoRequest;
import com.udea.innosistemas.model.entity.Equipo;
import com.udea.innosistemas.model.entity.MiembroEquipo;
import com.udea.innosistemas.model.entity.Usuario;
import com.udea.innosistemas.repository.EquipoRepository;
import com.udea.innosistemas.repository.MiembroEquipoRepository;
import com.udea.innosistemas.repository.UsuarioRepository;

class EquipoServiceTest {

    @InjectMocks
    private EquipoService equipoService;

    @Mock
    private EquipoRepository equipoRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private MiembroEquipoRepository miembroEquipoRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void crearEquipo_deberiaCrearEquipoYAgregarCreadorComoLider() {
        // Arrange
        String email = "andrea@udea.edu.co";
        CrearEquipoRequest request = new CrearEquipoRequest();
        request.setNombre("Equipo Test");

        Usuario usuario = new Usuario();
        usuario.setId(1);
        usuario.setEmail(email);
        usuario.setNombre("Andrea");

        Equipo equipo = Equipo.builder()
                .id(1)
                .nombre("Equipo Test")
                .fechaCreacion(LocalDateTime.now())
                .creador(usuario)
                .build();

        when(equipoRepository.existsByNombre(request.getNombre())).thenReturn(false);
        when(usuarioRepository.findByEmail(email)).thenReturn(Optional.of(usuario));
        when(equipoRepository.save(any(Equipo.class))).thenReturn(equipo);
        when(miembroEquipoRepository.save(any(MiembroEquipo.class))).thenReturn(null);
        when(miembroEquipoRepository.countByEquipo(any())).thenReturn(1);

        // Act
        var dto = equipoService.crearEquipo(request, email);

        // Assert
        assertEquals("Equipo Test", dto.getNombre());
        assertEquals(1, dto.getTotalMiembros());
        assertEquals("Andrea", dto.getCreador().getNombre());
    }

    @Test
    void listarEquiposDelUsuario_deberiaRetornarListaDeEquipos() {
        // Arrange
        String email = "andrea@udea.edu.co";
        Usuario usuario = new Usuario();
        usuario.setId(1);
        usuario.setEmail(email);

        Equipo equipo = Equipo.builder()
                .id(1)
                .nombre("Equipo Test")
                .fechaCreacion(LocalDateTime.now())
                .creador(usuario)
                .build();

        when(usuarioRepository.findByEmail(email)).thenReturn(Optional.of(usuario));
        when(equipoRepository.findByMiembro(usuario)).thenReturn(List.of(equipo));
        when(miembroEquipoRepository.countByEquipo(equipo)).thenReturn(3);

        // Act
        var lista = equipoService.listarEquiposDelUsuario(email);

        // Assert
        assertEquals(1, lista.size());
        assertEquals("Equipo Test", lista.get(0).getNombre());
        assertEquals(3, lista.get(0).getTotalMiembros());
    }

    @Test
    void obtenerEquipo_deberiaRetornarEquipoSiUsuarioPertenece() {
        // Arrange
        Integer equipoId = 1;
        String email = "andrea@udea.edu.co";

        Usuario usuario = new Usuario();
        usuario.setId(1);
        usuario.setEmail(email);
        usuario.setNombre("Andrea");

        Equipo equipo = Equipo.builder()
                .id(equipoId)
                .nombre("Equipo Test")
                .fechaCreacion(LocalDateTime.now())
                .creador(usuario)
                .build();

        when(equipoRepository.findById(equipoId)).thenReturn(Optional.of(equipo));
        when(usuarioRepository.findByEmail(email)).thenReturn(Optional.of(usuario));
        when(miembroEquipoRepository.existsByEquipoAndUsuario(equipo, usuario)).thenReturn(true);
        when(miembroEquipoRepository.countByEquipo(equipo)).thenReturn(4);

        // Act
        var dto = equipoService.obtenerEquipo(equipoId, email);

        // Assert
        assertEquals("Equipo Test", dto.getNombre());
        assertEquals(4, dto.getTotalMiembros());
    }

    @Test
    void obtenerEquipo_deberiaLanzarExcepcionSiUsuarioNoPertenece() {
        // Arrange
        Integer equipoId = 1;
        String email = "andrea@udea.edu.co";

        Usuario usuario = new Usuario();
        usuario.setId(1);
        usuario.setEmail(email);

        Equipo equipo = new Equipo();
        equipo.setId(equipoId);
        equipo.setNombre("Equipo Test");

        when(equipoRepository.findById(equipoId)).thenReturn(Optional.of(equipo));
        when(usuarioRepository.findByEmail(email)).thenReturn(Optional.of(usuario));
        when(miembroEquipoRepository.existsByEquipoAndUsuario(equipo, usuario)).thenReturn(false);

        // Assert
        assertThrows(RuntimeException.class, () -> equipoService.obtenerEquipo(equipoId, email));
    }

    @Test
    void obtenerEquipo_deberiaLanzarExcepcionSiNoExisteEquipo() {
        // Arrange
        Integer equipoId = 1;
        String email = "andrea@udea.edu.co";

        when(equipoRepository.findById(equipoId)).thenReturn(Optional.empty());

        // Assert
        assertThrows(ResourceNotFoundException.class, () -> equipoService.obtenerEquipo(equipoId, email));
    }
}
