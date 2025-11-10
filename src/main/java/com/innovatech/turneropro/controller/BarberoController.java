package com.innovatech.turneropro.controller;

import com.innovatech.turneropro.model.Usuario;
import com.innovatech.turneropro.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/barberos")
@CrossOrigin(origins = "*")
public class BarberoController {
    
    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @GetMapping("/disponibles")
    public ResponseEntity<List<Usuario>> obtenerBarberosDisponibles() {
        List<Usuario> barberos = usuarioRepository.findByRol(Usuario.RolUsuario.BARBERO);
        return ResponseEntity.ok(barberos);
    }
}
