package com.example.proyecto.proyecto.dto.output.usuario;

import com.example.proyecto.proyecto.dto.output.propiedad.PropiedadSimpleDTO;
import com.example.proyecto.proyecto.dto.output.solicitud.SolicitudSimpleDTO;
import com.example.proyecto.proyecto.entities.Usuario.TipoUsuario;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioDetailDTO {
    private Long id;
    private String nombre;
    private String email;
    private String telefono;
    private TipoUsuario tipoUsuario;
    private Boolean activo;
    private List<PropiedadSimpleDTO> propiedades;
    private List<SolicitudSimpleDTO> solicitudesRealizadas;
    private List<SolicitudSimpleDTO> solicitudesRecibidas;
}