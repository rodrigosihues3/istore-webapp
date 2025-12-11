// NAVEGACIÓN SPA (Estilo Admin)
function cargarVista(vista, url, elemento) {
  const dashboard = document.getElementById('vista-dashboard');
  const dinamico = document.getElementById('vista-dinamica');
  const contenedor = document.getElementById('contenido-dinamico-ajax');

  // Gestión de clases del sidebar
  if (elemento) {
    document.querySelectorAll('.nav-link').forEach(l => l.classList.remove('active'));
    elemento.classList.add('active');
  }

  if (vista === 'dashboard') {
    dashboard.classList.remove('d-none');
    dinamico.classList.add('d-none');
  } else {
    dashboard.classList.add('d-none');
    dinamico.classList.remove('d-none');

    // Feedback de carga
    contenedor.innerHTML = '<div class="text-center py-5"><div class="spinner-border text-primary"></div></div>';

    fetch(url)
      .then(res => res.text())
      .then(html => {
        contenedor.innerHTML = html;
      })
      .catch(err => {
        contenedor.innerHTML = '<div class="alert alert-danger">Error al cargar la vista.</div>';
      });
  }
}

// FUNCIONES DE PEDIDOS
function cargarTabla() {
  const contenedor = document.getElementById('contenido-dinamico-ajax');
  contenedor.style.opacity = '0.5';
  fetch('/empleado/pedidos/tabla')
    .then(res => res.text())
    .then(html => {
      contenedor.innerHTML = html;
      contenedor.style.opacity = '1';
    });
}

function cambiarEstado(idPedido, accion) {
  const formData = new FormData();
  formData.append('idPedido', idPedido);
  formData.append('accion', accion);

  const contenedor = document.getElementById('contenido-dinamico-ajax');

  // Feedback bloqueante para evitar doble clic
  contenedor.innerHTML = '<div class="text-center py-5"><div class="spinner-border text-primary"></div><p>Procesando...</p></div>';

  fetch('/empleado/pedidos/cambiar-estado', { method: 'POST', body: formData })
    .then(res => res.text())
    .then(html => {
      contenedor.innerHTML = html;
    });
}

let modalInstancia = null; // Variable global para controlar la instancia

function verDetalle(idPedido) {
  const modalEl = document.getElementById('modalDetalle');
  const contenido = document.getElementById('contenido-modal');

  // Gestión correcta de la instancia Bootstrap
  if (!modalInstancia) {
    modalInstancia = new bootstrap.Modal(modalEl);
  }

  // Spinner de carga
  contenido.innerHTML = '<div class="modal-body text-center p-5"><div class="spinner-border text-primary"></div></div>';

  modalInstancia.show();

  fetch(`/empleado/pedidos/detalle/${idPedido}`)
    .then(res => {
      if (!res.ok) throw new Error('Error en la respuesta');
      return res.text();
    })
    .then(html => {
      contenido.innerHTML = html;
    })
    .catch(err => {
      contenido.innerHTML = '<div class="modal-body text-danger text-center">Error al cargar detalles.</div>';
      console.error(err);
    });
}

// 5. BUSCADOR INVENTARIO
function filtrarTabla() {
  var input = document.getElementById("buscador");
  if (!input) return; // Validación por seguridad

  var filter = input.value.toUpperCase();
  var table = document.getElementById("tabla-inventario");
  var tr = table.getElementsByTagName("tr");

  for (var i = 1; i < tr.length; i++) {
    var tdNombre = tr[i].getElementsByTagName("td")[2]; // Columna Producto
    var tdSku = tr[i].getElementsByTagName("td")[1];    // Columna SKU
    if (tdNombre || tdSku) {
      var txtValue = (tdNombre.textContent || tdNombre.innerText) + (tdSku.textContent || tdSku.innerText);
      if (txtValue.toUpperCase().indexOf(filter) > -1) {
        tr[i].style.display = "";
      } else {
        tr[i].style.display = "none";
      }
    }
  }
}