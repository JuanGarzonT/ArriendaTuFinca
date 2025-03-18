package com.example.proyecto.proyecto.dto.input.solicitud;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SolicitudCreateDTO {
    private Long propiedadId;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private String comentarios;
}