package com.fashionmoda.api_productos_fashion.repository;

import com.fashionmoda.api_productos_fashion.model.CartItem;
import com.fashionmoda.api_productos_fashion.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    // Encuentra todos los items en el carrito para un usuario
    List<CartItem> findByUser(User user);

    // Encuentra todos los items en el carrito usando el ID del usuario
    List<CartItem> findByUserId(Long userId);

    // Encuentra un item específico en el carrito usando el usuario y el producto
    Optional<CartItem> findByUserAndProductId(User user, Long productId);

    // Encuentra un item específico en el carrito usando el ID del usuario y el producto
    Optional<CartItem> findByUserIdAndProductId(Long userId, Long productId);
    Optional<CartItem> findByIdAndUserId(Long userId, Long productId);
}





