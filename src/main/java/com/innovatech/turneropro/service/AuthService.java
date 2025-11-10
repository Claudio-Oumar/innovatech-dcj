package com.innovatech.turneropro.service;

import com.innovatech.turneropro.dto.AuthResponse;
import com.innovatech.turneropro.dto.LoginRequest;
import com.innovatech.turneropro.dto.RegistroRequest;
import com.innovatech.turneropro.model.Usuario;
import com.innovatech.turneropro.repository.UsuarioRepository;
import com.innovatech.turneropro.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AuthService {
    
    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private AuthenticationManager authenticationManager;
    
    @Autowired
    private JwtTokenProvider tokenProvider;
    
    public AuthResponse registrarUsuario(RegistroRequest request) {
        // Validar que el username no exista
        if (usuarioRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("El username ya está en uso");
        }
        
        // Validar que el email no exista
        if (usuarioRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("El email ya está registrado");
        }
        
        // Crear nuevo usuario
        Usuario usuario = new Usuario();
        usuario.setUsername(request.getUsername());
        usuario.setEmail(request.getEmail());
        usuario.setPassword(passwordEncoder.encode(request.getPassword()));
        usuario.setNombreCompleto(request.getNombreCompleto());
        usuario.setTelefono(request.getTelefono());
        usuario.setRol(Usuario.RolUsuario.valueOf(request.getRol().toUpperCase()));
        usuario.setActivo(true);
        usuario.setFechaCreacion(LocalDateTime.now());
        
        usuarioRepository.save(usuario);
        
        // Autenticar automáticamente después del registro
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );
        
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = tokenProvider.generarToken(authentication);
        
        return new AuthResponse(token, usuario.getId(), usuario.getUsername(), 
                usuario.getEmail(), usuario.getNombreCompleto(), usuario.getRol().name());
    }
    
    public AuthResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsernameOrEmail(),
                        request.getPassword()
                )
        );
        
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = tokenProvider.generarToken(authentication);
        
        // Buscar usuario
        Usuario usuario = usuarioRepository.findByUsername(request.getUsernameOrEmail())
                .orElseGet(() -> usuarioRepository.findByEmail(request.getUsernameOrEmail())
                        .orElseThrow(() -> new RuntimeException("Usuario no encontrado")));
        
        // Actualizar último acceso
        usuario.setUltimoAcceso(LocalDateTime.now());
        usuarioRepository.save(usuario);
        
        return new AuthResponse(token, usuario.getId(), usuario.getUsername(), 
                usuario.getEmail(), usuario.getNombreCompleto(), usuario.getRol().name());
    }
}
