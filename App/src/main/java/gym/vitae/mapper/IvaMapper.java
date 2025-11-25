package gym.vitae.mapper;

import gym.vitae.model.Iva;
import gym.vitae.model.dtos.catalogos.IvaCreateUpdateDTO;
import gym.vitae.model.dtos.catalogos.IvaListadoDTO;
import java.util.List;

public class IvaMapper {

  private IvaMapper() {}

  public static IvaListadoDTO toListadoDTO(Iva iva) {
    if (iva == null) return null;

    return new IvaListadoDTO(iva.getId(), iva.getPorcentaje(), iva.getActivo());
  }

  public static Iva toEntity(IvaCreateUpdateDTO dto) {
    if (dto == null) return null;

    Iva iva = new Iva();
    iva.setPorcentaje(dto.getPorcentaje());
    iva.setActivo(dto.getActivo());

    return iva;
  }

  public static void updateEntity(Iva iva, IvaCreateUpdateDTO dto) {
    if (iva == null || dto == null) return;

    if (dto.getPorcentaje() != null) {
      iva.setPorcentaje(dto.getPorcentaje());
    }
    if (dto.getActivo() != null) {
      iva.setActivo(dto.getActivo());
    }
  }

  public static List<IvaListadoDTO> toListadoDTOList(List<Iva> ivas) {
    if (ivas == null) return List.of();
    return ivas.stream().map(IvaMapper::toListadoDTO).toList();
  }
}
