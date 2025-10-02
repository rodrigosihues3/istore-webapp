package com.istore.appweb.configs.security;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.istore.appweb.entities.Usuarios;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UsuariosDetails implements UserDetails {

  private final Usuarios usuario;

  public UsuariosDetails(Usuarios usuario) {
    this.usuario = usuario;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return List.of(new SimpleGrantedAuthority("ROLE_" + usuario.getRol().getNombre()));
  }

  @Override
  public String getPassword() {
    return usuario.getPassword();
  }

  @Override
  public String getUsername() {
    return usuario.getNombreUsuario(); // lo que usas para loguear
  }

  public String getNombreCompleto() {
    return usuario.getNombres() + " " + usuario.getApellidos();
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }

}
