package com.example.proyecto.proyecto.dto.input.pago;

import com.example.proyecto.proyecto.entities.Pago.MetodoPago;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PagoCreateDTO {
    private Long solicitudId;
    private BigDecimal monto;
    private MetodoPago metodoPago;
    private String referenciaPago;
}