package com.alex.inventario.controller;

import com.alex.inventario.entity.Producto;
import com.alex.inventario.entity.Categoria;
import com.alex.inventario.repository.CategoriaRepository;
import com.alex.inventario.repository.ProductoRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/productos")
//@CrossOrigin(origins = "*")
@CrossOrigin
public class ProductoController {

    private final ProductoRepository repo;
    private final CategoriaRepository categoriaRepo;

    public ProductoController(ProductoRepository repo, CategoriaRepository categoriaRepo) {
        this.repo = repo;
        this.categoriaRepo = categoriaRepo;
    }

    @GetMapping
    public List<Producto> listar() {
        return repo.findAll();
    }

    @PostMapping
    public Producto guardar(@RequestBody Producto p) {

        // 🔍 VALIDACIÓN
        if (p.getCategoria() == null || p.getCategoria().getId() == null) {
            throw new RuntimeException("La categoría es obligatoria");
        }

        // 🔥 BUSCAR CATEGORÍA REAL EN BD
        Categoria categoria = categoriaRepo.findById(p.getCategoria().getId())
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));

        // 🔥 ASIGNAR CATEGORÍA REAL
        p.setCategoria(categoria);

        return repo.save(p);
    }
    @GetMapping("/{id}")
    public Producto obtenerPorId(@PathVariable Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
    }

    @GetMapping("/buscar")
    public List<Producto> buscar(@RequestParam(required = false) String query) {

        if (query == null || query.trim().isEmpty()) {
            return repo.findAll();
        }

        return repo.findByNombreContainingIgnoreCaseOrCodigoContainingIgnoreCase(query, query);
    }
}