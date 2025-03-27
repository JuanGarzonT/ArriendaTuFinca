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
import java.util.Optional;

@Repository
public interface SolicitudRepository extends CrudRepository<Solicitud, Long> {

    List<Solicitud> findAllByActivoTrue();

    List<Solicitud> findByArrendadorAndActivoTrue(Usuario arrendador);

    List<Solicitud> findByArrendatarioAndActivoTrue(Usuario arrendatario);

    List<Solicitud> findByPropiedadAndActivoTrue(Propiedad propiedad);

    List<Solicitud> findByEstadoAndActivoTrue(EstadoSolicitud estado);

    List<Solicitud> findByPropiedadAndEstadoAndActivoTrue(Propiedad propiedad, EstadoSolicitud estado);

    Optional<Solicitud> findByIdAndActivoTrue(Long id);

    @Query("SELECT s FROM Solicitud s WHERE s.propiedad = :propiedad AND s.activo = true AND " +
           "((s.fechaInicio BETWEEN :inicio AND :fin) OR " +
           "(s.fechaFin BETWEEN :inicio AND :fin) OR " +
           "(s.fechaInicio <= :inicio AND s.fechaFin >= :fin))")
    List<Solicitud> buscarSolicitudesPorFechasActivas(
            @Param("propiedad") Propiedad propiedad,
            @Param("inicio") LocalDate inicio,
            @Param("fin") LocalDate fin);
}
