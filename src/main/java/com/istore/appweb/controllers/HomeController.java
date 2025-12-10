package com.istore.appweb.controllers;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.istore.appweb.entities.Productos;
import com.istore.appweb.services.ProductosServices;

@Controller
public class HomeController {

    @Autowired
    private ProductosServices productosService;

    @GetMapping
    public String home(Model model) {
        List<Productos> destacados = productosService.getProductos().stream()
                .limit(8)
                .collect(Collectors.toList());

        model.addAttribute("productosDestacados", destacados);

        return "index";
    }

    // --- NUEVO ENDPOINT AJAX ---
    @GetMapping("/catalogo/filtrar")
    public String filtrarCatalogo(
            @RequestParam(required = false) String busqueda,
            @RequestParam(required = false) String categoria,
            Model model) {

        List<Productos> resultados = productosService.getProductos();

        // 1. Filtrar por Categoría (si viene el parámetro)
        if (categoria != null && !categoria.isEmpty()) {
            resultados = resultados.stream()
                    .filter(p -> p.getCategoria().getNombre().equalsIgnoreCase(categoria) ||
                    // Truco: Para que "iphones" coincida con "iPhone" (plural/singular)
                            (categoria.equalsIgnoreCase("iphones")
                                    && p.getCategoria().getNombre().equalsIgnoreCase("iPhone"))
                            ||
                            (categoria.equalsIgnoreCase("audifonos")
                                    && p.getCategoria().getNombre().equalsIgnoreCase("AirPods")))
                    .collect(Collectors.toList());
            model.addAttribute("tituloCatalogo",
                    "Categoría: " + categoria.substring(0, 1).toUpperCase() + categoria.substring(1));
        }

        // 2. Filtrar por Búsqueda (si viene el parámetro)
        if (busqueda != null && !busqueda.isEmpty()) {
            String q = busqueda.toLowerCase();
            resultados = resultados.stream()
                    .filter(p -> p.getNombre().toLowerCase().contains(q) ||
                            p.getDescripcion().toLowerCase().contains(q) ||
                            p.getCategoria().getNombre().toLowerCase().contains(q))
                    .collect(Collectors.toList());
            model.addAttribute("tituloCatalogo", "Resultados para: \"" + busqueda + "\"");
        }

        if (busqueda == null && categoria == null) {
            model.addAttribute("tituloCatalogo", "Catálogo Completo");
        }

        model.addAttribute("productos", resultados);

        // Retornamos el fragmento que creaste en el paso anterior
        return "catalogo :: listaProductos";
    }

    @GetMapping("/tiendas")
    public String tiendas() {
        return "tiendas";
    }

    @GetMapping("/nosotros")
    public String nosotros() {
        return "nosotros";
    }

}
