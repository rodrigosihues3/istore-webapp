package com.istore.appweb.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/catalogo")
public class CatalogoController {

    private final String CARPETA_BASE = "catalogo/";
    private final String VISTA_IPHONES = CARPETA_BASE + "iphones";
    private final String VISTA_RELOJES = CARPETA_BASE + "relojes";
    private final String VISTA_AUDIFONOS = CARPETA_BASE + "audifonos";
    private final String VISTA_ACCESORIOS = CARPETA_BASE + "accesorios";

    @GetMapping("/iphones")
    public String verIphones() {
        return VISTA_IPHONES;
    }

    @GetMapping("/relojes")
    public String verRelojes() {
        return VISTA_RELOJES;
    }

    @GetMapping("/audifonos")
    public String verAudifonos() {
        return VISTA_AUDIFONOS;
    }

    @GetMapping("/accesorios")
    public String verAccesorios() {
        return VISTA_ACCESORIOS;
    }

}
