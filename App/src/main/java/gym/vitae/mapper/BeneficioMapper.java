package gym.vitae.mapper;

import gym.vitae.model.Beneficio;
import gym.vitae.model.dtos.membresias.BeneficioDetalleDTO;
import gym.vitae.model.dtos.membresias.BeneficioListadoDTO;
import java.util.List;

public class BeneficioMapper {

  private BeneficioMapper() {}

  public static BeneficioListadoDTO toListadoDTO(Beneficio beneficio) {
    if (beneficio == null) return null;

    return new BeneficioListadoDTO(
        beneficio.getId(),
        beneficio.getNombre(),
        beneficio.getDescripcion(),
        beneficio.getActivo());
  }

  public static BeneficioDetalleDTO toDetalleDTO(Beneficio beneficio) {
    if (beneficio == null) return null;

    BeneficioDetalleDTO dto = new BeneficioDetalleDTO();
    dto.setId(beneficio.getId());
    dto.setNombre(beneficio.getNombre());
    dto.setDescripcion(beneficio.getDescripcion());
    dto.setActivo(beneficio.getActivo());
    dto.setCreatedAt(beneficio.getCreatedAt());
    dto.setUpdatedAt(beneficio.getUpdatedAt());

    return dto;
  }

  public static List<BeneficioListadoDTO> toListadoDTOList(List<Beneficio> beneficios) {
    if (beneficios == null) return List.of();
    return beneficios.stream().map(BeneficioMapper::toListadoDTO).toList();
  }
}
