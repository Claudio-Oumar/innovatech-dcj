package com.innovatech.turneropro.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservaRequest {
    
    @NotNull(message = "El ID del barbero es obligatorio")
    private Long barberoId;
    
    @NotNull(message = "El ID del servicio es obligatorio")
    private Long servicioId;
    
    @NotNull(message = "La fecha y hora de inicio es obligatoria")
    private LocalDateTime fechaHoraInicio;
    
    private String notasCliente;
}
