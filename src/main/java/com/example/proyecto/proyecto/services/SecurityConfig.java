package com.example.proyecto.proyecto.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import com.example.proyecto.proyecto.filter.JWTAuthorizationFilter;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JWTAuthorizationFilter jwtAuthorizationFilter;
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }
    
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // Configurar CORS
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            // Deshabilitar CSRF para APIs REST
            .csrf(csrf -> csrf.disable())
            // Establecer política de sesión sin estado (stateless)
            .sessionManagement(session -> 
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            // Configurar reglas de autorización para endpoints
            .authorizeHttpRequests(authorize -> authorize
                // Permitir acceso a endpoints públicos
                .requestMatchers(permitAllRequestMatcher()).permitAll()
                // Solicitar autenticación para todos los demás endpoints
                .anyRequest().authenticated())
            // Agregar el filtro JWT antes del filtro de autenticación por usuario y contraseña
            .addFilterBefore(jwtAuthorizationFilter, UsernamePasswordAuthenticationFilter.class);
        
        return http.build();
    }
    
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:4200"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
        configuration.setAllowedHeaders(Arrays.asList("Origin", "Content-Type", "Accept", "Authorization", "X-Requested-With"));
        configuration.setAllowCredentials(true);
        configuration.setExposedHeaders(Arrays.asList("X-Usuario-Id", "X-Usuario-Nombre", "X-Usuario-Tipo"));
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
    
    private RequestMatcher permitAllRequestMatcher() {
        return new OrRequestMatcher(
            // Endpoints de autenticación
            new AntPathRequestMatcher("/api/auth/**"),
            // Endpoints públicos específicos
            new AntPathRequestMatcher("/api/usuarios/registrar", HttpMethod.POST.name()),
            new AntPathRequestMatcher("/api/calificaciones/promedioPorPropiedad/**", HttpMethod.GET.name()),
            new AntPathRequestMatcher("/api/propiedades/listar", HttpMethod.GET.name()),
            new AntPathRequestMatcher("/api/propiedades/buscar/**", HttpMethod.GET.name()),
            new AntPathRequestMatcher("/api/propiedades/buscarPorArrendatario/**", HttpMethod.GET.name()),
            new AntPathRequestMatcher("/api/pagos/registrar",HttpMethod.POST.name())
        );
    }
}