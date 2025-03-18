package com.example.proyecto.proyecto.services;

import com.example.proyecto.proyecto.dto.input.usuario.UsuarioCreateDTO;
import com.example.proyecto.proyecto.dto.input.usuario.UsuarioUpdateDTO;
import com.example.proyecto.proyecto.dto.output.propiedad.PropiedadSimpleDTO;
import com.example.proyecto.proyecto.dto.output.solicitud.SolicitudSimpleDTO;
import com.example.proyecto.proyecto.dto.output.usuario.UsuarioDTO;
import com.example.proyecto.proyecto.dto.output.usuario.UsuarioDetailDTO;
import com.example.proyecto.proyecto.entities.Propiedad;
import com.example.proyecto.proyecto.entities.Solicitud;
import com.example.proyecto.proyecto.entities.Usuario;
import com.example.proyecto.proyecto.entities.Usuario.TipoUsuario;
import com.example.proyecto.proyecto.repository.PropiedadRepository;
import com.example.proyecto.proyecto.repository.SolicitudRepository;
import com.example.proyecto.proyecto.repository.UsuarioRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Autowired
    private PropiedadRepository propiedadRepository;
    
    @Autowired
    private SolicitudRepository solicitudRepository;
    
    @Autowired
    private ModelMapper modelMapper;

    @Transactional(readOnly = true)
    public List<UsuarioDTO> getAllUsuarios() {
        return StreamSupport.stream(usuarioRepository.findAll().spliterator(), false)
                .map(usuario -> modelMapper.map(usuario, UsuarioDTO.class))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public UsuarioDTO getUsuarioById(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + id));
        return modelMapper.map(usuario, UsuarioDTO.class);
    }

    @Transactional(readOnly = true)
    public UsuarioDTO getUsuarioByEmail(String email) {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con email: " + email));
        return modelMapper.map(usuario, UsuarioDTO.class);
    }

    @Transactional(readOnly = true)
    public UsuarioDetailDTO getUsuarioDetailById(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + id));
        
        List<Propiedad> propiedades = propiedadRepository.findByArrendatario(usuario);
        List<Solicitud> solicitudesRealizadas = solicitudRepository.findByArrendador(usuario);
        List<Solicitud> solicitudesRecibidas = solicitudRepository.findByArrendatario(usuario);
        
        UsuarioDetailDTO detailDTO = modelMapper.map(usuario, UsuarioDetailDTO.class);
        
        detailDTO.setPropiedades(propiedades.stream()
                .map(propiedad -> modelMapper.map(propiedad, PropiedadSimpleDTO.class))
                .collect(Collectors.toList()));
                
        detailDTO.setSolicitudesRealizadas(solicitudesRealizadas.stream()
                .map(solicitud -> modelMapper.map(solicitud, SolicitudSimpleDTO.class))
                .collect(Collectors.toList()));
                
        detailDTO.setSolicitudesRecibidas(solicitudesRecibidas.stream()
                .map(solicitud -> modelMapper.map(solicitud, SolicitudSimpleDTO.class))
                .collect(Collectors.toList()));
                
        return detailDTO;
    }

    @Transactional
    public UsuarioDTO createUsuario(UsuarioCreateDTO createUsuarioDTO) {
        if (usuarioRepository.existsByEmail(createUsuarioDTO.getEmail())) {
            throw new RuntimeException("Ya existe un usuario con el email: " + createUsuarioDTO.getEmail());
        }
        
        Usuario usuario = modelMapper.map(createUsuarioDTO, Usuario.class);
        // La contraseña se manejará más adelante
        
        Usuario savedUsuario = usuarioRepository.save(usuario);
        return modelMapper.map(savedUsuario, UsuarioDTO.class);
    }

    @Transactional
    public UsuarioDTO updateUsuario(Long id, UsuarioUpdateDTO updateUsuarioDTO) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + id));
        
        if (updateUsuarioDTO.getNombre() != null) {
            usuario.setNombre(updateUsuarioDTO.getNombre());
        }
        
        if (updateUsuarioDTO.getTelefono() != null) {
            usuario.setTelefono(updateUsuarioDTO.getTelefono());
        }
        
        // La actualización de contraseña se manejará más adelante
        
        Usuario updatedUsuario = usuarioRepository.save(usuario);
        return modelMapper.map(updatedUsuario, UsuarioDTO.class);
    }

    @Transactional
    public void deleteUsuario(Long id) {
        if (!usuarioRepository.existsById(id)) {
            throw new RuntimeException("Usuario no encontrado con ID: " + id);
        }
        usuarioRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<UsuarioDTO> getUsuariosByTipo(TipoUsuario tipoUsuario) {
        return usuarioRepository.findByTipoUsuario(tipoUsuario).stream()
                .map(usuario -> modelMapper.map(usuario, UsuarioDTO.class))
                .collect(Collectors.toList());
    }
}