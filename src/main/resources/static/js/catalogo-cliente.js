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

      // Si es "Inicio", recarga normal (opcional, o prevenir también)
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
      if (carrusel) carrusel.style.display = 'none';
      contenedor.innerHTML = html;
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
        // Llenar datos visuales del modal
        modalDetalle.querySelector('.modal-title').textContent = btn.dataset.nombre;
        const imgEl = modalDetalle.querySelector('img');
        if (imgEl) imgEl.src = btn.dataset.imagen || 'https://via.placeholder.com/300';

        const precioEl = modalDetalle.querySelector('#modalPrecio');
        if (precioEl) precioEl.textContent = 'S/. ' + parseFloat(btn.dataset.precio).toFixed(2);

        const descEl = modalDetalle.querySelector('#modalDescripcion');
        if (descEl) descEl.textContent = btn.dataset.descripcion;

        // Configurar el botón de acción del modal con el ID actual
        const btnAgregar = modalDetalle.querySelector('.btn-agregar-modal');
        btnAgregar.dataset.id = btn.dataset.id;

        // Validar Stock visualmente
        const stock = parseInt(btn.dataset.stock);
        if (stock <= 0) {
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

  // B. Botones "Agregar" directos en la tarjeta (Grilla)
  const botonesAgregarGrilla = document.querySelectorAll('.btn-agregar-carrito');
  botonesAgregarGrilla.forEach(btn => {
    btn.addEventListener('click', (e) => {
      e.stopPropagation();
      // Llama a la función global de carrito.js
      agregarAlCarrito(btn.dataset.id, 1);
    });
  });
}
