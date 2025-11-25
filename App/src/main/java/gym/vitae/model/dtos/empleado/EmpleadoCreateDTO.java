package gym.vitae.model.dtos.empleado;

import gym.vitae.model.enums.EstadoEmpleado;
import gym.vitae.model.enums.Genero;
import gym.vitae.model.enums.TipoContrato;
import java.time.LocalDate;

/**
 * DTO para crear un nuevo Empleado. Contiene solo los campos necesarios para el registro, sin ID ni
 * campos autogenerados.
 *
 * @param nombres Nombres del empleado
 * @param apellidos Apellidos del empleado
 * @param cedula Cédula
 * @param genero Género
 * @param telefono Teléfono
 * @param direccion Dirección
 * @param email Email
 * @param cargoId ID del cargo a asignar
 * @param tipoContrato Tipo de contrato
 * @param fechaIngreso Fecha de ingreso
 * @param estado Estado inicial (por defecto ACTIVO)
 */
public record EmpleadoCreateDTO(
    String nombres,
    String apellidos,
    String cedula,
    Genero genero,
    String telefono,
    String direccion,
    String email,
    Integer cargoId,
    TipoContrato tipoContrato,
    LocalDate fechaIngreso,
    EstadoEmpleado estado) {}
