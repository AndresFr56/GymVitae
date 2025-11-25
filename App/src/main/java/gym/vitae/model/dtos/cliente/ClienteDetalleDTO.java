package gym.vitae.model.dtos.cliente;

import gym.vitae.model.enums.EstadoCliente;
import gym.vitae.model.enums.Genero;
import java.time.LocalDate;

/**
 * DTO con todos los detalles del cliente. Usado para formularios de edición y vistas de detalle.
 */
public record ClienteDetalleDTO(
    Integer id,
    String codigoCliente,
    String nombres,
    String apellidos,
    String cedula,
    Genero genero,
    String telefono,
    String direccion,
    String email,
    LocalDate fechaNacimiento,
    String contactoEmergencia,
    String telefonoEmergencia,
    EstadoCliente estado) {}
