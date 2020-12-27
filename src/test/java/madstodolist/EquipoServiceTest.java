package madstodolist;

import madstodolist.model.Equipo;
import madstodolist.model.Usuario;
import madstodolist.service.EquipoService;
import madstodolist.service.UsuarioService;
import org.hibernate.LazyInitializationException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@RunWith(SpringRunner.class)
@SpringBootTest
public class EquipoServiceTest {

    @Autowired
    EquipoService equipoService;

    @Autowired
    UsuarioService usuarioService;

    @Test
    public void obtenerListadoEquipos() {
        // GIVEN
        // En el application.properties se cargan los datos de prueba del fichero datos-test.sql

        // WHEN
        List<Equipo> equipos = equipoService.findAllOrderedByName();

        // THEN
        assertThat(equipos).hasSize(2);
        assertThat(equipos.get(0).getNombre()).isEqualTo("Proyecto P1");
        assertThat(equipos.get(1).getNombre()).isEqualTo("Proyecto P3");
    }

    @Test
    public void obtenerEquipo() {
        // GIVEN
        // En el application.properties se cargan los datos de prueba del fichero datos-test.sql

        // WHEN
        Equipo equipo = equipoService.findById(1L);

        // THEN
        assertThat(equipo.getNombre()).isEqualTo("Proyecto P1");
        // Comprobamos que la relación con Usuarios es lazy: al
        // intentar acceder a la colección de usuarios se debe lanzar una
        // excepción de tipo LazyInitializationException.
        assertThatThrownBy(() -> {
            equipo.getUsuarios().size();
        }).isInstanceOf(LazyInitializationException.class);
    }

    @Test
    public void comprobarRelacionUsuarioEquipos() {
        // GIVEN
        // En el application.properties se cargan los datos de prueba del fichero datos-test.sql

        // WHEN
        Usuario usuario = usuarioService.findById(1L);

        // THEN
        assertThat(usuario.getEquipos()).hasSize(1);
    }

    @Test
    public void obtenerUsuariosEquipo() {
        // GIVEN
        // En el application.properties se cargan los datos de prueba del fichero datos-test.sql

        // WHEN
        List<Usuario> usuarios = equipoService.usuariosEquipo(1L);

        // THEN
        assertThat(usuarios).hasSize(1);
        assertThat(usuarios.get(0).getEmail()).isEqualTo("ana.garcia@gmail.com");
        // Comprobamos que la relación entre usuarios y equipos es eager
        // Primero comprobamos que la colección de equipos tiene 1 elemento
        assertThat(usuarios.get(0).getEquipos()).hasSize(1);
        // Y después que el elemento es el equipo Proyecto P1
        assertThat(usuarios.get(0).getEquipos().stream().findFirst().get().getNombre()).isEqualTo("Proyecto P1");
    }

    @Test
    @Transactional
    public void testNuevoEquipo() {
        // GIVEN

        // WHEN
        Equipo equipo = equipoService.nuevoEquipo("Proyecto test A");

        // THEN
        Equipo equipoBaseDatos = equipoService.findById(equipo.getId());
        assertThat(equipoBaseDatos).isNotNull();
        assertThat(equipoBaseDatos.getNombre().equals("Proyecto test A"));
    }

    @Test
    @Transactional
    public void testAnyadirAEquipo() {
        // GIVEN
        Usuario usuario = new Usuario("jack.skellington@gmail.com");
        usuario.setPassword("123");
        usuario = usuarioService.registrar(usuario);
        Long idUsuario = usuario.getId();
        Equipo equipo = equipoService.nuevoEquipo("Proyecto Navidad");
        Long idEquipo = equipo.getId();

        // WHEN
        equipoService.anyadirAEquipo(idUsuario, idEquipo);

        // THEN
        Usuario usuarioBD = usuarioService.findById(idUsuario);
        Equipo equipoBD = equipoService.findById(idEquipo);
        assertThat(equipoBD.getUsuarios()).contains(usuarioBD); // Equipo contiene usuario
        assertThat(usuarioBD.getEquipos()).contains(equipoBD);  // Usuario contiene equipo
    }

    @Test
    @Transactional
    public void testBorrarDeEquipo() {
        // GIVEN
        // datos-test.sql
        Usuario usuarioBD = usuarioService.findById(1L);
        Equipo equipoBD = equipoService.findById(1L);
        // Por asegurarnos de que el usuario está en el equipo al principio
        assertThat(equipoBD.getUsuarios()).contains(usuarioBD);
        assertThat(usuarioBD.getEquipos()).contains(equipoBD);

        // WHEN
        equipoService.borrarDeEquipo(1L, 1L);

        // THEN
        assertThat(equipoBD.getUsuarios()).doesNotContain(usuarioBD);
        assertThat(usuarioBD.getEquipos()).doesNotContain(equipoBD);
    }

    @Test
    @Transactional
    public void testUsuarioEstaEnEquipo() {
        // GIVEN
        // datos-test.sql

        // WHEN
        boolean esta = equipoService.usuarioEstaEnEquipo(1L, 1L);
        boolean noEsta = equipoService.usuarioEstaEnEquipo(1L, 2L);

        // THEN
        assertThat(esta).isTrue();
        assertThat(noEsta).isFalse();
    }

    @Test
    @Transactional
    public void testBorrarEquipo() {
        // GIVEN
        Equipo equipo = equipoService.nuevoEquipo("Proyecto test A");
        Long idEquipo = equipo.getId();

        // WHEN
        equipoService.borrarEquipo(idEquipo);

        // THEN
        assertThat(equipoService.findById(idEquipo)).isNull();
    }

    @Test
    @Transactional
    public void testModificarEquipo() {
        // GIVEN
        Equipo equipo = equipoService.nuevoEquipo("Proyecto AAA");
        Long idEquipo = equipo.getId();

        // WHEN
        Equipo equipoModificado = equipoService.modificarEquipo(idEquipo, "Proyecto BBB");
        Equipo equipoBD = equipoService.findById(idEquipo);

        // THEN
        assertThat(equipoModificado.getNombre()).isEqualTo("Proyecto BBB");
        assertThat(equipoBD.getNombre()).isEqualTo("Proyecto BBB");
    }
}
