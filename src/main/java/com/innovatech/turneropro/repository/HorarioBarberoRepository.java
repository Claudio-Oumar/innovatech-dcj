package com.innovatech.turneropro.repository;

import com.innovatech.turneropro.model.HorarioBarbero;
import com.innovatech.turneropro.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.DayOfWeek;
import java.util.List;

@Repository
public interface HorarioBarberoRepository extends JpaRepository<HorarioBarbero, Long> {
    List<HorarioBarbero> findByBarberoAndActivoTrue(Usuario barbero);
    List<HorarioBarbero> findByBarberoAndDiaSemanaAndActivoTrue(Usuario barbero, DayOfWeek diaSemana);
}
