package com.example.proyecto.proyecto.controllers;

import com.example.proyecto.proyecto.dto.input.login.LoginDTO;
import com.example.proyecto.proyecto.dto.output.jwt.TokenDTO;
import com.example.proyecto.proyecto.services.LoginService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST})
public class LoginController {

    @Autowired
    private LoginService loginService;

    @PostMapping("/login")
    public ResponseEntity<TokenDTO> login(@RequestBody LoginDTO loginDTO) {
        // Llamar al servicio de login que ahora devuelve un TokenDTO
        TokenDTO tokenResponse = loginService.login(loginDTO);
        
        return ResponseEntity.ok()
                .body(tokenResponse);
    }
    
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpSession session) {
        // Invalidar la sesión
        session.invalidate();
        return ResponseEntity.ok().build();
    }
}