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

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

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
        return StreamSupport.stream(solicitudRepository.findAll().spliterator(), false)
                .map(solicitud -> modelMapper.map(solicitud, SolicitudSimpleDTO.class))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public SolicitudDTO getSolicitudById(Long id) {
        Solicitud solicitud = solicitudRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Solicitud no encontrada con ID: " + id));
        return modelMapper.map(solicitud, SolicitudDTO.class);
    }

    @Transactional(readOnly = true)
    public List<SolicitudSimpleDTO> getSolicitudesByArrendadorId(Long arrendadorId) {
        Usuario arrendador = usuarioRepository.findById(arrendadorId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + arrendadorId));
        
        return solicitudRepository.findByArrendador(arrendador).stream()
                .map(solicitud -> modelMapper.map(solicitud, SolicitudSimpleDTO.class))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<SolicitudSimpleDTO> getSolicitudesByArrendatarioId(Long arrendatarioId) {
        Usuario arrendatario = usuarioRepository.findById(arrendatarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + arrendatarioId));
        
        return solicitudRepository.findByArrendatario(arrendatario).stream()
                .map(solicitud -> modelMapper.map(solicitud, SolicitudSimpleDTO.class))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<SolicitudSimpleDTO> getSolicitudesByPropiedadId(Long propiedadId) {
        Propiedad propiedad = propiedadRepository.findById(propiedadId)
                .orElseThrow(() -> new RuntimeException("Propiedad no encontrada con ID: " + propiedadId));
        
        return solicitudRepository.findByPropiedad(propiedad).stream()
                .map(solicitud -> modelMapper.map(solicitud, SolicitudSimpleDTO.class))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<SolicitudSimpleDTO> getSolicitudesByEstado(EstadoSolicitud estado) {
        return solicitudRepository.findByEstado(estado).stream()
                .map(solicitud -> modelMapper.map(solicitud, SolicitudSimpleDTO.class))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<SolicitudSimpleDTO> getSolicitudesByPropiedadIdAndEstado(Long propiedadId, EstadoSolicitud estado) {
        Propiedad propiedad = propiedadRepository.findById(propiedadId)
                .orElseThrow(() -> new RuntimeException("Propiedad no encontrada con ID: " + propiedadId));
        
        return solicitudRepository.findByPropiedadAndEstado(propiedad, estado).stream()
                .map(solicitud -> modelMapper.map(solicitud, SolicitudSimpleDTO.class))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public boolean verificarDisponibilidadFechas(Long propiedadId, LocalDate fechaInicio, LocalDate fechaFin) {
        Propiedad propiedad = propiedadRepository.findById(propiedadId)
                .orElseThrow(() -> new RuntimeException("Propiedad no encontrada con ID: " + propiedadId));
        
        // Verificar si la propiedad está disponible
        if (!propiedad.isDisponible()) {
            return false;
        }
        
        // Verificar si hay solicitudes aprobadas que se solapan con las fechas
        List<Solicitud> solicitudesSolapadas = solicitudRepository.buscarSolicitudesPorFechas(
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
        
        Propiedad propiedad = propiedadRepository.findById(createSolicitudDTO.getPropiedadId())
                .orElseThrow(() -> new RuntimeException("Propiedad no encontrada con ID: " + createSolicitudDTO.getPropiedadId()));
        
        // Verificar disponibilidad de fechas
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
        
        // Calcular monto total
        long dias = ChronoUnit.DAYS.between(createSolicitudDTO.getFechaInicio(), createSolicitudDTO.getFechaFin()) + 1;
        BigDecimal montoTotal = propiedad.getPrecioPorDia().multiply(BigDecimal.valueOf(dias));
        solicitud.setMontoTotal(montoTotal);
        
        Solicitud savedSolicitud = solicitudRepository.save(solicitud);
        return modelMapper.map(savedSolicitud, SolicitudDTO.class);
    }

    @Transactional
    public SolicitudDTO updateEstadoSolicitud(Long id, SolicitudUpdateDTO updateSolicitudDTO) {
        Solicitud solicitud = solicitudRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Solicitud no encontrada con ID: " + id));
        
        // Validar el cambio de estado
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
        if (!solicitudRepository.existsById(id)) {
            throw new RuntimeException("Solicitud no encontrada con ID: " + id);
        }
        
        Solicitud solicitud = solicitudRepository.findById(id).get();
        if (solicitud.getEstado() != EstadoSolicitud.PENDIENTE) {
            throw new RuntimeException("Solo se pueden eliminar solicitudes en estado PENDIENTE");
        }
        
        solicitudRepository.deleteById(id);
    }
}