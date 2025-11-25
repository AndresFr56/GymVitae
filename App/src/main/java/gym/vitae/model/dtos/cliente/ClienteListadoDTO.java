package gym.vitae.model.dtos.cliente;

import gym.vitae.model.enums.EstadoCliente;

/**
 * DTO para listado de clientes en tablas. Solo contiene campos esenciales para visualización
 * rápida.
 */
public record ClienteListadoDTO(
    Integer id,
    String codigoCliente,
    String nombreCompleto,
    String cedula,
    String telefono,
    EstadoCliente estado) {}
