package gym.vitae.model.dtos.empleado;

import gym.vitae.model.dtos.common.CargoDTO;
import gym.vitae.model.enums.EstadoEmpleado;
import gym.vitae.model.enums.Genero;
import gym.vitae.model.enums.TipoContrato;
import java.time.LocalDate;

/**
 * DTO de detalle completo de Empleado. Usado en formularios de visualización/edición. Incluye toda
 * la información relevante sin las relaciones OneToMany.
 *
 * @param id ID del empleado
 * @param codigoEmpleado Código único del empleado
 * @param nombres Nombres del empleado
 * @param apellidos Apellidos del empleado
 * @param cedula Cédula
 * @param genero Género
 * @param telefono Teléfono
 * @param direccion Dirección
 * @param email Email
 * @param cargo Información del cargo
 * @param tipoContrato Tipo de contrato
 * @param fechaIngreso Fecha de ingreso
 * @param fechaSalida Fecha de salida (si aplica)
 * @param estado Estado actual
 */
public record EmpleadoDetalleDTO(
    Integer id,
    String codigoEmpleado,
    String nombres,
    String apellidos,
    String cedula,
    Genero genero,
    String telefono,
    String direccion,
    String email,
    CargoDTO cargo,
    TipoContrato tipoContrato,
    LocalDate fechaIngreso,
    LocalDate fechaSalida,
    EstadoEmpleado estado) {}
