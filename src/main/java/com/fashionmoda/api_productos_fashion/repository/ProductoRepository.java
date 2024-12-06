package com.fashionmoda.api_productos_fashion.repository;

import com.fashionmoda.api_productos_fashion.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductoRepository extends JpaRepository<Producto, Long> {
}

