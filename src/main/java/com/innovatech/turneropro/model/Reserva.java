package com.innovatech.turneropro.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "reservas")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Reserva {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id", nullable = false)
    private Usuario cliente;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "barbero_id", nullable = false)
    private Usuario barbero;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "servicio_id", nullable = false)
    private Servicio servicio;
    
    @Column(nullable = false)
    private LocalDateTime fechaHoraInicio;
    
    @Column(nullable = false)
    private LocalDateTime fechaHoraFin;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoReserva estado = EstadoReserva.PENDIENTE;
    
    @Column(columnDefinition = "TEXT")
    private String notasCliente;
    
    @Column(nullable = false, updatable = false)
    private LocalDateTime fechaCreacion = LocalDateTime.now();
    
    private LocalDateTime fechaCancelacion;
    
    @Column(columnDefinition = "TEXT")
    private String motivoCancelacion;
    
    // Enum para estado de reserva
    public enum EstadoReserva {
        PENDIENTE,
        CONFIRMADA,
        CANCELADA,
        COMPLETADA,
        NO_ASISTIO
    }
}
