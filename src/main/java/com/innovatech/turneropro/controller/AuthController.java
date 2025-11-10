package com.innovatech.turneropro.controller;

import com.innovatech.turneropro.dto.AuthResponse;
import com.innovatech.turneropro.dto.LoginRequest;
import com.innovatech.turneropro.dto.RegistroRequest;
import com.innovatech.turneropro.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {
    
    @Autowired
    private AuthService authService;
    
    @PostMapping("/registro")
    public ResponseEntity<?> registrarUsuario(@Valid @RequestBody RegistroRequest request) {
        try {
            AuthResponse response = authService.registrarUsuario(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        }
    }
    
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
        try {
            AuthResponse response = authService.login(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ErrorResponse("Credenciales inválidas"));
        }
    }
    
    // Clase auxiliar para respuestas de error
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
