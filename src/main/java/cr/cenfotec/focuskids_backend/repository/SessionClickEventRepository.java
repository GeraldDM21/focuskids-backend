package cr.cenfotec.focuskids_backend.repository;

import cr.cenfotec.focuskids_backend.model.SessionClickEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SessionClickEventRepository extends JpaRepository<SessionClickEvent, Integer> {
    List<SessionClickEvent> findBySesionId(Integer sesionId);
    List<SessionClickEvent> findBySesionIdAndFueAcierto(Integer sesionId, Boolean fueAcierto);
}
