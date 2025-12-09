/**
 * ==========================================
 * LOGICA CENTRAL DEL DASHBOARD (SPA) - FINAL
 * ==========================================
 */

document.addEventListener("DOMContentLoaded", function () {
  inicializarComponentes();
  configurarDelegacionEventos();
  configurarIntercepcionFormularios();

  // --- RESTAURAR VISTA PREVIA ---
  const vistaGuardada = localStorage.getItem('istore_admin_view');

  if (vistaGuardada && vistaGuardada !== 'dashboard') {
    const link = document.querySelector(`a[onclick*="'${vistaGuardada}'"]`);
    if (link) {
      link.click();
    }
  }
});

// 1. REINICIALIZACIÓN Y DETECCIÓN DE ERRORES
function inicializarComponentes() {
  // A. Tooltips
  const tooltipTriggerList = document.querySelectorAll('[data-bs-toggle="tooltip"]');
  [...tooltipTriggerList].map(tooltipTriggerEl => new bootstrap.Tooltip(tooltipTriggerEl));

  // B. Lógica Inteligente para Reabrir Modales
  const divDinamico = document.getElementById('contenido-dinamico-ajax');
  if (!divDinamico) return;

  // ESTRATEGIA 1: Buscar si el servidor mandó la orden explícita
  const fragmento = divDinamico.firstElementChild;
  let idModalAbrir = fragmento ? fragmento.dataset.mostrarModal : null;

  // ESTRATEGIA 2 (RESPALDO ROBUSTO): Buscar inputs con error (.is-invalid)
  // Esto asegura que si hay campos rojos, el modal SIEMPRE se abra.
  if (!idModalAbrir) {
    const inputConError = divDinamico.querySelector('.is-invalid');
    if (inputConError) {
      // Buscamos el modal padre de ese input
      const modalPadre = inputConError.closest('.modal');
      if (modalPadre && modalPadre.id) {
        idModalAbrir = '#' + modalPadre.id;
        console.log("⚠️ Detectados campos con error en: " + idModalAbrir);
      }
    }
  }

  // Si encontramos un modal que debe abrirse...
  if (idModalAbrir) {
    // Buscamos el elemento DENTRO del contenido dinámico para asegurar que es el nuevo
    const modalEl = divDinamico.querySelector(idModalAbrir);

    if (modalEl) {
      // Pequeño delay para asegurar que el navegador renderizó el nuevo HTML
      setTimeout(() => {
        // 1. Asegurarnos que no haya otros abiertos
        document.querySelectorAll('.modal.show').forEach(m => {
          const instance = bootstrap.Modal.getInstance(m);
          if (instance) instance.hide();
        });

        // 2. Instanciar y mostrar el nuevo
        const modalInstance = new bootstrap.Modal(modalEl, {
          backdrop: 'static', // Evita cerrar al hacer clic fuera si hay error
          keyboard: false     // Evita cerrar con ESC si hay error
        });
        modalInstance.show();
        console.log("✅ Modal reabierto con errores: " + idModalAbrir);
      }, 100);
    } else {
      console.warn("❌ Se intentó abrir " + idModalAbrir + " pero no se encontró en el DOM.");
    }
  }
}

// 2. CONFIGURACIÓN DE EVENTOS (Editar/Eliminar)
function configurarDelegacionEventos() {

  // BOTONES EDITAR
  document.addEventListener('click', function (e) {
    // Usamos closest para detectar click en el botón o su ícono
    const boton = e.target.closest('[data-bs-target="#modalEditar"]');
    if (!boton) return;

    const entidad = boton.getAttribute("data-entidad");
    const modal = document.getElementById("modalEditar");

    if (modal) {
      // Resetear el formulario primero para evitar datos viejos
      const form = modal.querySelector('form');
      if (form) form.reset();

      // Lógica de llenado (Switch)
      // Asegúrate de que los nombres de los inputs coincidan con tu HTML
      switch (entidad) {
        case "usuario":
          safelySetValue(modal, "input[name='idUsuario']", boton.dataset.id);
          safelySetValue(modal, "input[name='nombres']", boton.dataset.nombres);
          safelySetValue(modal, "input[name='apellidos']", boton.dataset.apellidos);
          safelySetValue(modal, "input[name='dni']", boton.dataset.dni);
          safelySetValue(modal, "input[name='email']", boton.dataset.email);
          safelySetValue(modal, "input[name='telefono']", boton.dataset.telefono);
          safelySetValue(modal, "input[name='direccion']", boton.dataset.direccion);
          safelySetValue(modal, "input[name='nombreUsuario']", boton.dataset.nombreUsuario);

          const selectRol = modal.querySelector("select[name='idRol']");
          if (selectRol) selectRol.value = boton.dataset.idRol || 0;

          break;

        case "rol":
          safelySetValue(modal, "input[name='idRol']", boton.dataset.id);
          safelySetValue(modal, "input[name='nombre']", boton.dataset.nombre);
          safelySetValue(modal, "input[name='nivel']", boton.dataset.nivel);

          break;

        case "color":
          safelySetValue(modal, "input[name='idColor']", boton.dataset.id);
          safelySetValue(modal, "input[name='nombre']", boton.dataset.nombre);

          break;

        case "categoria":
          safelySetValue(modal, "input[name='idCategoria']", boton.dataset.id);
          safelySetValue(modal, "input[name='nombre']", boton.dataset.nombre);

          break;

        case "estadoCompra":
          safelySetValue(modal, "input[name='idEstadoCompra']", boton.dataset.id);
          safelySetValue(modal, "input[name='nombre']", boton.dataset.nombre);

          break;

        case "estadoPago":
          safelySetValue(modal, "input[name='idEstadoPago']", boton.dataset.id);
          safelySetValue(modal, "input[name='nombre']", boton.dataset.nombre);

          break;

        case "metodoPago":
          safelySetValue(modal, "input[name='idMetodoPago']", boton.dataset.id);
          safelySetValue(modal, "input[name='nombre']", boton.dataset.nombre);

          break;

        case "tipoComprobante":
          safelySetValue(modal, "input[name='idTipoComprobante']", boton.dataset.id);
          safelySetValue(modal, "input[name='nombre']", boton.dataset.nombre);

          break;

        case "producto":
          safelySetValue(modal, "input[name='idProducto']", boton.dataset.id);
          safelySetValue(modal, "input[name='nombre']", boton.dataset.nombre);
          safelySetValue(modal, "input[name='sku']", boton.dataset.sku);

          const selectCategoria = modal.querySelector("select[name='idCategoria']");
          if (selectCategoria) selectCategoria.value = boton.dataset.idCategoria || 0;

          safelySetValue(modal, "textarea[name='descripcion']", boton.dataset.descripcion);
          safelySetValue(modal, "input[name='precio']", boton.dataset.precio);

          break;

        case "pedido":
          safelySetValue(modal, "input[name='idPedido']", boton.dataset.id);

          // Campos de solo lectura (usamos ID porque no se envían en el form)
          safelySetValue(modal, "#editPedidoUsuarioEmail", boton.dataset.usuarioEmail);
          safelySetValue(modal, "#editPedidoUsuarioNombreUsuario", boton.dataset.usuario);
          safelySetValue(modal, "#editPedidoUsuarioNombre", boton.dataset.usuarioNombrecompleto);

          // Selects editables
          safelySetValue(modal, "select[name='idEstadoCompra']", boton.dataset.idEstadoCompra);
          safelySetValue(modal, "select[name='idMetodoPago']", boton.dataset.idMetodoPago);
          safelySetValue(modal, "select[name='idTipoComprobante']", boton.dataset.idTipoComprobante);
          break;

        case "pedidoItem":
          safelySetValue(modal, "input[name='idPedidoItem']", boton.dataset.idItem);
          safelySetValue(modal, "select[name='idProducto']", boton.dataset.idProducto);
          safelySetValue(modal, "input[name='cantidad']", boton.dataset.cantidad);
          safelySetValue(modal, "input[name='precio']", boton.dataset.precio);

          // Calcular el total preview manualmente al abrir para que no salga vacío
          const cant = parseFloat(boton.dataset.cantidad) || 0;
          const prec = parseFloat(boton.dataset.precio) || 0;
          const total = cant * prec;
          safelySetValue(modal, "#edit_item_totalPreview", 'S/ ' + total.toFixed(2));
          break;
      }
    }
  });

  // BOTONES ELIMINAR
  document.addEventListener('click', function (e) {
    const boton = e.target.closest('[data-bs-target="#modalEliminar"]');
    if (!boton) return;

    const entidad = boton.getAttribute("data-entidad");
    const modal = document.getElementById("modalEliminar");

    if (modal) {
      switch (entidad) {
        case "usuario":
          safelySetText(modal, "#del_idSpan", boton.dataset.id);
          safelySetText(modal, "#del_nombreCompleto", boton.dataset.nombreCompleto);
          safelySetText(modal, "#del_dni", boton.dataset.dni);
          safelySetText(modal, "#del_email", boton.dataset.email);
          safelySetText(modal, "#del_nombreUsuario", boton.dataset.nombreUsuario);
          safelySetText(modal, "#del_rol", boton.dataset.rol);
          safelySetValue(modal, "#del_id", boton.dataset.id);

          break;

        case "rol":
          safelySetText(modal, "#del_nivel", boton.dataset.nivel);
        case "color":
        case "categoria":
        case "estadoCompra":
        case "estadoPago":
        case "metodoPago":
        case "tipoComprobante":
          safelySetText(modal, "#del_idSpan", boton.dataset.id);
          safelySetText(modal, "#del_nombre", boton.dataset.nombre);
          safelySetValue(modal, "#del_id", boton.dataset.id);

          break;

        case "producto":
          safelySetText(modal, "#del_idSpan", boton.dataset.id);
          safelySetText(modal, "#del_sku", boton.dataset.sku);
          safelySetText(modal, "#del_nombre", boton.dataset.nombre);
          safelySetText(modal, "#del_categoria", boton.dataset.categoria);
          safelySetText(modal, "#del_precio", boton.dataset.precio);
          safelySetValue(modal, "#del_id", boton.dataset.id);

          break;

        case "pedido":
          safelySetText(modal, "#del_idSpan", boton.dataset.id);
          safelySetText(modal, "#del_total", 'S/ ' + parseFloat(boton.dataset.total).toFixed(2));
          safelySetText(modal, "#del_estadoCompra", boton.dataset.estadoCompra);
          safelySetText(modal, "#del_metodoPago", boton.dataset.metodoPago);
          safelySetText(modal, "#del_usuarioNombreCompleto", boton.dataset.usuarioNombrecompleto);
          safelySetText(modal, "#del_usuario", boton.dataset.usuario);
          safelySetText(modal, "#del_usuarioEmail", boton.dataset.usuarioEmail);
          safelySetValue(modal, "#del_id", boton.dataset.id);
          break;

        case "pedidoItem":
          safelySetText(modal, "#del_idSpan", boton.dataset.idItem);
          safelySetText(modal, "#del_productoNombre", boton.dataset.productoNombre);
          safelySetText(modal, "#del_totalItem", 'S/ ' + parseFloat(boton.dataset.totalItem).toFixed(2));
          safelySetValue(modal, "#del_id", boton.dataset.idItem);
          break;
      }
    }
  });
}

// Helpers para evitar errores si un campo no existe en el modal
function safelySetValue(parent, selector, value) {
  const el = parent.querySelector(selector);
  if (el) el.value = value || "";
}
function safelySetText(parent, selector, text) {
  const el = parent.querySelector(selector);
  if (el) el.textContent = text || "";
}

// 3. INTERCEPCIÓN DE FORMULARIOS (AJAX)
function configurarIntercepcionFormularios() {
  document.addEventListener('submit', function (e) {
    const form = e.target.closest('.modal form');
    if (form) {
      e.preventDefault();

      const url = form.action;
      const formData = new FormData(form);
      const btnSubmit = form.querySelector('button[type="submit"]');

      // Feedback visual
      const textoOriginal = btnSubmit.innerHTML;
      btnSubmit.disabled = true;
      btnSubmit.innerHTML = '<span class="spinner-border spinner-border-sm"></span> Procesando...';

      fetch(url, {
        method: 'POST',
        body: formData
      })
        .then(response => {
          if (!response.ok) throw new Error('Error ' + response.status);
          // Si hay redirección, recargamos el fragmento (usando text())
          return response.text();
        })
        .then(html => {
          if (html) {
            // 1. Limpieza Extrema de Bootstrap
            document.querySelectorAll('.modal-backdrop').forEach(el => el.remove());
            document.body.classList.remove('modal-open');
            document.body.style = '';

            // 2. Inyección
            const contenedorDinamico = document.getElementById('contenido-dinamico-ajax');
            contenedorDinamico.innerHTML = `<div class="fade-in">${html}</div>`;

            // 3. Reiniciar (Aquí se detectará si hay errores y se abrirá el modal)
            inicializarComponentes();
          }
        })
        .catch(err => {
          console.error(err);
          alert("Ocurrió un error: " + err.message);
          btnSubmit.disabled = false;
          btnSubmit.innerHTML = textoOriginal;
        });
    }
  });
}

// 4. NAVEGACIÓN
function cargarVista(nombreVista, urlFragmento, elementoLink) {
  // --- GUARDAR EN MEMORIA ---
  localStorage.setItem('istore_admin_view', nombreVista);

  if (elementoLink) {
    document.querySelectorAll('.nav-link-admin').forEach(link => link.classList.remove('active'));
    elementoLink.classList.add('active');
  }

  const divDashboard = document.getElementById('vista-dashboard');
  const divDinamico = document.getElementById('vista-dinamica');
  const contenedorDinamico = document.getElementById('contenido-dinamico-ajax');

  if (nombreVista === 'dashboard') {
    divDashboard.classList.remove('d-none');
    divDinamico.classList.add('d-none');
    contenedorDinamico.innerHTML = '';
  } else {
    divDashboard.classList.add('d-none');
    divDinamico.classList.remove('d-none');
    contenedorDinamico.innerHTML = '<div class="d-flex justify-content-center p-5"><div class="spinner-border text-primary"></div></div>';

    fetch(urlFragmento)
      .then(res => res.text())
      .then(html => {
        contenedorDinamico.innerHTML = `<div class="fade-in">${html}</div>`;
        inicializarComponentes();
      })
      .catch(err => {
        contenedorDinamico.innerHTML = `<div class="alert alert-danger m-3">Error de carga: ${err.message}</div>`;
      });
  }
}

/**
 * ====================================================================
 * LÓGICA DE CÁLCULO DE TOTALES (PEDIDOS)
 * ====================================================================
 */

// Esta función debe llamarse desde el HTML: onchange="actualizarPrecioItem('add')"
function actualizarPrecioItem(modo) { // modo puede ser 'add' o 'edit'
  const prefijo = (modo === 'add') ? 'add_item_' : 'edit_item_'; // Ajusta según los IDs del modal

  const idSelect = prefijo + 'idProducto';
  const idPrecio = prefijo + 'precio';

  const select = document.getElementById(idSelect);
  const inputPrecio = document.getElementById(idPrecio);

  if (select && inputPrecio) {
    const opcionSeleccionada = select.options[select.selectedIndex];
    const precio = opcionSeleccionada.getAttribute('data-precio');

    if (precio) {
      inputPrecio.value = parseFloat(precio).toFixed(2);
    } else {
      inputPrecio.value = '';
    }
    // Recalcular total inmediatamente
    calcularTotalItem(modo);
  }
}

// Esta función se llama: oninput="calcularTotalItem('add')"
function calcularTotalItem(modo) {
  // IDs basados en los modales
  const prefijo = (modo === 'add') ? 'add_item_' : 'edit_item_';

  const idCantidad = prefijo + 'cantidad';
  const idPrecio = prefijo + 'precio';
  const idTotal = prefijo + 'totalPreview';

  const inputCantidad = document.getElementById(idCantidad);
  const inputPrecio = document.getElementById(idPrecio);
  const inputTotal = document.getElementById(idTotal);

  if (inputCantidad && inputPrecio && inputTotal) {
    const cantidad = parseFloat(inputCantidad.value) || 0;
    const precio = parseFloat(inputPrecio.value) || 0;
    const total = cantidad * precio;

    inputTotal.value = `S/ ${total.toFixed(2)}`;
  }
}