package com.example.proyecto.proyecto.repository;

import com.example.proyecto.proyecto.entities.Calificacion;
import com.example.proyecto.proyecto.entities.Calificacion.TipoCalificacion;
import com.example.proyecto.proyecto.entities.Propiedad;
import com.example.proyecto.proyecto.entities.Usuario;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CalificacionRepository extends CrudRepository<Calificacion, Long> {

    List<Calificacion> findByActivoTrue();
    
    Optional<Calificacion> findByIdAndActivoTrue(Long id);
    
    List<Calificacion> findByUsuarioAndActivoTrue(Usuario usuario);
    
    List<Calificacion> findByPropiedadAndActivoTrue(Propiedad propiedad);
    
    List<Calificacion> findByTipoCalificacionAndActivoTrue(TipoCalificacion tipoCalificacion);
    
    @Query("SELECT AVG(c.puntuacion) FROM Calificacion c WHERE c.propiedad = :propiedad AND c.activo = true")
    Double calcularPromedioPuntuacionPropiedad(@Param("propiedad") Propiedad propiedad);
    
    @Query("SELECT AVG(c.puntuacion) FROM Calificacion c WHERE c.usuario = :usuario AND c.tipoCalificacion = :tipoCalificacion AND c.activo = true")
    Double calcularPromedioPuntuacionUsuario(@Param("usuario") Usuario usuario, @Param("tipoCalificacion") TipoCalificacion tipoCalificacion);
}