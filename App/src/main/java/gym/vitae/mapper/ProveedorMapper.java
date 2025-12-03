package gym.vitae.mapper;

import gym.vitae.model.Proveedore;
import gym.vitae.model.dtos.inventario.ProveedorCreateDTO;
import gym.vitae.model.dtos.inventario.ProveedorDetalleDTO;
import gym.vitae.model.dtos.inventario.ProveedorListadoDTO;
import gym.vitae.model.dtos.inventario.ProveedorUpdateDTO;
import java.util.List;

/** Clase para mapear proveedores en sus DTOs y viceversa. */
public class ProveedorMapper {

  private ProveedorMapper() {}

  /**
   * Mapea un proveedor a un DTO de listado.
   *
   * @param proveedor entidad de proveedor
   * @return ProveedorListadoDTO o null si proveedor es null
   */
  public static ProveedorListadoDTO toListadoDTO(Proveedore proveedor) {
    if (proveedor == null) {
      return null;
    }

    return new ProveedorListadoDTO(
        proveedor.getId(),
        proveedor.getCodigo(),
        proveedor.getNombre(),
        proveedor.getContacto(),
        proveedor.getTelefono(),
        proveedor.getEmail(),
        proveedor.getActivo());
  }

  /**
   * Mapea un proveedor en un DTO de detalle.
   *
   * @param proveedor entidad de proveedor
   * @return ProveedorDetalleDTO o null si proveedor es null
   */
  public static ProveedorDetalleDTO toDetalleDTO(Proveedore proveedor) {
    if (proveedor == null) {
      return null;
    }

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

  /**
   * Mapea un DTO de creación en una entidad Proveedore.
   *
   * @param dto datos del proveedor a crear
   * @param codigoProveedor código generado en el controlador para el proveedor
   * @return entidad Proveedore o null si dto es null
   */
  public static Proveedore toEntity(ProveedorCreateDTO dto, String codigoProveedor) {
    if (dto == null) {
      return null;
    }

    Proveedore proveedor = new Proveedore();
    proveedor.setCodigo(codigoProveedor);
    proveedor.setNombre(dto.getNombre());
    proveedor.setContacto(dto.getContacto());
    proveedor.setTelefono(dto.getTelefono());
    proveedor.setEmail(dto.getEmail());
    proveedor.setDireccion(dto.getDireccion());
    proveedor.setActivo(true);

    return proveedor;
  }

  /**
   * Actualiza los campos de una entidad Proveedore según los valores del DTO. Solo los campos no
   * nulos del DTO serán actualizados.
   *
   * @param proveedor entidad existente a modificar
   * @param dto datos actualizados del proveedor
   */
  public static void updateEntity(Proveedore proveedor, ProveedorUpdateDTO dto) {
    if (proveedor == null || dto == null) {
      return;
    }

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

  /**
   * Convierte una lista de entidades Proveedore en DTOs de listado.
   *
   * @param proveedores lista de entidades
   * @return lista de ProveedorListadoDTO; lista vacía si proveedores es null
   */
  public static List<ProveedorListadoDTO> toListadoDTOList(List<Proveedore> proveedores) {
    if (proveedores == null) {
      return List.of();
    }
    return proveedores.stream().map(ProveedorMapper::toListadoDTO).toList();
  }
}
