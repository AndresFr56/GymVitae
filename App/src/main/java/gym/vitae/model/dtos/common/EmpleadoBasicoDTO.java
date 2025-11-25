package gym.vitae.model.dtos.common;

/**
 * DTO básico de Empleado. Usado como referencia en Facturas, Nóminas, etc. Sin información
 * sensible.
 *
 * @param id ID del empleado
 * @param nombreCompleto Nombre completo (nombres + apellidos)
 * @param codigoEmpleado Código único del empleado
 */
public record EmpleadoBasicoDTO(Integer id, String nombreCompleto, String codigoEmpleado) {}
