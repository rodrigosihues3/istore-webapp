document.addEventListener("DOMContentLoaded", function () {
  // 1. RECUPERAR CONTADOR AL CARGAR PÁGINA
  actualizarBadgeNavbar();

  // Detectar cuando se abre el modal para cargar el contenido actualizado
  const cartModal = document.getElementById('cartModal');
  if (cartModal) {
    cartModal.addEventListener('show.bs.modal', function () {
      cargarCarrito();
    });
  }
});

// Función auxiliar para consultar estado actual
function actualizarBadgeNavbar() {
  fetch('/carrito/info')
    .then(res => res.json())
    .then(data => {
      const badge = document.getElementById('contador-carrito');
      if (badge) {
        badge.innerText = data.cantidadTotal;
        badge.style.display = data.cantidadTotal > 0 ? 'inline-block' : 'none';
      }
    })
    .catch(e => console.log("Carrito vacío"));
}

function agregarAlCarrito(idProducto, cantidad) {
  fetch('/carrito/agregar', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ idProducto: idProducto, cantidad: cantidad })
  })
    .then(res => res.json())
    .then(data => {
      actualizarBadgeNavbar(); // Actualiza badge inmediatamente
      mostrarToast("Producto agregado 🛒");
    })
    .catch(error => alert("Error al agregar. Intenta iniciar sesión."));
}

function cambiarCantidad(idProducto, nuevaCantidad) {
  if (nuevaCantidad < 1) return; // Validación mínima

  fetch('/carrito/actualizar', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ idProducto: idProducto, cantidad: nuevaCantidad })
  })
    .then(res => res.json())
    .then(data => {
      if (data.ok) {
        actualizarBadgeNavbar();
        cargarCarrito(); // Recarga el modal para ver nuevos precios
      }
    });
}

function eliminarDelCarrito(idProducto) {
  if (!confirm("¿Quitar producto?")) return;

  fetch('/carrito/eliminar', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ idProducto: idProducto })
  })
    .then(res => res.json())
    .then(data => {
      if (data.ok) {
        actualizarBadgeNavbar();
        cargarCarrito();
      }
    });
}

// CARGAR VISTA DEL CARRITO (AJAX HTML)
function cargarCarrito() {
  const url = '/carrito/vista';
  const modalDialog = document.querySelector('#cartModal .modal-dialog');
  const contenidoActual = document.getElementById('contenido-carrito-ajax');

  if (contenidoActual) {
    contenidoActual.style.opacity = '0.5';
  }

  fetch(url)
    .then(response => response.text())
    .then(html => {
      modalDialog.innerHTML = html;
    })
    .catch(error => {
      console.error('Error cargando carrito:', error);
      if (modalDialog) {
        modalDialog.innerHTML = '<div class="modal-content p-4 text-center text-danger">Error al cargar el carrito.</div>';
      }
    });
}

function mostrarToast(mensaje) {
  const toast = document.createElement('div');
  toast.className = 'position-fixed bottom-0 start-0 p-3';
  toast.style.zIndex = '2000';
  toast.innerHTML = `
        <div class="toast show align-items-center text-white bg-primary border-0" role="alert">
            <div class="d-flex">
                <div class="toast-body">${mensaje}</div>
                <button type="button" class="btn-close btn-close-white me-2 m-auto" data-bs-dismiss="toast"></button>
            </div>
        </div>
    `;
  document.body.appendChild(toast);
  setTimeout(() => toast.remove(), 3000);
}