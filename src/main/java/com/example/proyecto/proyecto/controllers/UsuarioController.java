package com.example.proyecto.proyecto.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.proyecto.proyecto.dto.UsuarioDto;
import com.example.proyecto.proyecto.entities.Usuario;
import com.example.proyecto.proyecto.services.UsuarioService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;



@RestController
@RequestMapping("controllers")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;


    @GetMapping("/listarUsuarios")
    public List<Usuario> consultarUsuarios() {
        return usuarioService.consultarUsuarios();
    }

    @PostMapping("registrarUsuario")
    public Usuario registrarUsuario(@RequestBody UsuarioDto usuarioJson) {

        Usuario usuario = new Usuario();

        usuario.setNombre_Usuario(usuarioJson.getNombre_Usuario());
        usuario.setDireccion(usuarioJson.getDireccion());
        usuario.setEdad(usuarioJson.getEdad());

        return usuarioService.registrarUsuario(usuario);
    }
}
