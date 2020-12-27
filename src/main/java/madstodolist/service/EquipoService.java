package madstodolist.service;

import madstodolist.model.Equipo;
import madstodolist.model.EquipoRepository;
import madstodolist.model.Usuario;
import madstodolist.model.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service
public class EquipoService {
    @Autowired
    private EquipoRepository equipoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Transactional
    public List<Equipo> findAllOrderedByName() {
        List<Equipo> equipos = equipoRepository.findAll();
        Collections.sort(equipos, Comparator.comparing(Equipo::getNombre));
        return equipos;
    }

    @Transactional(readOnly = true)
    public Equipo findById(Long equipoId) {
        return equipoRepository.findById(equipoId).orElse(null);
    }

    @Transactional(readOnly = true)
    public List<Usuario> usuariosEquipo(Long idEquipo) {
        Equipo equipo = equipoRepository.findById(idEquipo).orElse(null);
        List<Usuario> usuarios = new ArrayList(equipo.getUsuarios());
        return usuarios;
    }

    @Transactional
    public Equipo nuevoEquipo(String nombreEquipo) {
        Equipo equipo = new Equipo(nombreEquipo);
        equipoRepository.save(equipo);
        return equipo;
    }

    @Transactional
    public void anyadirAEquipo(Long idUsuario, Long idEquipo) {
        Usuario usuario = usuarioRepository.findById(idUsuario).orElse(null);
        if (usuario == null) {
            throw new EquipoServiceException("Usuario " + idUsuario + " no existe al añadir a equipo " + idEquipo);
        }
        Equipo equipo = equipoRepository.findById(idEquipo).orElse(null);
        if (equipo == null) {
            throw new EquipoServiceException("Equipo " + idEquipo + " no existe al añadirle el usuario " + idUsuario);
        }
        equipo.addUsuario(usuario);
    }

    @Transactional
    public void borrarDeEquipo(Long idUsuario, Long idEquipo) {
        Usuario usuario = usuarioRepository.findById(idUsuario).orElse(null);
        if (usuario == null) {
            throw new EquipoServiceException("Usuario " + idUsuario + " no existe al borrar del equipo " + idEquipo);
        }
        Equipo equipo = equipoRepository.findById(idEquipo).orElse(null);
        if (equipo == null) {
            throw new EquipoServiceException("Equipo " + idEquipo + " no existe al borrarle el usuario " + idUsuario);
        }
        equipo.removeUsuario(usuario);
    }

    @Transactional(readOnly = true)
    public boolean usuarioEstaEnEquipo(Long idUsuario, Long idEquipo) {
        Usuario usuario = usuarioRepository.findById(idUsuario).orElse(null);
        if (usuario == null) {
            throw new EquipoServiceException("Usuario " + idUsuario + " no existe");
        }
        Equipo equipo = equipoRepository.findById(idEquipo).orElse(null);
        if (equipo == null) {
            throw new EquipoServiceException("Equipo " + idEquipo + " no existe");
        }
        return this.usuariosEquipo(idEquipo).contains(usuario);
    }

    @Transactional
    public void borrarEquipo(Long idEquipo) {
        Equipo equipo = equipoRepository.findById(idEquipo).orElse(null);
        if (equipo == null) {
            throw new EquipoServiceException("Equipo con id " + idEquipo + " no existe");
        }
        equipoRepository.delete(equipo);
    }

    @Transactional
    public Equipo modificarEquipo(Long idEquipo, String nuevoNombre) {
        Equipo equipo = equipoRepository.findById(idEquipo).orElse(null);
        if (equipo == null) {
            throw new EquipoServiceException("Equipo con id " + idEquipo + " no existe");
        }
        equipo.setNombre(nuevoNombre);
        equipoRepository.save(equipo);
        return equipo;
    }

}
