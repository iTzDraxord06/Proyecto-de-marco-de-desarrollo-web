package com.clinica.controller;

import com.clinica.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/medicos")
public class MedicoController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @GetMapping
    public String listar(Model model) {
        var medicos = usuarioRepository.findAll().stream()
                .filter(u -> "ROLE_MEDICO".equals(u.getRol()))
                .toList();

        model.addAttribute("medicos", medicos);
        model.addAttribute("seccionActiva", "medicos");
        return "medicos/listar";
    }
}