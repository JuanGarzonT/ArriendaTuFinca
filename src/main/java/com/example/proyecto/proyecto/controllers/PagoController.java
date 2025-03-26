package com.example.proyecto.proyecto.controllers;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
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

import com.example.proyecto.proyecto.dto.input.pago.PagoCreateDTO;
import com.example.proyecto.proyecto.dto.input.pago.PagoUpdateDTO;
import com.example.proyecto.proyecto.dto.output.pago.PagoDTO;
import com.example.proyecto.proyecto.entities.Pago.EstadoPago;
import com.example.proyecto.proyecto.services.PagoService;

@RestController
@RequestMapping("/api/pagos")
public class PagoController {

    @Autowired
    private PagoService pagoService;

     @GetMapping("/listar")
    public List<PagoDTO> getAllPagos() {
        return pagoService.getAllPagos();
    }

    @GetMapping("/buscar/{id}")
    public PagoDTO getPagoById(@PathVariable Long id) {
        return pagoService.getPagoById(id);
    }

    @GetMapping("/buscarPorSolicitud/{solicitudId}")
    public PagoDTO getPagoBySolicitudId(@PathVariable Long solicitudId) {
        return pagoService.getPagoBySolicitudId(solicitudId);
    }

    @GetMapping("/buscarPorEstado/{estado}")
    public List<PagoDTO> getPagosByEstado(@PathVariable EstadoPago estado) {
        return pagoService.getPagosByEstado(estado);
    }

    @GetMapping("/buscarPorFechas")
    public List<PagoDTO> getPagosByFechas(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fin) {
        return pagoService.getPagosByFechas(inicio, fin);
    }

    @PostMapping("/registrar")
    public PagoDTO createPago(@RequestBody PagoCreateDTO createPagoDTO) {
        return pagoService.createPago(createPagoDTO);
    }

    @PutMapping("/actualizar/{id}")
    public PagoDTO updatePago(
            @PathVariable Long id,
            @RequestBody PagoUpdateDTO updatePagoDTO) {
        return pagoService.updatePago(id, updatePagoDTO);
    }

    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<Void> softDeletePago(@PathVariable Long id) {
        pagoService.softDeletePago(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/reactivar/{id}")
    public PagoDTO reactivarPago(@PathVariable Long id) {
        return pagoService.reactivarPago(id);
    }
}