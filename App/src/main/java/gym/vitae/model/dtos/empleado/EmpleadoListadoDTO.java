package gym.vitae.model.dtos.empleado;

import gym.vitae.model.enums.EstadoEmpleado;
import gym.vitae.model.enums.Genero;
import gym.vitae.model.enums.TipoContrato;
import java.time.LocalDate;

/**
 * DTO para listar empleados en tablas. Contiene los campos necesarios para mostrar en la UI de
 * listado.
 */
public record EmpleadoListadoDTO(
    Integer id,
    String codigoEmpleado,
    String nombres,
    String apellidos,
    String cedula,
    String telefono,
    Genero genero,
    String email,
    String cargoNombre,
    LocalDate fechaIngreso,
    TipoContrato tipoContrato,
    EstadoEmpleado estado) {}
