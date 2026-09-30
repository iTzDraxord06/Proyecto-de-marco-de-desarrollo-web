package com.clinica.controller;

import com.clinica.model.Paciente;
import com.clinica.service.PacienteService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/pacientes")
public class PacienteController {

    @Autowired
    private PacienteService pacienteService;

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("pacientes", pacienteService.listarTodos());
        model.addAttribute("seccionActiva", "pacientes");
        return "pacientes/listar";
    }

    @GetMapping("/nuevo")
    public String nuevo(Model model) {
        model.addAttribute("paciente", new Paciente());
        model.addAttribute("seccionActiva", "pacientes");
        return "pacientes/formulario";
    }

    @PostMapping("/guardar")
    public String guardar(@Valid @ModelAttribute("paciente") Paciente paciente, 
                          BindingResult result, 
                          Model model) {
        if (result.hasErrors()) {
            model.addAttribute("seccionActiva", "pacientes");
            return "pacientes/formulario";
        }
        pacienteService.guardar(paciente);
        return "redirect:/pacientes";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        pacienteService.buscarPorId(id).ifPresent(p -> model.addAttribute("paciente", p));
        model.addAttribute("seccionActiva", "pacientes");
        return "pacientes/formulario";
    }
}