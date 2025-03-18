package com.example.proyecto.proyecto.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.proyecto.proyecto.dto.input.calificacion.CalificacionCreateDTO;
import com.example.proyecto.proyecto.dto.input.calificacion.CalificacionUpdateDTO;
import com.example.proyecto.proyecto.dto.output.calificacion.CalificacionDTO;
import com.example.proyecto.proyecto.entities.Calificacion.TipoCalificacion;
import com.example.proyecto.proyecto.services.CalificacionService;

@RestController
@RequestMapping("/api/calificaciones")
public class CalificacionController {

    @Autowired
    private CalificacionService calificacionService;

    @GetMapping("/listar")
    public List<CalificacionDTO> getAllCalificaciones() {
        return calificacionService.getAllCalificaciones();
    }

    @GetMapping("/buscar/{id}")
    public CalificacionDTO getCalificacionById(@PathVariable Long id) {
        return calificacionService.getCalificacionById(id);
    }

    @GetMapping("/buscarPorUsuario/{usuarioId}")
    public List<CalificacionDTO> getCalificacionesByUsuario(@PathVariable Long usuarioId) {
        return calificacionService.getCalificacionesByUsuarioId(usuarioId);
    }

    @GetMapping("/buscarPorPropiedad/{propiedadId}")
    public List<CalificacionDTO> getCalificacionesByPropiedad(@PathVariable Long propiedadId) {
        return calificacionService.getCalificacionesByPropiedadId(propiedadId);
    }

    @GetMapping("/buscarPorTipo/{tipoCalificacion}")
    public List<CalificacionDTO> getCalificacionesByTipo(@PathVariable TipoCalificacion tipoCalificacion) {
        return calificacionService.getCalificacionesByTipo(tipoCalificacion);
    }

    @GetMapping("/promedioPorPropiedad/{propiedadId}")
    public Double getPromedioPuntuacionPropiedad(@PathVariable Long propiedadId) {
        return calificacionService.getPromedioPuntuacionPropiedad(propiedadId);
    }

    @GetMapping("/promedioPorUsuario/{usuarioId}/{tipoCalificacion}")
    public Double getPromedioPuntuacionUsuario(
            @PathVariable Long usuarioId,
            @PathVariable TipoCalificacion tipoCalificacion) {
        return calificacionService.getPromedioPuntuacionUsuario(usuarioId, tipoCalificacion);
    }

    @PostMapping("/registrar/{usuarioId}")
    public CalificacionDTO createCalificacion(
            @PathVariable Long usuarioId,
            @RequestBody CalificacionCreateDTO createCalificacionDTO) {
        return calificacionService.createCalificacion(usuarioId, createCalificacionDTO);
    }

    @PutMapping("/actualizar/{id}")
    public CalificacionDTO updateCalificacion(
            @PathVariable Long id,
            @RequestBody CalificacionUpdateDTO updateCalificacionDTO) {
        return calificacionService.updateCalificacion(id, updateCalificacionDTO);
    }

    @DeleteMapping("/eliminar/{id}")
    public void deleteCalificacion(@PathVariable Long id) {
        calificacionService.deleteCalificacion(id);
    }
}