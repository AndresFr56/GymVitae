package gym.vitae.model.dtos.clase;

import gym.vitae.model.enums.NivelClase;
import java.time.Instant;

/**
 * DTO de detalle completo de Clase. Usado en formularios de visualización/edición.
 *
 * @param id ID de la clase
 * @param nombre Nombre de la clase
 * @param descripcion Descripción de la clase
 * @param duracionMinutos Duración en minutos
 * @param capacidadMaxima Capacidad máxima de participantes
 * @param nivel Nivel de dificultad
 * @param activa Estado activo/inactivo
 * @param createdAt Fecha de creación
 * @param updatedAt Fecha de última actualización
 */
public record ClaseDetalleDTO(
    Integer id,
    String nombre,
    String descripcion,
    Integer duracionMinutos,
    Integer capacidadMaxima,
    NivelClase nivel,
    Boolean activa,
    Instant createdAt,
    Instant updatedAt) {}
