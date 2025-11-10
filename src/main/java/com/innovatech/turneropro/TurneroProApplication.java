package com.innovatech.turneropro;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class TurneroProApplication {
    public static void main(String[] args) {
        SpringApplication.run(TurneroProApplication.class, args);
        System.out.println("==============================================");
        System.out.println("TurneroPro - Barber Shop Edition");
        System.out.println("Aplicación iniciada correctamente");
        System.out.println("Accede a: http://localhost:8080");
        System.out.println("==============================================");
    }
}
