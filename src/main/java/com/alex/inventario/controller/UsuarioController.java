package com.alex.inventario.controller;

import com.alex.inventario.entity.Usuario;
import com.alex.inventario.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/usuarios")
@CrossOrigin("*")
public class UsuarioController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Usuario loginRequest) {

        Optional<Usuario> usuario = usuarioRepository.findByEmail(loginRequest.getEmail());

        if (usuario.isPresent() &&
            passwordEncoder.matches(
                loginRequest.getPassword(),
                usuario.get().getPassword()
            )) {
            return ResponseEntity.ok(usuario.get());
        }

        return ResponseEntity.status(401).body("Credenciales incorrectas");
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Usuario usuario) {

        // verificar si ya existe ese usuario
        if (usuarioRepository.existsByNombreAndEmail(
                usuario.getNombre(),
                usuario.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body("Error: El usuario ya está registrado");
        }

        // cifrar contraseña
        usuario.setPassword(
                passwordEncoder.encode(usuario.getPassword())
        );

        usuarioRepository.save(usuario);

        return ResponseEntity
                .ok("Usuario registrado correctamente");
    }

    @GetMapping
    public ResponseEntity<?> listarUsuarios() {
        return ResponseEntity.ok(usuarioRepository.findAll());
    }
}