package gym.vitae.model.dtos.empleado;

import gym.vitae.model.enums.EstadoEmpleado;
import gym.vitae.model.enums.Genero;
import gym.vitae.model.enums.TipoContrato;
import java.time.LocalDate;

/**
 * DTO para actualizar un Empleado existente. Similar a CreateDTO pero incluye campos que pueden
 * cambiar en una actualización.
 *
 * @param nombres Nombres del empleado
 * @param apellidos Apellidos del empleado
 * @param cedula Cédula
 * @param genero Género
 * @param telefono Teléfono
 * @param direccion Dirección
 * @param email Email
 * @param cargoId ID del cargo
 * @param tipoContrato Tipo de contrato
 * @param fechaIngreso Fecha de ingreso
 * @param fechaSalida Fecha de salida (puede ser null)
 * @param estado Estado del empleado
 */
public record EmpleadoUpdateDTO(
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
    LocalDate fechaSalida,
    EstadoEmpleado estado) {}
