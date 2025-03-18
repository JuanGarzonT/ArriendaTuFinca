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

@Repository
public interface CalificacionRepository extends CrudRepository<Calificacion, Long> {
    
    List<Calificacion> findByUsuario(Usuario usuario);
    
    List<Calificacion> findByPropiedad(Propiedad propiedad);
    
    List<Calificacion> findByTipoCalificacion(TipoCalificacion tipoCalificacion);
    
    List<Calificacion> findByPropiedadAndTipoCalificacion(Propiedad propiedad, TipoCalificacion tipoCalificacion);
    
    @Query("SELECT AVG(c.puntuacion) FROM Calificacion c WHERE c.propiedad = :propiedad")
    Double calcularPromedioPuntuacionPropiedad(@Param("propiedad") Propiedad propiedad);
    
    @Query("SELECT AVG(c.puntuacion) FROM Calificacion c WHERE c.usuario = :usuario AND c.tipoCalificacion = :tipoCalificacion")
    Double calcularPromedioPuntuacionUsuario(@Param("usuario") Usuario usuario, @Param("tipoCalificacion") TipoCalificacion tipoCalificacion);
}
