package com.example.proyecto.proyecto.dto.output.propiedad;

import com.example.proyecto.proyecto.dto.output.calificacion.CalificacionDTO;
import com.example.proyecto.proyecto.dto.output.solicitud.SolicitudSimpleDTO;
import com.example.proyecto.proyecto.dto.output.usuario.UsuarioDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PropiedadDetailDTO {
    private Long id;
    private String nombre;
    private String descripcion;
    private String ubicacion;
    private BigDecimal precioPorDia;
    private boolean disponible;
    private Boolean activo;
    private int capacidad;
    private String caracteristicas;
    private String direccion;
    private String ciudad;
    private String departamento;
    private UsuarioDTO arrendatario;
    private Double calificacionPromedio;
    private List<SolicitudSimpleDTO> solicitudes;
    private List<CalificacionDTO> calificaciones;
}