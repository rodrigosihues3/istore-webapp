package com.istore.appweb.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.istore.appweb.entities.Roles;

@Repository
public interface RolesRepository extends JpaRepository<Roles, Integer> {

}
