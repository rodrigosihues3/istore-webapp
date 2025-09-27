package com.istore.appweb;

import java.io.IOException;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.istore.appweb.entities.Usuarios;
import com.istore.appweb.services.UsuariosServices;

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

	@Bean
	@SuppressWarnings("unused")
	CommandLineRunner init(UsuariosServices servicioUsuarios) {
		return args -> {
			if (servicioUsuarios.getUsuarios().isEmpty()) {
				Usuarios admin = new Usuarios();
				admin.setNombreUsuario("admin");
				admin.setPassword("admin");
				admin.setNombres("admin");
				admin.setApellidos("admin admin");
				admin.setEmail("admin@istore.com");
				admin.setTelefono("0000000000");
				admin.setDireccion("IStore - Admin");
				admin.setRol("ADMINISTRADOR");

				servicioUsuarios.createUsuario(admin);
			}
		};
	}

}
