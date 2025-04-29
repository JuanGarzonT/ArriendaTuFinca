package com.example.proyecto.proyecto.controllers;

import com.example.proyecto.proyecto.dto.input.login.LoginDTO;
import com.example.proyecto.proyecto.dto.output.usuario.UsuarioLoginResponse;
import com.example.proyecto.proyecto.entities.Usuario.TipoUsuario;
import com.example.proyecto.proyecto.services.LoginService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST})
public class LoginController {

    @Autowired
    private LoginService loginService;

    @PostMapping("/login")
    public ResponseEntity<UsuarioLoginResponse> login(@RequestBody LoginDTO loginDTO, HttpSession session) {
        // Llamar al servicio de login
        UsuarioLoginResponse usuario = loginService.login(loginDTO);
        
        // Guardar información del usuario en la sesión
        session.setAttribute("usuarioId", usuario.getId());
        session.setAttribute("usuarioNombre", usuario.getNombre());
        session.setAttribute("usuarioEmail", usuario.getEmail());
        session.setAttribute("usuarioTipo", usuario.getTipoUsuario());
        
        // También puedes enviar esta información en headers
        return ResponseEntity.ok()
                .header("X-Usuario-Id", usuario.getId().toString())
                .header("X-Usuario-Nombre", usuario.getNombre())
                .header("X-Usuario-Tipo", usuario.getTipoUsuario().name())
                .body(usuario);
    }
    
    @GetMapping("/sesion")
    public ResponseEntity<UsuarioLoginResponse> getSessionInfo(HttpSession session) {
        // Verificar si hay una sesión activa
        Long usuarioId = (Long) session.getAttribute("usuarioId");
        if (usuarioId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        
        // Crear objeto de respuesta con datos de la sesión
        UsuarioLoginResponse usuario = new UsuarioLoginResponse(
                usuarioId,
                (String) session.getAttribute("usuarioNombre"),
                (String) session.getAttribute("usuarioEmail"),
                (TipoUsuario) session.getAttribute("usuarioTipo")
        );
        
        return ResponseEntity.ok(usuario);
    }
    
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpSession session) {
        // Invalidar la sesión
        session.invalidate();
        return ResponseEntity.ok().build();
    }
}