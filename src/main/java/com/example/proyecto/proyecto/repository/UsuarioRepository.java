package com.example.proyecto.proyecto.repository;

import org.springframework.data.repository.CrudRepository;

import com.example.proyecto.proyecto.entities.Usuario;

public interface UsuarioRepository extends CrudRepository<Usuario, Long> {

}
