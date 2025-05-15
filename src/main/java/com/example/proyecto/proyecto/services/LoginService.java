package com.example.proyecto.proyecto.services;

import com.example.proyecto.proyecto.dto.input.login.LoginDTO;
import com.example.proyecto.proyecto.dto.output.jwt.TokenDTO;
import com.example.proyecto.proyecto.dto.output.usuario.UsuarioDTO;
import com.example.proyecto.proyecto.dto.output.usuario.UsuarioLoginResponse;
import com.example.proyecto.proyecto.repository.UsuarioRepository;
import com.example.proyecto.proyecto.entities.Usuario;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class LoginService {

    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Autowired
    private JWTTokenService jwtTokenService;
    
    @Autowired
    private ModelMapper modelMapper;

    public TokenDTO login(LoginDTO loginDTO) {
        // Buscar usuario por email
        Usuario usuario = usuarioRepository.findByEmailAndActivoTrue(loginDTO.getEmail())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, 
                        "Usuario no encontrado con el correo: " + loginDTO.getEmail()));

        // Verificar contraseña
        if (!usuario.getPassword().equals(loginDTO.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Contraseña incorrecta");
        }

        // Mapear a DTO
        UsuarioDTO usuarioDTO = modelMapper.map(usuario, UsuarioDTO.class);
        
        // Generar token JWT
        String token = jwtTokenService.generarToken(usuarioDTO);
        
        // Crear y devolver TokenDTO
        return new TokenDTO(token, usuarioDTO);
    }
    
    // Mantener método existente para compatibilidad hacia atrás
    public UsuarioLoginResponse loginLegacy(LoginDTO loginDTO) {
        // Buscar usuario por email
        Usuario usuario = usuarioRepository.findByEmailAndActivoTrue(loginDTO.getEmail())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, 
                        "Usuario no encontrado con el correo: " + loginDTO.getEmail()));

        // Verificar contraseña
        if (!usuario.getPassword().equals(loginDTO.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Contraseña incorrecta");
        }

        // Crear DTO de respuesta
        return new UsuarioLoginResponse(
                usuario.getId(),
                usuario.getNombre(),
                usuario.getEmail(),
                usuario.getTipoUsuario()
        );
    }
}