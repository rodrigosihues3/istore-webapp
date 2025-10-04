package com.istore.appweb.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

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
}
