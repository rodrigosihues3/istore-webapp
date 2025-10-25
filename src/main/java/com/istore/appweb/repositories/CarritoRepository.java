package com.istore.appweb.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.istore.appweb.entities.Carrito;
@Repository
public interface CarritoRepository extends JpaRepository<Carrito, Integer>{

}
