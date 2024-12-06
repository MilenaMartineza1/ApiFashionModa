package com.fashionmoda.api_productos_fashion.repository;

import com.fashionmoda.api_productos_fashion.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);  // Consulta por correo
    boolean existsByEmail(String email);  // Verifica si el correo ya existe
}


