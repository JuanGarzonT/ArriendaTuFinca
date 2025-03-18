package com.example.proyecto.proyecto.dto.output.pago;

import com.example.proyecto.proyecto.entities.Pago.EstadoPago;
import com.example.proyecto.proyecto.entities.Pago.MetodoPago;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PagoDTO {
    private Long id;
    private Long solicitudId;
    private BigDecimal monto;
    private LocalDateTime fechaPago;
    private MetodoPago metodoPago;
    private String referenciaPago;
    private EstadoPago estadoPago;
}