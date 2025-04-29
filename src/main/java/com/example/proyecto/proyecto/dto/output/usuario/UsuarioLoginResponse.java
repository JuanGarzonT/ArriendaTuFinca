package com.example.proyecto.proyecto.dto.output.usuario;

import com.example.proyecto.proyecto.entities.Usuario.TipoUsuario;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor                 
public class UsuarioLoginResponse {
    private Long id;
    private String nombre;
    private String email;
    private TipoUsuario tipoUsuario;
}
