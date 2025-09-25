package com.istore.appweb;

import java.io.IOException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AppwebApplication {

	public static void main(String[] args) {
		String puerto = "3000";

		SpringApplication.run(AppwebApplication.class, args);

		abrirNavegador("http://localhost:" + puerto);
	}

	// Abre el navegador en la URL de la aplicación
	@SuppressWarnings("deprecation")
	private static void abrirNavegador(String url) {
		try {
			Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + url);
		} catch (IOException e) {
			System.out.println("Error al intentar abrir el navegador: " + e.getMessage());
		}
	}
}
