// archivo: modalEditarEntidad.js
document.addEventListener("DOMContentLoaded", () => {
  const botonesEditar = document.querySelectorAll("[data-bs-toggle='modal'][data-entidad]");

  botonesEditar.forEach(boton => {
    boton.addEventListener("click", () => {
      const entidad = boton.getAttribute("data-entidad");
      const modal = document.getElementById("modalEliminar");

      if (!modal) return; // Si no hay modal, retorna para evitar error

      // Rellenar datos según la entidad
      switch (entidad) {
        case "usuario": {
          const id = boton.getAttribute("data-id");
          const nombreCompleto = boton.getAttribute("data-nombre-completo");
          const dni = boton.getAttribute("data-dni");
          const email = boton.getAttribute("data-email");
          const nombreUsuario = boton.getAttribute("data-nombre-usuario");
          const rol = boton.getAttribute("data-rol");

          modal.querySelector("#idSpan").textContent = id || "";
          modal.querySelector("#nombreCompleto").textContent = nombreCompleto || "";
          modal.querySelector("#dni").textContent = dni || "";
          modal.querySelector("#email").textContent = email || "";
          modal.querySelector("#nombreUsuario").textContent = nombreUsuario || "";
          modal.querySelector("#rol").textContent = rol || "";

          modal.querySelector("#id").value = id || "";

          break;
        }

        case "rol": {
          const id = boton.getAttribute("data-id");
          const nombre = boton.getAttribute("data-nombre");
          const nivel = boton.getAttribute("data-nivel");

          modal.querySelector("#idSpan").textContent = id;
          modal.querySelector("#nombre").textContent = nombre || "";
          modal.querySelector("#nivel").textContent = nivel || "";

          modal.querySelector("#id").value = id || "";

          break;
        }

        default:
          console.warn(`⚠️ No se configuró el modal para la entidad: ${entidad}`);
      }
    });
  });

});
