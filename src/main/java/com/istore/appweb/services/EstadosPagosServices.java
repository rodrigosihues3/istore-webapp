package com.istore.appweb.services;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.istore.appweb.DTO.estadosPagos.EstadoPagoAgregarDTO;
import com.istore.appweb.DTO.estadosPagos.EstadoPagoEditarDTO;
import com.istore.appweb.configs.Utilidades;
import com.istore.appweb.entities.EstadosPagos;
import com.istore.appweb.repositories.EstadosPagosRepository;

@Service
public class EstadosPagosServices {
    @Autowired
    private EstadosPagosRepository repo;

    public List<EstadosPagos> getAll() {
        return repo.findAll(Sort.by(Sort.Direction.DESC, "idEstadoPago"));
    }

    public EstadosPagos getById(Integer id) {
        return repo.findById(id).get();
    }

    public EstadosPagos create(EstadosPagos estadoPago) {
        return repo.save(estadoPago);
    }

    public EstadosPagos update(EstadosPagos estadoPago) {
        return repo.save(estadoPago);
    }

    public void deleteById(Integer id) {
        repo.deleteById(id);
    }

    public EstadosPagos create(EstadoPagoAgregarDTO estadoPagoDto) {
        String nombre = Utilidades.normalizarTexto(estadoPagoDto.getNombre());

        if (repo.existsByNombre(nombre)) {
            throw new IllegalArgumentException("nombre:Este nombre de estado de pago ya existe, intente con otro.");
        }

        EstadosPagos estadoPago = new EstadosPagos();
        estadoPago.setNombre(nombre);

        return repo.save(estadoPago);
    }

    public EstadosPagos update(EstadoPagoEditarDTO estadoPagoDto) {
        EstadosPagos estadoPagoExistente = getById(estadoPagoDto.getIdEstadoPago());

        String nombre = Utilidades.normalizarTexto(estadoPagoDto.getNombre());
        if (repo.existsByNombre(nombre) && !estadoPagoExistente.getNombre().equals(nombre)) {
            throw new IllegalArgumentException("nombre:El nuevo nombre ingesado ya existe, intente con otro.");
        }

        estadoPagoExistente.setNombre(nombre);
        estadoPagoExistente.setFechaCreacion(LocalDateTime.now());

        return repo.save(estadoPagoExistente);
    }

}
