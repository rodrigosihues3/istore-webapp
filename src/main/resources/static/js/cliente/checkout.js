document.addEventListener("DOMContentLoaded", function () {
  const cartModal = document.getElementById('cartModal');
  if (cartModal) {
    cartModal.addEventListener('hidden.bs.modal', function () {
      window.location.reload();
    });
  }
});

function toggleFactura() {
  const isFactura = document.getElementById('btnFactura').checked;
  const lblDoc = document.getElementById('lblDoc');
  const lblNombre = document.getElementById('lblNombre');
  const inputDoc = document.getElementById('numDoc');
  const inputNombre = document.getElementById('nomEntidad');

  inputDoc.value = "";
  inputNombre.value = "";

  if (isFactura) {
    lblDoc.textContent = "RUC";
    lblNombre.textContent = "Razón Social";
    inputDoc.placeholder = "20...";
    inputDoc.setAttribute("maxLength", "11");
    inputDoc.setAttribute("minLength", "11");
  } else {
    lblDoc.textContent = "DNI";
    lblNombre.textContent = "Nombre Completo";
    inputDoc.placeholder = "";
    inputDoc.setAttribute("maxLength", "8");
    inputDoc.setAttribute("minLength", "8");
  }
}

function toggleMetodoPago() {
  const metodo = document.querySelector('input[name="metodoPago"]:checked').value;
  const infoYape = document.getElementById('infoYape');
  const infoTarjeta = document.getElementById('infoTarjeta');
  const btnContinuar = document.getElementById('btnContinuar');


  if (metodo === 'YAPE') {
    infoYape.classList.remove('d-none');
    infoTarjeta.classList.add('d-none');
    btnContinuar.textContent = "Confirmar Pago y Finalizar Pedido";
  } else {
    infoYape.classList.add('d-none');
    infoTarjeta.classList.remove('d-none');
    btnContinuar.textContent = "Pagar y Finalizar Pedido";
  }
}

function iniciarProcesoPago() {
  // Validar Datos Personales
  const doc = document.getElementById('numDoc').value.trim();
  const nom = document.getElementById('nomEntidad').value.trim();
  console.log("document.getElementById('pagoTarjeta').value :>> ", document.getElementById('pagoTarjeta').value);
  if (!doc || !nom) {
    alert("Por favor completa los datos del comprobante.");
    return;
  }

  // Detectar Método
  const metodo = document.querySelector('input[name="metodoPago"]:checked').value;

  if (metodo === 'TARJETA_CREDITO') {
    // ABRIR MODAL CULQI
    const modalCulqi = new bootstrap.Modal(document.getElementById('modalCulqi'));
    modalCulqi.show();
  } else {
    // CASO YAPE: Validar Nro Operación
    const nroOp = document.getElementById('nroOperacionYape').value.trim();
    if (!nroOp) {
      alert("Por favor ingresa el número de operación de Yape.");
      return;
    }

    enviarPedidoAlServidor(nroOp);
  }
}

// Acción del Botón "Saltar Pago" (Desde el Modal)
function finalizarCompraDemo() {
  // Generar token falso simulado
  const tokenSimulado = "TOK_TEST_" + Date.now();
  enviarPedidoAlServidor(tokenSimulado);
}

// Envío Real al Backend
function enviarPedidoAlServidor(referenciaPago) {
  const metodoSeleccionado = document.querySelector('input[name="metodoPago"]:checked').value;

  const data = {
    direccionEntrega: document.querySelector('input[name="sede"]:checked').value,
    tipoComprobante: document.querySelector('input[name="tipoDoc"]:checked').value,
    numeroDocumento: document.getElementById('numDoc').value,
    nombreEntidad: document.getElementById('nomEntidad').value,
    metodoPago: metodoSeleccionado,
    referenciaPago: referenciaPago
  };

  console.log("Enviando pedido:", data);

  fetch('/cliente/checkout/procesar', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(data)
  })
    .then(res => res.json())
    .then(resp => {
      if (resp.ok) {
        Swal.fire({
          title: '¡Excelente!',
          text: 'Tu pedido #123 ha sido confirmado.',
          icon: 'success',
          confirmButtonText: 'Ver mis pedidos'
        }).then((result) => {
          if (result.isConfirmed) {
            window.location.href = '/mi-cuenta/pedidos';
          }
        });
      } else {
        alert("Error: " + resp.mensaje);
      }
    })
    .catch(err => {
      console.error(err);
      alert("Error de conexión con el servidor");
    });
}

function abrirCulqi() {
  if (window.Culqi) {
    Culqi.open();
  } else {
    alert("Error: No se cargó la librería de Culqi.");
  }
}

document.getElementById('numDoc').addEventListener('input', function (e) {
  this.value = this.value.replace(/[^0-9]/g, '');
});