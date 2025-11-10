package com.innovatech.turneropro.service;

import com.innovatech.turneropro.model.Reserva;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;

@Service
public class EmailService {
    
    @Autowired
    private JavaMailSender mailSender;
    
    @Value("${mail.from}")
    private String fromEmail;
    
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    
    public void enviarConfirmacionReserva(Reserva reserva) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(reserva.getCliente().getEmail());
            message.setSubject("Confirmación de Reserva - TurneroPro");
            
            String texto = String.format(
                "Hola %s,\n\n" +
                "Tu reserva ha sido confirmada exitosamente.\n\n" +
                "Detalles de la reserva:\n" +
                "- Barbero: %s\n" +
                "- Servicio: %s\n" +
                "- Fecha y hora: %s\n" +
                "- Duración aproximada: %d minutos\n\n" +
                "Te esperamos!\n\n" +
                "Equipo TurneroPro - Barber Shop Edition",
                reserva.getCliente().getNombreCompleto(),
                reserva.getBarbero().getNombreCompleto(),
                reserva.getServicio().getNombre(),
                reserva.getFechaHoraInicio().format(FORMATTER),
                reserva.getServicio().getDuracionMinutos()
            );
            
            message.setText(texto);
            mailSender.send(message);
            
            System.out.println("Email de confirmación enviado a: " + reserva.getCliente().getEmail());
        } catch (Exception e) {
            System.err.println("Error al enviar email de confirmación: " + e.getMessage());
        }
    }
    
    public void enviarCancelacionReserva(Reserva reserva) {
        try {
            // Email al cliente
            SimpleMailMessage messageCliente = new SimpleMailMessage();
            messageCliente.setFrom(fromEmail);
            messageCliente.setTo(reserva.getCliente().getEmail());
            messageCliente.setSubject("Cancelación de Reserva - TurneroPro");
            
            String textoCliente = String.format(
                "Hola %s,\n\n" +
                "Tu reserva ha sido cancelada.\n\n" +
                "Detalles de la reserva cancelada:\n" +
                "- Barbero: %s\n" +
                "- Servicio: %s\n" +
                "- Fecha y hora: %s\n" +
                "- Motivo: %s\n\n" +
                "Puedes realizar una nueva reserva cuando lo desees.\n\n" +
                "Equipo TurneroPro - Barber Shop Edition",
                reserva.getCliente().getNombreCompleto(),
                reserva.getBarbero().getNombreCompleto(),
                reserva.getServicio().getNombre(),
                reserva.getFechaHoraInicio().format(FORMATTER),
                reserva.getMotivoCancelacion() != null ? reserva.getMotivoCancelacion() : "No especificado"
            );
            
            messageCliente.setText(textoCliente);
            mailSender.send(messageCliente);
            
            // Email al barbero
            SimpleMailMessage messageBarbero = new SimpleMailMessage();
            messageBarbero.setFrom(fromEmail);
            messageBarbero.setTo(reserva.getBarbero().getEmail());
            messageBarbero.setSubject("Cancelación de Reserva - TurneroPro");
            
            String textoBarbero = String.format(
                "Hola %s,\n\n" +
                "Se ha cancelado una reserva:\n\n" +
                "- Cliente: %s\n" +
                "- Servicio: %s\n" +
                "- Fecha y hora: %s\n\n" +
                "Equipo TurneroPro - Barber Shop Edition",
                reserva.getBarbero().getNombreCompleto(),
                reserva.getCliente().getNombreCompleto(),
                reserva.getServicio().getNombre(),
                reserva.getFechaHoraInicio().format(FORMATTER)
            );
            
            messageBarbero.setText(textoBarbero);
            mailSender.send(messageBarbero);
            
            System.out.println("Emails de cancelación enviados");
        } catch (Exception e) {
            System.err.println("Error al enviar email de cancelación: " + e.getMessage());
        }
    }
}
