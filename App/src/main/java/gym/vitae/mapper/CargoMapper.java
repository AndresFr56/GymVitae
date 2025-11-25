package gym.vitae.mapper;

import gym.vitae.model.Cargo;
import gym.vitae.model.dtos.catalogos.CargoCreateDTO;
import gym.vitae.model.dtos.catalogos.CargoDetalleDTO;
import gym.vitae.model.dtos.catalogos.CargoListadoDTO;
import gym.vitae.model.dtos.catalogos.CargoUpdateDTO;
import java.util.List;

public class CargoMapper {

  private CargoMapper() {}

  public static CargoListadoDTO toListadoDTO(Cargo cargo) {
    if (cargo == null) return null;

    return new CargoListadoDTO(
        cargo.getId(), cargo.getNombre(), cargo.getSalarioBase(), cargo.getActivo());
  }

  public static CargoDetalleDTO toDetalleDTO(Cargo cargo) {
    if (cargo == null) return null;

    CargoDetalleDTO dto = new CargoDetalleDTO();
    dto.setId(cargo.getId());
    dto.setNombre(cargo.getNombre());
    dto.setSalarioBase(cargo.getSalarioBase());
    dto.setDescripcion(cargo.getDescripcion());
    dto.setActivo(cargo.getActivo());
    dto.setCreatedAt(cargo.getCreatedAt());
    dto.setUpdatedAt(cargo.getUpdatedAt());

    return dto;
  }

  public static Cargo toEntity(CargoCreateDTO dto) {
    if (dto == null) return null;

    Cargo cargo = new Cargo();
    cargo.setNombre(dto.getNombre());
    cargo.setSalarioBase(dto.getSalarioBase());
    cargo.setDescripcion(dto.getDescripcion());
    cargo.setActivo(true);

    return cargo;
  }

  public static void updateEntity(Cargo cargo, CargoUpdateDTO dto) {
    if (cargo == null || dto == null) return;

    if (dto.getNombre() != null) {
      cargo.setNombre(dto.getNombre());
    }
    if (dto.getSalarioBase() != null) {
      cargo.setSalarioBase(dto.getSalarioBase());
    }
    if (dto.getDescripcion() != null) {
      cargo.setDescripcion(dto.getDescripcion());
    }
    if (dto.getActivo() != null) {
      cargo.setActivo(dto.getActivo());
    }
  }

  public static List<CargoListadoDTO> toListadoDTOList(List<Cargo> cargos) {
    if (cargos == null) return List.of();
    return cargos.stream().map(CargoMapper::toListadoDTO).toList();
  }
}
