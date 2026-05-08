package com.alex.inventario.repository;
import java.util.List;


import com.alex.inventario.entity.Producto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductoRepository extends JpaRepository<Producto, Long> {

    List<Producto> findByNombreContainingIgnoreCaseOrCodigoContainingIgnoreCase(
            String nombre,
            String codigo
    );
}