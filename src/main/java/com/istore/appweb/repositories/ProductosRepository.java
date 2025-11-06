package com.istore.appweb.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.istore.appweb.entities.Productos;

@Repository
public interface ProductosRepository extends JpaRepository<Productos, Integer> {

}