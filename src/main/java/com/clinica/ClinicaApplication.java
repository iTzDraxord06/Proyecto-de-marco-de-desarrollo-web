package com.clinica;

import com.clinica.model.Especialidad;
import com.clinica.model.Usuario;
import com.clinica.repository.EspecialidadRepository;
import com.clinica.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class ClinicaApplication {

    public static void main(String[] args) {
        SpringApplication.run(ClinicaApplication.class, args);
    }

    @Bean
    public CommandLineRunner initData(UsuarioRepository usuarioRepository, 
                                      EspecialidadRepository especialidadRepo, 
                                      PasswordEncoder encoder) {
        return args -> {
            // Especialidades iniciales (RF-07)
            if (especialidadRepo.count() == 0) {
                especialidadRepo.save(new Especialidad("Medicina General"));
                especialidadRepo.save(new Especialidad("Pediatría"));
                especialidadRepo.save(new Especialidad("Cardiología"));
                especialidadRepo.save(new Especialidad("Dermatología"));
            }

            if (usuarioRepository.count() == 0) {
                usuarioRepository.save(new Usuario("admin", encoder.encode("admin123"), "ROLE_ADMIN", "Administrador General"));
                usuarioRepository.save(new Usuario("recepcion", encoder.encode("recep123"), "ROLE_RECEPCIONISTA", "Recepcionista Turno"));
                usuarioRepository.save(new Usuario("dr_mendoza", encoder.encode("med123"), "ROLE_MEDICO", "Dr. Carlos Mendoza"));
                usuarioRepository.save(new Usuario("dra_rojas", encoder.encode("med123"), "ROLE_MEDICO", "Dra. Patricia Rojas"));
            }
        };
    }
}