package com.fashionmoda.api_productos_fashion.controller;

import com.fashionmoda.api_productos_fashion.dto.AuthRequest;
import com.fashionmoda.api_productos_fashion.dto.AuthResponse;
import com.fashionmoda.api_productos_fashion.dto.RegisterRequest;
import com.fashionmoda.api_productos_fashion.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest registerRequest) {
        try {
            // Registra un usuario regular
            String message = userService.registerUser(registerRequest);
            return ResponseEntity.ok(message);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al registrar el usuario: " + e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest authRequest) {
        try {
            AuthResponse authResponse = userService.authenticateUser(authRequest);
            return ResponseEntity.ok(authResponse);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new AuthResponse(null, "Error al autenticar el usuario: " + e.getMessage()));
        }
    }
}


