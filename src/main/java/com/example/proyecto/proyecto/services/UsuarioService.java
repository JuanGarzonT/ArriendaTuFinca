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

    /**
     * consultar el usuario
     * @return
     */
    public List<Usuario> consultarUsuarios() {
        return (List<Usuario>) usuarioRepository.findAll();
    }

    /**
     * @param usuario
     * @return el registro del usuario
     */
    public Usuario registrarUsuario(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }
}
