package gym.vitae.mapper;

import gym.vitae.model.Proveedore;
import gym.vitae.model.dtos.inventario.ProveedorCreateDTO;
import gym.vitae.model.dtos.inventario.ProveedorDetalleDTO;
import gym.vitae.model.dtos.inventario.ProveedorListadoDTO;
import gym.vitae.model.dtos.inventario.ProveedorUpdateDTO;
import java.util.List;

public class ProveedorMapper {

  private ProveedorMapper() {}

  public static ProveedorListadoDTO toListadoDTO(Proveedore proveedor) {
    if (proveedor == null) return null;

    return new ProveedorListadoDTO(
        proveedor.getId(),
        proveedor.getCodigo(),
        proveedor.getNombre(),
        proveedor.getContacto(),
        proveedor.getTelefono(),
        proveedor.getEmail(),
        proveedor.getActivo());
  }

  public static ProveedorDetalleDTO toDetalleDTO(Proveedore proveedor) {
    if (proveedor == null) return null;

    ProveedorDetalleDTO dto = new ProveedorDetalleDTO();
    dto.setId(proveedor.getId());
    dto.setCodigo(proveedor.getCodigo());
    dto.setNombre(proveedor.getNombre());
    dto.setContacto(proveedor.getContacto());
    dto.setTelefono(proveedor.getTelefono());
    dto.setEmail(proveedor.getEmail());
    dto.setDireccion(proveedor.getDireccion());
    dto.setActivo(proveedor.getActivo());
    dto.setCreatedAt(proveedor.getCreatedAt());
    dto.setUpdatedAt(proveedor.getUpdatedAt());

    return dto;
  }

  public static Proveedore toEntity(ProveedorCreateDTO dto) {
    if (dto == null) return null;

    Proveedore proveedor = new Proveedore();
    proveedor.setCodigo(dto.getCodigo());
    proveedor.setNombre(dto.getNombre());
    proveedor.setContacto(dto.getContacto());
    proveedor.setTelefono(dto.getTelefono());
    proveedor.setEmail(dto.getEmail());
    proveedor.setDireccion(dto.getDireccion());
    proveedor.setActivo(true);

    return proveedor;
  }

  public static void updateEntity(Proveedore proveedor, ProveedorUpdateDTO dto) {
    if (proveedor == null || dto == null) return;

    if (dto.getCodigo() != null) {
      proveedor.setCodigo(dto.getCodigo());
    }
    if (dto.getNombre() != null) {
      proveedor.setNombre(dto.getNombre());
    }
    if (dto.getContacto() != null) {
      proveedor.setContacto(dto.getContacto());
    }
    if (dto.getTelefono() != null) {
      proveedor.setTelefono(dto.getTelefono());
    }
    if (dto.getEmail() != null) {
      proveedor.setEmail(dto.getEmail());
    }
    if (dto.getDireccion() != null) {
      proveedor.setDireccion(dto.getDireccion());
    }
    if (dto.getActivo() != null) {
      proveedor.setActivo(dto.getActivo());
    }
  }

  public static List<ProveedorListadoDTO> toListadoDTOList(List<Proveedore> proveedores) {
    if (proveedores == null) return List.of();
    return proveedores.stream().map(ProveedorMapper::toListadoDTO).toList();
  }
}
