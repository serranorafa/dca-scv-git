package madstodolist;

import madstodolist.model.Usuario;
import madstodolist.model.UsuarioRepository;
import madstodolist.service.UsuarioService;
import madstodolist.service.UsuarioServiceException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UsuarioTest {

    Logger logger = LoggerFactory.getLogger(UsuarioTest.class);

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Test
    public void crearUsuario() throws Exception {

        // GIVEN
        Usuario usuario = new Usuario("juan.gutierrez@gmail.com");

        // WHEN
        usuario.setNombre("Juan Gutiérrez");
        usuario.setPassword("12345678");

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        usuario.setFechaNacimiento(sdf.parse("1997-02-20"));

        // THEN
        assertThat(usuario.getEmail()).isEqualTo("juan.gutierrez@gmail.com");
        assertThat(usuario.getNombre()).isEqualTo("Juan Gutiérrez");
        assertThat(usuario.getPassword()).isEqualTo("12345678");
        assertThat(usuario.getFechaNacimiento()).isEqualTo(sdf.parse("1997-02-20"));
    }

    @Test
    @Transactional
    public void crearUsuarioBaseDatos() throws Exception {

        // GIVEN
        Usuario usuario = new Usuario("juan.gutierrez@gmail.com");
        usuario.setNombre("Juan Gutiérrez");
        usuario.setPassword("12345678");

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        usuario.setFechaNacimiento(sdf.parse("1997-02-20"));

        // WHEN

        usuarioRepository.save(usuario);

        // THEN
        assertThat(usuario.getId()).isNotNull();
        logger.info("Identificador del usuario: " + usuario.getId());
        assertThat(usuario.getEmail()).isEqualTo("juan.gutierrez@gmail.com");
        assertThat(usuario.getNombre()).isEqualTo("Juan Gutiérrez");
        assertThat(usuario.getPassword()).isEqualTo("12345678");
        assertThat(usuario.getFechaNacimiento()).isEqualTo(sdf.parse("1997-02-20"));
    }

    @Test
    @Transactional(readOnly = true)
    public void buscarUsuarioEnBaseDatos() {
        // GIVEN
        // En el application.properties se cargan los datos de prueba del fichero datos-test.sql

        // WHEN

        Usuario usuario = usuarioRepository.findById(1L).orElse(null);

        // THEN
        assertThat(usuario).isNotNull();
        assertThat(usuario.getId()).isEqualTo(1L);
        assertThat(usuario.getNombre()).isEqualTo("Ana García");
    }

    @Test
    @Transactional(readOnly = true)
    public void buscarUsuarioPorEmail() {
        // GIVEN
        // Datos cargados de datos-test.sql

        // WHEN
        Usuario usuario = usuarioRepository.findByEmail("ana.garcia@gmail.com").orElse(null);

        // THEN
        assertThat(usuario.getNombre()).isEqualTo("Ana García");
    }


    @Test
    public void comprobarIgualdadSinId() {
        // GIVEN

        Usuario usuario1 = new Usuario("mariafernandez@gmail.com");
        Usuario usuario2 = new Usuario("mariafernandez@gmail.com");
        Usuario usuario3 = new Usuario("antoniolopez@gmail.com");

        // THEN

        assertThat(usuario1).isEqualTo(usuario1);
        assertThat(usuario1).isEqualTo(usuario2);
        assertThat(usuario1).isNotEqualTo(usuario3);
    }

    @Test
    public void comprobarIgualdadConId() {
        // GIVEN
        Usuario usuario1 = new Usuario("juangutierrez@gmail.com");
        usuario1.setId(1L);

        Usuario usuario2 = new Usuario("mariafernandez@gmail.com");
        usuario2.setId(1L);

        Usuario usuario3 = new Usuario("antoniolopez@gmail.com");
        usuario3.setId(2L);

        // THEN
        assertThat(usuario1).isEqualTo(usuario2);
        assertThat(usuario1).isNotEqualTo(usuario3);
    }
}