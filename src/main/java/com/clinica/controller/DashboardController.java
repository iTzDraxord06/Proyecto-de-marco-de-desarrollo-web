package com.clinica.controller;

import com.clinica.repository.CitaRepository;
import com.clinica.repository.PacienteRepository;
import com.clinica.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import java.time.LocalDate;

@Controller
public class DashboardController {

    @Autowired
    private PacienteRepository pacienteRepository;

    @Autowired
    private CitaRepository citaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @GetMapping({"/", "/dashboard"})
    public String dashboard(Model model) {
        long totalPacientes = pacienteRepository.count();
        var todasLasCitas = citaRepository.findAll();

        long citasHoy = todasLasCitas.stream()
                .filter(c -> c.getFecha() != null && c.getFecha().equals(LocalDate.now()))
                .count();

        long citasPendientes = todasLasCitas.stream()
                .filter(c -> "PROGRAMADA".equalsIgnoreCase(c.getEstado()) || "Pendiente".equalsIgnoreCase(c.getEstado()))
                .count();

        long totalMedicos = usuarioRepository.findAll().stream()
                .filter(u -> "ROLE_MEDICO".equals(u.getRol()))
                .count();

        model.addAttribute("seccionActiva", "inicio");
        model.addAttribute("totalPacientes", totalPacientes);
        model.addAttribute("citasHoy", citasHoy);
        model.addAttribute("totalMedicos", totalMedicos); 
        model.addAttribute("totalPendientes", citasPendientes);
        model.addAttribute("proximasCitas", citaRepository.findByOrderByFechaDescHoraDesc());

        return "dashboard";
    }
}