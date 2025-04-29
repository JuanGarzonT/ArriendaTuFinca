package com.example.proyecto.proyecto.dto.output.propiedad;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PropiedadSimpleDTO {
    private Long id;
    private String nombre;
    private String descripcion;
    private String ubicacion;
    private BigDecimal precioPorDia;
    private boolean disponible;
    private String imagen;
}