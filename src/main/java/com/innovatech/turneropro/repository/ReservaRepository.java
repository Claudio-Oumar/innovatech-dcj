package com.innovatech.turneropro.repository;

import com.innovatech.turneropro.model.Reserva;
import com.innovatech.turneropro.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Long> {
    List<Reserva> findByCliente(Usuario cliente);
    List<Reserva> findByBarbero(Usuario barbero);
    List<Reserva> findByEstado(Reserva.EstadoReserva estado);
    
    @Query("SELECT r FROM Reserva r WHERE r.barbero = :barbero " +
           "AND r.fechaHoraInicio < :fin AND r.fechaHoraFin > :inicio " +
           "AND r.estado != 'CANCELADA'")
    List<Reserva> findReservasSolapadas(
        @Param("barbero") Usuario barbero,
        @Param("inicio") LocalDateTime inicio,
        @Param("fin") LocalDateTime fin
    );
    
    List<Reserva> findByClienteAndEstadoNot(Usuario cliente, Reserva.EstadoReserva estado);
}
