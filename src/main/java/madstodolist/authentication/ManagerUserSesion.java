package madstodolist.authentication;

import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSession;

@Component
public class ManagerUserSesion {

    // Añadimos el id de usuario en la sesión HTTP para hacer
    // una autorización sencilla. En los métodos de controllers
    // comprobamos si el id del usuario logeado coincide con el obtenido
    // desde la URL
    public void logearUsuario(HttpSession session, Long idUsuario, String nombreUsuario, boolean isAdmin) {
        session.setAttribute("idUsuarioLogeado", idUsuario);
        session.setAttribute("nombreUsuarioLogeado", nombreUsuario);
        session.setAttribute("isAdmin", isAdmin);
    }

    public void comprobarUsuarioLogeado(HttpSession session, Long idUsuario) {
        Long idUsuarioLogeado = (Long) session.getAttribute("idUsuarioLogeado");
        if (!idUsuario.equals(idUsuarioLogeado))
            throw new UsuarioNoLogeadoException();
    }

    public void comprobarUsuarioAdmin(HttpSession session) {
        boolean isAdmin = (boolean) session.getAttribute("isAdmin");
        if (!isAdmin)
            throw new UsuarioNoLogeadoException();
    }

    // Si el usuario logeado no es admin ni coincide con idUsuario, error 401
    public void comprobarUsuarioLogeadoOAdmin(HttpSession session, Long idUsuario) {
        boolean isAdmin = (boolean) session.getAttribute("isAdmin");
        Long idUsuarioLogeado = (Long) session.getAttribute("idUsuarioLogeado");

        if (!isAdmin && !idUsuario.equals(idUsuarioLogeado))
            throw new UsuarioNoLogeadoException();
    }

    public void comprobarCualquierUsuarioLogeado(HttpSession session) {
        if (session.getAttribute("idUsuarioLogeado") == null) {
            throw new UsuarioNoLogeadoException();
        }
    }
}
