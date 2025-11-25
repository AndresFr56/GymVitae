package gym.vitae.mapper;

import gym.vitae.model.Categoria;
import gym.vitae.model.dtos.inventario.CategoriaCreateDTO;
import gym.vitae.model.dtos.inventario.CategoriaDetalleDTO;
import gym.vitae.model.dtos.inventario.CategoriaListadoDTO;
import gym.vitae.model.dtos.inventario.CategoriaUpdateDTO;
import java.util.List;

public class CategoriaMapper {

  private CategoriaMapper() {}

  public static CategoriaListadoDTO toListadoDTO(Categoria categoria) {
    if (categoria == null) return null;

    return new CategoriaListadoDTO(
        categoria.getId(),
        categoria.getNombre(),
        categoria.getTipo(),
        categoria.getSubtipo(),
        categoria.getActivo());
  }

  public static CategoriaDetalleDTO toDetalleDTO(Categoria categoria) {
    if (categoria == null) return null;

    CategoriaDetalleDTO dto = new CategoriaDetalleDTO();
    dto.setId(categoria.getId());
    dto.setNombre(categoria.getNombre());
    dto.setTipo(categoria.getTipo());
    dto.setSubtipo(categoria.getSubtipo());
    dto.setDescripcion(categoria.getDescripcion());
    dto.setActivo(categoria.getActivo());
    dto.setCreatedAt(categoria.getCreatedAt());
    dto.setUpdatedAt(categoria.getUpdatedAt());

    return dto;
  }

  public static Categoria toEntity(CategoriaCreateDTO dto) {
    if (dto == null) return null;

    Categoria categoria = new Categoria();
    categoria.setNombre(dto.getNombre());
    categoria.setTipo(dto.getTipo());
    categoria.setSubtipo(dto.getSubtipo());
    categoria.setDescripcion(dto.getDescripcion());
    categoria.setActivo(true);

    return categoria;
  }

  public static void updateEntity(Categoria categoria, CategoriaUpdateDTO dto) {
    if (categoria == null || dto == null) return;

    if (dto.getNombre() != null) {
      categoria.setNombre(dto.getNombre());
    }
    if (dto.getTipo() != null) {
      categoria.setTipo(dto.getTipo());
    }
    if (dto.getSubtipo() != null) {
      categoria.setSubtipo(dto.getSubtipo());
    }
    if (dto.getDescripcion() != null) {
      categoria.setDescripcion(dto.getDescripcion());
    }
    if (dto.getActivo() != null) {
      categoria.setActivo(dto.getActivo());
    }
  }

  public static List<CategoriaListadoDTO> toListadoDTOList(List<Categoria> categorias) {
    if (categorias == null) return List.of();
    return categorias.stream().map(CategoriaMapper::toListadoDTO).toList();
  }
}
