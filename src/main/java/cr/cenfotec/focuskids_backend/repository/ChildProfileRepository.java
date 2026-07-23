package cr.cenfotec.focuskids_backend.repository;

import cr.cenfotec.focuskids_backend.model.ChildProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

// Aqui se definen las consultas a la base de datos para los perfiles de ninos
@Repository
public interface ChildProfileRepository extends JpaRepository<ChildProfile, Long> {

    // Trae todos los perfiles de un padre, ordenados por nombre
    List<ChildProfile> findByParentUserIdOrderByNameAsc(Long parentUserId);

    // Trae solo los perfiles activos de un padre
    List<ChildProfile> findByParentUserIdAndActiveTrueOrderByNameAsc(Long parentUserId);

    // Busca un perfil por su id y verifica que pertenezca al padre correcto
    Optional<ChildProfile> findByIdAndParentUserId(Long id, Long parentUserId);

    // Verifica si ya existe un perfil con ese nombre para ese padre (evita duplicados)
    boolean existsByNameAndParentUserId(String name, Long parentUserId);
}
