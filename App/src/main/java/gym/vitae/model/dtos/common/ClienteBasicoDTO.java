package gym.vitae.model.dtos.common;

/**
 * DTO básico de Cliente. Usado como referencia en Facturas, Membresías, etc.
 *
 * @param id ID del cliente
 * @param nombreCompleto Nombre completo (nombres + apellidos)
 * @param cedula Cédula del cliente
 * @param codigoCliente Código único del cliente
 */
public record ClienteBasicoDTO(
    Integer id, String nombreCompleto, String cedula, String codigoCliente) {}
