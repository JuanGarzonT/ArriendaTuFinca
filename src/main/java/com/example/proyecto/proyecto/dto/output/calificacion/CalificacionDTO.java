package com.example.proyecto.proyecto.dto.output.calificacion;

import com.example.proyecto.proyecto.dto.output.propiedad.PropiedadSimpleDTO;
import com.example.proyecto.proyecto.dto.output.usuario.UsuarioDTO;
import com.example.proyecto.proyecto.entities.Calificacion.TipoCalificacion;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CalificacionDTO {
    private Long id;
    private UsuarioDTO usuario;
    private PropiedadSimpleDTO propiedad;
    private Integer puntuacion;
    private String comentario;
    private LocalDateTime fechaCalificacion;
    private TipoCalificacion tipoCalificacion;
}