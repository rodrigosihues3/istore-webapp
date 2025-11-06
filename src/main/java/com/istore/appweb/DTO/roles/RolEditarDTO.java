package com.istore.appweb.DTO.roles;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RolEditarDTO {

  @NotNull(message = "{NotNull.roles.idRol}")
  private Integer idRol;

  @NotNull(message = "{NotNull.roles.nombre}")
  @NotBlank(message = "{NotBlank.roles.nombre}")
  @Pattern(regexp = "^(?=.*\\p{L}.*\\p{L})\\s*[\\p{L}\\s]+\\s*$", message = "{Pattern.roles.nombre}")
  private String nombre;

  @NotNull(message = "{NotNull.roles.nivel}")
  @Min(0)
  @Max(10)
  @PositiveOrZero(message = "{PositiveOrZero.roles.nivel}")
  private Integer nivel;

}
