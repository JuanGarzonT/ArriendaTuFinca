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
        return pagoRepository.findByEstadoPagoAndActivoTrue(EstadoPago.PENDIENTE)
                .stream()
                .map(pago -> modelMapper.map(pago, PagoDTO.class))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public PagoDTO getPagoById(Long id) {
        Pago pago = pagoRepository.findByIdAndActivoTrue(id)
                .orElseThrow(() -> new RuntimeException("Pago no encontrado con ID: " + id));
        return modelMapper.map(pago, PagoDTO.class);
    }

    @Transactional(readOnly = true)
    public PagoDTO getPagoBySolicitudId(Long solicitudId) {
        Solicitud solicitud = solicitudRepository.findById(solicitudId)
                .orElseThrow(() -> new RuntimeException("Solicitud no encontrada con ID: " + solicitudId));

        Pago pago = pagoRepository.findBySolicitudAndActivoTrue(solicitud)
                .orElseThrow(() -> new RuntimeException("No existe un pago para la solicitud con ID: " + solicitudId));

        return modelMapper.map(pago, PagoDTO.class);
    }

    @Transactional(readOnly = true)
    public List<PagoDTO> getPagosByEstado(EstadoPago estadoPago) {
        return pagoRepository.findByEstadoPagoAndActivoTrue(estadoPago).stream()
                .map(pago -> modelMapper.map(pago, PagoDTO.class))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<PagoDTO> getPagosByFechas(LocalDateTime inicio, LocalDateTime fin) {
        return pagoRepository.findByFechaPagoBetweenAndActivoTrue(inicio, fin).stream()
                .map(pago -> modelMapper.map(pago, PagoDTO.class))
                .collect(Collectors.toList());
    }

    @Transactional
    public PagoDTO createPago(PagoCreateDTO createPagoDTO) {
        Solicitud solicitud = solicitudRepository.findById(createPagoDTO.getSolicitudId())
                .orElseThrow(() -> new RuntimeException("Solicitud no encontrada con ID: " + createPagoDTO.getSolicitudId()));

        if (pagoRepository.existsBySolicitudAndActivoTrue(solicitud)) {
            throw new RuntimeException("Ya existe un pago activo para la solicitud con ID: " + createPagoDTO.getSolicitudId());
        }

        Pago pago = new Pago();
        pago.setSolicitud(solicitud);
        pago.setMonto(createPagoDTO.getMonto());
        pago.setFechaPago(LocalDateTime.now());
        pago.setMetodoPago(createPagoDTO.getMetodoPago());
        pago.setReferenciaPago(createPagoDTO.getReferenciaPago());
        pago.setEstadoPago(EstadoPago.PENDIENTE);
        pago.setActivo(true);

        Pago savedPago = pagoRepository.save(pago);
        return modelMapper.map(savedPago, PagoDTO.class);
    }

    @Transactional
    public PagoDTO updatePago(Long id, PagoUpdateDTO updatePagoDTO) {
        Pago pago = pagoRepository.findByIdAndActivoTrue(id)
                .orElseThrow(() -> new RuntimeException("Pago no encontrado con ID: " + id));

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
    public void softDeletePago(Long id) {
        Pago pago = pagoRepository.findByIdAndActivoTrue(id)
                .orElseThrow(() -> new RuntimeException("Pago no encontrado con ID: " + id));

        pago.setActivo(false);
        pagoRepository.save(pago);
    }

    @Transactional
    public PagoDTO reactivarPago(Long id) {
        Pago pago = pagoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pago no encontrado con ID: " + id));

        if (pago.getActivo()) {
            throw new RuntimeException("El pago ya está activo");
        }

        pago.setActivo(true);
        Pago reactivatedPago = pagoRepository.save(pago);
        return modelMapper.map(reactivatedPago, PagoDTO.class);
    }
}