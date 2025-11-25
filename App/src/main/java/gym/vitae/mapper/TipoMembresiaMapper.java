package gym.vitae.mapper;

import gym.vitae.model.TiposMembresia;
import gym.vitae.model.dtos.membresias.TipoMembresiaCreateDTO;
import gym.vitae.model.dtos.membresias.TipoMembresiaDetalleDTO;
import gym.vitae.model.dtos.membresias.TipoMembresiaListadoDTO;
import gym.vitae.model.dtos.membresias.TipoMembresiaUpdateDTO;
import java.util.List;

public class TipoMembresiaMapper {

  private TipoMembresiaMapper() {}

  public static TipoMembresiaListadoDTO toListadoDTO(TiposMembresia tipoMembresia) {
    if (tipoMembresia == null) return null;

    return new TipoMembresiaListadoDTO(
        tipoMembresia.getId(),
        tipoMembresia.getNombre(),
        tipoMembresia.getDescripcion(),
        tipoMembresia.getDuracionDias(),
        tipoMembresia.getCosto(),
        tipoMembresia.getAccesoCompleto(),
        tipoMembresia.getActivo());
  }

  public static TipoMembresiaDetalleDTO toDetalleDTO(TiposMembresia tipoMembresia) {
    if (tipoMembresia == null) return null;

    TipoMembresiaDetalleDTO dto = new TipoMembresiaDetalleDTO();
    dto.setId(tipoMembresia.getId());
    dto.setNombre(tipoMembresia.getNombre());
    dto.setDescripcion(tipoMembresia.getDescripcion());
    dto.setDuracionDias(tipoMembresia.getDuracionDias());
    dto.setCosto(tipoMembresia.getCosto());
    dto.setAccesoCompleto(tipoMembresia.getAccesoCompleto());
    dto.setActivo(tipoMembresia.getActivo());
    dto.setCreatedAt(tipoMembresia.getCreatedAt());
    dto.setUpdatedAt(tipoMembresia.getUpdatedAt());

    return dto;
  }

  public static TiposMembresia toEntity(TipoMembresiaCreateDTO dto) {
    if (dto == null) return null;

    TiposMembresia tipoMembresia = new TiposMembresia();
    tipoMembresia.setNombre(dto.getNombre());
    tipoMembresia.setDescripcion(dto.getDescripcion());
    tipoMembresia.setDuracionDias(dto.getDuracionDias());
    tipoMembresia.setCosto(dto.getCosto());
    tipoMembresia.setAccesoCompleto(
        dto.getAccesoCompleto() != null ? dto.getAccesoCompleto() : true);

    return tipoMembresia;
  }

  public static void updateEntity(TiposMembresia tipoMembresia, TipoMembresiaUpdateDTO dto) {
    if (tipoMembresia == null || dto == null) return;

    if (dto.getNombre() != null) {
      tipoMembresia.setNombre(dto.getNombre());
    }
    if (dto.getDescripcion() != null) {
      tipoMembresia.setDescripcion(dto.getDescripcion());
    }
    if (dto.getDuracionDias() != null) {
      tipoMembresia.setDuracionDias(dto.getDuracionDias());
    }
    if (dto.getCosto() != null) {
      tipoMembresia.setCosto(dto.getCosto());
    }
    if (dto.getAccesoCompleto() != null) {
      tipoMembresia.setAccesoCompleto(dto.getAccesoCompleto());
    }
    if (dto.getActivo() != null) {
      tipoMembresia.setActivo(dto.getActivo());
    }
  }

  public static List<TipoMembresiaListadoDTO> toListadoDTOList(List<TiposMembresia> tipos) {
    if (tipos == null) return List.of();
    return tipos.stream().map(TipoMembresiaMapper::toListadoDTO).toList();
  }
}
