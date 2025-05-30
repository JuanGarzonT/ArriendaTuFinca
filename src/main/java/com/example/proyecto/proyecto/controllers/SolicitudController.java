package com.example.proyecto.proyecto.controllers;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.proyecto.proyecto.dto.input.solicitud.SolicitudCreateDTO;
import com.example.proyecto.proyecto.dto.input.solicitud.SolicitudUpdateDTO;
import com.example.proyecto.proyecto.dto.output.solicitud.SolicitudDTO;
import com.example.proyecto.proyecto.dto.output.solicitud.SolicitudSimpleDTO;
import com.example.proyecto.proyecto.entities.Solicitud.EstadoSolicitud;
import com.example.proyecto.proyecto.services.SolicitudService;


@RestController
@RequestMapping("/api/solicitudes")
//@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST})
public class SolicitudController {

    @Autowired
    private SolicitudService solicitudService;

    @GetMapping("/listar")
    public List<SolicitudSimpleDTO> getAllSolicitudes() {
        return solicitudService.getAllSolicitudes();
    }

    @GetMapping("/buscar/{id}")
    public SolicitudDTO getSolicitudById(@PathVariable Long id) {
        return solicitudService.getSolicitudById(id);
    }

    @GetMapping("/buscarPorArrendador/{arrendadorId}")
    public List<SolicitudSimpleDTO> getSolicitudesByArrendador(
            @PathVariable Long arrendadorId) {
        return solicitudService.getSolicitudesByArrendadorId(arrendadorId);
    }

    @GetMapping("/buscarPorArrendatario/{arrendatarioId}")
    public List<SolicitudSimpleDTO> getSolicitudesByArrendatario(
            @PathVariable Long arrendatarioId) {
        return solicitudService.getSolicitudesByArrendatarioId(arrendatarioId);
    }

    @GetMapping("/buscarPorPropiedad/{propiedadId}")
    public List<SolicitudSimpleDTO> getSolicitudesByPropiedad(
            @PathVariable Long propiedadId) {
        return solicitudService.getSolicitudesByPropiedadId(propiedadId);
    }

    @GetMapping("/buscarPorEstado/{estado}")
    public List<SolicitudSimpleDTO> getSolicitudesByEstado(
            @PathVariable EstadoSolicitud estado) {
        return solicitudService.getSolicitudesByEstado(estado);
    }

    @GetMapping("/buscarPorPropiedadYEstado/{propiedadId}/{estado}")
    public List<SolicitudSimpleDTO> getSolicitudesByPropiedadAndEstado(
            @PathVariable Long propiedadId,
            @PathVariable EstadoSolicitud estado) {
        return solicitudService.getSolicitudesByPropiedadIdAndEstado(propiedadId, estado);
    }

    @GetMapping("/verificarDisponibilidad")
    public Boolean verificarDisponibilidadFechas(
            @RequestParam Long propiedadId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin) {
        return solicitudService.verificarDisponibilidadFechas(propiedadId, fechaInicio, fechaFin);
    }

    @PostMapping("/registrar/{arrendadorId}")
    public SolicitudDTO createSolicitud(
            @PathVariable Long arrendadorId,
            @RequestBody SolicitudCreateDTO createSolicitudDTO) {
        return solicitudService.createSolicitud(arrendadorId, createSolicitudDTO);
    }

    @PutMapping("/actualizar/{id}")
    public SolicitudDTO updateEstadoSolicitud(
            @PathVariable Long id,
            @RequestBody SolicitudUpdateDTO updateSolicitudDTO) {
        return solicitudService.updateEstadoSolicitud(id, updateSolicitudDTO);
    }

    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<Void> deleteSolicitud(@PathVariable Long id) {
        solicitudService.deleteSolicitud(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/reactivar/{id}")
    public SolicitudDTO reactivarSolicitud(@PathVariable Long id) {
        return solicitudService.reactivarSolicitud(id);
    }
}
