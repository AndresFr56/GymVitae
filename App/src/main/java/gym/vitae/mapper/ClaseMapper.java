package gym.vitae.mapper;

import gym.vitae.model.Clase;
import gym.vitae.model.dtos.clase.ClaseCreateDTO;
import gym.vitae.model.dtos.clase.ClaseDetalleDTO;
import gym.vitae.model.dtos.clase.ClaseListadoDTO;
import gym.vitae.model.dtos.clase.ClaseUpdateDTO;
import gym.vitae.model.enums.NivelClase;
import java.util.List;

/** Mapper para conversiones entre Clase entity y DTOs. */
public final class ClaseMapper {

  private ClaseMapper() {
    throw new UnsupportedOperationException("Utility class");
  }

  /**
   * Convierte Clase a ClaseListadoDTO.
   *
   * @param clase Entity Clase
   * @return ClaseListadoDTO o null si el entity es null
   */
  public static ClaseListadoDTO toListadoDTO(Clase clase) {
    if (clase == null) {
      return null;
    }

    return new ClaseListadoDTO(
        clase.getId(),
        clase.getNombre(),
        clase.getDuracionMinutos(),
        clase.getCapacidadMaxima(),
        clase.getNivel(),
        clase.getActiva());
  }

  /**
   * Convierte lista de Clase a lista de ClaseListadoDTO.
   *
   * @param clases Lista de entities Clase
   * @return Lista de ClaseListadoDTO
   */
  public static List<ClaseListadoDTO> toListadoDTOList(List<Clase> clases) {
    if (clases == null) {
      return List.of();
    }
    return clases.stream().map(ClaseMapper::toListadoDTO).toList();
  }

  /**
   * Convierte Clase a ClaseDetalleDTO.
   *
   * @param clase Entity Clase
   * @return ClaseDetalleDTO o null si el entity es null
   */
  public static ClaseDetalleDTO toDetalleDTO(Clase clase) {
    if (clase == null) {
      return null;
    }

    return new ClaseDetalleDTO(
        clase.getId(),
        clase.getNombre(),
        clase.getDescripcion(),
        clase.getDuracionMinutos(),
        clase.getCapacidadMaxima(),
        clase.getNivel(),
        clase.getActiva(),
        clase.getCreatedAt(),
        clase.getUpdatedAt());
  }

  /**
   * Crea una nueva entidad Clase desde ClaseCreateDTO.
   *
   * @param dto DTO con datos para creación
   * @return Nueva instancia de Clase
   */
  public static Clase toEntity(ClaseCreateDTO dto) {
    if (dto == null) {
      return null;
    }

    Clase clase = new Clase();
    clase.setNombre(dto.nombre());
    clase.setDescripcion(dto.descripcion());
    clase.setDuracionMinutos(dto.duracionMinutos());
    clase.setCapacidadMaxima(dto.capacidadMaxima());
    clase.setNivel(dto.nivel() != null ? dto.nivel() : NivelClase.TODOS);
    clase.setActiva(true);

    return clase;
  }

  /**
   * Actualiza una entidad Clase existente desde ClaseUpdateDTO.
   *
   * @param clase Entity Clase a actualizar
   * @param dto DTO con datos actualizados
   */
  public static void updateEntity(Clase clase, ClaseUpdateDTO dto) {
    if (clase == null || dto == null) {
      return;
    }

    clase.setNombre(dto.nombre());
    clase.setDescripcion(dto.descripcion());
    clase.setDuracionMinutos(dto.duracionMinutos());
    clase.setCapacidadMaxima(dto.capacidadMaxima());
    clase.setNivel(dto.nivel());
    clase.setActiva(dto.activa());
  }
}
