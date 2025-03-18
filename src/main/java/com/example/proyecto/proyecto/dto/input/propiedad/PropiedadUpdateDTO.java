package com.example.proyecto.proyecto.dto.input.propiedad;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PropiedadUpdateDTO {
    private String nombre;
    private String descripcion;
    private BigDecimal precioPorDia;
    private boolean disponible;
    private int capacidad;
    private String caracteristicas;
}