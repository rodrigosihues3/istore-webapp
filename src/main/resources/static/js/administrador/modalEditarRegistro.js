document.addEventListener("DOMContentLoaded", () => {
  const botonesEditar = document.querySelectorAll("[data-bs-toggle='modal'][data-entidad]");

  botonesEditar.forEach(boton => {
    boton.addEventListener("click", () => {
      const entidad = boton.getAttribute("data-entidad");
      const modal = document.getElementById("modalEditar");

      if (!modal) return; // Si no hay modal, retorna para evitar error

      // Rellenar datos según la entidad
      switch (entidad) {
        case "pedido": {
          // Obtener datos del botón (en 'pedidos.html')
          // Hidden ID
          const id = boton.getAttribute("data-id");
          // Datos extra (solo lectura)
          const usuarioNombre = boton.getAttribute("data-usuario-nombrecompleto");
          const usuarioEmail = boton.getAttribute("data-usuario-email");
          const usuarioNombreUsuario = boton.getAttribute("data-usuario");
          // Datos de campos a editar
          const idMetodoPago = boton.getAttribute("data-id-metodo-pago");
          const idTipoComprobante = boton.getAttribute("data-id-tipo-comprobante");
          const idEstadoCompra = boton.getAttribute("data-id-estado-compra");

          // Datos para incializar el formulario del modal
          // Hidden ID
          modal.querySelector("input[name='idPedido']").value = id || "";
          // Campos de solo lectura
          modal.querySelector("#editPedidoUsuarioNombreUsuario").value = usuarioNombreUsuario || "N/A";
          modal.querySelector("#editPedidoUsuarioEmail").value = usuarioEmail || "N/A";
          modal.querySelector("#editPedidoUsuarioNombre").value = usuarioNombre || "N/A";
          // Selects (los campos del DTO a editar)
          modal.querySelector("select[name='idMetodoPago']").value = idMetodoPago || "";
          modal.querySelector("select[name='idTipoComprobante']").value = idTipoComprobante || "";
          modal.querySelector("select[name='idEstadoCompra']").value = idEstadoCompra || "";

          break;
        }

        case "pedidoItem": {
          // Obtener datos del botón (de 'detalles-pedido.html')
          const idItem = boton.getAttribute("data-id-item");
          const idProducto = boton.getAttribute("data-id-producto");
          const cantidad = boton.getAttribute("data-cantidad");
          const precio = boton.getAttribute("data-precio");

          // Poblar el formulario (que tiene id="modalEditar")
          modal.querySelector("input[name='idPedidoItem']").value = idItem || "";
          modal.querySelector("select[name='idProducto']").value = idProducto || "";
          modal.querySelector("input[name='cantidad']").value = cantidad || "";
          modal.querySelector("input[name='precio']").value = precio || "";

          break;
        }

        default:
          console.warn(`⚠️ No se configuró el modal para la entidad: ${entidad}`);
      }
    });
  });

});
