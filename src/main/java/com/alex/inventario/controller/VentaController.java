package com.alex.inventario.controller;

import com.alex.inventario.entity.Venta;
import com.alex.inventario.entity.Producto;
import com.alex.inventario.repository.VentaRepository;
import com.alex.inventario.repository.ProductoRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.ResponseEntity;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api/ventas")
//@CrossOrigin(origins = "*")
public class VentaController {

    @Autowired
    private VentaRepository ventaRepository;

    @Autowired
    private ProductoRepository productoRepository;

    // ✅ LISTAR TODAS LAS VENTAS
    @GetMapping
    public List<Venta> listar() {
        return ventaRepository.findAll();
    }

    // ✅ REGISTRAR UNA VENTA
    @PostMapping
public ResponseEntity<Map<String, Object>> guardar(@RequestBody Venta venta) {

    // Validar producto
    Long productoId = venta.getProducto().getId();

    Producto producto = productoRepository.findById(productoId)
            .orElseThrow(() -> new RuntimeException(
                    "Producto no encontrado con ID: " + productoId));

    // Validar stock disponible
    if (producto.getStock() < venta.getCantidad()) {
        throw new RuntimeException("Stock insuficiente");
    }

    // Descontar stock
    producto.setStock(producto.getStock() - venta.getCantidad());
    productoRepository.save(producto);

    // Fecha automática si viene vacía
    if (venta.getFecha() == null) {
        venta.setFecha(LocalDateTime.now());
    }

    // Asignar producto validado
    venta.setProducto(producto);

    // Guardar venta
    Venta ventaGuardada = ventaRepository.save(venta);

    // Respuesta personalizada
    Map<String, Object> response = new HashMap<>();
    response.put("status", "success");
    response.put("message", "Venta registrada correctamente");
    response.put("data", ventaGuardada);

    return ResponseEntity.ok(response);
}
    // ✅ OBTENER POR ID
    @GetMapping("/{id}")
    public Venta obtener(@PathVariable Long id) {
        return ventaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Venta no encontrada"));
    }

    // ✅ ELIMINAR
    @DeleteMapping("/{id}")
    public String eliminar(@PathVariable Long id) {
        ventaRepository.deleteById(id);
        return "Venta eliminada correctamente";
    }
}