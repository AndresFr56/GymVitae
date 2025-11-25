package gym.vitae.mapper;

import gym.vitae.model.Categoria;
import gym.vitae.model.Equipo;
import gym.vitae.model.dtos.inventario.EquipoCreateDTO;
import gym.vitae.model.dtos.inventario.EquipoDetalleDTO;
import gym.vitae.model.dtos.inventario.EquipoListadoDTO;
import gym.vitae.model.dtos.inventario.EquipoUpdateDTO;
import java.util.List;

public class EquipoMapper {

  private EquipoMapper() {}

  public static EquipoListadoDTO toListadoDTO(Equipo equipo) {
    if (equipo == null) return null;

    return new EquipoListadoDTO(
        equipo.getId(),
        equipo.getCodigo(),
        equipo.getNombre(),
        equipo.getMarca(),
        equipo.getModelo(),
        equipo.getEstado(),
        equipo.getUbicacion(),
        equipo.getCategoria() != null ? equipo.getCategoria().getNombre() : null);
  }

  public static EquipoDetalleDTO toDetalleDTO(Equipo equipo) {
    if (equipo == null) return null;

    EquipoDetalleDTO dto = new EquipoDetalleDTO();
    dto.setId(equipo.getId());
    dto.setCodigo(equipo.getCodigo());
    dto.setNombre(equipo.getNombre());
    dto.setDescripcion(equipo.getDescripcion());
    dto.setMarca(equipo.getMarca());
    dto.setModelo(equipo.getModelo());
    dto.setFechaAdquisicion(equipo.getFechaAdquisicion());
    dto.setCosto(equipo.getCosto());
    dto.setEstado(equipo.getEstado());
    dto.setUbicacion(equipo.getUbicacion());
    dto.setObservaciones(equipo.getObservaciones());
    dto.setCreatedAt(equipo.getCreatedAt());
    dto.setUpdatedAt(equipo.getUpdatedAt());

    // Categoria info
    if (equipo.getCategoria() != null) {
      dto.setCategoriaId(equipo.getCategoria().getId());
      dto.setCategoriaNombre(equipo.getCategoria().getNombre());
    }

    return dto;
  }

  public static Equipo toEntity(EquipoCreateDTO dto, Categoria categoria) {
    if (dto == null) return null;

    Equipo equipo = new Equipo();
    equipo.setCategoria(categoria);
    equipo.setCodigo(dto.getCodigo());
    equipo.setNombre(dto.getNombre());
    equipo.setDescripcion(dto.getDescripcion());
    equipo.setMarca(dto.getMarca());
    equipo.setModelo(dto.getModelo());
    equipo.setFechaAdquisicion(dto.getFechaAdquisicion());
    equipo.setCosto(dto.getCosto());
    equipo.setUbicacion(dto.getUbicacion());
    equipo.setObservaciones(dto.getObservaciones());

    return equipo;
  }

  public static void updateEntity(Equipo equipo, EquipoUpdateDTO dto, Categoria categoria) {
    if (equipo == null || dto == null) return;

    if (categoria != null) {
      equipo.setCategoria(categoria);
    }
    if (dto.getCodigo() != null) {
      equipo.setCodigo(dto.getCodigo());
    }
    if (dto.getNombre() != null) {
      equipo.setNombre(dto.getNombre());
    }
    if (dto.getDescripcion() != null) {
      equipo.setDescripcion(dto.getDescripcion());
    }
    if (dto.getMarca() != null) {
      equipo.setMarca(dto.getMarca());
    }
    if (dto.getModelo() != null) {
      equipo.setModelo(dto.getModelo());
    }
    if (dto.getFechaAdquisicion() != null) {
      equipo.setFechaAdquisicion(dto.getFechaAdquisicion());
    }
    if (dto.getCosto() != null) {
      equipo.setCosto(dto.getCosto());
    }
    if (dto.getEstado() != null) {
      equipo.setEstado(dto.getEstado());
    }
    if (dto.getUbicacion() != null) {
      equipo.setUbicacion(dto.getUbicacion());
    }
    if (dto.getObservaciones() != null) {
      equipo.setObservaciones(dto.getObservaciones());
    }
  }

  public static List<EquipoListadoDTO> toListadoDTOList(List<Equipo> equipos) {
    if (equipos == null) return List.of();
    return equipos.stream().map(EquipoMapper::toListadoDTO).toList();
  }
}
