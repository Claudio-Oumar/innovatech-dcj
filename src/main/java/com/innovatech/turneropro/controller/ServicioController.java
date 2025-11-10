package com.innovatech.turneropro.controller;

import com.innovatech.turneropro.model.Servicio;
import com.innovatech.turneropro.repository.ServicioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/servicios")
@CrossOrigin(origins = "*")
public class ServicioController {
    
    @Autowired
    private ServicioRepository servicioRepository;
    
    @GetMapping
    public ResponseEntity<List<Servicio>> obtenerServicios() {
        List<Servicio> servicios = servicioRepository.findByActivoTrue();
        return ResponseEntity.ok(servicios);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerServicioPorId(@PathVariable Long id) {
        return servicioRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
