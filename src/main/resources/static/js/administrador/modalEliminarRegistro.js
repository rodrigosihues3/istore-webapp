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

        case "color": {
          const id = boton.getAttribute("data-id");
          const nombre = boton.getAttribute("data-nombre");

          modal.querySelector("#idSpan").textContent = id;
          modal.querySelector("#nombre").textContent = nombre || "";

          modal.querySelector("#id").value = id || "";

          break;
        }

        case "estadoCompra": {
          const id = boton.getAttribute("data-id");
          const nombre = boton.getAttribute("data-nombre");

          modal.querySelector("#idSpan").textContent = id;
          modal.querySelector("#nombre").textContent = nombre || "";

          modal.querySelector("#id").value = id || "";

          break;
        }

        case "estadoPago": {
          const id = boton.getAttribute("data-id");
          const nombre = boton.getAttribute("data-nombre");

          modal.querySelector("#idSpan").textContent = id;
          modal.querySelector("#nombre").textContent = nombre || "";

          modal.querySelector("#id").value = id || "";

          break;
        }

        case "metodoPago": {
          const id = boton.getAttribute("data-id");
          const nombre = boton.getAttribute("data-nombre");

          modal.querySelector("#idSpan").textContent = id;
          modal.querySelector("#nombre").textContent = nombre || "";

          modal.querySelector("#id").value = id || "";

          break;
        }

        case "tipoComprobante": {
          const id = boton.getAttribute("data-id");
          const nombre = boton.getAttribute("data-nombre");

          modal.querySelector("#idSpan").textContent = id;
          modal.querySelector("#nombre").textContent = nombre || "";

          modal.querySelector("#id").value = id || "";

          break;
        }

        case "pedido": {
          // Obtener todos los datos del botón
          const id = boton.getAttribute("data-id");
          const metodoPago = boton.getAttribute("data-metodo-pago");
          const tipoComprobante = boton.getAttribute("data-tipo-comprobante");
          const estadoCompra = boton.getAttribute("data-estado-compra");
          const total = boton.getAttribute("data-total");
          const usuarioNombreCompleto = boton.getAttribute("data-usuario-nombrecompleto");
          const usuario = boton.getAttribute("data-usuario");
          const usuarioEmail = boton.getAttribute("data-usuario-email");

          // Poblar los <span> de la tarjeta
          // Hidden id
          modal.querySelector("#id").value = id || "";
          // Campos de solo lectura
          modal.querySelector("#idSpan").textContent = id || "";
          modal.querySelector("#usuarioNombreCompleto").textContent = usuarioNombreCompleto || "N/A";
          modal.querySelector("#usuario").textContent = usuario || "N/A";
          modal.querySelector("#usuarioEmail").textContent = usuarioEmail || "N/A";
          modal.querySelector("#estadoCompra").textContent = estadoCompra || "N/A";
          modal.querySelector("#metodoPago").textContent = metodoPago || "N/A";
          modal.querySelector("#tipoComprobante").textContent = tipoComprobante || "N/A";
          // Campo "total" formateado como moneda
          modal.querySelector("#total").textContent = total ? `S/ ${parseFloat(total).toFixed(2)}` : "S/ 0.00";

          break;
        }

        case "pedidoItem": {
          // Obtener datos del botón (de 'detalles-pedido.html')
          const idItem = boton.getAttribute("data-id-item");
          const productoNombre = boton.getAttribute("data-producto-nombre");
          const totalItem = boton.getAttribute("data-total-item");

          // Poblar el modal (que tiene id="modalEliminar")
          modal.querySelector("#idSpan").textContent = idItem || "";
          modal.querySelector("#productoNombre").textContent = productoNombre || "N/A";
          // Formateado del total para que se vea como moneda
          modal.querySelector("#totalItem").textContent = totalItem ? `S/ ${parseFloat(totalItem).toFixed(2)}` : "S/ 0.00";
          // Poblar el <input> oculto
          modal.querySelector("#id").value = idItem || "";

          break;
        }

        case "producto": {
          const id = boton.getAttribute("data-id");
          const sku = boton.getAttribute("data-sku");
          const nombre = boton.getAttribute("data-nombre");
          const precio = boton.getAttribute("data-precio");
          const idCategoria = boton.getAttribute("data-id-categoria");

          modal.querySelector("#idSpan").textContent = id || "";
          modal.querySelector("#sku").textContent = sku || "";
          modal.querySelector("#nombreProducto").textContent = nombre || "";
          modal.querySelector("#precio").textContent = precio || "";
          modal.querySelector("#categoria").textContent = idCategoria || "";
          // Asegurarse de poblar el input oculto usado por el formulario de eliminación
          const hiddenInput = modal.querySelector("#id");
          if (hiddenInput) hiddenInput.value = id || "";
          break;
        }

        case "categoria":
        case "categorias": {
          const id = boton.getAttribute("data-id");
          const nombre = boton.getAttribute("data-nombre");

          modal.querySelector("#idSpan").textContent = id || "";
          modal.querySelector("#nombre").textContent = nombre || "";
          modal.querySelector("#id").value = id || "";

          break;
        }

        default:
          console.warn(`⚠️ No se configuró el modal para la entidad: ${entidad}`);
      }
    });
  });

});
