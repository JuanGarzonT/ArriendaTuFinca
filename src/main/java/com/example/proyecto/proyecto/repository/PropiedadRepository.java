package com.example.proyecto.proyecto.repository;

import com.example.proyecto.proyecto.entities.Propiedad;
import com.example.proyecto.proyecto.entities.Usuario;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PropiedadRepository extends CrudRepository<Propiedad, Long> {
    
    List<Propiedad> findByArrendatario(Usuario arrendatario);
    
    List<Propiedad> findByDisponible(boolean disponible);
    
    List<Propiedad> findByCiudad(String ciudad);
    
    List<Propiedad> findByDepartamento(String departamento);
    
    @Query("SELECT p FROM Propiedad p WHERE p.ubicacion LIKE %:ubicacion%")
    List<Propiedad> buscarPorUbicacion(@Param("ubicacion") String ubicacion);
    
    @Query("SELECT p FROM Propiedad p WHERE p.disponible = true AND p.capacidad >= :capacidad")
    List<Propiedad> buscarDisponiblesPorCapacidad(@Param("capacidad") int capacidad);
}