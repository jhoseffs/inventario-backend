package com.alex.inventario.controller;

import com.alex.inventario.entity.Producto;
import com.alex.inventario.repository.ProductoRepository;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.util.List;

@RestController
@RequestMapping("/api/reportes")
public class ReporteController {

    private final ProductoRepository productoRepository;

    public ReporteController(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    @GetMapping("/productos/pdf")
    public ResponseEntity<byte[]> generarPdfProductos() {

        try {
            List<Producto> productos = productoRepository.findAll();

            Document document = new Document();
            ByteArrayOutputStream out = new ByteArrayOutputStream();

            PdfWriter.getInstance(document, out);
            document.open();

            document.add(new Paragraph("REPORTE DE PRODUCTOS"));
            document.add(new Paragraph(" "));

            PdfPTable table = new PdfPTable(5);

            table.addCell("ID");
            table.addCell("Nombre");
            table.addCell("Código");
            table.addCell("Stock");
            table.addCell("Precio Venta");

            for (Producto p : productos) {
                table.addCell(String.valueOf(p.getId()));
                table.addCell(p.getNombre());
                table.addCell(p.getCodigo());
                table.addCell(String.valueOf(p.getStock()));
                table.addCell(String.valueOf(p.getPrecioVenta()));
            }

            document.add(table);
            document.close();

            return ResponseEntity.ok()
                    .header("Content-Type", "application/pdf")
                    .header("Content-Disposition", "attachment; filename=productos.pdf")
                    .body(out.toByteArray());

        } catch (Exception e) {
            throw new RuntimeException("Error al generar PDF");
        }
    }
}