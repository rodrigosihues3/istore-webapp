document.addEventListener("DOMContentLoaded", () => {
  const botonesEditar = document.querySelectorAll("[data-bs-toggle='modal'][data-entidad]");

  botonesEditar.forEach(boton => {
    boton.addEventListener("click", () => {
      const entidad = boton.getAttribute("data-entidad");
      const modal = document.getElementById("modalEliminar");

      if (!modal) return; // Si no hay modal, retorna para evitar error

      // Rellenar datos según la entidad
      switch (entidad) {
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

        default:
          console.warn(`⚠️ No se configuró el modal para la entidad: ${entidad}`);
      }
    });
  });

});
