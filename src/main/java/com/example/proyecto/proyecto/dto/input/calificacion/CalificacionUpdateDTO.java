package com.example.proyecto.proyecto.dto.input.calificacion;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CalificacionUpdateDTO {
    private Integer puntuacion;
    private String comentario;
}