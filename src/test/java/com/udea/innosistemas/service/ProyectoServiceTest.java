// package com.udea.innosistemas.service;

// import java.util.Optional;

// import static org.junit.jupiter.api.Assertions.assertEquals;
// import static org.junit.jupiter.api.Assertions.assertNotNull;
// import static org.junit.jupiter.api.Assertions.assertThrows;
// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;
// import static org.mockito.ArgumentMatchers.any;
// import org.mockito.InjectMocks;
// import org.mockito.Mock;
// import static org.mockito.Mockito.when;
// import org.mockito.MockitoAnnotations;
// import org.springframework.security.core.userdetails.UsernameNotFoundException;

// import com.udea.innosistemas.exception.ResourceNotFoundException;
// import com.udea.innosistemas.model.dto.CrearProyectoRequest;
// import com.udea.innosistemas.model.dto.ProyectoDto;
// import com.udea.innosistemas.model.entity.Equipo;
// import com.udea.innosistemas.model.entity.Proyecto;
// import com.udea.innosistemas.model.entity.Usuario;
// import com.udea.innosistemas.repository.EquipoRepository;
// import com.udea.innosistemas.repository.MiembroEquipoRepository;
// import com.udea.innosistemas.repository.ProyectoRepository;
// import com.udea.innosistemas.repository.UsuarioRepository;

// public class ProyectoServiceTest {

//     @Mock
//     private ProyectoRepository proyectoRepository;
//     @Mock
//     private EquipoRepository equipoRepository;
//     @Mock
//     private UsuarioRepository usuarioRepository;
//     @Mock
//     private MiembroEquipoRepository miembroEquipoRepository;

//     @InjectMocks
//     private ProyectoService proyectoService;

//     private Equipo equipo;
//     private Usuario usuario;
//     private CrearProyectoRequest request;

//     @BeforeEach
//     public void setUp() {
//         MockitoAnnotations.openMocks(this);

//         equipo = Equipo.builder()
//                 .id(1)
//                 .nombre("Equipo 1")
//                 .build();

//         usuario = Usuario.builder()
//                 .id(1)
//                 .nombre("John Doe")
//                 .email("john.doe@example.com")
//                 .build();

//         // request = CrearProyectoRequest.builder()
//         //         .nombre("Proyecto Test")
//         //         .descripcion("Descripcion de prueba")
//         //         .equipoId(equipo.getId())
//         //         .build();
//     }

//     @Test
//     public void testCrearProyecto_Exitoso() {
//         // Arrange
//         when(equipoRepository.findById(equipo.getId())).thenReturn(Optional.of(equipo));
//         when(usuarioRepository.findByEmail(usuario.getEmail())).thenReturn(Optional.of(usuario));
//         when(miembroEquipoRepository.existsByEquipoAndUsuario(equipo, usuario)).thenReturn(true);
//         when(proyectoRepository.existsByNombreAndEquipo(request.getNombre(), equipo)).thenReturn(false);
//         when(proyectoRepository.save(any(Proyecto.class)))
//                 .thenAnswer(invocation -> {
//                     Proyecto p = invocation.getArgument(0);
//                     p.setId(1); // simulamos que ya tiene ID luego de persistir
//                     return p;
//                 });

//         // Act
//         ProyectoDto proyectoDto = proyectoService.crearProyecto(request, usuario.getEmail());

//         // Assert
//         assertNotNull(proyectoDto);
//         assertEquals(request.getNombre(), proyectoDto.getNombre());
//         assertEquals(request.getDescripcion(), proyectoDto.getDescripcion());
//     }

//     @Test
//     public void testCrearProyecto_EquipoNoEncontrado() {
//         when(equipoRepository.findById(equipo.getId())).thenReturn(Optional.empty());

//         ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
//             proyectoService.crearProyecto(request, usuario.getEmail());
//         });

//         // assertEquals("Equipo", exception.getResourceName());
//     }

//     @Test
//     public void testCrearProyecto_UsuarioNoEncontrado() {
//         when(equipoRepository.findById(equipo.getId())).thenReturn(Optional.of(equipo));
//         when(usuarioRepository.findByEmail(usuario.getEmail())).thenReturn(Optional.empty());

//         assertThrows(UsernameNotFoundException.class, () -> {
//             proyectoService.crearProyecto(request, usuario.getEmail());
//         });
//     }

//     @Test
//     public void testCrearProyecto_UsuarioNoEsMiembro() {
//         when(equipoRepository.findById(equipo.getId())).thenReturn(Optional.of(equipo));
//         when(usuarioRepository.findByEmail(usuario.getEmail())).thenReturn(Optional.of(usuario));
//         when(miembroEquipoRepository.existsByEquipoAndUsuario(equipo, usuario)).thenReturn(false);

//         RuntimeException exception = assertThrows(RuntimeException.class, () -> {
//             proyectoService.crearProyecto(request, usuario.getEmail());
//         });

//         assertEquals("El usuario no es miembro del equipo", exception.getMessage());
//     }

//     @Test
//     public void testCrearProyecto_NombreDuplicado() {
//         when(equipoRepository.findById(equipo.getId())).thenReturn(Optional.of(equipo));
//         when(usuarioRepository.findByEmail(usuario.getEmail())).thenReturn(Optional.of(usuario));
//         when(miembroEquipoRepository.existsByEquipoAndUsuario(equipo, usuario)).thenReturn(true);
//         when(proyectoRepository.existsByNombreAndEquipo(request.getNombre(), equipo)).thenReturn(true);

//         RuntimeException exception = assertThrows(RuntimeException.class, () -> {
//             proyectoService.crearProyecto(request, usuario.getEmail());
//         });

//         assertEquals("Ya existe un proyecto con el nombre: " + request.getNombre(), exception.getMessage());
//     }
// }
