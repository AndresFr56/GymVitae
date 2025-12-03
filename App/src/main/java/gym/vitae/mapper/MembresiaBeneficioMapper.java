package gym.vitae.mapper;

import gym.vitae.model.Beneficio;
import gym.vitae.model.MembresiaBeneficio;
import gym.vitae.model.TiposMembresia;
import gym.vitae.model.dtos.membresias.MembresiaBeneficioCreateDTO;
import gym.vitae.model.dtos.membresias.MembresiaBeneficioDetalleDTO;
import gym.vitae.model.dtos.membresias.MembresiaBeneficioListadoDTO;
import java.util.List;

public class MembresiaBeneficioMapper {

  private MembresiaBeneficioMapper() {}

  public static MembresiaBeneficioListadoDTO toListadoDTO(MembresiaBeneficio membresiaBeneficio) {
    if (membresiaBeneficio == null) return null;

    return new MembresiaBeneficioListadoDTO(
        membresiaBeneficio.getId(),
        membresiaBeneficio.getTipoMembresia() != null
            ? membresiaBeneficio.getTipoMembresia().getNombre()
            : null,
        membresiaBeneficio.getBeneficio() != null
            ? membresiaBeneficio.getBeneficio().getNombre()
            : null);
  }

  public static MembresiaBeneficioDetalleDTO toDetalleDTO(MembresiaBeneficio membresiaBeneficio) {
    if (membresiaBeneficio == null) return null;

    MembresiaBeneficioDetalleDTO dto = new MembresiaBeneficioDetalleDTO();
    dto.setId(membresiaBeneficio.getId());
    dto.setCreatedAt(membresiaBeneficio.getCreatedAt());

    // Tipo Membresía
    if (membresiaBeneficio.getTipoMembresia() != null) {
      dto.setTipoMembresiaId(membresiaBeneficio.getTipoMembresia().getId());
      dto.setTipoMembresiaNombre(membresiaBeneficio.getTipoMembresia().getNombre());
    }

    // Beneficio
    if (membresiaBeneficio.getBeneficio() != null) {
      dto.setBeneficioId(membresiaBeneficio.getBeneficio().getId());
      dto.setBeneficioNombre(membresiaBeneficio.getBeneficio().getNombre());
      dto.setBeneficioDescripcion(membresiaBeneficio.getBeneficio().getDescripcion());
    }

    return dto;
  }

  public static List<MembresiaBeneficioListadoDTO> toListadoDTOList(
      List<MembresiaBeneficio> membresiaBeneficios) {
    if (membresiaBeneficios == null) return List.of();
    return membresiaBeneficios.stream().map(MembresiaBeneficioMapper::toListadoDTO).toList();
  }

  public static MembresiaBeneficio toEntity(
      MembresiaBeneficioCreateDTO dto, TiposMembresia tipoMembresia, Beneficio beneficio) {

    if (dto == null || tipoMembresia == null || beneficio == null) {
      return null;
    }

    MembresiaBeneficio entity = new MembresiaBeneficio();
    entity.setTipoMembresia(tipoMembresia);
    entity.setBeneficio(beneficio);

    return entity;
  }
}
