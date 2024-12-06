package com.fashionmoda.api_productos_fashion.service;

import com.fashionmoda.api_productos_fashion.model.Producto;
import com.fashionmoda.api_productos_fashion.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductoService {

    @Autowired
    private ProductoRepository productoRepository;

    // Obtener todos los productos
    public List<Producto> obtenerProductos() {
        return productoRepository.findAll();
    }

    // Agregar un nuevo producto
    public Producto agregarProducto(Producto producto) {
        return productoRepository.save(producto);
    }

    // Actualizar un producto existente
    public Producto actualizarProducto(Long id, Producto producto) {
        if (productoRepository.existsById(id)) {
            producto.setId(id);
            return productoRepository.save(producto);
        } else {
            throw new IllegalArgumentException("Producto no encontrado");
        }
    }

    // Eliminar un producto
    public void eliminarProducto(Long id) {
        if (productoRepository.existsById(id)) {
            productoRepository.deleteById(id);
        } else {
            throw new IllegalArgumentException("Producto no encontrado");
        }
    }
}
