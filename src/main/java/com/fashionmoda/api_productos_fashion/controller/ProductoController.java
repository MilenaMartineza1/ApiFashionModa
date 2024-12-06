package com.fashionmoda.api_productos_fashion.controller;

import com.fashionmoda.api_productos_fashion.model.Producto;
import com.fashionmoda.api_productos_fashion.service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/productos")
public class ProductoController {

    @Autowired
    private ProductoService productoService;

    // Obtener todos los productos
    @GetMapping
    public ResponseEntity<List<Producto>> obtenerProductos() {
        List<Producto> productos = productoService.obtenerProductos();
        return ResponseEntity.ok(productos);
    }

    // Agregar un nuevo producto
    @PostMapping
    public ResponseEntity<Producto> agregarProducto(@RequestBody Producto producto) {
        Producto productoCreado = productoService.agregarProducto(producto);
        return ResponseEntity.status(HttpStatus.CREATED).body(productoCreado);
    }

    // Actualizar un producto existente
    @PutMapping("/{id}")
    public ResponseEntity<Producto> actualizarProducto(@PathVariable Long id, @RequestBody Producto producto) {
        Producto productoActualizado = productoService.actualizarProducto(id, producto);
        return ResponseEntity.ok(productoActualizado);
    }

    // Eliminar un producto
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarProducto(@PathVariable Long id) {
        productoService.eliminarProducto(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    // Exportar productos (por ejemplo, devolver todos los productos en formato JSON)
    @GetMapping("/exportar")
    public ResponseEntity<List<Producto>> exportarProductos() {
        List<Producto> productos = productoService.obtenerProductos(); // Aquí podrías implementar exportación más compleja si es necesario
        return ResponseEntity.ok(productos);
    }
}
