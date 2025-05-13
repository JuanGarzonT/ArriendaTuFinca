package com.example.proyecto.proyecto.services;

import com.example.proyecto.proyecto.dto.input.solicitud.SolicitudCreateDTO;
import com.example.proyecto.proyecto.dto.input.solicitud.SolicitudUpdateDTO;
import com.example.proyecto.proyecto.dto.output.solicitud.SolicitudDTO;
import com.example.proyecto.proyecto.dto.output.solicitud.SolicitudSimpleDTO;
import com.example.proyecto.proyecto.entities.Propiedad;
import com.example.proyecto.proyecto.entities.Solicitud;
import com.example.proyecto.proyecto.entities.Solicitud.EstadoSolicitud;
import com.example.proyecto.proyecto.entities.Usuario;
import com.example.proyecto.proyecto.repository.PropiedadRepository;
import com.example.proyecto.proyecto.repository.SolicitudRepository;
import com.example.proyecto.proyecto.repository.UsuarioRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
@CrossOrigin("*")

@Service
public class SolicitudService {

    @Autowired
    private SolicitudRepository solicitudRepository;
    
    @Autowired
    private PropiedadRepository propiedadRepository;
    
    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Autowired
    private ModelMapper modelMapper;


    @Transactional(readOnly = true)
    public List<SolicitudSimpleDTO> getAllSolicitudes() {
        return solicitudRepository.findAllByActivoTrue().stream()
                .map(solicitud -> modelMapper.map(solicitud, SolicitudSimpleDTO.class))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public SolicitudDTO getSolicitudById(Long id) {
        Solicitud solicitud = solicitudRepository.findByIdAndActivoTrue(id)
                .orElseThrow(() -> new RuntimeException("Solicitud no encontrada con ID: " + id));
        return modelMapper.map(solicitud, SolicitudDTO.class);
    }

    @Transactional(readOnly = true)
    public List<SolicitudSimpleDTO> getSolicitudesByArrendadorId(Long arrendadorId) {
        Usuario arrendador = usuarioRepository.findById(arrendadorId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + arrendadorId));
    
        List<Solicitud> solicitudes = solicitudRepository.findByArrendadorAndActivoTrue(arrendador);
        List<SolicitudSimpleDTO> solicitudDTOs = new ArrayList<>();
    
        for (Solicitud solicitud : solicitudes) {
            SolicitudSimpleDTO dto = new SolicitudSimpleDTO();
            
            // Mapeo manual de propiedades simples
            dto.setId(solicitud.getId());
            dto.setFechaInicio(solicitud.getFechaInicio());
            dto.setFechaFin(solicitud.getFechaFin());
            dto.setEstado(solicitud.getEstado());
            
            // Mapeo manual de propiedades de objetos relacionados
            if (solicitud.getPropiedad() != null) {
                dto.setPropiedadNombre(solicitud.getPropiedad().getNombre());
            }
            
            if (solicitud.getArrendador() != null) {
                dto.setArrendadorNombre(solicitud.getArrendador().getNombre());
            }
            
            if (solicitud.getArrendatario() != null) {
                dto.setArrendatarioNombre(solicitud.getArrendatario().getNombre());
            }
            
            solicitudDTOs.add(dto);
        }
    
        return solicitudDTOs;
    }
    

        @Transactional(readOnly = true)
        public List<SolicitudSimpleDTO> getSolicitudesByArrendatarioId(Long arrendatarioId) {
        Usuario arrendatario = usuarioRepository.findById(arrendatarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + arrendatarioId));

        List<Solicitud> solicitudes = solicitudRepository.findByArrendatarioAndActivoTrue(arrendatario);
        List<SolicitudSimpleDTO> solicitudDTOs = new ArrayList<>();

        for (Solicitud solicitud : solicitudes) {
                SolicitudSimpleDTO dto = new SolicitudSimpleDTO();
                
                // Mapeo manual de propiedades simples
                dto.setId(solicitud.getId());
                dto.setFechaInicio(solicitud.getFechaInicio());
                dto.setFechaFin(solicitud.getFechaFin());
                dto.setEstado(solicitud.getEstado());
                
                // Mapeo manual de propiedades de objetos relacionados
                if (solicitud.getPropiedad() != null) {
                dto.setPropiedadNombre(solicitud.getPropiedad().getNombre());
                }
                
                if (solicitud.getArrendador() != null) {
                dto.setArrendadorNombre(solicitud.getArrendador().getNombre());
                }
                
                if (solicitud.getArrendatario() != null) {
                dto.setArrendatarioNombre(solicitud.getArrendatario().getNombre());
                }
                
                solicitudDTOs.add(dto);
        }

        return solicitudDTOs;
        }

    @Transactional(readOnly = true)
    public List<SolicitudSimpleDTO> getSolicitudesByPropiedadId(Long propiedadId) {
        Propiedad propiedad = propiedadRepository.findByIdAndActivoTrue(propiedadId)
                .orElseThrow(() -> new RuntimeException("Propiedad no encontrada con ID: " + propiedadId));

        return solicitudRepository.findByPropiedadAndActivoTrue(propiedad).stream()
                .map(solicitud -> modelMapper.map(solicitud, SolicitudSimpleDTO.class))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<SolicitudSimpleDTO> getSolicitudesByEstado(EstadoSolicitud estado) {
        return solicitudRepository.findByEstadoAndActivoTrue(estado).stream()
                .map(solicitud -> modelMapper.map(solicitud, SolicitudSimpleDTO.class))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<SolicitudSimpleDTO> getSolicitudesByPropiedadIdAndEstado(Long propiedadId, EstadoSolicitud estado) {
        Propiedad propiedad = propiedadRepository.findByIdAndActivoTrue(propiedadId)
                .orElseThrow(() -> new RuntimeException("Propiedad no encontrada con ID: " + propiedadId));

        return solicitudRepository.findByPropiedadAndEstadoAndActivoTrue(propiedad, estado).stream()
                .map(solicitud -> modelMapper.map(solicitud, SolicitudSimpleDTO.class))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public boolean verificarDisponibilidadFechas(Long propiedadId, LocalDate fechaInicio, LocalDate fechaFin) {
        Propiedad propiedad = propiedadRepository.findByIdAndActivoTrue(propiedadId)
                .orElseThrow(() -> new RuntimeException("Propiedad no encontrada con ID: " + propiedadId));

        if (!propiedad.isDisponible()) {
            return false;
        }

        List<Solicitud> solicitudesSolapadas = solicitudRepository.buscarSolicitudesPorFechasActivas(
                propiedad, fechaInicio, fechaFin);

        return solicitudesSolapadas.stream()
                .noneMatch(solicitud ->
                        solicitud.getEstado() == EstadoSolicitud.APROBADA ||
                        solicitud.getEstado() == EstadoSolicitud.COMPLETADA);
    }

    @Transactional
    public SolicitudDTO createSolicitud(Long arrendadorId, SolicitudCreateDTO createSolicitudDTO) {
        Usuario arrendador = usuarioRepository.findById(arrendadorId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + arrendadorId));

        Propiedad propiedad = propiedadRepository.findByIdAndActivoTrue(createSolicitudDTO.getPropiedadId())
                .orElseThrow(() -> new RuntimeException("Propiedad no encontrada con ID: " + createSolicitudDTO.getPropiedadId()));

        if (!verificarDisponibilidadFechas(propiedad.getId(), createSolicitudDTO.getFechaInicio(), createSolicitudDTO.getFechaFin())) {
            throw new RuntimeException("La propiedad no está disponible para las fechas seleccionadas");
        }

        Solicitud solicitud = new Solicitud();
        solicitud.setPropiedad(propiedad);
        solicitud.setArrendador(arrendador);
        solicitud.setArrendatario(propiedad.getArrendatario());
        solicitud.setFechaInicio(createSolicitudDTO.getFechaInicio());
        solicitud.setFechaFin(createSolicitudDTO.getFechaFin());
        solicitud.setComentarios(createSolicitudDTO.getComentarios());
        solicitud.setEstado(EstadoSolicitud.PENDIENTE);
        solicitud.setActivo(true);

        long dias = ChronoUnit.DAYS.between(createSolicitudDTO.getFechaInicio(), createSolicitudDTO.getFechaFin()) + 1;
        BigDecimal montoTotal = propiedad.getPrecioPorDia().multiply(BigDecimal.valueOf(dias));
        solicitud.setMontoTotal(montoTotal);

        Solicitud savedSolicitud = solicitudRepository.save(solicitud);
        return modelMapper.map(savedSolicitud, SolicitudDTO.class);
    }

    @Transactional
    public SolicitudDTO updateEstadoSolicitud(Long id, SolicitudUpdateDTO updateSolicitudDTO) {
        Solicitud solicitud = solicitudRepository.findByIdAndActivoTrue(id)
                .orElseThrow(() -> new RuntimeException("Solicitud no encontrada con ID: " + id));

        if (solicitud.getEstado() == EstadoSolicitud.COMPLETADA) {
            throw new RuntimeException("No se puede modificar una solicitud que ya está completada");
        }

        if (updateSolicitudDTO.getEstado() != null) {
            solicitud.setEstado(updateSolicitudDTO.getEstado());
        }

        if (updateSolicitudDTO.getComentarios() != null) {
            solicitud.setComentarios(updateSolicitudDTO.getComentarios());
        }

        Solicitud updatedSolicitud = solicitudRepository.save(solicitud);
        return modelMapper.map(updatedSolicitud, SolicitudDTO.class);
    }

    @Transactional
    public void deleteSolicitud(Long id) {
        Solicitud solicitud = solicitudRepository.findByIdAndActivoTrue(id)
                .orElseThrow(() -> new RuntimeException("Solicitud no encontrada con ID: " + id));

        if (solicitud.getEstado() != EstadoSolicitud.PENDIENTE) {
            throw new RuntimeException("Solo se pueden eliminar solicitudes en estado PENDIENTE");
        }

        solicitud.setActivo(false);
        solicitudRepository.save(solicitud);
    }

    @Transactional
    public SolicitudDTO reactivarSolicitud(Long id) {
        Solicitud solicitud = solicitudRepository.findById(id)
                .filter(s -> !s.getActivo()) // Aseguramos que esté inactiva
                .orElseThrow(() -> new RuntimeException("Solicitud no encontrada o ya está activa con ID: " + id));

        solicitud.setActivo(true);
        Solicitud reactivada = solicitudRepository.save(solicitud);
        return modelMapper.map(reactivada, SolicitudDTO.class);
    }
}
