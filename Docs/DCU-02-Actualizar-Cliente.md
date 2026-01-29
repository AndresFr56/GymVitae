# Descripción de Casos de Uso - Actualizar Cliente

| Casos de Uso | Actualizar cliente | CU002 |
|--------------|-------------------|-------|
| **Actores** | Administrador o Recepcionista | |
| **Propósito** | Modificar los datos de un cliente registrado | |
| **Tipo** | Secundario y Esencial | |
| **Resumen** | El Administrador o Recepcionista modifican los datos de un cliente registrado en el sistema. | |
| **Precondiciones** | El Administrador o Recepcionista deben estar registrados y autenticados en el sistema. | |
| **Postcondiciones** | Se actualizan los datos del cliente en el sistema. | |
| **Referencias** | RF.17, RF.19 | |

## Curso de los típico de eventos

| Acciones de los actores<br>Administrador o Recepcionista | Respuesta del Sistema |
|----------------------------------------------------------|----------------------|
| 1. Selecciona la opción "Clientes" del menú lateral y escoge la opción "Vista de Clientes". | 2. Presenta un panel de Gestión de Clientes, mostrando el listado de registros existentes y las opciones de "Agregar", "Actualizar" y "Eliminar". |
| 3. Selecciona un registro de la tabla marcando el checkbox y escoge la opción "Actualizar". | 4. Presenta un formulario lateral derecho con los datos actuales del cliente:<br>- Código de Cliente (Solo lectura - no editable)<br>- Nombres *<br>- Apellidos *<br>- Cédula * (10 dígitos)<br>- Género * (MASCULINO/FEMENINO)<br>- Fecha de Nacimiento<br>- Teléfono * (10 dígitos)<br>- Email<br>- Dirección<br>- Contacto de Emergencia<br>- Teléfono de Emergencia (10 dígitos)<br>- Estado * (ACTIVO/SUSPENDIDO/INACTIVO)<br><br>Los campos marcados con * son obligatorios. |
| 5. Modifica los datos:<br>- Código: CLI-2024000001 (No editable)<br>- Nombres: Carlos<br>- Apellidos: Mendoza Silva<br>- Cédula: 0912345679<br>- Género: MASCULINO<br>- Fecha Nacimiento: 15/01/1985<br>- Teléfono: 0987654321<br>- Email: carlos.mendoza.nuevo@example.com<br>- Dirección: Calle Nueva #456<br>- Contacto Emergencia: Ana Mendoza<br>- Teléfono Emergencia: 0998765432<br>- Estado: ACTIVO<br><br>Y escoge la opción "Guardar". | 6. Valida los nuevos datos:<br>- Cédula única (excluyendo el cliente actual)<br>- Nombres y Apellidos únicos (excluyendo el cliente actual)<br>- Email único (excluyendo el cliente actual)<br>- Formatos correctos<br><br>Actualiza el registro y presenta un mensaje informativo: "Cliente actualizado exitosamente".<br><br>Cierra el formulario y actualiza la tabla con los datos modificados. |

## Curso alterno de eventos

| # | Evento |
|---|--------|
| 3 | Si no se selecciona un registro antes de escoger la opción "Actualizar", el sistema muestra un mensaje de advertencia: "Por favor seleccione al menos un registro". |
| 3 | Si se seleccionan múltiples registros antes de escoger la opción "Actualizar", el sistema muestra un mensaje de advertencia: "Por favor seleccione solo un registro para actualizar". |
| 5 | Si escoge la opción "Cancelar", el sistema no realiza la actualización de los datos del registro y cierra el formulario. |
| 6 | **Validaciones de campos obligatorios:**<br>1. Si los campos Nombres, Apellidos, Cédula, Género, Teléfono o Estado están en blanco, el sistema presenta un mensaje de error: "El [nombre del campo] es obligatorio".<br>2. Si el campo Email no está vacío y no contiene un dominio válido, el sistema presenta un mensaje de error: "El formato del email no es válido".<br>3. Si el campo Cédula tiene menos de 10 dígitos, el sistema presenta un mensaje de error: "La cédula debe tener exactamente 10 dígitos".<br>4. Si el campo Teléfono tiene menos de 10 dígitos, el sistema presenta un mensaje de error: "El teléfono debe tener 10 dígitos".<br>5. Si el Teléfono de Emergencia no está vacío y tiene menos de 10 dígitos, el sistema presenta un mensaje de error: "El teléfono de emergencia debe tener 10 dígitos".<br><br>**Validaciones de duplicados (excluyendo el cliente actual):**<br>6. Si la Cédula ya existe en otro cliente diferente, el sistema presenta un mensaje de error: "Cédula existente en otro Cliente, verifique o ingrese otro valor".<br>7. Si la combinación Nombres + Apellidos ya existe en otro cliente diferente, el sistema presenta un mensaje de error: "Nombres y Apellidos existentes en otro Cliente, verifique o ingrese otros valores".<br>8. Si el Email ya existe en otro cliente diferente, el sistema presenta un mensaje de error: "Email existente en otro Cliente, verifique o ingrese otro valor".<br><br>**Validaciones de formato:**<br>9. Si los campos Nombres, Apellidos o Contacto de Emergencia contienen caracteres no alfabéticos (números o símbolos), el sistema no permite su ingreso.<br>10. Si los campos Cédula, Teléfono o Teléfono de Emergencia contienen caracteres no numéricos, el sistema no permite su ingreso.<br>11. Si cualquier campo de texto excede los 100 caracteres, el sistema no permite ingresar más caracteres.<br>12. El campo Código no puede ser modificado bajo ninguna circunstancia. |
