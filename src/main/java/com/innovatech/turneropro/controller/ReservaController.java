package com.innovatech.turneropro.controller;

import com.innovatech.turneropro.dto.ReservaRequest;
import com.innovatech.turneropro.model.Reserva;
import com.innovatech.turneropro.service.ReservaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/reservas")
@CrossOrigin(origins = "*")
public class ReservaController {
    
    @Autowired
    private ReservaService reservaService;
    
    @PostMapping
    @PreAuthorize("hasRole('CLIENTE')")
    public ResponseEntity<?> crearReserva(@Valid @RequestBody ReservaRequest request) {
        try {
            Reserva reserva = reservaService.crearReserva(request);
            return ResponseEntity.ok(reserva);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        }
    }
    
    @GetMapping("/mis-reservas")
    public ResponseEntity<List<Reserva>> obtenerMisReservas() {
        List<Reserva> reservas = reservaService.obtenerMisReservas();
        return ResponseEntity.ok(reservas);
    }
    
    @PutMapping("/{reservaId}/cancelar")
    @PreAuthorize("hasRole('CLIENTE')")
    public ResponseEntity<?> cancelarReserva(
            @PathVariable Long reservaId,
            @RequestBody Map<String, String> body) {
        try {
            String motivo = body.getOrDefault("motivo", "Sin motivo especificado");
            Reserva reserva = reservaService.cancelarReserva(reservaId, motivo);
            return ResponseEntity.ok(reserva);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        }
    }
    
    // Clase auxiliar
    static class ErrorResponse {
        private String mensaje;
        
        public ErrorResponse(String mensaje) {
            this.mensaje = mensaje;
        }
        
        public String getMensaje() {
            return mensaje;
        }
        
        public void setMensaje(String mensaje) {
            this.mensaje = mensaje;
        }
    }
}
