package com.alex.inventario.controller;

import com.alex.inventario.entity.Categoria;
import com.alex.inventario.repository.CategoriaRepository;
import org.springframework.web.bind.annotation.*;

import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;


import java.util.List;

@RestController
@RequestMapping("/api/categorias")
//@CrossOrigin(origins = "*")
public class CategoriaController {

    private final CategoriaRepository repo;

    public CategoriaController(CategoriaRepository repo) {
        this.repo = repo;
    }

    // LISTAR TODAS
    @GetMapping
    public List<Categoria> listar() {
        return repo.findAll();
    }

    // GUARDAR
    @PostMapping
    public Categoria guardar(@RequestBody Categoria categoria) {
        return repo.save(categoria);
    }

    // OBTENER POR ID
    @GetMapping("/{id}")
    public Categoria obtener(@PathVariable Long id) {
        return repo.findById(id).orElse(null);
    }

    // EDITAR
    @PutMapping("/{id}")
    public Categoria actualizar(@PathVariable Long id, @RequestBody Categoria categoriaActualizada) {
        Categoria categoria = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));

        categoria.setNombre(categoriaActualizada.getNombre());

        return repo.save(categoria);
    }

    // ELIMINAR
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminar(@PathVariable Long id) {

        Categoria categoria = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));

        if (!categoria.getProductos().isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body("No se puede eliminar porque tiene productos asociados");
        }

        repo.delete(categoria);

        return ResponseEntity.ok("Categoría eliminada correctamente");
    }
}