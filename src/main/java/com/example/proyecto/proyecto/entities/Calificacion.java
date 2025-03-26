package com.example.proyecto.proyecto.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "calificaciones")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Calificacion {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "propiedad_id", nullable = false)
    private Propiedad propiedad;
    
    @Column(nullable = false)
    private Integer puntuacion; // Por ejemplo, de 1 a 5 estrellas
    
    @Column(columnDefinition = "TEXT")
    private String comentario;
    
    @Column(nullable = false)
    private LocalDateTime fechaCalificacion;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoCalificacion tipoCalificacion;

    @Column(nullable = false)
    private Boolean activo = true;
    
    // Enum para el tipo de calificación
    public enum TipoCalificacion {
        PROPIEDAD, // Calificación a la propiedad
        ARRENDATARIO, // Calificación al arrendatario
        ARRENDADOR // Calificación al arrendador
    }
}