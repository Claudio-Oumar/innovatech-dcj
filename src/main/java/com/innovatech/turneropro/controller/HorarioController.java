package com.innovatech.turneropro.controller;

import com.innovatech.turneropro.dto.HorarioRequest;
import com.innovatech.turneropro.model.HorarioBarbero;
import com.innovatech.turneropro.service.HorarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/horarios")
@CrossOrigin(origins = "*")
public class HorarioController {
    
    @Autowired
    private HorarioService horarioService;
    
    @PostMapping
    @PreAuthorize("hasRole('BARBERO')")
    public ResponseEntity<?> crearHorario(@Valid @RequestBody HorarioRequest request) {
        try {
            HorarioBarbero horario = horarioService.crearHorario(request);
            return ResponseEntity.ok(horario);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        }
    }
    
    @GetMapping("/mis-horarios")
    @PreAuthorize("hasRole('BARBERO')")
    public ResponseEntity<List<HorarioBarbero>> obtenerMisHorarios() {
        List<HorarioBarbero> horarios = horarioService.obtenerMisHorarios();
        return ResponseEntity.ok(horarios);
    }
    
    @GetMapping("/barbero/{barberoId}")
    public ResponseEntity<List<HorarioBarbero>> obtenerHorariosBarbero(@PathVariable Long barberoId) {
        List<HorarioBarbero> horarios = horarioService.obtenerHorariosBarbero(barberoId);
        return ResponseEntity.ok(horarios);
    }
    
    @DeleteMapping("/{horarioId}")
    @PreAuthorize("hasRole('BARBERO')")
    public ResponseEntity<?> eliminarHorario(@PathVariable Long horarioId) {
        try {
            horarioService.eliminarHorario(horarioId);
            return ResponseEntity.ok().body(new SuccessResponse("Horario eliminado correctamente"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        }
    }
    
    // Clases auxiliares
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
    
    static class SuccessResponse {
        private String mensaje;
        
        public SuccessResponse(String mensaje) {
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
