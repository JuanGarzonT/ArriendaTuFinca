package com.example.proyecto.proyecto.dto.input.solicitud;

import com.example.proyecto.proyecto.entities.Solicitud.EstadoSolicitud;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SolicitudUpdateDTO {
    private EstadoSolicitud estado;
    private String comentarios;
}