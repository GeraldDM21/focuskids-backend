package cr.cenfotec.focuskids_backend.repository;

import cr.cenfotec.focuskids_backend.model.ResumenSemanal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ResumenSemanalRepository extends JpaRepository<ResumenSemanal, Integer> {
    List<ResumenSemanal> findByPerfilIdOrderBySemanaInicioDesc(Integer perfilId);
    Optional<ResumenSemanal> findByPerfilIdAndSemanaInicio(Integer perfilId, LocalDate semanaInicio);
}
