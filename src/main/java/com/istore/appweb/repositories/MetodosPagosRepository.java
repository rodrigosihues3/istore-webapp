package com.istore.appweb.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.istore.appweb.entities.MetodosPagos;

@Repository
public interface MetodosPagosRepository extends JpaRepository<MetodosPagos, Integer> {

  boolean existsByNombre(String nombre);

  Optional<MetodosPagos> findByNombre(String nombre);

}