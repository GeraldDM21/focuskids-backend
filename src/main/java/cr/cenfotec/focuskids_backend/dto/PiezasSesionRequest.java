package cr.cenfotec.focuskids_backend.dto;

import lombok.Data;

@Data
public class PiezasSesionRequest {
    private Integer perfilId;
    private String  nivel;
    private Integer piezasTotales;
    private Integer piezasColocadas;
    private Integer rotaciones;
    private Integer piezasFallidas;
    private Integer tiempoUsadoSegundos;
    private Integer tiempoTotalSegundos;
    private Integer puntosBonus;
    private Boolean completada;
    private Boolean subioNivel;
}
