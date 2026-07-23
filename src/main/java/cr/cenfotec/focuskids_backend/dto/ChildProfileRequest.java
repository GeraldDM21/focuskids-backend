package cr.cenfotec.focuskids_backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;
import lombok.Data;

// Esta clase define los datos que el frontend envia al backend para crear o editar un perfil
@Data
public class ChildProfileRequest {

    // El nombre es obligatorio y debe tener entre 2 y 100 letras
    @NotBlank(message = "El nombre del nino es obligatorio")
    @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres")
    private String name;

    // Avatar opcional (se guarda como texto, ej: "fox")
    @Size(max = 255)
    private String avatar;

    // Edad entre 1 y 18 anos
    @Min(value = 1, message = "La edad debe ser mayor a 0")
    @Max(value = 18, message = "La edad debe ser menor a 18")
    private Integer edad;

    // Condicion del nino, es opcional
    @Size(max = 255, message = "La condicion no puede superar 255 caracteres")
    private String condicion;
}
