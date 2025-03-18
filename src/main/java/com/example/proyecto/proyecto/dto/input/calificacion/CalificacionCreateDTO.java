package com.example.proyecto.proyecto.dto.input.calificacion;

import com.example.proyecto.proyecto.entities.Calificacion.TipoCalificacion;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CalificacionCreateDTO {
    private Long propiedadId;
    private Integer puntuacion;
    private String comentario;
    private TipoCalificacion tipoCalificacion;
}