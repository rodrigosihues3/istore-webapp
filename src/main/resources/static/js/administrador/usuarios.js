document.addEventListener("DOMContentLoaded", function () {
  // ########################################### USUARIOS ###########################################
  // Para el modal editar, oculta opciones de rol según nivel del usuario logueado
  const modalEditar = document.getElementById('modalEditar');
  if (modalEditar) {
    // Evento de bootstrap que se usa en el elemento modal
    modalEditar.addEventListener('show.bs.modal', (event) => {
      const button = event.relatedTarget; // botón que abrió el modal
      if (!button) return;

      const rolUsuarioEditar = button.getAttribute('data-id-rol'); // idRol del usuario que se edita (string)
      const nombreUsuarioEditar = button.getAttribute('data-nombre-usuario');

      const selectRol = modalEditar.querySelector('select[name="idRol"]');
      if (!selectRol) return;

      // Datos del usuario actual tomados del body (cargados por el controlador)
      const usuarioActual = document.body.getAttribute('data-usuario-actual') || '';
      const nivelActual = parseInt(document.body.getAttribute('data-nivel-actual')) || 0;

      Array.from(selectRol.options).forEach(opt => {
        // data-nivel de cada <option>
        const nivelRolOpcion = parseInt(opt.getAttribute('data-nivel')) || 0;

        // Ocultar si el rol tiene (nivel >= nivelDelUsuario) y no es el propio usuario que se edita
        if (nivelRolOpcion >= nivelActual && nombreUsuarioEditar !== usuarioActual && nivelActual != 10) {
          opt.hidden = true;
        } else {
          opt.hidden = false;
        }
      });

      // Seleccionar la opción correspondiente al rol del usuario que se está editando
      selectRol.value = rolUsuarioEditar;
    });
  }
});