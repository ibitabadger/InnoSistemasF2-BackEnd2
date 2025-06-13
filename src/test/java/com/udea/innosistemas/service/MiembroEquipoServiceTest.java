package com.udea.innosistemas.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.udea.innosistemas.model.dto.AgregarMiembroRequest;
import com.udea.innosistemas.model.dto.MiembroEquipoDto;
import com.udea.innosistemas.model.entity.Equipo;
import com.udea.innosistemas.model.entity.MiembroEquipo;
import com.udea.innosistemas.model.entity.Usuario;
import com.udea.innosistemas.model.enums.RolEquipo;
import com.udea.innosistemas.repository.EquipoRepository;
import com.udea.innosistemas.repository.MiembroEquipoRepository;
import com.udea.innosistemas.repository.UsuarioRepository;

class MiembroEquipoServiceTest {

    private MiembroEquipoService miembroEquipoService;
    private MiembroEquipoRepository miembroEquipoRepository;
    private EquipoRepository equipoRepository;
    private UsuarioRepository usuarioRepository;

    private Usuario lider;
    private Usuario nuevoMiembro;
    private Equipo equipo;

    @BeforeEach
    void setUp() {
        miembroEquipoRepository = mock(MiembroEquipoRepository.class);
        equipoRepository = mock(EquipoRepository.class);
        usuarioRepository = mock(UsuarioRepository.class);
        miembroEquipoService = new MiembroEquipoService(miembroEquipoRepository, equipoRepository, usuarioRepository);

        lider = Usuario.builder().id(1).email("lider@udea.edu.co").nombre("Líder").build();
        nuevoMiembro = Usuario.builder().id(2).email("miembro@udea.edu.co").nombre("Miembro").build();
        equipo = Equipo.builder().id(10).nombre("Equipo Test").creador(lider).build();
    }

    @Test
    void agregarMiembro_deberiaAgregarMiembroSiTodoEsValido() {
        AgregarMiembroRequest request = new AgregarMiembroRequest();
        request.setEmail(nuevoMiembro.getEmail());
        request.setRol(RolEquipo.MIEMBRO);

        when(equipoRepository.findById(equipo.getId())).thenReturn(Optional.of(equipo));
        when(usuarioRepository.findByEmail(lider.getEmail())).thenReturn(Optional.of(lider));
        when(miembroEquipoRepository.existsByEquipoAndUsuarioAndRol(equipo, lider, RolEquipo.LIDER)).thenReturn(true);
        when(usuarioRepository.findByEmail(nuevoMiembro.getEmail())).thenReturn(Optional.of(nuevoMiembro));
        when(miembroEquipoRepository.existsByEquipoAndUsuario(equipo, nuevoMiembro)).thenReturn(false);

        ArgumentCaptor<MiembroEquipo> captor = ArgumentCaptor.forClass(MiembroEquipo.class);
        when(miembroEquipoRepository.save(any())).thenAnswer(i -> i.getArguments()[0]);

        MiembroEquipoDto result = miembroEquipoService.agregarMiembro(equipo.getId(), request, lider.getEmail());

        assertThat(result.getUsuario().getEmail()).isEqualTo(nuevoMiembro.getEmail());
        assertThat(result.getRol()).isEqualTo(RolEquipo.MIEMBRO);
        verify(miembroEquipoRepository).save(captor.capture());
        assertThat(captor.getValue().getFechaIncorporacion()).isBeforeOrEqualTo(LocalDateTime.now());
    }

    @Test
    void eliminarMiembro_deberiaEliminarSiLiderValidoYMiembroPerteneceAlEquipo() {
        MiembroEquipo miembro = MiembroEquipo.builder()
                .id(100)
                .rol(RolEquipo.MIEMBRO)
                .usuario(nuevoMiembro)
                .equipo(equipo)
                .build();

        when(equipoRepository.findById(equipo.getId())).thenReturn(Optional.of(equipo));
        when(usuarioRepository.findByEmail(lider.getEmail())).thenReturn(Optional.of(lider));
        when(miembroEquipoRepository.existsByEquipoAndUsuarioAndRol(equipo, lider, RolEquipo.LIDER)).thenReturn(true);
        when(miembroEquipoRepository.findById(miembro.getId())).thenReturn(Optional.of(miembro));

        miembroEquipoService.eliminarMiembro(equipo.getId(), miembro.getId(), lider.getEmail());

        verify(miembroEquipoRepository).delete(miembro);
    }

    @Test
    void listarMiembrosDeEquipo_deberiaRetornarMiembrosSiUsuarioTieneAcceso() {
        MiembroEquipo miembro = MiembroEquipo.builder()
                .id(100)
                .usuario(nuevoMiembro)
                .equipo(equipo)
                .rol(RolEquipo.MIEMBRO)
                .fechaIncorporacion(LocalDateTime.now())
                .build();

        when(equipoRepository.findById(equipo.getId())).thenReturn(Optional.of(equipo));
        when(usuarioRepository.findByEmail(lider.getEmail())).thenReturn(Optional.of(lider));
        when(miembroEquipoRepository.existsByEquipoAndUsuario(equipo, lider)).thenReturn(true);
        when(miembroEquipoRepository.findByEquipo(equipo)).thenReturn(List.of(miembro));

        List<MiembroEquipoDto> miembros = miembroEquipoService.listarMiembrosDeEquipo(equipo.getId(), lider.getEmail());

        assertThat(miembros).hasSize(1);
        assertThat(miembros.get(0).getUsuario().getEmail()).isEqualTo(nuevoMiembro.getEmail());
    }

    @Test
    void agregarMiembro_lanzaExcepcionSiLiderNoEsLider() {
        when(equipoRepository.findById(equipo.getId())).thenReturn(Optional.of(equipo));
        when(usuarioRepository.findByEmail(lider.getEmail())).thenReturn(Optional.of(lider));
        when(miembroEquipoRepository.existsByEquipoAndUsuarioAndRol(equipo, lider, RolEquipo.LIDER)).thenReturn(false);

        AgregarMiembroRequest request = new AgregarMiembroRequest();
        request.setEmail(nuevoMiembro.getEmail());
        request.setRol(RolEquipo.MIEMBRO);

        assertThatThrownBy(() -> miembroEquipoService.agregarMiembro(equipo.getId(), request, lider.getEmail()))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Solo el líder puede agregar");
    }
}
