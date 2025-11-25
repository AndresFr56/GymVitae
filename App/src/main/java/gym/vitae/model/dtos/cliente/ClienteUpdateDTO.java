package gym.vitae.model.dtos.cliente;

import gym.vitae.model.enums.EstadoCliente;
import gym.vitae.model.enums.Genero;
import java.time.LocalDate;

/** DTO para actualización de clientes existentes. Incluye estado pero no código. */
public record ClienteUpdateDTO(
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
