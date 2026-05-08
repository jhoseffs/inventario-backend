package com.alex.inventario.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

import java.util.List;

@Configuration
public class SecurityConfig {

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(request -> {

                    org.springframework.web.cors.CorsConfiguration config =
                            new org.springframework.web.cors.CorsConfiguration();

                  config.setAllowedOrigins(List.of(
        "https://inventario-backend-eqe7.onrender.com",
        "http://localhost:5500", // <-- Agregado
        "http://127.0.0.1:5500"  // <-- Agregado
 ));

                    config.setAllowedMethods(List.of(
                            "GET", "POST", "PUT", "DELETE", "OPTIONS"
                    ));

                    config.setAllowedHeaders(List.of("*"));
                    config.setAllowCredentials(true);

                    return config;
                }))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/usuarios/**").permitAll()
                        .anyRequest().permitAll()
                );

        return http.build();
    }
}