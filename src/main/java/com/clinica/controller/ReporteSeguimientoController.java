package com.clinica.controller;

import com.clinica.model.Cita;
import com.clinica.repository.CitaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class ReporteSeguimientoController {

    @Autowired
    private CitaRepository citaRepository;

    @GetMapping("/reportes")
    public String verReportes(Model model) {
        List<Cita> todas = citaRepository.findAll();

        long programadas = todas.stream().filter(c -> "PROGRAMADA".equalsIgnoreCase(c.getEstado())).count();
        long atendidas = todas.stream().filter(c -> "ATENDIDO".equalsIgnoreCase(c.getEstado())).count();
        long canceladas = todas.stream().filter(c -> "CANCELADA".equalsIgnoreCase(c.getEstado())).count();

        model.addAttribute("totalCitas", todas.size());
        model.addAttribute("programadas", programadas);
        model.addAttribute("atendidas", atendidas);
        model.addAttribute("canceladas", canceladas);
        model.addAttribute("citasAtendidas", todas);
        model.addAttribute("seccionActiva", "reportes");
        return "reportes/index";
    }

    @GetMapping("/seguimiento")
    public String verSeguimiento(Model model, Authentication auth) {
        List<Cita> historial = citaRepository.findAll().stream()
                .filter(c -> "ATENDIDO".equalsIgnoreCase(c.getEstado()) && c.getDiagnostico() != null)
                .toList();

        model.addAttribute("historial", historial);
        model.addAttribute("seccionActiva", "seguimiento");
        return "seguimiento/index";
    }
}