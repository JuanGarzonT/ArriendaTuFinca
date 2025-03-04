package com.example.proyecto.proyecto.entities;


import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Where(clause = "status = 0")
@SQLDelete( sql = "UPTADE usuario SET status = 1 WHERE id_usuario = ?")


public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_Usuario;
    private String nombre_Usuario;
    private String direccion;
    private Integer edad;
    private Integer status = 0;
    
}
