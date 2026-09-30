package com.clinica.controller;

import com.clinica.model.Cita;
import com.clinica.repository.CitaRepository;
import com.clinica.repository.EspecialidadRepository;
import com.clinica.repository.PacienteRepository;
import com.clinica.repository.UsuarioRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/citas")
public class CitaController {

    @Autowired
    private CitaRepository citaRepository;

    @Autowired
    private PacienteRepository pacienteRepository;

    @Autowired
    private EspecialidadRepository especialidadRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @GetMapping
    public String listar(Model model, Authentication auth) {
        List<Cita> citas;
        boolean esMedico = auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_MEDICO"));

        if (esMedico) {
            String nombreMedico = usuarioRepository.findByUsername(auth.getName())
                    .map(u -> u.getNombreCompleto()).orElse(auth.getName());
            citas = citaRepository.findAll().stream()
                    .filter(c -> c.getMedico() != null && c.getMedico().equalsIgnoreCase(nombreMedico))
                    .toList();
        } else {
            citas = citaRepository.findByOrderByFechaDescHoraDesc();
        }

        model.addAttribute("citas", citas);
        model.addAttribute("seccionActiva", "citas");
        return "citas/listar";
    }

    @GetMapping("/registrar")
    public String registrarForm(Model model) {
        model.addAttribute("cita", new Cita());
        model.addAttribute("pacientes", pacienteRepository.findAll());
        model.addAttribute("especialidades", especialidadRepository.findAll());
        model.addAttribute("medicos", usuarioRepository.findAll().stream()
                .filter(u -> "ROLE_MEDICO".equals(u.getRol())).toList());
        model.addAttribute("seccionActiva", "citas");
        return "citas/registrar";
    }

    @PostMapping("/guardar")
    public String guardar(@Valid @ModelAttribute("cita") Cita cita, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("pacientes", pacienteRepository.findAll());
            model.addAttribute("especialidades", especialidadRepository.findAll());
            model.addAttribute("medicos", usuarioRepository.findAll().stream()
                    .filter(u -> "ROLE_MEDICO".equals(u.getRol())).toList());
            model.addAttribute("seccionActiva", "citas");
            return "citas/registrar";
        }

        if (cita.getEstado() == null || cita.getEstado().isBlank()) {
            cita.setEstado("PROGRAMADA");
        }

        citaRepository.save(cita);
        return "redirect:/citas";
    }

    @PostMapping("/atender/{id}")
    public String atender(@PathVariable Long id, @RequestParam("diagnostico") String diagnostico) {
        citaRepository.findById(id).ifPresent(cita -> {
            cita.setEstado("ATENDIDO");
            cita.setDiagnostico(diagnostico);
            citaRepository.save(cita);
        });
        return "redirect:/citas";
    }

    @GetMapping("/cancelar/{id}")
    public String cancelar(@PathVariable Long id) {
        citaRepository.findById(id).ifPresent(cita -> {
            cita.setEstado("CANCELADA");
            citaRepository.save(cita);
        });
        return "redirect:/citas";
    }
}