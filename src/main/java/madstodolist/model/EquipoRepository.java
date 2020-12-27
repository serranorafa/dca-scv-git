package madstodolist.model;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface EquipoRepository extends CrudRepository<Equipo, Long> {
    List<Equipo> findAll();
}
