package com.istore.appweb.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.istore.appweb.entities.TiposComprobantes;

@Repository
public interface TiposComprobantesRepository extends JpaRepository<TiposComprobantes, Integer> {

  boolean existsByNombre(String nombre);

  Optional<TiposComprobantes> findByNombre(String nombre);

}