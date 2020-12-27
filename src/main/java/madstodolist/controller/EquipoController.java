package madstodolist.controller;

import madstodolist.authentication.ManagerUserSesion;
import madstodolist.controller.exception.EquipoNotFoundException;
import madstodolist.model.Equipo;
import madstodolist.model.Usuario;
import madstodolist.service.EquipoService;
import madstodolist.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class EquipoController {

    @Autowired
    EquipoService equipoService;

    @Autowired
    UsuarioService usuarioService;

    @Autowired
    ManagerUserSesion managerUserSesion;

    @GetMapping("/equipos")
    public String listadoEquipos(Model model, HttpSession session) {
        managerUserSesion.comprobarCualquierUsuarioLogeado(session);
        List<Equipo> equipos = equipoService.findAllOrderedByName();
        model.addAttribute("equipos", equipos);
        model.addAttribute("equipoService", equipoService); // Para poder llamar a 'usuarioEstaEnEquipo()' desde la vista
        return "listaEquipos";
    }

    @GetMapping("/equipos/{id}/usuarios")
    public String listadoUsuariosEquipo(@PathVariable(value="id") Long idEquipo, Model model, HttpSession session) {
        managerUserSesion.comprobarCualquierUsuarioLogeado(session);
        Equipo equipo = equipoService.findById(idEquipo);
        if (equipo == null) {
            throw new EquipoNotFoundException();
        }
        List<Usuario> usuarios = equipoService.usuariosEquipo(idEquipo);
        model.addAttribute("equipo", equipo);
        model.addAttribute("usuarios", usuarios);
        return "listaUsuariosEquipo";
    }

    @GetMapping("/equipos/nuevo")
    public String formNuevoEquipo(@ModelAttribute EquipoData equipoData, Model model, HttpSession session) {
        managerUserSesion.comprobarCualquierUsuarioLogeado(session);
        return "formNuevoEquipo";
    }

    @PostMapping("/equipos/nuevo")
    public String nuevoEquipo(@ModelAttribute EquipoData equipoData,
                              Model model, RedirectAttributes flash,
                              HttpSession session) {

        managerUserSesion.comprobarCualquierUsuarioLogeado(session);

        Equipo equipo = equipoService.nuevoEquipo(equipoData.getNombre());
        if (equipoData.getAddUsuario()) {
            equipoService.anyadirAEquipo((Long) session.getAttribute("idUsuarioLogeado"), equipo.getId());
        }
        flash.addFlashAttribute("mensaje", "Equipo creado correctamente");
        return "redirect:/equipos";
    }

    @GetMapping("/equipos/{id}/alternarPertenencia")
    public String alternarPertenenciaEquipo(@PathVariable(value="id") Long idEquipo, Model model, HttpSession session) {
        managerUserSesion.comprobarCualquierUsuarioLogeado(session);
        Long idUsuario = (Long) session.getAttribute("idUsuarioLogeado");

        if (equipoService.usuarioEstaEnEquipo(idUsuario, idEquipo)) {
            equipoService.borrarDeEquipo(idUsuario, idEquipo);
        }
        else {
            equipoService.anyadirAEquipo(idUsuario, idEquipo);
        }
        return "redirect:/equipos";
    }

    @GetMapping("/equipos/{id}/editar")
    public String formEditarEquipo(@PathVariable(value="id") Long idEquipo, @ModelAttribute EquipoData equipoData,
                                   Model model, HttpSession session) {

        Equipo equipo = equipoService.findById(idEquipo);
        if (equipo == null) {
            throw new EquipoNotFoundException();
        }
        managerUserSesion.comprobarUsuarioAdmin(session);

        model.addAttribute("equipo", equipo);
        equipoData.setNombre(equipo.getNombre());
        return "formEditarEquipo";
    }

    @PostMapping("/equipos/{id}/editar")
    public String grabaEquipoModificado(@PathVariable(value="id") Long idEquipo, @ModelAttribute EquipoData equipoData,
                                        Model model, RedirectAttributes flash, HttpSession session) {

        Equipo equipo = equipoService.findById(idEquipo);
        if (equipo == null) {
            throw new EquipoNotFoundException();
        }
        managerUserSesion.comprobarUsuarioAdmin(session);

        equipoService.modificarEquipo(idEquipo, equipoData.getNombre());
        flash.addFlashAttribute("mensaje", "Equipo modificado correctamente");
        return "redirect:/equipos";
    }

    @DeleteMapping("/equipos/{id}")
    @ResponseBody
    public String borrarTarea(@PathVariable(value="id") Long idEquipo, RedirectAttributes flash, HttpSession session) {
        Equipo equipo = equipoService.findById(idEquipo);
        if (equipo == null) {
            throw new EquipoNotFoundException();
        }

        managerUserSesion.comprobarUsuarioAdmin(session);

        equipoService.borrarEquipo(idEquipo);
        return "";
    }
}
