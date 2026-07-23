package cr.cenfotec.focuskids_backend.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

// Esta clase representa el perfil de un nino en la base de datos
@Entity
@Table(name = "child_profiles")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChildProfile {

    // Numero unico que identifica cada perfil
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Nombre del nino
    @Column(nullable = false, length = 100)
    private String name;

    // Avatar elegido (guarda el nombre como "fox", "lion", etc.)
    @Column(length = 255)
    private String avatar;

    // Edad del nino
    @Column
    private Integer edad;

    // Condicion del nino (opcional, ej: TDAH leve)
    @Column(length = 255)
    private String condicion;

    // Si esta en false el perfil esta desactivado pero guarda el historial
    @Column(nullable = false)
    private boolean active = true;

    // ID del padre al que pertenece este perfil
    @Column(name = "parent_user_id", nullable = false)
    private Long parentUserId;

    // Nombre del ultimo juego que jugo este nino
    @Column(name = "last_game_name", length = 100)
    private String lastGameName;

    // Fecha en que jugo por ultima vez
    @Column(name = "last_game_played_at")
    private LocalDateTime lastGamePlayedAt;

    // Fecha en que se creo el perfil (se pone automatico)
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    // Fecha de la ultima actualizacion (se actualiza automatico)
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Se ejecuta automaticamente cuando se guarda por primera vez
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    // Se ejecuta automaticamente cada vez que se edita el perfil
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
