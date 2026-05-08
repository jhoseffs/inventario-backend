package com.alex.inventario.repository;
import com.alex.inventario.entity.Venta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


public interface VentaRepository extends JpaRepository<Venta, Long> {
}