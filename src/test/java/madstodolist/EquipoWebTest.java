package madstodolist;

import madstodolist.authentication.ManagerUserSesion;
import madstodolist.model.Equipo;
import madstodolist.model.Tarea;
import madstodolist.model.Usuario;
import madstodolist.service.EquipoService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class EquipoWebTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EquipoService equipoService;

    @MockBean
    private ManagerUserSesion managerUserSesion;

    @Test
    public void listadoEquipos() throws Exception {
        Equipo equipo1 = new Equipo("Proyecto P1");
        Equipo equipo2 = new Equipo("Proyecto P2");

        when(equipoService.findAllOrderedByName()).thenReturn(Arrays.asList(equipo1, equipo2));

        this.mockMvc.perform(get("/equipos"))
                .andDo(print())
                .andExpect(content().string(allOf(
                        containsString("Listado de equipos"),
                        containsString("Proyecto P1"),
                        containsString("Proyecto P2"))));

    }

    @Test
    public void listadoUsuariosEquipo() throws Exception {
        Equipo equipo = new Equipo("Proyecto P1");
        Usuario usuario = new Usuario("ana.garcia@gmail.com");
        usuario.setNombre("Ana García");
        usuario.setId(1L);

        when(equipoService.findById(1L)).thenReturn(equipo);
        when(equipoService.usuariosEquipo(1L)).thenReturn(Arrays.asList(usuario));

        this.mockMvc.perform(get("/equipos/1/usuarios"))
                .andDo(print())
                .andExpect(content().string(allOf(
                        containsString("Componentes de Proyecto P1"),
                        containsString("Ana García"))));

    }

    @Test
    public void nuevoEquipoDevuelveForm() throws Exception {
        this.mockMvc.perform(get("/equipos/nuevo"))
                .andDo(print())
                .andExpect(content().string(containsString("action=\"/equipos/nuevo\"")));
    }

    @Test
    public void editarEquipoDevuelveForm() throws Exception {
        Equipo equipo = new Equipo("El equipo A");
        equipo.setId(3L);

        when(equipoService.findById(3L)).thenReturn(equipo);

        this.mockMvc.perform(get("/equipos/3/editar"))
                .andDo(print())
                .andExpect(content().string(allOf(
                        // Contiene la acción para enviar el post a la URL correcta
                        containsString("action=\"/equipos/3/editar\""),
                        // Contiene el texto de la tarea a editar
                        containsString("El equipo A"),
                        // Contiene enlace a listar equipos si se cancela la edición
                        containsString("href=\"/equipos\""))));
    }

}
