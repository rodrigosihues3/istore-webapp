package com.istore.appweb.configs.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.istore.appweb.entities.Usuarios;
import com.istore.appweb.services.UsuariosServices;

@Service
public class UsuariosDetailsService implements UserDetailsService {

  @Autowired
  private UsuariosServices servicio;

  @Override
  public UserDetails loadUserByUsername(String nombreUsuarioOrEmail) throws UsernameNotFoundException {
    Usuarios usuario = servicio.getUsuarioByNombreUsuario(nombreUsuarioOrEmail.toUpperCase())
        .or(() -> servicio.getUsuarioByEmail(nombreUsuarioOrEmail.toUpperCase()))
        .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

    return new UsuariosDetails(usuario);
  }

}
