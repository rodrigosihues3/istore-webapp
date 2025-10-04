package com.istore.appweb.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.istore.appweb.entities.Categorias;
import com.istore.appweb.repositories.CategoriasRepository;

@Service
public class CategoriasServices {
    @Autowired
    private CategoriasRepository repositorio;
    public List<Categorias> getCategorias() {
        return repositorio.findAll(Sort.by(Sort.Direction.DESC, "idCategoria"));

    }

    public Categorias getCategoriaById(Integer id) {
        return repositorio.findById(id).get();
    }

    public Categorias createCategoria(Categorias categoria) {
        return repositorio.save(categoria);
    }

    public Categorias updateCategoria(Categorias categoria) {
        return repositorio.save(categoria);
    }

    public void deleteById(Integer id) {
        repositorio.deleteById(id);
    }
}
