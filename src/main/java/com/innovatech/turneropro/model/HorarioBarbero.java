package com.innovatech.turneropro.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.DayOfWeek;
import java.time.LocalTime;

@Entity
@Table(name = "horarios_barbero")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HorarioBarbero {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "barbero_id", nullable = false)
    private Usuario barbero;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DayOfWeek diaSemana; // MONDAY, TUESDAY, etc.
    
    @Column(nullable = false)
    private LocalTime horaInicio;
    
    @Column(nullable = false)
    private LocalTime horaFin;
    
    @Column(nullable = false)
    private Boolean activo = true;
}
