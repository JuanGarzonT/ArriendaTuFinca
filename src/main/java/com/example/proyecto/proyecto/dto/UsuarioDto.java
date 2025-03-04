package com.example.proyecto.proyecto.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor

public class UsuarioDto {


    private String nombre_Usuario;
    private String direccion;
    private Integer edad;

    @Override
    public String toString() {
        return "{" +
            ", nombre_Usuario='" + getNombre_Usuario() + "'" +
            ", direccion='" + getDireccion() + "'" +
            ", edad='" + getEdad() + "'" +
            "}";
    }

}
