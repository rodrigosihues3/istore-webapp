document.addEventListener("DOMContentLoaded", function () {
  configurarNavbarCategorias();
  configurarBuscador();
  // Opcional: Cargar todos los productos al inicio si no hay destacados
  // cargarProductos(); 
});

// 1. Manejo del Navbar de Categorías
function configurarNavbarCategorias() {
  const links = document.querySelectorAll('.nav-link[href^="/catalogo/"]');

  links.forEach(link => {
    link.addEventListener('click', function (e) {
      e.preventDefault(); // Evita recargar la página

      // Extraer la categoría de la URL (ej: /catalogo/iphones -> iphones)
      const url = this.getAttribute('href');
      const categoria = url.split('/').pop();

      // Actualizar clases active
      document.querySelectorAll('.nav-link').forEach(l => l.classList.remove('active'));
      this.classList.add('active');

      cargarProductos({ categoria: categoria });
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

// 4. Configurar eventos de los elementos dinámicos (Botones dentro del fragmento)
function configurarEventosProductos() {
  // Lógica para el Modal de Detalles (Genérico)
  const botonesDetalle = document.querySelectorAll('.btn-detalle-producto');
  const modalDetalle = document.getElementById('modalDetalleProducto');

  if (modalDetalle) {
    botonesDetalle.forEach(btn => {
      btn.addEventListener('click', () => {
        // Llenar datos del modal
        modalDetalle.querySelector('.modal-title').textContent = btn.dataset.nombre;
        modalDetalle.querySelector('img').src = btn.dataset.imagen || 'https://via.placeholder.com/300';
        modalDetalle.querySelector('#modalPrecio').textContent = 'S/. ' + parseFloat(btn.dataset.precio).toFixed(2);
        modalDetalle.querySelector('#modalDescripcion').textContent = btn.dataset.descripcion;

        // Configurar botón "Agregar" del modal
        const btnAgregar = modalDetalle.querySelector('.btn-agregar-modal');
        btnAgregar.dataset.id = btn.dataset.id; // Pasar ID para el carrito

        if (parseInt(btn.dataset.stock) <= 0) {
          btnAgregar.disabled = true;
          btnAgregar.textContent = "Agotado";
        } else {
          btnAgregar.disabled = false;
          btnAgregar.textContent = "Agregar al carrito";
        }
      });
    });
  }
}