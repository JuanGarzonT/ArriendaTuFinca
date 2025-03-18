package com.example.proyecto.proyecto.dto.input.usuario;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioUpdateDTO {
    private String nombre;
    private String telefono;
    private String password;
}