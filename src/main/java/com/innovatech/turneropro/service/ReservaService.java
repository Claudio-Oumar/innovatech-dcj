package com.innovatech.turneropro.service;

import com.innovatech.turneropro.dto.ReservaRequest;
import com.innovatech.turneropro.model.Reserva;
import com.innovatech.turneropro.model.Servicio;
import com.innovatech.turneropro.model.Usuario;
import com.innovatech.turneropro.repository.ReservaRepository;
import com.innovatech.turneropro.repository.ServicioRepository;
import com.innovatech.turneropro.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReservaService {
    
    @Autowired
    private ReservaRepository reservaRepository;
    
    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Autowired
    private ServicioRepository servicioRepository;
    
    @Autowired
    private EmailService emailService;
    
    public Reserva crearReserva(ReservaRequest request) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Usuario cliente = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        
        Usuario barbero = usuarioRepository.findById(request.getBarberoId())
                .orElseThrow(() -> new RuntimeException("Barbero no encontrado"));
        
        Servicio servicio = servicioRepository.findById(request.getServicioId())
                .orElseThrow(() -> new RuntimeException("Servicio no encontrado"));
        
        // Calcular hora de fin basándose en la duración del servicio
        LocalDateTime fechaHoraFin = request.getFechaHoraInicio()
                .plusMinutes(servicio.getDuracionMinutos());
        
        // Validar que no haya solapamiento
        List<Reserva> reservasSolapadas = reservaRepository.findReservasSolapadas(
                barbero, request.getFechaHoraInicio(), fechaHoraFin);
        
        if (!reservasSolapadas.isEmpty()) {
            throw new RuntimeException("El horario seleccionado no está disponible");
        }
        
        // Crear reserva
        Reserva reserva = new Reserva();
        reserva.setCliente(cliente);
        reserva.setBarbero(barbero);
        reserva.setServicio(servicio);
        reserva.setFechaHoraInicio(request.getFechaHoraInicio());
        reserva.setFechaHoraFin(fechaHoraFin);
        reserva.setNotasCliente(request.getNotasCliente());
        reserva.setEstado(Reserva.EstadoReserva.CONFIRMADA);
        reserva.setFechaCreacion(LocalDateTime.now());
        
        Reserva reservaGuardada = reservaRepository.save(reserva);
        
        // Enviar notificación por correo
        emailService.enviarConfirmacionReserva(reservaGuardada);
        
        return reservaGuardada;
    }
    
    public List<Reserva> obtenerMisReservas() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        
        if (usuario.getRol() == Usuario.RolUsuario.CLIENTE) {
            return reservaRepository.findByCliente(usuario);
        } else if (usuario.getRol() == Usuario.RolUsuario.BARBERO) {
            return reservaRepository.findByBarbero(usuario);
        }
        
        return List.of();
    }
    
    public Reserva cancelarReserva(Long reservaId, String motivo) {
        Reserva reserva = reservaRepository.findById(reservaId)
                .orElseThrow(() -> new RuntimeException("Reserva no encontrada"));
        
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        if (!reserva.getCliente().getUsername().equals(username)) {
            throw new RuntimeException("No tienes permiso para cancelar esta reserva");
        }
        
        if (reserva.getEstado() == Reserva.EstadoReserva.CANCELADA) {
            throw new RuntimeException("La reserva ya está cancelada");
        }
        
        reserva.setEstado(Reserva.EstadoReserva.CANCELADA);
        reserva.setFechaCancelacion(LocalDateTime.now());
        reserva.setMotivoCancelacion(motivo);
        
        Reserva reservaCancelada = reservaRepository.save(reserva);
        
        // Enviar notificación de cancelación
        emailService.enviarCancelacionReserva(reservaCancelada);
        
        return reservaCancelada;
    }
}
