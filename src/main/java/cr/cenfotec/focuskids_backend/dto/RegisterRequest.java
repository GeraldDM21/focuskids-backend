package cr.cenfotec.focuskids_backend.dto;

import cr.cenfotec.focuskids_backend.model.UsuarioRol;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterRequest {

    @NotBlank(message = "El nombre es requerido")
    private String nombre;

    @NotBlank(message = "El email es requerido")
    @Email(message = "Formato de email inválido")
    private String email;

    @NotBlank(message = "La contraseña es requerida")
    @Size(min = 8, message = "La contraseña debe tener al menos 8 caracteres")
    private String password;

    @NotNull(message = "El rol es requerido")
    private UsuarioRol rol;

    // Campos opcionales según el rol
    private String telefono;           // PADRE
    private String relacionConNino;    // PADRE
    private String institucion;        // DOCENTE
    private String gradoGrupo;         // DOCENTE
    private String nivelAcceso;        // ADMINISTRADOR
}
