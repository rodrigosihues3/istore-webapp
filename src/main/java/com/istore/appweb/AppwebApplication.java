package com.istore.appweb;

import java.io.IOException;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.istore.appweb.entities.Roles;
import com.istore.appweb.entities.Usuarios;
import com.istore.appweb.services.RolesServices;
import com.istore.appweb.services.UsuariosServices;

@SpringBootApplication
public class AppwebApplication {

	public static void main(String[] args) {
		String puerto = "3000";

		SpringApplication.run(AppwebApplication.class, args);

		// abrirNavegador("http://localhost:" + puerto);
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
	CommandLineRunner init(RolesServices servicioRoles, UsuariosServices servicioUsuarios) {
		return args -> {
			if (servicioRoles.getRoles().isEmpty()) {
				Roles owner = new Roles();
				owner.setNombre("OWNER");
				owner.setNivel(10);

				servicioRoles.createRol(owner);

				Roles admin = new Roles();
				admin.setNombre("ADMINISTRADOR");
				admin.setNivel(9);

				servicioRoles.createRol(admin);

				Roles empleado = new Roles();
				empleado.setNombre("EMPLEADO");
				empleado.setNivel(1);

				servicioRoles.createRol(empleado);

				Roles cliente = new Roles();
				cliente.setNombre("CLIENTE");
				cliente.setNivel(0);

				servicioRoles.createRol(cliente);
			}

			if (servicioUsuarios.getUsuarios().isEmpty()) {
				Usuarios owner = new Usuarios();
				owner.setNombreUsuario("rodrigosy");
				owner.setPassword("rodrigosy");
				owner.setNombres("Rodrigo");
				owner.setApellidos("Sihues Yanqui");
				owner.setEmail("sihues3@gmail.com");
				owner.setTelefono("961211119");
				owner.setDireccion("IStore - Owner");
				owner.setRol(servicioRoles.getByNombre("OWNER"));

				servicioUsuarios.createUsuario(owner);

				Usuarios admin = new Usuarios();
				admin.setNombreUsuario("admin");
				admin.setPassword("admin");
				admin.setNombres("admin");
				admin.setApellidos("admin admin");
				admin.setEmail("admin@istore.com");
				admin.setTelefono("0000000000");
				admin.setDireccion("IStore - Admin");
				admin.setRol(servicioRoles.getByNombre("ADMINISTRADOR"));

				servicioUsuarios.createUsuario(admin);
			}
		};
	}

}
