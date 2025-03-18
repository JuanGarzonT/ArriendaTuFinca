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
    
    Optional<Pago> findBySolicitud(Solicitud solicitud);
    
    List<Pago> findByEstadoPago(EstadoPago estadoPago);
    
    List<Pago> findByFechaPagoBetween(LocalDateTime inicio, LocalDateTime fin);
    
    boolean existsBySolicitud(Solicitud solicitud);
}
