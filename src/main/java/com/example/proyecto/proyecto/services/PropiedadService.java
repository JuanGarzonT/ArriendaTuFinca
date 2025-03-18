package com.example.proyecto.proyecto.services;

import com.example.proyecto.proyecto.dto.input.propiedad.PropiedadCreateDTO;
import com.example.proyecto.proyecto.dto.input.propiedad.PropiedadUpdateDTO;
import com.example.proyecto.proyecto.dto.output.calificacion.CalificacionDTO;
import com.example.proyecto.proyecto.dto.output.propiedad.PropiedadDTO;
import com.example.proyecto.proyecto.dto.output.propiedad.PropiedadDetailDTO;
import com.example.proyecto.proyecto.dto.output.propiedad.PropiedadSimpleDTO;
import com.example.proyecto.proyecto.dto.output.solicitud.SolicitudSimpleDTO;
import com.example.proyecto.proyecto.entities.Propiedad;
import com.example.proyecto.proyecto.entities.Usuario;
import com.example.proyecto.proyecto.repository.CalificacionRepository;
import com.example.proyecto.proyecto.repository.PropiedadRepository;
import com.example.proyecto.proyecto.repository.UsuarioRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class PropiedadService {

    @Autowired
    private PropiedadRepository propiedadRepository;
    
    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Autowired
    private CalificacionRepository calificacionRepository;
    
    @Autowired
    private ModelMapper modelMapper;

    @Transactional(readOnly = true)
    public List<PropiedadSimpleDTO> getAllPropiedades() {
        return StreamSupport.stream(propiedadRepository.findAll().spliterator(), false)
                .map(propiedad -> modelMapper.map(propiedad, PropiedadSimpleDTO.class))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public PropiedadDTO getPropiedadById(Long id) {
        Propiedad propiedad = propiedadRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Propiedad no encontrada con ID: " + id));
        
        PropiedadDTO propiedadDTO = modelMapper.map(propiedad, PropiedadDTO.class);
        
        // Añadir la calificación promedio
        Double calificacionPromedio = calificacionRepository.calcularPromedioPuntuacionPropiedad(propiedad);
        propiedadDTO.setCalificacionPromedio(calificacionPromedio);
        
        return propiedadDTO;
    }

    @Transactional(readOnly = true)
    public PropiedadDetailDTO getPropiedadDetailById(Long id) {
        Propiedad propiedad = propiedadRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Propiedad no encontrada con ID: " + id));
        
        PropiedadDetailDTO detailDTO = modelMapper.map(propiedad, PropiedadDetailDTO.class);
        
        // Convertir solicitudes y calificaciones usando ModelMapper
        detailDTO.setSolicitudes(propiedad.getSolicitudes().stream()
                .map(solicitud -> modelMapper.map(solicitud, SolicitudSimpleDTO.class))
                .collect(Collectors.toList()));
                
        detailDTO.setCalificaciones(propiedad.getCalificaciones().stream()
                .map(calificacion -> modelMapper.map(calificacion, CalificacionDTO.class))
                .collect(Collectors.toList()));
                
        // Añadir la calificación promedio
        Double calificacionPromedio = calificacionRepository.calcularPromedioPuntuacionPropiedad(propiedad);
        detailDTO.setCalificacionPromedio(calificacionPromedio);
        
        return detailDTO;
    }

    @Transactional(readOnly = true)
    public List<PropiedadSimpleDTO> getPropiedadesByArrendatarioId(Long arrendatarioId) {
        Usuario arrendatario = usuarioRepository.findById(arrendatarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + arrendatarioId));
        
        return propiedadRepository.findByArrendatario(arrendatario).stream()
                .map(propiedad -> modelMapper.map(propiedad, PropiedadSimpleDTO.class))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<PropiedadSimpleDTO> getPropiedadesByDisponibilidad(boolean disponible) {
        return propiedadRepository.findByDisponible(disponible).stream()
                .map(propiedad -> modelMapper.map(propiedad, PropiedadSimpleDTO.class))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<PropiedadSimpleDTO> getPropiedadesByUbicacion(String ubicacion) {
        return propiedadRepository.buscarPorUbicacion(ubicacion).stream()
                .map(propiedad -> modelMapper.map(propiedad, PropiedadSimpleDTO.class))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<PropiedadSimpleDTO> getPropiedadesByCiudad(String ciudad) {
        return propiedadRepository.findByCiudad(ciudad).stream()
                .map(propiedad -> modelMapper.map(propiedad, PropiedadSimpleDTO.class))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<PropiedadSimpleDTO> getPropiedadesByDepartamento(String departamento) {
        return propiedadRepository.findByDepartamento(departamento).stream()
                .map(propiedad -> modelMapper.map(propiedad, PropiedadSimpleDTO.class))
                .collect(Collectors.toList());
    }

    @Transactional
    public PropiedadDTO createPropiedad(Long arrendatarioId, PropiedadCreateDTO createPropiedadDTO) {
        Usuario arrendatario = usuarioRepository.findById(arrendatarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + arrendatarioId));
        
        Propiedad propiedad = modelMapper.map(createPropiedadDTO, Propiedad.class);
        propiedad.setArrendatario(arrendatario);
        
        Propiedad savedPropiedad = propiedadRepository.save(propiedad);
        return modelMapper.map(savedPropiedad, PropiedadDTO.class);
    }

    @Transactional
    public PropiedadDTO updatePropiedad(Long id, PropiedadUpdateDTO updatePropiedadDTO) {
        Propiedad propiedad = propiedadRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Propiedad no encontrada con ID: " + id));
        
        // Actualizar solo los campos proporcionados
        if (updatePropiedadDTO.getNombre() != null) {
            propiedad.setNombre(updatePropiedadDTO.getNombre());
        }
        
        if (updatePropiedadDTO.getDescripcion() != null) {
            propiedad.setDescripcion(updatePropiedadDTO.getDescripcion());
        }
        
        if (updatePropiedadDTO.getPrecioPorDia() != null) {
            propiedad.setPrecioPorDia(updatePropiedadDTO.getPrecioPorDia());
        }
        
        propiedad.setDisponible(updatePropiedadDTO.isDisponible());
        
        if (updatePropiedadDTO.getCapacidad() > 0) {
            propiedad.setCapacidad(updatePropiedadDTO.getCapacidad());
        }
        
        if (updatePropiedadDTO.getCaracteristicas() != null) {
            propiedad.setCaracteristicas(updatePropiedadDTO.getCaracteristicas());
        }
        
        Propiedad updatedPropiedad = propiedadRepository.save(propiedad);
        return modelMapper.map(updatedPropiedad, PropiedadDTO.class);
    }

    @Transactional
    public void deletePropiedad(Long id) {
        if (!propiedadRepository.existsById(id)) {
            throw new RuntimeException("Propiedad no encontrada con ID: " + id);
        }
        propiedadRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Double getCalificacionPromedio(Long propiedadId) {
        Propiedad propiedad = propiedadRepository.findById(propiedadId)
                .orElseThrow(() -> new RuntimeException("Propiedad no encontrada con ID: " + propiedadId));
        
        return calificacionRepository.calcularPromedioPuntuacionPropiedad(propiedad);
    }
}