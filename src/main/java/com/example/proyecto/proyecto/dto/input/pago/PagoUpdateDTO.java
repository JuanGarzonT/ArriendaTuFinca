package com.example.proyecto.proyecto.dto.input.pago;

import com.example.proyecto.proyecto.entities.Pago.EstadoPago;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PagoUpdateDTO {
    private EstadoPago estadoPago;
    private String referenciaPago;
}