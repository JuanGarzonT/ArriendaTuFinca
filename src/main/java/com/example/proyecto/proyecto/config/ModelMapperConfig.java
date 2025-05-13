package com.example.proyecto.proyecto.config;

import com.example.proyecto.proyecto.dto.output.calificacion.CalificacionDTO;
import com.example.proyecto.proyecto.dto.output.propiedad.PropiedadDetailDTO;
import com.example.proyecto.proyecto.dto.output.solicitud.SolicitudSimpleDTO;
import com.example.proyecto.proyecto.entities.Calificacion;
import com.example.proyecto.proyecto.entities.Propiedad;
import com.example.proyecto.proyecto.entities.Solicitud;

import org.hibernate.collection.spi.PersistentCollection;
import org.modelmapper.AbstractConverter;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class ModelMapperConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        
        // Configuración básica
        modelMapper.getConfiguration()
            .setMatchingStrategy(MatchingStrategies.STRICT)
            .setSkipNullEnabled(true)
            .setFieldMatchingEnabled(true)
            .setFieldAccessLevel(org.modelmapper.config.Configuration.AccessLevel.PRIVATE);
        
        return modelMapper;
    }
}