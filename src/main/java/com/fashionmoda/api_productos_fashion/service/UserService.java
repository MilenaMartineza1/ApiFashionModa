package com.fashionmoda.api_productos_fashion.service;

import com.fashionmoda.api_productos_fashion.dto.AuthRequest;
import com.fashionmoda.api_productos_fashion.dto.AuthResponse;
import com.fashionmoda.api_productos_fashion.dto.RegisterRequest;
import com.fashionmoda.api_productos_fashion.model.User;
import com.fashionmoda.api_productos_fashion.repository.UserRepository;
import com.fashionmoda.api_productos_fashion.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    private static final String ADMIN_EMAIL = "admin18@example.com";

    public String registerUser(RegisterRequest registerRequest) {
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new IllegalArgumentException("El correo ya está en uso.");
        }

        if (!registerRequest.isTermsAccepted()) {
            throw new IllegalArgumentException("Debe aceptar los términos y condiciones.");
        }

        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));

        if (ADMIN_EMAIL.equals(registerRequest.getEmail())) {
            user.setRole("ROLE_ADMIN");
        } else {
            user.setRole("ROLE_USER");
        }

        userRepository.save(user);
        return "Usuario registrado exitosamente.";
    }

    public AuthResponse authenticateUser(AuthRequest authRequest) {
        Optional<User> userOptional = userRepository.findByEmail(authRequest.getEmail());

        if (userOptional.isEmpty() ||
                !passwordEncoder.matches(authRequest.getPassword(), userOptional.get().getPassword())) {
            throw new IllegalArgumentException("Credenciales inválidas.");
        }

        User user = userOptional.get();
        String token = jwtTokenProvider.generateToken(user.getEmail(), user.getRole());

        return new AuthResponse(token, "Autenticación exitosa.");
    }
}
