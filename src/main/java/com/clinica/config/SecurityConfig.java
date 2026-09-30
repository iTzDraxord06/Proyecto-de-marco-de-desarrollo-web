package com.clinica.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/css/**", "/js/**", "/imagenes/**").permitAll()
                        .requestMatchers("/login", "/h2-console/**").permitAll()
                        
                        .requestMatchers(HttpMethod.GET, "/pacientes").hasAnyAuthority("ROLE_ADMIN", "ROLE_RECEPCIONISTA", "ROLE_MEDICO")
                        
                        .requestMatchers("/pacientes/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_RECEPCIONISTA")
                        
                        .requestMatchers("/citas/registrar", "/citas/guardar", "/citas/cancelar/**")
                        .hasAnyAuthority("ROLE_ADMIN", "ROLE_RECEPCIONISTA")
                        .requestMatchers("/citas/**").authenticated()
                        
                        .requestMatchers("/dashboard", "/medicos/**", "/servicios/**").authenticated()
                        .requestMatchers("/reportes/**", "/seguimiento/**").authenticated()
                        .anyRequest().authenticated())
                .formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/dashboard", true)
                        .failureUrl("/login?error=true")
                        .permitAll())
                .logout(logout -> logout
                        .logoutSuccessUrl("/login?logout=true")
                        .permitAll());

        http.csrf(csrf -> csrf.ignoringRequestMatchers("/h2-console/**"));
        http.headers(headers -> headers.frameOptions(frame -> frame.disable()));

        return http.build();
    }
}