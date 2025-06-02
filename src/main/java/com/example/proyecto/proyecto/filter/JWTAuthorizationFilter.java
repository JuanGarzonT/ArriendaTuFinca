package com.example.proyecto.proyecto.filter;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.proyecto.proyecto.services.JWTTokenService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.example.proyecto.proyecto.services.CustomUserDetailsService;

@Component
public class JWTAuthorizationFilter extends OncePerRequestFilter {
    
    public static final String HEADER = "Authorization";
    public static final String PREFIX = "Bearer ";
    
    @Autowired
    private CustomUserDetailsService userDetailsService;
    
    @Autowired
    private JWTTokenService jwtTokenService;
    
    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request, 
            @NonNull HttpServletResponse response, 
            @NonNull FilterChain chain) throws ServletException, IOException {

        System.out.println("JWTAuthorizationFilter: Procesando solicitud a " + request.getRequestURI());

        // Rutas públicas que deben ignorarse en el filtro
        String path = request.getRequestURI();
        if (path.equals("/api/usuarios/registrar") || path.startsWith("/api/auth")) {
            chain.doFilter(request, response);
            return;
        }

        try {
            if (existeJWTToken(request)) {
                System.out.println("Token JWT encontrado en la solicitud");
                Claims claims = validarToken(request);
                if (claims.get("roles") != null) {
                    String username = getUsername(request);
                    System.out.println("Usuario autenticado: " + username);
                    UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                    UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                            username, userDetails.getPassword(), userDetails.getAuthorities());
                    auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(auth);
                    System.out.println("Autenticación exitosa para el usuario: " + username);
                } else {
                    System.out.println("No se encontraron roles en el token");
                    SecurityContextHolder.clearContext();
                }
            } else {
                System.out.println("No se encontró token JWT en la solicitud");
                SecurityContextHolder.clearContext();
            }
            chain.doFilter(request, response);

        } catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException | SignatureException e) {
            System.out.println("Error al validar el token: " + e.getMessage());
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.getWriter().write("Token inválido: " + e.getMessage());
        }
    }

    
    private Claims validarToken(HttpServletRequest request) {
        String jwtToken = request.getHeader(HEADER).replace(PREFIX, "");
        return jwtTokenService.decodificarToken(jwtToken);
    }
    
    private String getUsername(HttpServletRequest request) {
        String jwtToken = request.getHeader(HEADER).replace(PREFIX, "");
        return jwtTokenService.getUsername(jwtToken);
    }
    
    private boolean existeJWTToken(HttpServletRequest request) {
        String authenticationHeader = request.getHeader(HEADER);
        boolean existe = authenticationHeader != null && authenticationHeader.startsWith(PREFIX);
        if (existe) {
            System.out.println("Header de autorización encontrado: " + authenticationHeader);
        } else {
            System.out.println("No se encontró header de autorización válido");
        }
        return existe;
    }
}