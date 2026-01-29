# Descripción de Casos de Uso - Consultar Cliente con Filtros

| Casos de Uso | Consultar cliente con filtros | CU004 |
|--------------|-------------------------------|-------|
| **Actores** | Administrador o Recepcionista | |
| **Propósito** | Buscar y visualizar clientes aplicando filtros de búsqueda y estado | |
| **Tipo** | Primario y Esencial | |
| **Resumen** | El Administrador o Recepcionista consultan la lista de clientes aplicando criterios de búsqueda por nombre/cédula y filtro de estado. | |
| **Precondiciones** | El Administrador o Recepcionista deben estar registrados y autenticados en el sistema. Deben existir clientes registrados en el sistema. | |
| **Postcondiciones** | Se muestra la lista de clientes filtrada según los criterios especificados con paginación. | |
| **Referencias** | RF.17, RF.21 | |

## Curso de los típico de eventos

| Acciones de los actores<br>Administrador o Recepcionista | Respuesta del Sistema |
|----------------------------------------------------------|----------------------|
| 1. Selecciona la opción "Clientes" del menú lateral y escoge la opción "Vista de Clientes". | 2. Presenta un panel de Gestión de Clientes mostrando:<br>- Campo de búsqueda con placeholder "Buscar..."<br>- ComboBox de Estado con opciones: "Todos", "ACTIVO", "INACTIVO", "SUSPENDIDO"<br>- Botón "Limpiar Filtros"<br>- Tabla con columnas: SELECT, #, Código, Nombre Completo, Cédula, Teléfono, Estado<br>- Panel de paginación con total de registros (ej: "Total: 45")<br>- Botones de acciones: Agregar, Actualizar, Eliminar |
| 3. **Búsqueda por texto:** Ingresa en el campo de búsqueda "Pedro" para encontrar clientes con ese nombre. | 4. El sistema filtra en tiempo real los clientes que coincidan con el texto ingresado en:<br>- Nombres y Apellidos: "Pedro", "Pedro Sánchez", etc.<br>- Cédula: Si el texto contiene números (ej: "0967890123")<br><br>Muestra los resultados filtrados:<br>- # 1 | CLI-2024000001 | Pedro Sánchez Ruiz | 0967890123 | 0932109876 | ACTIVO<br>- # 2 | CLI-2024000005 | Pedro López García | 0912345678 | 0987654321 | ACTIVO<br><br>Actualiza el contador: "Total: 2" |
| 5. **Filtro por estado:** Selecciona en el ComboBox "Estado" la opción "ACTIVO". | 6. El sistema filtra adicionalmente los resultados mostrando solo clientes con estado ACTIVO:<br>- # 1 | CLI-2024000001 | Pedro Sánchez Ruiz | 0967890123 | 0932109876 | ACTIVO<br>- # 2 | CLI-2024000005 | Pedro López García | 0912345678 | 0987654321 | ACTIVO<br><br>Actualiza el contador: "Total: 2" |
| 7. **Navegación con paginación:** Si hay más de 10 registros, observa el panel de paginación con números de página. Selecciona la página 2 para ver más resultados. | 8. Carga la segunda página mostrando los siguientes 10 registros (offset: 10, limit: 10):<br>- # 11 | CLI-2024000003 | Pedro Martínez Ruiz | 0934567890 | 0976543210 | ACTIVO<br>- # 12 | CLI-2024000007 | Pedro Fernández Silva | 0956789012 | 0998765432 | ACTIVO<br>...<br>...<br><br>Actualiza el indicador de página actual. |
| 9. **Limpiar filtros:** Selecciona el botón "Limpiar Filtros". | 10. El sistema limpia el campo de búsqueda, vuelve a seleccionar "Todos" en el ComboBox de Estado, y carga nuevamente la tabla completa en la página 1 mostrando todos los clientes sin filtros. Actualiza el contador al total de clientes: "Total: 45" |

## Curso alterno de eventos

| # | Evento |
|---|--------|
| 3 | Si el texto de búsqueda está vacío o contiene solo espacios, el sistema no aplica filtro de búsqueda y muestra todos los clientes (respetando el filtro de estado si está activo). |
| 3 | Si no hay coincidencias para la búsqueda "Pedro", el sistema muestra un listado vacío y el contador indica: "Total: 0". |
| 5 | Si se selecciona "Todos" en el filtro de estado, el sistema muestra clientes con cualquier estado (ACTIVO, INACTIVO, SUSPENDIDO) que coincidan con la búsqueda. |
| 5 | Si se selecciona "INACTIVO" y no hay clientes con ese estado, el sistema muestra un listado vacío: "Total: 0". |
| 7 | Si la página solicitada está fuera del rango disponible, el sistema mantiene la página actual o vuelve a la página 1. |
| 7 | Si hay exactamente 10 registros o menos, el panel de paginación solo muestra una página (la actual). |
| 9 | Si se intenta limpiar filtros estando ya sin filtros activos, el sistema no realiza cambios visibles. |
| 10 | **Validaciones de búsqueda:**<br>1. Si el campo de búsqueda contiene más de 200 caracteres, el sistema trunca automáticamente el texto a 200 caracteres y aplica el filtro.<br>2. La búsqueda es insensible a mayúsculas/minúsculas (ej: "PEDRO", "pedro", "Pedro" todos devuelven los mismos resultados).<br><br>**Validaciones de paginación:**<br>3. El tamaño de página es fijo en 10 registros por página.<br>4. El total de registros se calcula correctamente incluyendo todos los estados cuando se selecciona "Todos".<br>5. Cuando se cambia el filtro de estado o búsqueda, la tabla regresa automáticamente a la página 1. |
