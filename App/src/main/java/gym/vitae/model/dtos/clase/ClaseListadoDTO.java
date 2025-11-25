package gym.vitae.model.dtos.clase;

import gym.vitae.model.enums.NivelClase;

/**
 * DTO para listar clases en tablas. Contiene los campos necesarios para mostrar en la UI de
 * listado.
 *
 * @param id ID de la clase
 * @param nombre Nombre de la clase
 * @param duracionMinutos Duración en minutos
 * @param capacidadMaxima Capacidad máxima de participantes
 * @param nivel Nivel de dificultad
 * @param activa Estado activo/inactivo
 */
public record ClaseListadoDTO(
    Integer id,
    String nombre,
    Integer duracionMinutos,
    Integer capacidadMaxima,
    NivelClase nivel,
    Boolean activa) {}
