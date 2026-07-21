package cr.cenfotec.focuskids_backend.dto.juego;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RegistrarOperacionRequest {

    @NotNull
    @Min(1)
    @Max(20)
    private Integer numeroOperacion;

    @NotNull
    private Integer numero1;

    @NotNull
    private Integer numero2;

    @NotBlank
    private String operador;

    @NotNull
    private Integer resultadoCorrecto;

    private Integer respuestaSeleccionada;

    @NotBlank
    private String tipoResultado;

    private Long tiempoRespuestaMs;

    @NotNull
    @Min(1000)
    private Integer velocidadCaidaMs;

    @NotNull
    @Min(1)
    @Max(5)
    private Integer nivel;
}