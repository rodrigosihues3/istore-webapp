package com.istore.appweb.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.istore.appweb.entities.Usuarios;

@Repository
public interface UsuariosRepository extends JpaRepository<Usuarios, Integer> {

  Optional<Usuarios> findByNombreUsuario(String nombreUsuario);

  Optional<Usuarios> findByEmail(String email);

}
