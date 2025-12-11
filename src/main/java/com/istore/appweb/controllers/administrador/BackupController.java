package com.istore.appweb.controllers.administrador;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.istore.appweb.repositories.*;

@Controller
@RequestMapping("/admin/data")
public class BackupController {

  @Autowired
  private UsuariosRepository usuarioRepo;
  @Autowired
  private ProductosRepository productoRepo;
  @Autowired
  private PedidosRepository pedidoRepo;
  @Autowired
  private CategoriasRepository categoriaRepo;
  @Autowired
  private ColoresRepository repoColores;
  @Autowired
  private EstadosComprasRepository repoEstadosCompras;
  @Autowired
  private EstadosPagosRepository repoEstadosPagos;
  @Autowired
  private MetodosPagosRepository repoMetodosPago;
  @Autowired
  private TiposComprobantesRepository repoTiposComprobante;
  @Autowired
  private RolesRepository repoRoles;
  @Autowired
  private PedidosItemsRepository pedidoItemRepo;

  @GetMapping("/exportar-json")
  public ResponseEntity<byte[]> descargarBackup() {
    try {
      // Recolectar toda la data
      Map<String, Object> data = new HashMap<>();
      data.put("categorias", categoriaRepo.findAll());
      data.put("colores", repoColores.findAll());
      data.put("estados_compras", repoEstadosCompras.findAll());
      data.put("estados_pagos", repoEstadosPagos.findAll());
      data.put("metodos_pagos", repoMetodosPago.findAll());
      data.put("pedidos", pedidoRepo.findAll());
      data.put("pedidos_items", pedidoItemRepo.findAll());
      data.put("productos", productoRepo.findAll());
      data.put("roles", repoRoles.findAll());
      data.put("tipos_comprobantes", repoTiposComprobante.findAll());
      data.put("usuarios", usuarioRepo.findAll());
      data.put("fecha_backup", LocalDateTime.now().toString());

      // Convertir a JSON
      ObjectMapper mapper = new ObjectMapper();
      mapper.registerModule(new JavaTimeModule()); // Para manejar las fechas
      String jsonString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(data);

      // Archivo para descarga
      byte[] isr = jsonString.getBytes();
      String fileName = "iStore_Backup_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"))
          + ".json";

      return ResponseEntity.ok()
          .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
          .contentType(MediaType.APPLICATION_JSON)
          .body(isr);

    } catch (Exception e) {
      e.printStackTrace();
      return ResponseEntity.internalServerError().build();
    }
  }
}