package com.istore.appweb.configs.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http.csrf(csrf -> csrf.disable())
        .authorizeHttpRequests(auth -> auth
            // 1. Recursos estáticos (siempre públicos)
            .requestMatchers("/css/**", "/js/**", "/img/**", "/webjars/**").permitAll()

            // 2. Zona Cliente (Cualquier usuario logueado)
            .requestMatchers("/mi-cuenta/**").authenticated()

            // 3. Zona Admin (Solo Jefes)
            .requestMatchers("/admin/**").hasAnyRole("ADMINISTRADOR", "OWNER")

            // 4. Zona Empleado (Jefes también pueden entrar aquí)
            .requestMatchers("/empleado/**").hasAnyRole("EMPLEADO", "ADMINISTRADOR", "OWNER")

            // 5. Todo lo demás (Catálogo, Home, Login) es público
            .anyRequest().permitAll())
        .formLogin(form -> form
            .loginPage("/iniciar-sesion")
            .usernameParameter("username") // nuestro campo único que acepta user o email
            .passwordParameter("password")
            // Lógica de redirección inteligente según el rol
            .successHandler((request, response, authentication) -> {
              var roles = authentication.getAuthorities();
              String redirectUrl = "/"; // Por defecto al home

              if (roles.stream().anyMatch(r -> r.getAuthority().equals("ROLE_ADMINISTRADOR") ||
                  r.getAuthority().equals("ROLE_OWNER"))) {
                redirectUrl = "/admin";
              } else if (roles.stream().anyMatch(r -> r.getAuthority().equals("ROLE_EMPLEADO"))) {
                redirectUrl = "/empleado";
              }

              response.sendRedirect(redirectUrl);
            })
            .permitAll())
        .logout(logout -> logout
            .logoutUrl("/cerrar-sesion")
            .logoutSuccessUrl("/")
            .permitAll());

    return http.build();
  }

}
