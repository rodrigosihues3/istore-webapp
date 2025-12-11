package com.istore.appweb.models;

import lombok.Data;

@Data
public class CheckoutForm {

  private String direccionEntrega; // La sede elegida
  private String tipoComprobante; // "BOLETA" o "FACTURA"
  private String numeroDocumento; // DNI o RUC
  private String nombreEntidad; // Razón Social o Nombre
  private String metodoPago;
  private String referenciaPago;

}
