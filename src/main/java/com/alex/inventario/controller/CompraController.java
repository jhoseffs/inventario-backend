package com.alex.inventario.controller;

import com.alex.inventario.entity.Compra;
import com.alex.inventario.entity.Producto;
import com.alex.inventario.repository.CompraRepository;
import com.alex.inventario.repository.ProductoRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.ResponseEntity;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/compras")
//@CrossOrigin(origins = "*")
public class CompraController {

    @Autowired
    private CompraRepository compraRepository;

    @Autowired
    private ProductoRepository productoRepository;

    // ✅ LISTAR TODAS LAS COMPRAS
    @GetMapping
    public List<Compra> listar() {
        return compraRepository.findAll();
    }

    // ✅ REGISTRAR UNA COMPRA
    
   @PostMapping
public ResponseEntity<Map<String, Object>> guardar(@RequestBody Compra compra) {

    // Validar que el producto exista
    Long productoId = compra.getProducto().getId();

    Producto producto = productoRepository.findById(productoId)
            .orElseThrow(() -> new RuntimeException(
                    "Producto no encontrado con ID: " + productoId));

    // Asignar producto válido
    compra.setProducto(producto);

    // Asignar fecha si viene vacía
    if (compra.getFecha() == null) {
        compra.setFecha(LocalDateTime.now());
    }

    // Actualizar stock
    producto.setStock(producto.getStock() + compra.getCantidad());
    productoRepository.save(producto);

    // Guardar compra
    Compra compraGuardada = compraRepository.save(compra);

    // Crear respuesta para frontend
    Map<String, Object> response = new HashMap<>();
    response.put("status", "success");
    response.put("message", "Compra registrada correctamente");
    response.put("data", compraGuardada);

    return ResponseEntity.ok(response);
}

    // ✅ OBTENER COMPRA POR ID
    @GetMapping("/{id}")
    public Compra obtenerPorId(@PathVariable Long id) {
        return compraRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Compra no encontrada"));
    }

    // ✅ ELIMINAR COMPRA
    @DeleteMapping("/{id}")
    public String eliminar(@PathVariable Long id) {
        compraRepository.deleteById(id);
        return "Compra eliminada correctamente";
    }
}