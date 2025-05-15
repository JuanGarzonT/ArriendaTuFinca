package com.example.proyecto.proyecto.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.proyecto.proyecto.dto.output.usuario.UsuarioDTO;
import com.example.proyecto.proyecto.entities.Usuario;
import com.example.proyecto.proyecto.repository.UsuarioRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;
    
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public UserDetails loadUserByUsername(String usernameJson) throws UsernameNotFoundException {
        try {
            // El username puede ser un JSON o un email simple
            UsuarioDTO usuarioDTO;
            try {
                usuarioDTO = objectMapper.readValue(usernameJson, UsuarioDTO.class);
                
                // Crear authorities para el usuario basado en su tipoUsuario
                List<GrantedAuthority> authorities = new ArrayList<>();
                authorities.add(new SimpleGrantedAuthority("ROLE_" + usuarioDTO.getTipoUsuario().name()));
                
                return new org.springframework.security.core.userdetails.User(
                        usuarioDTO.getEmail(),
                        "placeholder-password", // Este password no se usa en la verificación JWT
                        authorities
                );
                
            } catch (Exception e) {
                // Si no es un JSON válido, asumimos que es un email
                Usuario usuario = usuarioRepository.findByEmailAndActivoTrue(usernameJson)
                        .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con email: " + usernameJson));
                
                List<GrantedAuthority> authorities = new ArrayList<>();
                authorities.add(new SimpleGrantedAuthority("ROLE_" + usuario.getTipoUsuario().name()));
                
                return new org.springframework.security.core.userdetails.User(
                        usuario.getEmail(),
                        usuario.getPassword(),
                        usuario.getActivo(),
                        true,
                        true,
                        true,
                        authorities
                );
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            throw new UsernameNotFoundException("Error al cargar el usuario: " + e.getMessage());
        }
    }
}