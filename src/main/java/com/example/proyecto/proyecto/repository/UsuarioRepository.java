package com.example.proyecto.proyecto.repository;

import com.example.proyecto.proyecto.entities.Usuario;
import com.example.proyecto.proyecto.entities.Usuario.TipoUsuario;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends CrudRepository<Usuario, Long> {
    
    Optional<Usuario> findByEmailAndActivoTrue(String email);
    
    Optional<Usuario> findByIdAndActivoTrue(Long id);
    
    List<Usuario> findByActivoTrue();
    
    List<Usuario> findByTipoUsuarioAndActivoTrue(TipoUsuario tipoUsuario);
    
    boolean existsByEmailAndActivoTrue(String email);
}