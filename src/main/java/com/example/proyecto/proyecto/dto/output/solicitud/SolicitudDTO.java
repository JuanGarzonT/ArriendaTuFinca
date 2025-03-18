package com.example.proyecto.proyecto.dto.output.solicitud;

import com.example.proyecto.proyecto.dto.output.pago.PagoDTO;
import com.example.proyecto.proyecto.dto.output.propiedad.PropiedadSimpleDTO;
import com.example.proyecto.proyecto.dto.output.usuario.UsuarioDTO;
import com.example.proyecto.proyecto.entities.Solicitud.EstadoSolicitud;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SolicitudDTO {
    private Long id;
    private PropiedadSimpleDTO propiedad;
    private UsuarioDTO arrendador;
    private UsuarioDTO arrendatario;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private BigDecimal montoTotal;
    private EstadoSolicitud estado;
    private String comentarios;
    private PagoDTO pago;
}