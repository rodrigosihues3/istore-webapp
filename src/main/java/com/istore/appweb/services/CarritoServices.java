package com.istore.appweb.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.istore.appweb.entities.Carrito;
import com.istore.appweb.repositories.CarritoRepository;

@Service
public class CarritoServices {
   @Autowired
    private CarritoRepository repositorio;
    
    public List<Carrito> getAll() {
        return repositorio.findAll(Sort.by(Sort.Direction.DESC, "idCarrito"));
    }

    public Carrito getById(Integer id) {
        return repositorio.findById(id).get();
    }

    public Carrito create(Carrito carrito) {
        return repositorio.save(carrito);
    }

    public Carrito update(Carrito carrito) {
        return repositorio.save(carrito);
    }

    public void deleteById(Integer id) {
        repositorio.deleteById(id);
    }
    
}
