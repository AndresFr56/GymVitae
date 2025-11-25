package gym.vitae.model.dtos.cliente;

import gym.vitae.model.enums.Genero;
import java.time.LocalDate;

/** DTO para creación de nuevos clientes. No incluye ID ni código (son generados). */
public record ClienteCreateDTO(
    String nombres,
    String apellidos,
    String cedula,
    Genero genero,
    String telefono,
    String direccion,
    String email,
    LocalDate fechaNacimiento,
    String contactoEmergencia,
    String telefonoEmergencia) {}
