package com.fashionmoda.api_productos_fashion.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtTokenProvider {

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expirationTime}")
    private long expirationTime;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public String generateToken(String username, String role) {
        try {
            Map<String, Object> payload = new HashMap<>();
            payload.put("sub", username);
            payload.put("role", role.startsWith("ROLE_") ? role : "ROLE_" + role);
            payload.put("iat", System.currentTimeMillis() / 1000);
            payload.put("exp", (System.currentTimeMillis() + expirationTime) / 1000);

            String unsignedToken = encodeHeader() + "." + encodePayload(payload);
            return unsignedToken + "." + signToken(unsignedToken);
        } catch (Exception e) {
            throw new RuntimeException("Error al generar el token", e);
        }
    }

    private String encodeHeader() throws Exception {
        Map<String, String> header = Map.of("alg", "HS256", "typ", "JWT");
        return Base64.getUrlEncoder().withoutPadding().encodeToString(objectMapper.writeValueAsString(header).getBytes(StandardCharsets.UTF_8));
    }

    private String encodePayload(Map<String, Object> payload) throws Exception {
        return Base64.getUrlEncoder().withoutPadding().encodeToString(objectMapper.writeValueAsString(payload).getBytes(StandardCharsets.UTF_8));
    }

    private String signToken(String unsignedToken) {
        try {
            Mac mac = Mac.getInstance(SignatureAlgorithm.HS256.getJcaName());
            mac.init(new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), SignatureAlgorithm.HS256.getJcaName()));
            return Base64.getUrlEncoder().withoutPadding().encodeToString(mac.doFinal(unsignedToken.getBytes(StandardCharsets.UTF_8)));
        } catch (Exception e) {
            throw new RuntimeException("Error al firmar el token", e);
        }
    }

    public boolean validateToken(String token) {
        try {
            String[] parts = token.split("\\.");
            String unsignedToken = parts[0] + "." + parts[1];
            return signToken(unsignedToken).equals(parts[2]);
        } catch (Exception e) {
            return false;
        }
    }

    public String getUsernameFromToken(String token) {
        try {
            String[] parts = token.split("\\.");
            String payload = new String(Base64.getUrlDecoder().decode(parts[1]), StandardCharsets.UTF_8);
            return objectMapper.readTree(payload).get("sub").asText();
        } catch (Exception e) {
            throw new RuntimeException("Error al leer el token", e);
        }
    }

    public String getRoleFromToken(String token) {
        try {
            String[] parts = token.split("\\.");
            String payload = new String(Base64.getUrlDecoder().decode(parts[1]), StandardCharsets.UTF_8);
            return objectMapper.readTree(payload).get("role").asText();
        } catch (Exception e) {
            throw new RuntimeException("Error al leer el token", e);
        }
    }
}
