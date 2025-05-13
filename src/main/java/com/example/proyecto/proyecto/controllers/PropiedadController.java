package com.example.proyecto.proyecto.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.proyecto.proyecto.dto.input.propiedad.PropiedadCreateDTO;
import com.example.proyecto.proyecto.dto.input.propiedad.PropiedadUpdateDTO;
import com.example.proyecto.proyecto.dto.output.propiedad.PropiedadDTO;
import com.example.proyecto.proyecto.dto.output.propiedad.PropiedadDetailDTO;
import com.example.proyecto.proyecto.dto.output.propiedad.PropiedadSimpleDTO;
import com.example.proyecto.proyecto.services.PropiedadService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/propiedades")
@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST})
public class PropiedadController {

    @Autowired
    private PropiedadService propiedadService;

 
    @GetMapping("/listar")
    public List<PropiedadSimpleDTO> getAllPropiedades() {
        return propiedadService.getAllPropiedades();
    }

    @GetMapping("/buscar/{id}")
    public PropiedadDTO getPropiedadById(@PathVariable Long id) {
        return propiedadService.getPropiedadById(id);
    }

    @GetMapping("/detalle/{id}")
    public PropiedadDetailDTO getPropiedadDetalleById(@PathVariable Long id) {
        return propiedadService.getPropiedadDetailById(id);
    }

    @GetMapping("/buscarPorArrendatario/{arrendatarioId}")
    public List<PropiedadDetailDTO> getPropiedadesByArrendatario(
            @PathVariable Long arrendatarioId) {
        return propiedadService.getPropiedadesByArrendatarioId(arrendatarioId);
    }

    @GetMapping("/listarDisponibles")
    public List<PropiedadSimpleDTO> getPropiedadesDisponibles() {
        return propiedadService.getPropiedadesByDisponibilidad(true);
    }

    @GetMapping("/buscarPorUbicacion")
    public List<PropiedadSimpleDTO> getPropiedadesByUbicacion(
            @RequestParam String ubicacion) {
        return propiedadService.getPropiedadesByUbicacion(ubicacion);
    }

    @GetMapping("/buscarPorCiudad/{ciudad}")
    public List<PropiedadSimpleDTO> getPropiedadesByCiudad(
            @PathVariable String ciudad) {
        return propiedadService.getPropiedadesByCiudad(ciudad);
    }

    @GetMapping("/buscarPorDepartamento/{departamento}")
    public List<PropiedadSimpleDTO> getPropiedadesByDepartamento(
            @PathVariable String departamento) {
        return propiedadService.getPropiedadesByDepartamento(departamento);
    }

    @GetMapping("/calificacionPromedio/{id}")
    public Double getCalificacionPromedio(@PathVariable Long id) {
        return propiedadService.getCalificacionPromedio(id);
    }

    @PostMapping("/registrar/{arrendatarioId}")
    public PropiedadDTO createPropiedad(
            @PathVariable Long arrendatarioId,
            @RequestBody PropiedadCreateDTO createPropiedadDTO) {
        return propiedadService.createPropiedad(arrendatarioId, createPropiedadDTO);
    }

    @PutMapping("/actualizar/{id}")
    public PropiedadDTO updatePropiedad(
            @PathVariable Long id,
            @RequestBody PropiedadUpdateDTO updatePropiedadDTO) {
        return propiedadService.updatePropiedad(id, updatePropiedadDTO);
    }

    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<Void> softDeletePropiedad(@PathVariable Long id) {
        propiedadService.softDeletePropiedad(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/reactivar/{id}")
    public PropiedadDTO reactivarPropiedad(@PathVariable Long id) {
        return propiedadService.reactivarPropiedad(id);
    }
}