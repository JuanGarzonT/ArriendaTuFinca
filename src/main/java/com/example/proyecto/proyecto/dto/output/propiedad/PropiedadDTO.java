package com.example.proyecto.proyecto.dto.output.propiedad;

import com.example.proyecto.proyecto.dto.output.usuario.UsuarioDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PropiedadDTO {
    private Long id;
    private String nombre;
    private String descripcion;
    private String ubicacion;
    private BigDecimal precioPorDia;
    private boolean disponible;
    private int capacidad;
    private String caracteristicas;
    private String direccion;
    private String ciudad;
    private String departamento;
    private UsuarioDTO arrendatario;
    private Double calificacionPromedio;
}