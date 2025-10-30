package com.istore.appweb.services;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.istore.appweb.DTO.estadosCompras.EstadoCompraAgregarDTO;
import com.istore.appweb.DTO.estadosCompras.EstadoCompraEditarDTO;
import com.istore.appweb.configs.Utilidades;
import com.istore.appweb.entities.EstadosCompras;
import com.istore.appweb.repositories.EstadosComprasRepository;

@Service
public class EstadosComprasServices {
    @Autowired
    private EstadosComprasRepository repo;

    public List<EstadosCompras> getAll() {
        return repo.findAll(Sort.by(Sort.Direction.DESC, "idEstadoCompra"));
    }

    public EstadosCompras getById(Integer id) {
        return repo.findById(id).get();
    }

    public EstadosCompras create(EstadosCompras estadoCompra) {
        return repo.save(estadoCompra);
    }

    public EstadosCompras update(EstadosCompras estadoCompra) {
        return repo.save(estadoCompra);
    }

    public void deleteById(Integer id) {
        repo.deleteById(id);
    }

    public EstadosCompras create(EstadoCompraAgregarDTO estadoCompraDto) {
        String nombre = Utilidades.normalizarTexto(estadoCompraDto.getNombre());

        if (repo.existsByNombre(nombre)) {
            throw new IllegalArgumentException("nombre:Este nombre de estado de compra ya existe, intente con otro.");
        }

        EstadosCompras estadoCompra = new EstadosCompras();
        estadoCompra.setNombre(nombre);

        return repo.save(estadoCompra);
    }

    public EstadosCompras update(EstadoCompraEditarDTO estadoCompraDto) {
        EstadosCompras estadoCompraExistente = getById(estadoCompraDto.getIdEstadoCompra());

        String nombre = Utilidades.normalizarTexto(estadoCompraDto.getNombre());
        if (repo.existsByNombre(nombre) && !estadoCompraExistente.getNombre().equals(nombre)) {
            throw new IllegalArgumentException("nombre:El nuevo nombre ingesado ya existe, intente con otro.");
        }

        estadoCompraExistente.setNombre(nombre);
        estadoCompraExistente.setFechaCreacion(LocalDateTime.now());

        return repo.save(estadoCompraExistente);
    }

}
