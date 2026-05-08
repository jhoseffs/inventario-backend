package com.alex.inventario.repository;
import com.alex.inventario.entity.Compra;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface CompraRepository extends JpaRepository<Compra, Long> {
}