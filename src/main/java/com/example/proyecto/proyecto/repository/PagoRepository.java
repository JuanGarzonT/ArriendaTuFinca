package com.example.proyecto.proyecto.repository;

import com.example.proyecto.proyecto.entities.Pago;
import com.example.proyecto.proyecto.entities.Pago.EstadoPago;
import com.example.proyecto.proyecto.entities.Solicitud;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface PagoRepository extends CrudRepository<Pago, Long> {
    
    Optional<Pago> findBySolicitudAndActivoTrue(Solicitud solicitud);
    
    List<Pago> findByEstadoPagoAndActivoTrue(EstadoPago estadoPago);
    
    List<Pago> findByFechaPagoBetweenAndActivoTrue(LocalDateTime inicio, LocalDateTime fin);
    
    boolean existsBySolicitudAndActivoTrue(Solicitud solicitud);
    
    Optional<Pago> findByIdAndActivoTrue(Long id);
}