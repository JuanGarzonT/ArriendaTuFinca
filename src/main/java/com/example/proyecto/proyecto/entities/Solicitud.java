package com.example.proyecto.proyecto.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "solicitudes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Solicitud {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "propiedad_id", nullable = false)
    private Propiedad propiedad;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "arrendador_id", nullable = false)
    private Usuario arrendador;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "arrendatario_id", nullable = false)
    private Usuario arrendatario;
    
    @Column(nullable = false)
    private LocalDate fechaInicio;
    
    @Column(nullable = false)
    private LocalDate fechaFin;
    
    @Column(nullable = false)
    private BigDecimal montoTotal;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoSolicitud estado;
    
    @Column(columnDefinition = "TEXT")
    private String comentarios;
    
    @OneToOne(mappedBy = "solicitud", cascade = CascadeType.ALL)
    private Pago pago;

    @Column(nullable = false)
    private Boolean activo = true;
    
    // Enum para el estado de la solicitud
    public enum EstadoSolicitud {
        PENDIENTE,
        APROBADA,
        RECHAZADA,
        CANCELADA,
        COMPLETADA
    }
}
