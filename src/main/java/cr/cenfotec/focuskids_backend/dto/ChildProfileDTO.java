package cr.cenfotec.focuskids_backend.dto;

import cr.cenfotec.focuskids_backend.model.ChildProfile;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

// Esta clase define los datos que el backend le devuelve al frontend
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChildProfileDTO {

    private Long id;
    private String name;
    private String avatar;
    private Integer edad;
    private String condicion;
    private boolean active;
    private String lastGameName;
    private LocalDateTime lastGamePlayedAt;
    private LocalDateTime createdAt;

    // Convierte un perfil de la base de datos al formato que se envia al frontend
    public static ChildProfileDTO fromEntity(ChildProfile p) {
        ChildProfileDTO dto = new ChildProfileDTO();
        dto.setId(p.getId());
        dto.setName(p.getName());
        dto.setAvatar(p.getAvatar());
        dto.setEdad(p.getEdad());
        dto.setCondicion(p.getCondicion());
        dto.setActive(p.isActive());
        dto.setLastGameName(p.getLastGameName());
        dto.setLastGamePlayedAt(p.getLastGamePlayedAt());
        dto.setCreatedAt(p.getCreatedAt());
        return dto;
    }
}
