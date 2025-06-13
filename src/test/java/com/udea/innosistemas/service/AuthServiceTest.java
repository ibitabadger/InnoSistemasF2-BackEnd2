// AuthServiceTest.java
package com.udea.innosistemas.service;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.udea.innosistemas.model.dto.LoginRequest;
import com.udea.innosistemas.model.dto.LoginResponse;
import com.udea.innosistemas.model.dto.RegistroRequest;
import com.udea.innosistemas.model.dto.UsuarioDto;
import com.udea.innosistemas.model.entity.Usuario;
import com.udea.innosistemas.model.enums.RolSistema;
import com.udea.innosistemas.repository.UsuarioRepository;

class AuthServiceTest {

    private UsuarioRepository usuarioRepository;
    private PasswordEncoder passwordEncoder;
    private JwtService jwtService;
    private AuthenticationManager authenticationManager;
    private UsuarioService usuarioService;
    private AuthService authService;

    @BeforeEach
    void setUp() {
        usuarioRepository = mock(UsuarioRepository.class);
        passwordEncoder = mock(PasswordEncoder.class);
        jwtService = mock(JwtService.class);
        authenticationManager = mock(AuthenticationManager.class);
        usuarioService = mock(UsuarioService.class);

        authService = new AuthService(
                usuarioRepository,
                passwordEncoder,
                jwtService,
                authenticationManager,
                usuarioService
        );
    }

    @Test
    void registrarUsuario_deberiaRegistrarCorrectamente() {
        RegistroRequest request = RegistroRequest.builder()
                .nombre("Andrea")
                .email("andrea@udea.edu.co")
                .password("1234")
                .rol(RolSistema.ESTUDIANTE)
                .build();

        when(usuarioRepository.existsByEmail(request.getEmail())).thenReturn(false);
        when(passwordEncoder.encode(request.getPassword())).thenReturn("hashed1234");
        when(jwtService.generateToken(any(Usuario.class))).thenReturn("mock-jwt");

        // Simulamos que el repositorio guarda el usuario y le asigna un ID
        Usuario usuarioGuardado = Usuario.builder()
                .id(1)
                .nombre(request.getNombre())
                .email(request.getEmail())
                .password("hashed1234")
                .rol(request.getRol())
                .fechaCreacion(LocalDateTime.now())
                .build();

        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuarioGuardado);

        LoginResponse response = authService.registrarUsuario(request);

        assertNotNull(response);
        assertEquals("mock-jwt", response.getToken());
        assertEquals(request.getEmail(), response.getUsuario().getEmail());

        verify(usuarioRepository, times(1)).save(any(Usuario.class));
        verify(jwtService, times(1)).generateToken(any(Usuario.class));
    }

    @Test
    void registrarUsuario_deberiaLanzarExcepcionSiEmailExiste() {
        RegistroRequest request = RegistroRequest.builder()
                .nombre("Andrea")
                .email("andrea@udea.edu.co")
                .password("1234")
                .rol(RolSistema.ESTUDIANTE)
                .build();

        when(usuarioRepository.existsByEmail(request.getEmail())).thenReturn(true);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            authService.registrarUsuario(request);
        });

        assertEquals("El email ya está en uso", exception.getMessage());
        verify(usuarioRepository, never()).save(any());
    }

    @Test
    void autenticarUsuario_deberiaAutenticarCorrectamente() {
        LoginRequest loginRequest = LoginRequest.builder()
                .email("andrea@udea.edu.co")
                .password("1234")
                .build();

        Usuario usuario = Usuario.builder()
                .email(loginRequest.getEmail())
                .nombre("Andrea")
                .rol(RolSistema.ESTUDIANTE)
                .build();

        Authentication authentication = mock(Authentication.class);

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(usuario);
        when(jwtService.generateToken(usuario)).thenReturn("mock-jwt");
        when(usuarioService.buscarPorEmail(usuario.getEmail()))
                .thenReturn(UsuarioDto.builder()
                        .email(usuario.getEmail())
                        .nombre(usuario.getNombre())
                        .rol(usuario.getRol())
                        .build());

        LoginResponse response = authService.autenticarUsuario(loginRequest);

        assertNotNull(response);
        assertEquals("mock-jwt", response.getToken());
        assertEquals("Andrea", response.getUsuario().getNombre());
        verify(usuarioService, times(1)).actualizarUltimoAcceso(usuario.getEmail());
    }
}
