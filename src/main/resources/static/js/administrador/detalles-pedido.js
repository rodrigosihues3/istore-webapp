document.addEventListener("DOMContentLoaded", () => {
  //  Función reutilizable para calcular y mostrar el total del item.
  //  El backend lo recalculará, esto es solo una previsualización.
  function actualizarTotalPreview(cantidadInput, precioInput, previewInput) {
    const cantidad = parseFloat(cantidadInput.value) || 0;
    const precio = parseFloat(precioInput.value) || 0;
    const total = cantidad * precio;

    // Formatear como moneda (S/.)
    previewInput.value = `S/ ${total.toFixed(2)}`;
  }

  // --- LÓGICA PARA EL MODAL "AGREGAR" ---
  const modalAgregarItem = document.getElementById("modalAgregar");
  if (modalAgregarItem) {
    const selectProducto = modalAgregarItem.querySelector("#itemAddProductoSelect");
    const inputCantidad = modalAgregarItem.querySelector("#itemAddCantidadInput");
    const inputPrecio = modalAgregarItem.querySelector("#itemAddPrecioInput");
    const previewTotal = modalAgregarItem.querySelector("#itemAddTotalPreview");

    // Escuchar el SELECT de Producto
    selectProducto.addEventListener("change", () => {
      const opcionSeleccionada = selectProducto.options[selectProducto.selectedIndex];
      const precio = opcionSeleccionada.getAttribute("data-precio");

      if (precio) {
        inputPrecio.value = parseFloat(precio).toFixed(2);
      } else {
        inputPrecio.value = "";
      }

      // Recalcular el total
      actualizarTotalPreview(inputCantidad, inputPrecio, previewTotal);
    });

    // Escuchar el INPUT de Cantidad
    inputCantidad.addEventListener("input", () => {
      actualizarTotalPreview(inputCantidad, inputPrecio, previewTotal);
    });

    // Escuchar el INPUT de Precio (cuando se cambia manualmente)
    inputPrecio.addEventListener("input", () => {
      actualizarTotalPreview(inputCantidad, inputPrecio, previewTotal);
    });
  }

  // --- LÓGICA PARA EL MODAL "EDITAR" ---
  const modalEditarItem = document.getElementById("modalEditar");
  if (modalEditarItem) {
    const selectProducto = modalEditarItem.querySelector("#itemEditProductoSelect");
    const inputCantidad = modalEditarItem.querySelector("#itemEditCantidadInput");
    const inputPrecio = modalEditarItem.querySelector("#itemEditPrecioInput");
    const previewTotal = modalEditarItem.querySelector("#itemEditTotalPreview");

    // Asegurar la existencia de los selectores de las variables
    if (selectProducto && inputCantidad && inputPrecio && previewTotal) {
      // Escuchar el SELECT de Producto
      selectProducto.addEventListener("change", () => {
        const opcionSeleccionada = selectProducto.options[selectProducto.selectedIndex];
        const precio = opcionSeleccionada.getAttribute("data-precio");

        if (precio) {
          inputPrecio.value = parseFloat(precio).toFixed(2);
        } else {
          inputPrecio.value = "";
        }

        // Recalcular el total
        actualizarTotalPreview(inputCantidad, inputPrecio, previewTotal);
      });

      // Escuchar el INPUT de Cantidad
      inputCantidad.addEventListener("input", () => {
        actualizarTotalPreview(inputCantidad, inputPrecio, previewTotal);
      });

      // Escuchar el INPUT de Precio (cuando se cambia manualmente)
      inputPrecio.addEventListener("input", () => {
        actualizarTotalPreview(inputCantidad, inputPrecio, previewTotal);
      });
    }
  }
});