document.addEventListener("DOMContentLoaded", function () {
  // Reabrir modal por contener errores de validación y mostrarlo nuevamente
  const modalId = document.body.dataset.mostrarModal;
  if (modalId) {
    const modal = document.querySelector(modalId);
    if (modal) new bootstrap.Modal(modal).show();
  }
});
