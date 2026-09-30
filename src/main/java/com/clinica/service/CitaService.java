package com.clinica.service;

import com.clinica.model.Cita;
import com.clinica.repository.CitaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class CitaService {

    @Autowired
    private CitaRepository citaRepository;

    public List<Cita> listarTodas() {
        return citaRepository.findByOrderByFechaDescHoraDesc();
    }

    public Cita guardar(Cita cita) {
        if (cita.getEstado() == null || cita.getEstado().isBlank()) {
            cita.setEstado("PROGRAMADA");
        }
        return citaRepository.save(cita);
    }

    public Optional<Cita> buscarPorId(Long id) {
        return citaRepository.findById(id);
    }

    public boolean cancelar(Long id) {
        return citaRepository.findById(id).map(cita -> {
            cita.setEstado("CANCELADA");
            citaRepository.save(cita);
            return true;
        }).orElse(false);
    }
}