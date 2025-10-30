package com.istore.appweb.services;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.istore.appweb.DTO.colores.ColorAgregarDTO;
import com.istore.appweb.DTO.colores.ColorEditarDTO;
import com.istore.appweb.configs.Utilidades;
import com.istore.appweb.entities.Colores;
import com.istore.appweb.repositories.ColoresRepository;

@Service
public class ColoresServices {
    @Autowired
    private ColoresRepository repositorio;

    public List<Colores> getColores() {
        return repositorio.findAll(Sort.by(Sort.Direction.DESC, "idColor"));

    }

    public Colores getColorById(Integer id) {
        return repositorio.findById(id).get();
    }

    public Colores createColor(Colores color) {
        return repositorio.save(color);
    }

    public Colores updateColor(Colores color) {
        return repositorio.save(color);
    }

    public void deleteById(Integer id) {
        repositorio.deleteById(id);
    }

    public Colores createColor(ColorAgregarDTO colorDto) {
        String nombreColor = Utilidades.normalizarTexto(colorDto.getNombre());

        if (repositorio.existsByNombre(nombreColor)) {
            throw new IllegalArgumentException("nombre:Este nombre de color ya existe, intente con otro.");
        }

        Colores color = new Colores();
        color.setNombre(nombreColor);

        return repositorio.save(color);
    }

    public Colores updateColor(ColorEditarDTO colorDto) {
        Colores colorExistente = getColorById(colorDto.getIdColor());

        String nombreColor = Utilidades.normalizarTexto(colorDto.getNombre());
        if (repositorio.existsByNombre(nombreColor) && !colorExistente.getNombre().equals(nombreColor)) {
            throw new IllegalArgumentException("nombre:El nuevo nombre de color ingesado ya existe, intente con otro.");
        }

        colorExistente.setNombre(nombreColor);
        colorExistente.setFechaCreacion(LocalDateTime.now());

        return repositorio.save(colorExistente);
    }

}
