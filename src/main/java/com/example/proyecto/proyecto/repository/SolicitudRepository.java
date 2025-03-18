package com.example.proyecto.proyecto.repository;

import com.example.proyecto.proyecto.entities.Propiedad;
import com.example.proyecto.proyecto.entities.Solicitud;
import com.example.proyecto.proyecto.entities.Solicitud.EstadoSolicitud;
import com.example.proyecto.proyecto.entities.Usuario;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface SolicitudRepository extends CrudRepository<Solicitud, Long> {
    
    List<Solicitud> findByArrendador(Usuario arrendador);
    
    List<Solicitud> findByArrendatario(Usuario arrendatario);
    
    List<Solicitud> findByPropiedad(Propiedad propiedad);
    
    List<Solicitud> findByEstado(EstadoSolicitud estado);
    
    List<Solicitud> findByPropiedadAndEstado(Propiedad propiedad, EstadoSolicitud estado);
    
    @Query("SELECT s FROM Solicitud s WHERE s.propiedad = :propiedad AND " +
           "((s.fechaInicio BETWEEN :inicio AND :fin) OR " +
           "(s.fechaFin BETWEEN :inicio AND :fin) OR " +
           "(s.fechaInicio <= :inicio AND s.fechaFin >= :fin))")
    List<Solicitud> buscarSolicitudesPorFechas(
            @Param("propiedad") Propiedad propiedad,
            @Param("inicio") LocalDate inicio,
            @Param("fin") LocalDate fin);
}