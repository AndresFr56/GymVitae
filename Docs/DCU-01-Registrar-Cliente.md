# Descripción de Casos de Uso - Registrar Cliente

| Casos de Uso | Registrar cliente | CU001 |
|--------------|-------------------|-------|
| **Actores** | Administrador o Recepcionista | |
| **Propósito** | Registrar un nuevo cliente en el sistema | |
| **Tipo** | Primario y Esencial | |
| **Resumen** | El Administrador o Recepcionista registran un nuevo cliente en el sistema con sus datos personales, de contacto y emergencia. | |
| **Precondiciones** | El Administrador o Recepcionista deben estar registrados y autenticados en el sistema. | |
| **Postcondiciones** | Se registran los datos del nuevo cliente con estado ACTIVO y código generado automáticamente. | |
| **Referencias** | RF.17, RF.18 | |
| Curso de los típico de eventos | | |
| Acciones de los actores<br>Administrador o Recepcionista | Respuesta del Sistema |
|----------------------------------------------------------|----------------------|
| 1. Selecciona la opción "Clientes" del menú lateral y escoge la opción "Vista de Clientes". | 2. Presenta un panel de Gestión de Clientes, mostrando el listado de registros existentes y las opciones de "Agregar", "Actualizar" y "Eliminar". |
| 3. Selecciona la opción "Agregar". | 4. Presenta un formulario lateral derecho con los campos del cliente:<br>- Código de Cliente (Solo lectura - generado automáticamente)<br>- Nombres *<br>- Apellidos *<br>- Cédula * (10 dígitos)<br>- Género * (MASCULINO/FEMENINO)<br>- Fecha de Nacimiento<br>- Teléfono * (10 dígitos)<br>- Email<br>- Dirección<br>- Contacto de Emergencia<br>- Teléfono de Emergencia (10 dígitos)<br><br>Los campos marcados con * son obligatorios. |
| 5. Ingresa los datos:<br>- Código: (Generado: CLI-2026001)<br>- Nombres: Pedro<br>- Apellidos: Sánchez Ruiz<br>- Cédula: 0967890123<br>- Género: MASCULINO<br>- Fecha Nacimiento: 15/03/1990<br>- Teléfono: 0932109876<br>- Email: pedro.sanchez@example.com<br>- Dirección: Av. Principal #123<br>- Contacto Emergencia: María Sánchez<br>- Teléfono Emergencia: 0987654321<br><br>Y escoge la opción "Guardar". | 6. Valida los datos ingresados:<br>- Cédula única (no duplicada)<br>- Nombres y Apellidos únicos<br>- Email único<br>- Formatos correctos<br><br>Genera código automático (CLI-{año}{secuencia}), asigna estado ACTIVO por defecto, guarda el registro y presenta un mensaje informativo: "Cliente registrado exitosamente con código: CLI-2026001".<br><br>Cierra el formulario y actualiza la tabla con el nuevo cliente. |

## Curso alterno de eventos

| # | Evento |
|---|--------|
| 3 | Si no se selecciona la opción "Agregar", el sistema no muestra el formulario de registro. |
| 5 | Si escoge la opción "Cancelar", el sistema cierra el formulario sin guardar cambios. |
| 6 | **Validaciones de campos obligatorios:**<br>1. Si los campos Nombres, Apellidos, Cédula, Género o Teléfono están en blanco, el sistema presenta un mensaje de error: "El [nombre del campo] es obligatorio".<br>2. Si el campo Email no está vacío y no contiene un dominio válido, el sistema presenta un mensaje de error: "El formato del email no es válido".<br>3. Si el campo Cédula tiene menos de 10 dígitos, el sistema presenta un mensaje de error: "La cédula debe tener exactamente 10 dígitos".<br>4. Si el campo Teléfono tiene menos de 10 dígitos, el sistema presenta un mensaje de error: "El teléfono debe tener 10 dígitos".<br>5. Si el Teléfono de Emergencia no está vacío y tiene menos de 10 dígitos, el sistema presenta un mensaje de error: "El teléfono de emergencia debe tener 10 dígitos".<br><br>**Validaciones de duplicados:**<br>6. Si la Cédula ya existe en otro cliente, el sistema presenta un mensaje de error: "Cédula existente en otro Cliente, verifique o ingrese otro valor".<br>7. Si la combinación Nombres + Apellidos ya existe, el sistema presenta un mensaje de error: "Nombres y Apellidos existentes en otro Cliente, verifique o ingrese otros valores".<br>8. Si el Email ya existe en otro cliente, el sistema presenta un mensaje de error: "Email existente en otro Cliente, verifique o ingrese otro valor".<br><br>**Validaciones de formato:**<br>9. Si los campos Nombres, Apellidos o Contacto de Emergencia contienen caracteres no alfabéticos (números o símbolos), el sistema no permite su ingreso.<br>10. Si los campos Cédula, Teléfono o Teléfono de Emergencia contienen caracteres no numéricos, el sistema no permite su ingreso.<br>11. Si cualquier campo de texto excede los 100 caracteres, el sistema no permite ingresar más caracteres. |
