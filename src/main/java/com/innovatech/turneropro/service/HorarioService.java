package com.innovatech.turneropro.service;

import com.innovatech.turneropro.dto.HorarioRequest;
import com.innovatech.turneropro.model.HorarioBarbero;
import com.innovatech.turneropro.model.Usuario;
import com.innovatech.turneropro.repository.HorarioBarberoRepository;
import com.innovatech.turneropro.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.util.List;

@Service
public class HorarioService {
    
    @Autowired
    private HorarioBarberoRepository horarioRepository;
    
    @Autowired
    private UsuarioRepository usuarioRepository;
    
    public HorarioBarbero crearHorario(HorarioRequest request) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Usuario barbero = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        
        if (barbero.getRol() != Usuario.RolUsuario.BARBERO) {
            throw new RuntimeException("Solo los barberos pueden configurar horarios");
        }
        
        HorarioBarbero horario = new HorarioBarbero();
        horario.setBarbero(barbero);
        horario.setDiaSemana(DayOfWeek.valueOf(request.getDiaSemana().toUpperCase()));
        horario.setHoraInicio(request.getHoraInicio());
        horario.setHoraFin(request.getHoraFin());
        horario.setActivo(true);
        
        return horarioRepository.save(horario);
    }
    
    public List<HorarioBarbero> obtenerHorariosBarbero(Long barberoId) {
        Usuario barbero = usuarioRepository.findById(barberoId)
                .orElseThrow(() -> new RuntimeException("Barbero no encontrado"));
        
        return horarioRepository.findByBarberoAndActivoTrue(barbero);
    }
    
    public List<HorarioBarbero> obtenerMisHorarios() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Usuario barbero = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        
        return horarioRepository.findByBarberoAndActivoTrue(barbero);
    }
    
    public void eliminarHorario(Long horarioId) {
        HorarioBarbero horario = horarioRepository.findById(horarioId)
                .orElseThrow(() -> new RuntimeException("Horario no encontrado"));
        
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        if (!horario.getBarbero().getUsername().equals(username)) {
            throw new RuntimeException("No tienes permiso para eliminar este horario");
        }
        
        horario.setActivo(false);
        horarioRepository.save(horario);
    }
}
