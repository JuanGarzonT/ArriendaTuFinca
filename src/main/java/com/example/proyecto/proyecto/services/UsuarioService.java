package com.example.proyecto.proyecto.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.proyecto.proyecto.entities.Usuario;
import com.example.proyecto.proyecto.repository.UsuarioRepository;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;


    public List<Usuario> consultarUsuarios() {
        return (List<Usuario>) usuarioRepository.findAll();
    }


    public Usuario registrarUsuario(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }
}
