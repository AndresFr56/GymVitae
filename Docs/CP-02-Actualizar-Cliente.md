| Título | Actualización de cliente |
|--------|--------------------------|
| **Nombre de Caso de Uso** | Actualizar cliente |
| **Número de Caso de Uso** | 02 |
| **Número de prueba** | 1 |
| **Descripción de prueba** | Comprobar si se actualiza correctamente el cliente |
| **Precondición** | Autenticación del usuario Recepcionista y cliente existente en el sistema |
| **Pasos y Condiciones** | 1. Ingresar como recepcionista<br>   Correo Electrónico: "ana.torres@gymvitae.com"<br>   Cédula: 0945678901<br>2. Ir a la sección de Clientes<br>3. Seleccionar un cliente existente de la tabla marcando el checkbox<br>4. Seleccionar la opción Actualizar<br>5. Modificar los datos del cliente:<br>   - Código del cliente: CLI-2024000001 (No editable)<br>   - Nombres: Carlos<br>   - Apellidos: Mendoza Silva<br>   - Cédula: 0912345679<br>   - Género: MASCULINO<br>   - Teléfono: 0987654321<br>   - Email: carlos.mendoza.nuevo@example.com<br>   - Fecha de nacimiento: 15/01/1985<br>   - Dirección: Calle Nueva #456<br>   - Contacto de emergencia: Ana Mendoza<br>   - Teléfono de emergencia: 0998765432<br>   - Estado: ACTIVO<br>6. Seleccionar la opción guardar |
| **Resultados esperados** | Se mostrará un mensaje indicando que se ha actualizado correctamente<br>Se mostrará el cliente con los datos actualizados en el listado<br>El código del cliente no debe cambiar |
| | ☐ Éxito<br>☐ Fallo<br>☐ Error (excepción) |
| **Observación** | |
| **Nombre del experto** | Ricardo Villamar M. |
| **Responsable de diseño** | Ricardo Villamar M. |
