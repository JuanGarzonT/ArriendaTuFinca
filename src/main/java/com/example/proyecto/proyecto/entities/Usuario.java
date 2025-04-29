package com.example.proyecto.proyecto.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "usuarios")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String nombre;
    
    @Column(nullable = false, unique = true)
    private String email;
    
    @Column(nullable = false)
    private String password;
    
    @Column(nullable = true)
    private String telefono;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoUsuario tipoUsuario;

    @Column(nullable = false)
    private Boolean activo = true;
    
    @OneToMany(mappedBy = "arrendatario", cascade = CascadeType.ALL)
    private List<Propiedad> propiedades;
    
    @OneToMany(mappedBy = "arrendador")
    private List<Solicitud> solicitudesRealizadas;
    
    @OneToMany(mappedBy = "arrendatario")
    private List<Solicitud> solicitudesRecibidas;
    
    @OneToMany(mappedBy = "usuario")
    private List<Calificacion> calificacionesRealizadas;
    
    // Enum para el tipo de usuario
    public enum TipoUsuario {
        ARRENDATARIO, // El que pone en arriendo propiedades
        ARRENDADOR    // El que arrienda propiedades
    }
}