package com.fashionmoda.api_productos_fashion.dto;

public class AuthResponse {

    private String token;
    private String message;

    // Constructor
    public AuthResponse(String token, String message) {
        this.token = token;
        this.message = message;
    }

    // Getters and setters
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

