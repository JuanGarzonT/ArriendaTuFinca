package com.example.proyecto.proyecto.dto.output.solicitud;

import com.example.proyecto.proyecto.entities.Solicitud.EstadoSolicitud;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SolicitudSimpleDTO {
    private Long id;
    private String propiedadNombre;
    private String arrendadorNombre;
    private String arrendatarioNombre;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private EstadoSolicitud estado;
}