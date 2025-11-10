package com.innovatech.turneropro.config;

import com.innovatech.turneropro.model.HorarioBarbero;
import com.innovatech.turneropro.model.Servicio;
import com.innovatech.turneropro.model.Usuario;
import com.innovatech.turneropro.repository.HorarioBarberoRepository;
import com.innovatech.turneropro.repository.ServicioRepository;
import com.innovatech.turneropro.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Component
public class DataSeeder implements CommandLineRunner {
    
    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Autowired
    private ServicioRepository servicioRepository;
    
    @Autowired
    private HorarioBarberoRepository horarioBarberoRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Override
    public void run(String... args) throws Exception {
        // Solo ejecutar si la BD está vacía
        if (usuarioRepository.count() > 0) {
            System.out.println("La base de datos ya contiene datos. Omitiendo seeding...");
            return;
        }
        
        System.out.println("===========================================");
        System.out.println("Inicializando base de datos con datos de prueba...");
        System.out.println("===========================================");
        
        // Crear usuarios de prueba
        crearUsuarios();
        
        // Crear servicios
        crearServicios();
        
        // Crear horarios para barberos
        crearHorarios();
        
        System.out.println("===========================================");
        System.out.println("[OK] Datos de prueba creados exitosamente!");
        System.out.println("===========================================");
        System.out.println("Usuarios de prueba:");
        System.out.println("  Admin: admin / password123");
        System.out.println("  Barbero1: barbero1 / password123");
        System.out.println("  Barbero2: barbero2 / password123");
        System.out.println("  Cliente1: cliente1 / password123");
        System.out.println("  Cliente2: cliente2 / password123");
        System.out.println("===========================================");
    }
    
    private void crearUsuarios() {
        // Administrador
        Usuario admin = new Usuario();
        admin.setUsername("admin");
        admin.setEmail("admin@turneropro.com");
        admin.setPassword(passwordEncoder.encode("password123"));
        admin.setNombreCompleto("Administrador Sistema");
        admin.setTelefono("0991234567");
        admin.setRol(Usuario.RolUsuario.ADMINISTRADOR);
        admin.setActivo(true);
        admin.setFechaCreacion(LocalDateTime.now());
        usuarioRepository.save(admin);
        
        // Barbero 1
        Usuario barbero1 = new Usuario();
        barbero1.setUsername("barbero1");
        barbero1.setEmail("barbero1@turneropro.com");
        barbero1.setPassword(passwordEncoder.encode("password123"));
        barbero1.setNombreCompleto("Carlos Martínez");
        barbero1.setTelefono("0991234568");
        barbero1.setRol(Usuario.RolUsuario.BARBERO);
        barbero1.setActivo(true);
        barbero1.setFechaCreacion(LocalDateTime.now());
        usuarioRepository.save(barbero1);
        
        // Barbero 2
        Usuario barbero2 = new Usuario();
        barbero2.setUsername("barbero2");
        barbero2.setEmail("barbero2@turneropro.com");
        barbero2.setPassword(passwordEncoder.encode("password123"));
        barbero2.setNombreCompleto("Miguel Sánchez");
        barbero2.setTelefono("0991234569");
        barbero2.setRol(Usuario.RolUsuario.BARBERO);
        barbero2.setActivo(true);
        barbero2.setFechaCreacion(LocalDateTime.now());
        usuarioRepository.save(barbero2);
        
        // Cliente 1
        Usuario cliente1 = new Usuario();
        cliente1.setUsername("cliente1");
        cliente1.setEmail("cliente1@turneropro.com");
        cliente1.setPassword(passwordEncoder.encode("password123"));
        cliente1.setNombreCompleto("Juan Pérez");
        cliente1.setTelefono("0991234570");
        cliente1.setRol(Usuario.RolUsuario.CLIENTE);
        cliente1.setActivo(true);
        cliente1.setFechaCreacion(LocalDateTime.now());
        usuarioRepository.save(cliente1);
        
        // Cliente 2
        Usuario cliente2 = new Usuario();
        cliente2.setUsername("cliente2");
        cliente2.setEmail("cliente2@turneropro.com");
        cliente2.setPassword(passwordEncoder.encode("password123"));
        cliente2.setNombreCompleto("María González");
        cliente2.setTelefono("0991234571");
        cliente2.setRol(Usuario.RolUsuario.CLIENTE);
        cliente2.setActivo(true);
        cliente2.setFechaCreacion(LocalDateTime.now());
        usuarioRepository.save(cliente2);
        
        System.out.println("[OK] 5 usuarios creados");
    }
    
    private void crearServicios() {
        crearServicio("Corte Clásico", "Corte de cabello estilo clásico con tijeras y máquina", 30, 12.00);
        crearServicio("Corte + Barba", "Corte de cabello y arreglo de barba completo", 45, 18.00);
        crearServicio("Barba", "Arreglo y perfilado de barba", 20, 8.00);
        crearServicio("Rapado", "Rapado completo con máquina", 15, 10.00);
        crearServicio("Corte Premium", "Corte premium con lavado y peinado", 60, 25.00);
        crearServicio("Tinte de Cabello", "Aplicación de tinte de cabello completo", 90, 35.00);
        
        System.out.println("[OK] 6 servicios creados");
    }
    
    private void crearServicio(String nombre, String descripcion, int duracion, double precio) {
        Servicio servicio = new Servicio();
        servicio.setNombre(nombre);
        servicio.setDescripcion(descripcion);
        servicio.setDuracionMinutos(duracion);
        servicio.setPrecio(BigDecimal.valueOf(precio));
        servicio.setActivo(true);
        servicioRepository.save(servicio);
    }
    
    private void crearHorarios() {
        Usuario barbero1 = usuarioRepository.findByUsername("barbero1").orElse(null);
        Usuario barbero2 = usuarioRepository.findByUsername("barbero2").orElse(null);
        
        if (barbero1 != null) {
            // Horarios de lunes a viernes 9:00-18:00 para barbero1
            crearHorario(barbero1, DayOfWeek.MONDAY, LocalTime.of(9, 0), LocalTime.of(18, 0));
            crearHorario(barbero1, DayOfWeek.TUESDAY, LocalTime.of(9, 0), LocalTime.of(18, 0));
            crearHorario(barbero1, DayOfWeek.WEDNESDAY, LocalTime.of(9, 0), LocalTime.of(18, 0));
            crearHorario(barbero1, DayOfWeek.THURSDAY, LocalTime.of(9, 0), LocalTime.of(18, 0));
            crearHorario(barbero1, DayOfWeek.FRIDAY, LocalTime.of(9, 0), LocalTime.of(18, 0));
        }
        
        if (barbero2 != null) {
            // Horarios de lunes a sábado 10:00-19:00 para barbero2
            crearHorario(barbero2, DayOfWeek.MONDAY, LocalTime.of(10, 0), LocalTime.of(19, 0));
            crearHorario(barbero2, DayOfWeek.TUESDAY, LocalTime.of(10, 0), LocalTime.of(19, 0));
            crearHorario(barbero2, DayOfWeek.WEDNESDAY, LocalTime.of(10, 0), LocalTime.of(19, 0));
            crearHorario(barbero2, DayOfWeek.THURSDAY, LocalTime.of(10, 0), LocalTime.of(19, 0));
            crearHorario(barbero2, DayOfWeek.FRIDAY, LocalTime.of(10, 0), LocalTime.of(19, 0));
            crearHorario(barbero2, DayOfWeek.SATURDAY, LocalTime.of(10, 0), LocalTime.of(19, 0));
        }
        
        System.out.println("[OK] Horarios configurados para 2 barberos");
    }
    
    private void crearHorario(Usuario barbero, DayOfWeek dia, LocalTime inicio, LocalTime fin) {
        HorarioBarbero horario = new HorarioBarbero();
        horario.setBarbero(barbero);
        horario.setDiaSemana(dia);
        horario.setHoraInicio(inicio);
        horario.setHoraFin(fin);
        horario.setActivo(true);
        horarioBarberoRepository.save(horario);
    }
}
