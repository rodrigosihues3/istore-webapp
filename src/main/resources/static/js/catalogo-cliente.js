document.addEventListener("DOMContentLoaded", function () {
  configurarNavbarCategorias();
  configurarBuscador();
  configurarEventosProductos();

  // Listener para el botón "Agregar" DENTRO del Modal (si existe al inicio)
  const btnAgregarModal = document.querySelector('.btn-agregar-modal');
  if (btnAgregarModal) {
    btnAgregarModal.addEventListener('click', () => {
      const idProducto = btnAgregarModal.dataset.id;
      agregarAlCarrito(idProducto, 1);
      const modalEl = document.getElementById('modalDetalleProducto');
      const modal = bootstrap.Modal.getInstance(modalEl);
      modal.hide();
    });
  }
});

// 1. Manejo del Navbar de Categorías
function configurarNavbarCategorias() {
  // Seleccionamos TODOS los links dentro del menú de categorías
  const container = document.querySelector('.nav-categorias');
  if (!container) return;

  const links = container.querySelectorAll('.nav-link');

  links.forEach(link => {
    link.addEventListener('click', function (e) {
      // Solo interceptamos si es un link interno del catálogo
      const href = this.getAttribute('href');
      if (!href.startsWith('/catalogo') && href !== '/') return;

      // Si es "Inicio", dejamos que recargue normal (opcional, o prevenir también)
      if (href === '/') return;

      e.preventDefault();

      // 1. LIMPIEZA VISUAL: Quitar 'active' de TODOS los links del menú
      links.forEach(l => l.classList.remove('active'));

      // 2. ACTIVAR EL ACTUAL
      this.classList.add('active');

      // 3. LÓGICA DE CARGA
      if (href === '/catalogo') {
        // Caso "Ver Todo"
        cargarProductos({});
      } else {
        // Caso Categoría específica (ej: /catalogo/iphones)
        const categoria = href.split('/').pop();
        cargarProductos({ categoria: categoria });
      }
    });
  });
}

// 2. Manejo del Buscador
function configurarBuscador() {
  const formBuscador = document.querySelector('form[role="search"]');

  if (formBuscador) {
    formBuscador.addEventListener('submit', function (e) {
      e.preventDefault();
      const input = this.querySelector('input[type="search"]');
      const valor = input.value.trim();

      if (valor) {
        cargarProductos({ busqueda: valor });
        // Limpiar selección del navbar
        document.querySelectorAll('.nav-link').forEach(l => l.classList.remove('active'));
      }
    });
  }
}

// 3. Función Central AJAX
function cargarProductos(params) {
  const contenedor = document.getElementById('productos'); // El <main id="productos">
  const carrusel = document.querySelector('section'); // El carrusel para ocultarlo si buscamos

  // Feedback de carga
  contenedor.innerHTML = '<div class="text-center py-5"><div class="spinner-border text-primary" role="status"></div></div>';

  // Construir URL
  const queryParams = new URLSearchParams(params).toString();
  const url = `/catalogo/filtrar?${queryParams}`;

  fetch(url)
    .then(response => response.text())
    .then(html => {
      // Ocultar carrusel al filtrar para dar foco a los resultados
      if (carrusel) carrusel.style.display = 'none';

      // Inyectar HTML
      contenedor.innerHTML = html;

      // Reinicializar eventos de los nuevos botones (Detalles y Carrito)
      configurarEventosProductos();
    })
    .catch(err => {
      console.error(err);
      contenedor.innerHTML = '<div class="alert alert-danger text-center">Error al cargar productos.</div>';
    });
}

// 4. Configurar eventos de los elementos dinámicos
function configurarEventosProductos() {
  // A. Lógica para el Modal de Detalles (Visualización)
  const botonesDetalle = document.querySelectorAll('.btn-detalle-producto');
  const modalDetalle = document.getElementById('modalDetalleProducto');

  if (modalDetalle) {
    botonesDetalle.forEach(btn => {
      btn.addEventListener('click', () => {
        // Llenar datos visuales
        modalDetalle.querySelector('.modal-title').textContent = btn.dataset.nombre;
        modalDetalle.querySelector('img').src = btn.dataset.imagen || 'https://via.placeholder.com/300';
        modalDetalle.querySelector('#modalPrecio').textContent = 'S/. ' + parseFloat(btn.dataset.precio).toFixed(2);
        modalDetalle.querySelector('#modalDescripcion').textContent = btn.dataset.descripcion;

        // PREPARAR EL BOTÓN DE AGREGAR DEL MODAL
        const btnAgregar = modalDetalle.querySelector('.btn-agregar-modal');
        btnAgregar.dataset.id = btn.dataset.id; // ¡Importante! Pasamos el ID

        // Validar Stock visualmente
        if (parseInt(btn.dataset.stock) <= 0) {
          btnAgregar.disabled = true;
          btnAgregar.textContent = "Agotado";
          btnAgregar.classList.replace('btn-primary', 'btn-secondary');
        } else {
          btnAgregar.disabled = false;
          btnAgregar.textContent = "Agregar al carrito";
          btnAgregar.classList.replace('btn-secondary', 'btn-primary');
        }
      });
    });
  }

  // B. Lógica de "Agregar al Carrito" (Desde la Grilla)
  const botonesAgregarGrilla = document.querySelectorAll('.btn-agregar-carrito');
  botonesAgregarGrilla.forEach(btn => {
    btn.addEventListener('click', (e) => {
      e.stopPropagation(); // Evita que se abra el modal de detalle al hacer clic en agregar
      agregarAlCarrito(btn.dataset.id, 1); // Cantidad 1 por defecto desde grilla
    });
  });
}

// 5. Listener para el botón "Agregar" DENTRO del Modal
// (Este se configura una sola vez al carga la página, fuera de configurarEventosProductos)
document.addEventListener("DOMContentLoaded", function () {
  // ... tus inits anteriores ...

  const btnAgregarModal = document.querySelector('.btn-agregar-modal');
  if (btnAgregarModal) {
    btnAgregarModal.addEventListener('click', () => {
      // Aquí tu compañero podría querer leer un input de cantidad si lo agregan al modal
      const idProducto = btnAgregarModal.dataset.id;
      agregarAlCarrito(idProducto, 1);

      // Opcional: Cerrar modal
      const modalEl = document.getElementById('modalDetalleProducto');
      const modal = bootstrap.Modal.getInstance(modalEl);
      modal.hide();
    });
  }
});

/**
 * ====================================================================
 * ZONA DE INTEGRACIÓN PARA EL COMPAÑERO (CARRITO)
 * ====================================================================
 */
function agregarAlCarrito(idProducto, cantidad) {
  console.log(`🛒 Solicitud para agregar: Producto ID ${idProducto}, Cantidad: ${cantidad}`);

  // --- AQUÍ TU COMPAÑERO DEBE PONER SU FETCH ---
  /*
  fetch('/carrito/agregar', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ idProducto: idProducto, cantidad: cantidad })
  })
  .then(res => res.json())
  .then(data => {
      alert("Producto agregado!");
      // Actualizar contador del navbar...
  });
  */

  // Feedback temporal para la demo
  alert("✅ Lógica lista para conectar el carrito.\nProducto ID: " + idProducto);
}