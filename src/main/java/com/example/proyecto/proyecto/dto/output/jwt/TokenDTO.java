package com.example.proyecto.proyecto.dto.output.jwt;

import java.util.Calendar;
import java.util.Date;

import com.example.proyecto.proyecto.dto.output.usuario.UsuarioDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TokenDTO {
    private String token;
    private UsuarioDTO usuario;
    
    public String getType() {
        return "Bearer ";
    }
    
    public Date getDate() {
        return Calendar.getInstance().getTime();
    }
}