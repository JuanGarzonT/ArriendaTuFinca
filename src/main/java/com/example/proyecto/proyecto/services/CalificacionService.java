package com.example.proyecto.proyecto.services;

import com.example.proyecto.proyecto.dto.input.calificacion.CalificacionCreateDTO;
import com.example.proyecto.proyecto.dto.input.calificacion.CalificacionUpdateDTO;
import com.example.proyecto.proyecto.dto.output.calificacion.CalificacionDTO;
import com.example.proyecto.proyecto.entities.Calificacion;
import com.example.proyecto.proyecto.entities.Calificacion.TipoCalificacion;
import com.example.proyecto.proyecto.entities.Propiedad;
import com.example.proyecto.proyecto.entities.Usuario;
import com.example.proyecto.proyecto.repository.CalificacionRepository;
import com.example.proyecto.proyecto.repository.PropiedadRepository;
import com.example.proyecto.proyecto.repository.UsuarioRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CalificacionService {

    @Autowired
    private CalificacionRepository calificacionRepository;
    
    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Autowired
    private PropiedadRepository propiedadRepository;
    
    @Autowired
    private ModelMapper modelMapper;


    @Transactional(readOnly = true)
    public List<CalificacionDTO> getAllCalificaciones() {
        return calificacionRepository.findByActivoTrue()
                .stream()
                .map(calificacion -> modelMapper.map(calificacion, CalificacionDTO.class))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public CalificacionDTO getCalificacionById(Long id) {
        Calificacion calificacion = calificacionRepository.findByIdAndActivoTrue(id)
                .orElseThrow(() -> new RuntimeException("Calificación no encontrada con ID: " + id));
        return modelMapper.map(calificacion, CalificacionDTO.class);
    }

    @Transactional(readOnly = true)
    public List<CalificacionDTO> getCalificacionesByUsuarioId(Long usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + usuarioId));

        return calificacionRepository.findByUsuarioAndActivoTrue(usuario)
                .stream()
                .map(calificacion -> modelMapper.map(calificacion, CalificacionDTO.class))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<CalificacionDTO> getCalificacionesByPropiedadId(Long propiedadId) {
        Propiedad propiedad = propiedadRepository.findById(propiedadId)
                .orElseThrow(() -> new RuntimeException("Propiedad no encontrada con ID: " + propiedadId));

        return calificacionRepository.findByPropiedadAndActivoTrue(propiedad)
                .stream()
                .map(calificacion -> modelMapper.map(calificacion, CalificacionDTO.class))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<CalificacionDTO> getCalificacionesByTipo(TipoCalificacion tipoCalificacion) {
        return calificacionRepository.findByTipoCalificacionAndActivoTrue(tipoCalificacion)
                .stream()
                .map(calificacion -> modelMapper.map(calificacion, CalificacionDTO.class))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Double getPromedioPuntuacionPropiedad(Long propiedadId) {
        Propiedad propiedad = propiedadRepository.findById(propiedadId)
                .orElseThrow(() -> new RuntimeException("Propiedad no encontrada con ID: " + propiedadId));

        return calificacionRepository.calcularPromedioPuntuacionPropiedad(propiedad);
    }

    @Transactional(readOnly = true)
    public Double getPromedioPuntuacionUsuario(Long usuarioId, TipoCalificacion tipoCalificacion) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + usuarioId));

        return calificacionRepository.calcularPromedioPuntuacionUsuario(usuario, tipoCalificacion);
    }

    @Transactional
    public CalificacionDTO createCalificacion(Long usuarioId, CalificacionCreateDTO createCalificacionDTO) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + usuarioId));

        Propiedad propiedad = propiedadRepository.findById(createCalificacionDTO.getPropiedadId())
                .orElseThrow(() -> new RuntimeException("Propiedad no encontrada con ID: " + createCalificacionDTO.getPropiedadId()));

        // Validar puntuación (entre 1 y 5)
        if (createCalificacionDTO.getPuntuacion() < 1 || createCalificacionDTO.getPuntuacion() > 5) {
            throw new RuntimeException("La puntuación debe estar entre 1 y 5");
        }

        Calificacion calificacion = new Calificacion();
        calificacion.setUsuario(usuario);
        calificacion.setPropiedad(propiedad);
        calificacion.setPuntuacion(createCalificacionDTO.getPuntuacion());
        calificacion.setComentario(createCalificacionDTO.getComentario());
        calificacion.setFechaCalificacion(LocalDateTime.now());
        calificacion.setTipoCalificacion(createCalificacionDTO.getTipoCalificacion());
        calificacion.setActivo(true);

        Calificacion savedCalificacion = calificacionRepository.save(calificacion);
        return modelMapper.map(savedCalificacion, CalificacionDTO.class);
    }

    @Transactional
    public CalificacionDTO updateCalificacion(Long id, CalificacionUpdateDTO updateCalificacionDTO) {
        Calificacion calificacion = calificacionRepository.findByIdAndActivoTrue(id)
                .orElseThrow(() -> new RuntimeException("Calificación no encontrada con ID: " + id));

        if (updateCalificacionDTO.getPuntuacion() != null) {
            if (updateCalificacionDTO.getPuntuacion() < 1 || updateCalificacionDTO.getPuntuacion() > 5) {
                throw new RuntimeException("La puntuación debe estar entre 1 y 5");
            }
            calificacion.setPuntuacion(updateCalificacionDTO.getPuntuacion());
        }

        if (updateCalificacionDTO.getComentario() != null) {
            calificacion.setComentario(updateCalificacionDTO.getComentario());
        }

        Calificacion updatedCalificacion = calificacionRepository.save(calificacion);
        return modelMapper.map(updatedCalificacion, CalificacionDTO.class);
    }

    @Transactional
    public void softDeleteCalificacion(Long id) {
        Calificacion calificacion = calificacionRepository.findByIdAndActivoTrue(id)
                .orElseThrow(() -> new RuntimeException("Calificación no encontrada con ID: " + id));

        calificacion.setActivo(false);
        calificacionRepository.save(calificacion);
    }

    @Transactional
    public CalificacionDTO reactivarCalificacion(Long id) {
        Calificacion calificacion = calificacionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Calificación no encontrada con ID: " + id));

        if (calificacion.getActivo()) {
            throw new RuntimeException("La calificación ya está activa");
        }

        calificacion.setActivo(true);
        Calificacion reactivatedCalificacion = calificacionRepository.save(calificacion);
        return modelMapper.map(reactivatedCalificacion, CalificacionDTO.class);
    }
}