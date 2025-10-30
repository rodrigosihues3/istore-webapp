package com.istore.appweb.services;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.istore.appweb.DTO.tiposComprobantes.TipoComprobanteAgregarDTO;
import com.istore.appweb.DTO.tiposComprobantes.TipoComprobanteEditarDTO;
import com.istore.appweb.configs.Utilidades;
import com.istore.appweb.entities.TiposComprobantes;
import com.istore.appweb.repositories.TiposComprobantesRepository;

@Service
public class TiposComprobantesServices {
    @Autowired
    private TiposComprobantesRepository repo;

    public List<TiposComprobantes> getAll() {
        return repo.findAll(Sort.by(Sort.Direction.DESC, "idTipoComprobante"));
    }

    public TiposComprobantes getById(Integer id) {
        return repo.findById(id).get();
    }

    public TiposComprobantes create(TiposComprobantes tipoComprobante) {
        return repo.save(tipoComprobante);
    }

    public TiposComprobantes update(TiposComprobantes tipoComprobante) {
        return repo.save(tipoComprobante);
    }

    public void deleteById(Integer id) {
        repo.deleteById(id);
    }

    public TiposComprobantes create(TipoComprobanteAgregarDTO tipoComprobanteDto) {
        String nombre = Utilidades.normalizarTexto(tipoComprobanteDto.getNombre());

        if (repo.existsByNombre(nombre)) {
            throw new IllegalArgumentException("nombre:Este nombre ya existe, intente con otro.");
        }

        TiposComprobantes tipoComprobante = new TiposComprobantes();
        tipoComprobante.setNombre(nombre);

        return repo.save(tipoComprobante);
    }

    public TiposComprobantes update(TipoComprobanteEditarDTO tipoComprobanteDto) {
        TiposComprobantes tipoComprobanteExistente = getById(tipoComprobanteDto.getIdTipoComprobante());

        String nombre = Utilidades.normalizarTexto(tipoComprobanteDto.getNombre());
        if (repo.existsByNombre(nombre) && !tipoComprobanteExistente.getNombre().equals(nombre)) {
            throw new IllegalArgumentException("nombre:El nuevo nombre ingesado ya existe, intente con otro.");
        }

        tipoComprobanteExistente.setNombre(nombre);
        tipoComprobanteExistente.setFechaCreacion(LocalDateTime.now());

        return repo.save(tipoComprobanteExistente);
    }

}
