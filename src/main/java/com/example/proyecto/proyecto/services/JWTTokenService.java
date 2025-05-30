package com.example.proyecto.proyecto.services;

import java.security.Key;
import java.util.ArrayList;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.example.proyecto.proyecto.dto.output.usuario.UsuarioDTO;
import com.example.proyecto.proyecto.entities.Usuario;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.util.List;

@Service
public class JWTTokenService {
    
    @Value("${jwt.secret:CLAVE_SECRETA_POR_DEFECTO}")
    private String secret;
    
    @Value("${jwt.expiration:3600000}") // 1 hora por defecto
    private long jwtExpiration;
    
    private final Key jwtKey = Keys.secretKeyFor(SignatureAlgorithm.HS512);
    
    private final ObjectMapper objectMapper = new ObjectMapper();
    
    // Genera token para el usuario
    public String generarToken(UsuarioDTO usuarioDTO) {
        String username;
        try {
            username = objectMapper.writeValueAsString(usuarioDTO);
        } catch (Exception e) {
            e.printStackTrace();
            username = usuarioDTO.getEmail();
        }
        
        System.out.println("Username en JWT: " + username);
        
        // Crear una colección de authorities
        List<String> roles = new ArrayList<>();
        roles.add("ROLE_" + usuarioDTO.getTipoUsuario().name());
        
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpiration);
        
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .claim("roles", roles)
                .signWith(jwtKey, SignatureAlgorithm.HS512)
                .compact();
    }

    public String generarToken(Usuario usuario) {
        UsuarioDTO usuarioDTO = new UsuarioDTO();
        usuarioDTO.setId(usuario.getId());
        usuarioDTO.setNombre(usuario.getNombre());
        usuarioDTO.setEmail(usuario.getEmail());
        usuarioDTO.setTelefono(usuario.getTelefono());
        usuarioDTO.setTipoUsuario(usuario.getTipoUsuario());
        
        return generarToken(usuarioDTO);
    }
    
    public String getUsername(String jwtToken) {
        return decodificarToken(jwtToken).getSubject();
    }
    
    public UsuarioDTO getUsuarioFromToken(String jwtToken) {
        try {
            String userJson = getUsername(jwtToken);
            return objectMapper.readValue(userJson, UsuarioDTO.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public Date getFechaExpiracion(String jwtToken) {
        return decodificarToken(jwtToken).getExpiration();
    }

    public Claims decodificarToken(String jwtToken) {
        return Jwts.parserBuilder()
                .setSigningKey(jwtKey)
                .build()
                .parseClaimsJws(jwtToken)
                .getBody();
    }
    

    public boolean validarToken(String jwtToken) {
        try {
            Jwts.parserBuilder()
                .setSigningKey(jwtKey)
                .build()
                .parseClaimsJws(jwtToken);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}