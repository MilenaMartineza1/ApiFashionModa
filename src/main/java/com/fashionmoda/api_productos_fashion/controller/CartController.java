package com.fashionmoda.api_productos_fashion.controller;

import com.fashionmoda.api_productos_fashion.model.CartItem;
import com.fashionmoda.api_productos_fashion.model.Producto;
import com.fashionmoda.api_productos_fashion.model.User;
import com.fashionmoda.api_productos_fashion.repository.CartItemRepository;
import com.fashionmoda.api_productos_fashion.repository.ProductoRepository;
import com.fashionmoda.api_productos_fashion.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    // Obtener el carrito del usuario autenticado
    @GetMapping
    public ResponseEntity<List<CartItem>> getCart(Authentication authentication) {
        String email = authentication.getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con el email: " + email));

        List<CartItem> cartItems = cartItemRepository.findByUser(user); // Llama a findByUser
        return ResponseEntity.ok(cartItems);
    }

    // Agregar un producto al carrito
    @PostMapping
    public ResponseEntity<CartItem> addToCart(
            @RequestParam Long productId,
            @RequestParam Integer quantity,
            Authentication authentication) {

        String email = authentication.getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con el email: " + email));

        Producto producto = productoRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con el ID: " + productId));

        CartItem cartItem = cartItemRepository.findByUserAndProductId(user, productId)
                .orElseGet(() -> {
                    CartItem newItem = new CartItem();
                    newItem.setUser(user);
                    newItem.setProduct(producto);
                    newItem.setQuantity(0);
                    return newItem;
                });

        cartItem.setQuantity(cartItem.getQuantity() + quantity);
        cartItemRepository.save(cartItem);

        return ResponseEntity.ok(cartItem);
    }

    // Reducir cantidad de un producto en el carrito
    @PutMapping("/reduce")
    public ResponseEntity<?> reduceCartItem(
            @RequestParam Long productId,
            Authentication authentication) {

        // Obtener el usuario autenticado
        String email = authentication.getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con el email: " + email));

        // Buscar el item del carrito para este usuario y producto
        CartItem cartItem = cartItemRepository.findByUserAndProductId(user, productId)
                .orElseThrow(() -> new RuntimeException("Item no encontrado en el carrito para el producto ID: " + productId));

        // Reducir la cantidad si es mayor que 1, o eliminarlo si es igual a 1
        if (cartItem.getQuantity() > 1) {
            cartItem.setQuantity(cartItem.getQuantity() - 1); // Reducir cantidad en 1
            cartItemRepository.save(cartItem); // Guardar cambios
            return ResponseEntity.ok(cartItem); // Retornar el item actualizado
        } else {
            cartItemRepository.delete(cartItem); // Si la cantidad es 1, eliminar del carrito
            return ResponseEntity.noContent().build(); // Retornar respuesta vacía (204)
        }
    }


    // Eliminar un producto del carrito
    @DeleteMapping("/{cartItemId}")
    public ResponseEntity<Void> removeFromCart(@PathVariable Long cartItemId, Authentication authentication) {
        String email = authentication.getName(); // Obtener email del usuario autenticado
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con el email: " + email));

        // Buscar el CartItem por ID y userId
        CartItem cartItem = cartItemRepository.findByIdAndUserId(cartItemId, user.getId())
                .orElseThrow(() -> new RuntimeException("Item no encontrado en el carrito o no pertenece a este usuario."));

        cartItemRepository.delete(cartItem);
        return ResponseEntity.noContent().build();
    }


}

