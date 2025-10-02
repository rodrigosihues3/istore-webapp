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
            .requestMatchers("/mi-cuenta").authenticated()
            .requestMatchers("/admin/**").hasAnyRole("ADMINISTRADOR", "OWNER")
            .anyRequest().permitAll())
        .formLogin(form -> form
            .loginPage("/iniciar-sesion")
            .usernameParameter("username") // nuestro campo único que acepta user o email
            .passwordParameter("password")
            .permitAll())
        .logout(logout -> logout
            .logoutUrl("/cerrar-sesion")
            .logoutSuccessUrl("/")
            .permitAll());

    return http.build();
  }

}
