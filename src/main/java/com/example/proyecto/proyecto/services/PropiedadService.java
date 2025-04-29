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
        return propiedadRepository.findAllByActivoTrue().stream()
                .map(propiedad -> modelMapper.map(propiedad, PropiedadSimpleDTO.class))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public PropiedadDTO getPropiedadById(Long id) {
        Propiedad propiedad = propiedadRepository.findByIdAndActivoTrue(id)
                .orElseThrow(() -> new RuntimeException("Propiedad no encontrada con ID: " + id));

        PropiedadDTO propiedadDTO = modelMapper.map(propiedad, PropiedadDTO.class);
        Double calificacionPromedio = calificacionRepository.calcularPromedioPuntuacionPropiedad(propiedad);
        propiedadDTO.setCalificacionPromedio(calificacionPromedio);
        return propiedadDTO;
    }

    @Transactional(readOnly = true)
    public PropiedadDetailDTO getPropiedadDetailById(Long id) {
        Propiedad propiedad = propiedadRepository.findByIdAndActivoTrue(id)
                .orElseThrow(() -> new RuntimeException("Propiedad no encontrada con ID: " + id));

        PropiedadDetailDTO detailDTO = modelMapper.map(propiedad, PropiedadDetailDTO.class);

        detailDTO.setSolicitudes(propiedad.getSolicitudes().stream()
                .map(solicitud -> modelMapper.map(solicitud, SolicitudSimpleDTO.class))
                .collect(Collectors.toList()));

        detailDTO.setCalificaciones(propiedad.getCalificaciones().stream()
                .map(calificacion -> modelMapper.map(calificacion, CalificacionDTO.class))
                .collect(Collectors.toList()));

        Double calificacionPromedio = calificacionRepository.calcularPromedioPuntuacionPropiedad(propiedad);
        detailDTO.setCalificacionPromedio(calificacionPromedio);

        return detailDTO;
    }

    @Transactional(readOnly = true)
    public List<PropiedadDetailDTO> getPropiedadesByArrendatarioId(Long arrendatarioId) {
        Usuario arrendatario = usuarioRepository.findById(arrendatarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + arrendatarioId));

        return propiedadRepository.findByArrendatarioAndActivoTrue(arrendatario).stream()
                .map(propiedad -> modelMapper.map(propiedad, PropiedadDetailDTO.class))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<PropiedadSimpleDTO> getPropiedadesByDisponibilidad(boolean disponible) {
        return propiedadRepository.findByDisponibleAndActivoTrue(disponible).stream()
                .map(propiedad -> modelMapper.map(propiedad, PropiedadSimpleDTO.class))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<PropiedadSimpleDTO> getPropiedadesByUbicacion(String ubicacion) {
        return propiedadRepository.buscarPorUbicacionAndActivoTrue(ubicacion).stream()
                .map(propiedad -> modelMapper.map(propiedad, PropiedadSimpleDTO.class))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<PropiedadSimpleDTO> getPropiedadesByCiudad(String ciudad) {
        return propiedadRepository.findByCiudadAndActivoTrue(ciudad).stream()
                .map(propiedad -> modelMapper.map(propiedad, PropiedadSimpleDTO.class))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<PropiedadSimpleDTO> getPropiedadesByDepartamento(String departamento) {
        return propiedadRepository.findByDepartamentoAndActivoTrue(departamento).stream()
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
        Propiedad propiedad = propiedadRepository.findByIdAndActivoTrue(id)
                .orElseThrow(() -> new RuntimeException("Propiedad no encontrada con ID: " + id));

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
    public void softDeletePropiedad(Long id) {
        Propiedad propiedad = propiedadRepository.findByIdAndActivoTrue(id)
                .orElseThrow(() -> new RuntimeException("Propiedad no encontrada con ID: " + id));
        propiedad.setActivo(false);
        propiedadRepository.save(propiedad);
    }

    @Transactional
    public PropiedadDTO reactivarPropiedad(Long id) {
        Propiedad propiedad = propiedadRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Propiedad no encontrada con ID: " + id));
        propiedad.setActivo(true);
        Propiedad reactivada = propiedadRepository.save(propiedad);
        return modelMapper.map(reactivada, PropiedadDTO.class);
    }

    @Transactional(readOnly = true)
    public Double getCalificacionPromedio(Long propiedadId) {
        Propiedad propiedad = propiedadRepository.findByIdAndActivoTrue(propiedadId)
                .orElseThrow(() -> new RuntimeException("Propiedad no encontrada con ID: " + propiedadId));
        return calificacionRepository.calcularPromedioPuntuacionPropiedad(propiedad);
    }
}
