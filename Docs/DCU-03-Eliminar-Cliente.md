# Descripción de Casos de Uso - Eliminar Cliente

| Casos de Uso | Eliminar cliente | CU003 |
|--------------|------------------|-------|
| **Actores** | Administrador o Recepcionista | |
| **Propósito** | Dar de baja un cliente registrado en el sistema | |
| **Tipo** | Secundario y Esencial | |
| **Resumen** | El Administrador o Recepcionista eliminan (dan de baja) un cliente registrado en el sistema cambiando su estado a SUSPENDIDO. | |
| **Precondiciones** | El Administrador o Recepcionista deben estar registrados y autenticados en el sistema. El cliente a eliminar debe existir en el sistema. | |
| **Postcondiciones** | El cliente es marcado como SUSPENDIDO en el sistema y no aparece en los listados activos. | |
| **Referencias** | RF.17, RF.20 | |

## Curso de los típico de eventos

| Acciones de los actores<br>Administrador o Recepcionista | Respuesta del Sistema |
|----------------------------------------------------------|----------------------|
| 1. Selecciona la opción "Clientes" del menú lateral y escoge la opción "Vista de Clientes". | 2. Presenta un panel de Gestión de Clientes, mostrando el listado de registros existentes y las opciones de "Agregar", "Actualizar" y "Eliminar". |
| 3. Selecciona uno o más registros marcando sus checkboxes en la columna "SELECT". Por ejemplo:<br>- Cliente 1: CLI-2024000001 - Pedro Sánchez Ruiz<br>- Cliente 2: CLI-2024000002 - Laura Martínez Flores | 4. Los registros seleccionados quedan marcados visualmente en la tabla. |
| 5. Selecciona la opción "Eliminar". | 6. Presenta un cuadro de diálogo de confirmación con el mensaje: "¿Está seguro de eliminar [N] registro(s)?" donde [N] es la cantidad de registros seleccionados. |
| 7. Confirma la eliminación seleccionando "Sí". | 8. Cambia el estado de los clientes seleccionados a SUSPENDIDO (soft delete). Presenta un mensaje informativo: "Clientes eliminados exitosamente". Actualiza la tabla removiendo los registros eliminados del listado actual. |

## Curso alterno de eventos

| # | Evento |
|---|--------|
| 3 | Si no se selecciona ningún registro antes de escoger la opción "Eliminar", el sistema muestra un mensaje de advertencia: "Por favor seleccione al menos un registro". |
| 5 | Si no hay registros disponibles para eliminar, el botón "Eliminar" permanece deshabilitado. |
| 7 | Si selecciona "No" o "Cancelar" en el cuadro de confirmación, el sistema no realiza la eliminación y cierra el diálogo. |
| 8 | **Validaciones de eliminación:**<br>1. Si alguno de los clientes seleccionados ya está en estado SUSPENDIDO, el sistema intenta cambiar su estado de todas formas (idempotencia).<br>2. Si ocurre un error durante la eliminación, el sistema presenta un mensaje de error especificando qué registro(s) fallaron y permite reintentar la operación. |
