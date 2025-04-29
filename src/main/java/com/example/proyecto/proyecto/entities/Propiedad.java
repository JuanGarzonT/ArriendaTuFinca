package com.example.proyecto.proyecto.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "propiedades")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Propiedad {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String nombre;
    
    @Column(nullable = false, columnDefinition = "TEXT")
    private String descripcion;
    
    @Column(nullable = false)
    private String ubicacion;
    
    @Column(nullable = false)
    private BigDecimal precioPorDia;
    
    @Column(nullable = false)
    private boolean disponible;
    
   @Column(nullable = true)
   private String imagen; 

    @Column(nullable = false)
    private Boolean activo = true;
    
    // Información adicional de la propiedad
    private int capacidad;
    private String caracteristicas;
    private String direccion;
    private String ciudad;
    private String departamento;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "arrendatario_id", nullable = false)
    private Usuario arrendatario;
    
    @OneToMany(mappedBy = "propiedad", cascade = CascadeType.ALL)
    private List<Solicitud> solicitudes;
    
    @OneToMany(mappedBy = "propiedad", cascade = CascadeType.ALL)
    private List<Calificacion> calificaciones;
}