package com.istore.appweb.services;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.istore.appweb.DTO.metodosPagos.MetodoPagoAgregarDTO;
import com.istore.appweb.DTO.metodosPagos.MetodoPagoEditarDTO;
import com.istore.appweb.configs.Utilidades;
import com.istore.appweb.entities.MetodosPagos;
import com.istore.appweb.repositories.MetodosPagosRepository;

@Service
public class MetodosPagosServices {
    @Autowired
    private MetodosPagosRepository repo;

    public List<MetodosPagos> getAll() {
        return repo.findAll(Sort.by(Sort.Direction.DESC, "idMetodoPago"));
    }

    public MetodosPagos getById(Integer id) {
        return repo.findById(id).get();
    }

    public MetodosPagos create(MetodosPagos metodoPago) {
        return repo.save(metodoPago);
    }

    public MetodosPagos update(MetodosPagos metodoPago) {
        return repo.save(metodoPago);
    }

    public void deleteById(Integer id) {
        repo.deleteById(id);
    }

    public MetodosPagos create(MetodoPagoAgregarDTO metodoPagoDto) {
        String nombre = Utilidades.normalizarTexto(metodoPagoDto.getNombre());

        if (repo.existsByNombre(nombre)) {
            throw new IllegalArgumentException("nombre:Este nombre ya existe, intente con otro.");
        }

        MetodosPagos metodoPago = new MetodosPagos();
        metodoPago.setNombre(nombre);

        return repo.save(metodoPago);
    }

    public MetodosPagos update(MetodoPagoEditarDTO metodoPagoDto) {
        MetodosPagos metodoPagoExistente = getById(metodoPagoDto.getIdMetodoPago());

        String nombre = Utilidades.normalizarTexto(metodoPagoDto.getNombre());
        if (repo.existsByNombre(nombre) && !metodoPagoExistente.getNombre().equals(nombre)) {
            throw new IllegalArgumentException("nombre:El nuevo nombre ingesado ya existe, intente con otro.");
        }

        metodoPagoExistente.setNombre(nombre);
        metodoPagoExistente.setFechaCreacion(LocalDateTime.now());

        return repo.save(metodoPagoExistente);
    }

}
