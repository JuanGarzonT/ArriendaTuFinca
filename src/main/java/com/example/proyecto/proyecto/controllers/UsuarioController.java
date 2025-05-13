package com.example.proyecto.proyecto.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.proyecto.proyecto.dto.input.usuario.UsuarioCreateDTO;
import com.example.proyecto.proyecto.dto.input.usuario.UsuarioUpdateDTO;
import com.example.proyecto.proyecto.dto.output.usuario.UsuarioDTO;
import com.example.proyecto.proyecto.dto.output.usuario.UsuarioDetailDTO;
import com.example.proyecto.proyecto.entities.Usuario.TipoUsuario;
import com.example.proyecto.proyecto.services.UsuarioService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/usuarios")
@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST})
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/listar")
    public List<UsuarioDTO> getAllUsuarios() {
        return usuarioService.getAllUsuarios();
    }

    @GetMapping("/buscar/{id}")
    public UsuarioDTO getUsuarioById(@PathVariable Long id) {
        return usuarioService.getUsuarioById(id);
    }

    @GetMapping("/buscarPorEmail/{email}")
    public UsuarioDTO getUsuarioByEmail(@PathVariable String email) {
        return usuarioService.getUsuarioByEmail(email);
    }

    @GetMapping("/detalle/{id}")
    public UsuarioDetailDTO getUsuarioDetalleById(@PathVariable Long id) {
        return usuarioService.getUsuarioDetailById(id);
    }

    @GetMapping("/buscarPorTipo/{tipoUsuario}")
    public List<UsuarioDTO> getUsuariosByTipo(@PathVariable TipoUsuario tipoUsuario) {
        return usuarioService.getUsuariosByTipo(tipoUsuario);
    }

    @PostMapping("/registrar")
    public UsuarioDTO createUsuario(@RequestBody UsuarioCreateDTO createUsuarioDTO) {
        return usuarioService.createUsuario(createUsuarioDTO);
    }

    @PutMapping("/actualizar/{id}")
    public UsuarioDTO updateUsuario(
            @PathVariable Long id,
            @RequestBody UsuarioUpdateDTO updateUsuarioDTO) {
        return usuarioService.updateUsuario(id, updateUsuarioDTO);
    }

    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<Void> softDeleteUsuario(@PathVariable Long id) {
        usuarioService.softDeleteUsuario(id);
        return ResponseEntity.ok().build();
    }
    
    @PutMapping("/reactivar/{id}")
    public UsuarioDTO reactivarUsuario(@PathVariable Long id) {
        return usuarioService.reactivarUsuario(id);
    }
}