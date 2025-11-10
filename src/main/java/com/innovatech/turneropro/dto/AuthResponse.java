package com.innovatech.turneropro.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {
    private String token;
    private String tipo = "Bearer";
    private Long id;
    private String username;
    private String email;
    private String nombreCompleto;
    private String rol;
    
    public AuthResponse(String token, Long id, String username, String email, String nombreCompleto, String rol) {
        this.token = token;
        this.id = id;
        this.username = username;
        this.email = email;
        this.nombreCompleto = nombreCompleto;
        this.rol = rol;
    }
}
