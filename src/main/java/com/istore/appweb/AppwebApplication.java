package com.istore.appweb;

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
		// String puerto = "3000";

		SpringApplication.run(AppwebApplication.class, args);
		// abrirNavegador("http://localhost:" + puerto);
	}

	// Abre el navegador en la URL de la aplicación
	// private static void abrirNavegador(String url) {
	// try {
	// // Forma moderna y multiplataforma
	// if (java.awt.Desktop.isDesktopSupported()) {
	// java.awt.Desktop.getDesktop().browse(new java.net.URI(url));
	// } else {
	// // Respaldo para sistemas muy antiguos o sin UI
	// String[] cmd = { "rundll32", "url.dll,FileProtocolHandler", url };
	// Runtime.getRuntime().exec(cmd);
	// }
	// } catch (Exception e) {
	// System.out.println("Error al intentar abrir el navegador: " +
	// e.getMessage());
	// }
	// }

	@Bean
	CommandLineRunner init(RolesServices servicioRoles, UsuariosServices servicioUsuarios) {
		return args -> {
			Roles ownerRol = buscarOCrearRol(servicioRoles, "OWNER", 10);
			Roles adminRol = buscarOCrearRol(servicioRoles, "ADMINISTRADOR", 9);
			Roles empleadoRol = buscarOCrearRol(servicioRoles, "EMPLEADO", 1);
			Roles clienteRol = buscarOCrearRol(servicioRoles, "CLIENTE", 0);

			System.out.println("Roles creados: ");
			System.out.println(ownerRol.toString());
			System.out.println(adminRol.toString());
			System.out.println(empleadoRol.toString());
			System.out.println(clienteRol.toString());

			if (servicioUsuarios.getUsuarios().isEmpty()) {
				Usuarios user_owner = new Usuarios();
				user_owner.setNombreUsuario("rodrigosy");
				user_owner.setPassword("rodrigosy");
				user_owner.setNombres("Rodrigo");
				user_owner.setApellidos("Sihues Yanqui");
				user_owner.setEmail("sihues3@gmail.com");
				user_owner.setTelefono("961211119");
				user_owner.setDireccion("IStore - Owner");
				user_owner.setRol(ownerRol);

				servicioUsuarios.createUsuario(user_owner);

				Usuarios user_admin = new Usuarios();
				user_admin.setNombreUsuario("admin");
				user_admin.setPassword("admin");
				user_admin.setNombres("admin");
				user_admin.setApellidos("admin admin");
				user_admin.setEmail("admin@istore.com");
				user_admin.setTelefono("0000000000");
				user_admin.setDireccion("IStore - Admin");
				user_admin.setRol(adminRol);

				servicioUsuarios.createUsuario(user_admin);
			}
		};
	}

	private Roles buscarOCrearRol(RolesServices servicio, String nombre, Integer nivel) {
		Roles rol = servicio.getByNombre(nombre);
		if (rol == null) {
			rol = new Roles();
			rol.setNombre(nombre);
			rol.setNivel(nivel);
			return servicio.createRol(rol); // Asegúrate que tu servicio retorne el objeto guardado
		}
		return rol;
	}

}
