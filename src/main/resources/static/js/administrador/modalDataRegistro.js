// archivo: modalEditarEntidad.js
document.addEventListener("DOMContentLoaded", () => {
  const botonesEditar = document.querySelectorAll("[data-bs-toggle='modal'][data-entidad]");

  botonesEditar.forEach(boton => {
    boton.addEventListener("click", () => {
      const entidad = boton.getAttribute("data-entidad");
      const modal = document.getElementById("modalEditar");

      if (!modal) return; // Si no hay modal, retorna para evitar error

      // Rellenar datos según la entidad
      switch (entidad) {
        case "usuario": {
          const id = boton.getAttribute("data-id");
          const nombres = boton.getAttribute("data-nombres");
          const apellidos = boton.getAttribute("data-apellidos");
          const dni = boton.getAttribute("data-dni");
          const email = boton.getAttribute("data-email");
          const telefono = boton.getAttribute("data-telefono");
          const direccion = boton.getAttribute("data-direccion");
          const nombreUsuario = boton.getAttribute("data-nombre-usuario");
          const password = boton.getAttribute("data-password");
          const rol = boton.getAttribute("data-rol");

          modal.querySelector("input[name='idUsuario']").value = id || "";
          modal.querySelector("input[name='nombres']").value = nombres || "";
          modal.querySelector("input[name='apellidos']").value = apellidos || "";
          modal.querySelector("input[name='dni']").value = dni || "";
          modal.querySelector("input[name='email']").value = email || "";
          modal.querySelector("input[name='telefono']").value = telefono || "";
          modal.querySelector("input[name='direccion']").value = direccion || "";
          modal.querySelector("input[name='nombreUsuario']").value = nombreUsuario || "";
          modal.querySelector("input[name='password']").value = password || "";
          modal.querySelector("select[name='rol']").value = rol || "CLIENTE";

          break;
        }

        case "rol": {
          const titulo = boton.getAttribute("data-titulo");
          const precio = boton.getAttribute("data-precio");

          modal.querySelector("input[name='titulo']").value = titulo || "";
          modal.querySelector("input[name='precio']").value = precio || "";
          
          break;
        }

        default:
          console.warn(`⚠️ No se configuró el modal para la entidad: ${entidad}`);
      }
    });
  });

});
