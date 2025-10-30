package com.istore.appweb.configs;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class Utilidades {

  public static String normalizarTexto(String texto) {
    if (texto == null) {
      return null;
    }

    return texto
        .trim() // quita espacios al inicio y al final
        .replaceAll("\\s{2,}", " ") // reemplaza múltiples espacios por uno solo
        .toUpperCase(); // convierte a mayúsculas
  }

}
