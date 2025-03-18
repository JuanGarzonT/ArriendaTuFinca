package com.example.proyecto.proyecto.services;

import com.example.proyecto.proyecto.dto.input.pago.PagoCreateDTO;
import com.example.proyecto.proyecto.dto.input.pago.PagoUpdateDTO;
import com.example.proyecto.proyecto.dto.output.pago.PagoDTO;
import com.example.proyecto.proyecto.entities.Pago;
import com.example.proyecto.proyecto.entities.Pago.EstadoPago;
import com.example.proyecto.proyecto.entities.Solicitud;
import com.example.proyecto.proyecto.repository.PagoRepository;
import com.example.proyecto.proyecto.repository.SolicitudRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class PagoService {

    @Autowired
    private PagoRepository pagoRepository;
    
    @Autowired
    private SolicitudRepository solicitudRepository;
    
    @Autowired
    private ModelMapper modelMapper;

    @Transactional(readOnly = true)
    public List<PagoDTO> getAllPagos() {
        return StreamSupport.stream(pagoRepository.findAll().spliterator(), false)
                .map(pago -> modelMapper.map(pago, PagoDTO.class))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public PagoDTO getPagoById(Long id) {
        Pago pago = pagoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pago no encontrado con ID: " + id));
        return modelMapper.map(pago, PagoDTO.class);
    }

    @Transactional(readOnly = true)
    public PagoDTO getPagoBySolicitudId(Long solicitudId) {
        Solicitud solicitud = solicitudRepository.findById(solicitudId)
                .orElseThrow(() -> new RuntimeException("Solicitud no encontrada con ID: " + solicitudId));
        
        Pago pago = pagoRepository.findBySolicitud(solicitud)
                .orElseThrow(() -> new RuntimeException("No existe un pago para la solicitud con ID: " + solicitudId));
        
        return modelMapper.map(pago, PagoDTO.class);
    }

    @Transactional(readOnly = true)
    public List<PagoDTO> getPagosByEstado(EstadoPago estadoPago) {
        return pagoRepository.findByEstadoPago(estadoPago).stream()
                .map(pago -> modelMapper.map(pago, PagoDTO.class))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<PagoDTO> getPagosByFechas(LocalDateTime inicio, LocalDateTime fin) {
        return pagoRepository.findByFechaPagoBetween(inicio, fin).stream()
                .map(pago -> modelMapper.map(pago, PagoDTO.class))
                .collect(Collectors.toList());
    }

    @Transactional
    public PagoDTO createPago(PagoCreateDTO createPagoDTO) {
        Solicitud solicitud = solicitudRepository.findById(createPagoDTO.getSolicitudId())
                .orElseThrow(() -> new RuntimeException("Solicitud no encontrada con ID: " + createPagoDTO.getSolicitudId()));
        
        // Verificar si ya existe un pago para esta solicitud
        if (pagoRepository.existsBySolicitud(solicitud)) {
            throw new RuntimeException("Ya existe un pago para la solicitud con ID: " + createPagoDTO.getSolicitudId());
        }
        
        Pago pago = new Pago();
        pago.setSolicitud(solicitud);
        pago.setMonto(createPagoDTO.getMonto());
        pago.setFechaPago(LocalDateTime.now());
        pago.setMetodoPago(createPagoDTO.getMetodoPago());
        pago.setReferenciaPago(createPagoDTO.getReferenciaPago());
        pago.setEstadoPago(EstadoPago.PENDIENTE); // Por defecto, el pago queda en estado pendiente
        
        Pago savedPago = pagoRepository.save(pago);
        return modelMapper.map(savedPago, PagoDTO.class);
    }

    @Transactional
    public PagoDTO updatePago(Long id, PagoUpdateDTO updatePagoDTO) {
        Pago pago = pagoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pago no encontrado con ID: " + id));
        
        // No se permite modificar un pago que ya está completado o reembolsado
        if (pago.getEstadoPago() == EstadoPago.COMPLETADO || pago.getEstadoPago() == EstadoPago.REEMBOLSADO) {
            throw new RuntimeException("No se puede modificar un pago que ya está " + pago.getEstadoPago());
        }
        
        if (updatePagoDTO.getEstadoPago() != null) {
            pago.setEstadoPago(updatePagoDTO.getEstadoPago());
        }
        
        if (updatePagoDTO.getReferenciaPago() != null) {
            pago.setReferenciaPago(updatePagoDTO.getReferenciaPago());
        }
        
        Pago updatedPago = pagoRepository.save(pago);
        return modelMapper.map(updatedPago, PagoDTO.class);
    }

    @Transactional
    public void deletePago(Long id) {
        if (!pagoRepository.existsById(id)) {
            throw new RuntimeException("Pago no encontrado con ID: " + id);
        }
        
        Pago pago = pagoRepository.findById(id).get();
        if (pago.getEstadoPago() != EstadoPago.PENDIENTE) {
            throw new RuntimeException("Solo se pueden eliminar pagos en estado PENDIENTE");
        }
        
        pagoRepository.deleteById(id);
    }
}