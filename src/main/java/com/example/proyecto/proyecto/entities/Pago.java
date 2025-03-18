package com.example.proyecto.proyecto.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "pagos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Pago {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "solicitud_id", nullable = false, unique = true)
    private Solicitud solicitud;
    
    @Column(nullable = false)
    private BigDecimal monto;
    
    @Column(nullable = false)
    private LocalDateTime fechaPago;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MetodoPago metodoPago;
    
    @Column(nullable = false)
    private String referenciaPago;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoPago estadoPago;
    
    // Enum para el método de pago
    public enum MetodoPago {
        TARJETA_CREDITO,
        TRANSFERENCIA_BANCARIA,
        EFECTIVO,
        OTRO
    }
    
    // Enum para el estado del pago
    public enum EstadoPago {
        PENDIENTE,
        COMPLETADO,
        RECHAZADO,
        REEMBOLSADO
    }
}