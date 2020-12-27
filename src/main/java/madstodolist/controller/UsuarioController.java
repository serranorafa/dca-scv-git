package madstodolist.controller;


import madstodolist.authentication.ManagerUserSesion;
import madstodolist.controller.exception.UsuarioNotFoundException;
import madstodolist.model.Usuario;
import madstodolist.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class UsuarioController {

    @Autowired
    UsuarioService usuarioService;

    @Autowired
    ManagerUserSesion managerUserSesion;

    @GetMapping("/usuarios")
    public String listadoUsuarios(Model model, HttpSession session) {
        managerUserSesion.comprobarUsuarioAdmin(session);

        List<Usuario> usuarios = usuarioService.allUsuarios();
        model.addAttribute("usuarios", usuarios);
        return "listaUsuarios";
    }

    @GetMapping("/usuarios/{id}")
    public String descripcionUsuario(@PathVariable(value="id") Long idUsuario, Model model, HttpSession session) {
        managerUserSesion.comprobarUsuarioLogeadoOAdmin(session, idUsuario);

        Usuario usuario = usuarioService.findById(idUsuario);
        if (usuario == null) {
            throw new UsuarioNotFoundException();
        }

        model.addAttribute("usuario", usuario);
        return "descripcionUsuario";
    }

    @GetMapping("/usuarios/{id}/bloquear")
    public String modificarUsuario(@PathVariable(value="id") Long idUsuario, Model model, RedirectAttributes flash, HttpSession session) {
        managerUserSesion.comprobarUsuarioAdmin(session);
        Usuario usuario = usuarioService.findById(idUsuario);

        usuarioService.bloqueoUsuario(idUsuario, !usuario.isBloqueado());

        String msg = "desbloqueado";
        if (usuario.isBloqueado())
            msg = "bloqueado";

        flash.addFlashAttribute("mensaje", "Usuario " + msg);
        return "redirect:/usuarios";
    }
}
