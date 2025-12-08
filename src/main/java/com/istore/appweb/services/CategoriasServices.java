package com.istore.appweb.services;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.istore.appweb.DTO.categorias.CategoriaAgregarDTO;
import com.istore.appweb.DTO.categorias.CategoriaEditarDTO;
import com.istore.appweb.configs.Utilidades;
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

    public Categorias createCategoria(CategoriaAgregarDTO categoriaDto) {
        String nombreCategoria = Utilidades.normalizarTexto(categoriaDto.getNombre());

        if (repositorio.existsByNombre(nombreCategoria)) {
            throw new IllegalArgumentException("nombre:Este nombre de categoría ya existe, intente con otro.");
        }

        Categorias categoria = new Categorias();
        categoria.setNombre(nombreCategoria);

        return repositorio.save(categoria);
    }

    public Categorias updateCategoria(CategoriaEditarDTO categoriaDto) {
        Categorias categoriaExistente = getCategoriaById(categoriaDto.getIdCategoria());

        String nombreCategoria = Utilidades.normalizarTexto(categoriaDto.getNombre());
        if (repositorio.existsByNombre(nombreCategoria) && !categoriaExistente.getNombre().equals(nombreCategoria)) {
            throw new IllegalArgumentException(
                    "nombre:El nuevo nombre de categoría ingresado ya existe, intente con otro.");
        }

        categoriaExistente.setNombre(nombreCategoria);
        categoriaExistente.setFechaCreacion(LocalDateTime.now());

        return repositorio.save(categoriaExistente);
    }

}
