package gym.vitae.model.dtos.empleado;

import gym.vitae.model.dtos.common.CargoDTO;
import gym.vitae.model.enums.EstadoEmpleado;

/**
 * DTO para autenticación. Contiene solo los datos necesarios para el proceso de login y
 * verificación de permisos.
 *
 * @param id ID del empleado
 * @param nombreCompleto Nombre completo (nombres + apellidos)
 * @param email Email del empleado
 * @param cedula Cédula (usada como contraseña)
 * @param cargo Información del cargo (para validar permisos)
 * @param estado Estado del empleado
 */
public record EmpleadoAuthDTO(
    Integer id,
    String nombreCompleto,
    String email,
    String cedula,
    CargoDTO cargo,
    EstadoEmpleado estado) {}
