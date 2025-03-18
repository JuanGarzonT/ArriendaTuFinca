package com.example.proyecto.proyecto.dto.input.propiedad;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PropiedadCreateDTO {
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
}