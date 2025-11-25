package gym.vitae.model.dtos.clase;

import gym.vitae.model.enums.NivelClase;

/**
 * DTO para crear una nueva Clase.
 *
 * @param nombre Nombre de la clase
 * @param descripcion Descripción de la clase (opcional)
 * @param duracionMinutos Duración en minutos
 * @param capacidadMaxima Capacidad máxima de participantes
 * @param nivel Nivel de dificultad
 */
public record ClaseCreateDTO(
    String nombre,
    String descripcion,
    Integer duracionMinutos,
    Integer capacidadMaxima,
    NivelClase nivel) {}
