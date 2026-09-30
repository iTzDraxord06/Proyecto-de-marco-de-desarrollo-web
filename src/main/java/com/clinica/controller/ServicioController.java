package com.clinica.controller;

import com.clinica.model.Especialidad;
import com.clinica.repository.EspecialidadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/servicios")
public class ServicioController {

    @Autowired
    private EspecialidadRepository especialidadRepository;

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("especialidades", especialidadRepository.findAll());
        model.addAttribute("nuevaEspecialidad", new Especialidad());
        model.addAttribute("seccionActiva", "servicios");
        return "servicios/listar";
    }

    @PostMapping("/guardar")
    public String guardar(@ModelAttribute("nuevaEspecialidad") Especialidad especialidad) {
        if (especialidad.getNombre() != null && !especialidad.getNombre().isBlank()) {
            especialidadRepository.save(especialidad);
        }
        return "redirect:/servicios";
    }
}