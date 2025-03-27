package com.example.proyecto.proyecto.repository;

import com.example.proyecto.proyecto.entities.Propiedad;
import com.example.proyecto.proyecto.entities.Usuario;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PropiedadRepository extends CrudRepository<Propiedad, Long> {

    List<Propiedad> findAllByActivoTrue();
    
    Optional<Propiedad> findByIdAndActivoTrue(Long id);
    
    List<Propiedad> findByArrendatarioAndActivoTrue(Usuario arrendatario);
    
    List<Propiedad> findByDisponibleAndActivoTrue(boolean disponible);
    
    List<Propiedad> findByCiudadAndActivoTrue(String ciudad);
    
    List<Propiedad> findByDepartamentoAndActivoTrue(String departamento);
    
    @Query("SELECT p FROM Propiedad p WHERE p.ubicacion LIKE %:ubicacion%")
    List<Propiedad> buscarPorUbicacionAndActivoTrue(@Param("ubicacion") String ubicacion);
    
    @Query("SELECT p FROM Propiedad p WHERE p.disponible = true AND p.capacidad >= :capacidad")
    List<Propiedad> buscarDisponiblesPorCapacidadAndActivoTrue(@Param("capacidad") int capacidad);
}