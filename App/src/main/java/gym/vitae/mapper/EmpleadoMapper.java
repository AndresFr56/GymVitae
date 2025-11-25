package gym.vitae.mapper;

import gym.vitae.model.Cargo;
import gym.vitae.model.Empleado;
import gym.vitae.model.dtos.common.CargoDTO;
import gym.vitae.model.dtos.common.EmpleadoBasicoDTO;
import gym.vitae.model.dtos.empleado.EmpleadoAuthDTO;
import gym.vitae.model.dtos.empleado.EmpleadoCreateDTO;
import gym.vitae.model.dtos.empleado.EmpleadoDetalleDTO;
import gym.vitae.model.dtos.empleado.EmpleadoListadoDTO;
import gym.vitae.model.dtos.empleado.EmpleadoUpdateDTO;
import java.util.List;

/**
 * Mapper para convertir entre entidades Empleado y sus DTOs. Centraliza la lógica de conversión
 * para mantener consistencia.
 */
public class EmpleadoMapper {

  private EmpleadoMapper() {
    // Utility class
  }

  /**
   * Convierte Empleado a EmpleadoListadoDTO.
   *
   * @param empleado Entidad Empleado
   * @return DTO de listado
   */
  public static EmpleadoListadoDTO toListadoDTO(Empleado empleado) {
    if (empleado == null) {
      return null;
    }

    return new EmpleadoListadoDTO(
        empleado.getId(),
        empleado.getCodigoEmpleado(),
        empleado.getNombres(),
        empleado.getApellidos(),
        empleado.getCedula(),
        empleado.getTelefono(),
        empleado.getGenero(),
        empleado.getEmail(),
        empleado.getCargo() != null ? empleado.getCargo().getNombre() : null,
        empleado.getFechaIngreso(),
        empleado.getTipoContrato(),
        empleado.getEstado());
  }

  /**
   * Convierte lista de Empleado a lista de EmpleadoListadoDTO.
   *
   * @param empleados Lista de entidades
   * @return Lista de DTOs de listado
   */
  public static List<EmpleadoListadoDTO> toListadoDTOList(List<Empleado> empleados) {
    if (empleados == null) {
      return List.of();
    }
    return empleados.stream().map(EmpleadoMapper::toListadoDTO).toList();
  }

  /**
   * Convierte Empleado a EmpleadoAuthDTO para autenticación.
   *
   * @param empleado Entidad Empleado
   * @return DTO de autenticación
   */
  public static EmpleadoAuthDTO toAuthDTO(Empleado empleado) {
    if (empleado == null) {
      return null;
    }

    CargoDTO cargoDTO = null;
    if (empleado.getCargo() != null) {
      cargoDTO =
          new CargoDTO(
              empleado.getCargo().getId(),
              empleado.getCargo().getNombre(),
              empleado.getCargo().getSalarioBase());
    }

    return new EmpleadoAuthDTO(
        empleado.getId(),
        empleado.getNombres() + " " + empleado.getApellidos(),
        empleado.getEmail(),
        empleado.getCedula(),
        cargoDTO,
        empleado.getEstado());
  }

  /**
   * Convierte Empleado a EmpleadoDetalleDTO.
   *
   * @param empleado Entidad Empleado
   * @return DTO de detalle
   */
  public static EmpleadoDetalleDTO toDetalleDTO(Empleado empleado) {
    if (empleado == null) {
      return null;
    }

    CargoDTO cargoDTO = null;
    if (empleado.getCargo() != null) {
      cargoDTO =
          new CargoDTO(
              empleado.getCargo().getId(),
              empleado.getCargo().getNombre(),
              empleado.getCargo().getSalarioBase());
    }

    return new EmpleadoDetalleDTO(
        empleado.getId(),
        empleado.getCodigoEmpleado(),
        empleado.getNombres(),
        empleado.getApellidos(),
        empleado.getCedula(),
        empleado.getGenero(),
        empleado.getTelefono(),
        empleado.getDireccion(),
        empleado.getEmail(),
        cargoDTO,
        empleado.getTipoContrato(),
        empleado.getFechaIngreso(),
        empleado.getFechaSalida(),
        empleado.getEstado());
  }

  /**
   * Convierte Empleado a EmpleadoBasicoDTO.
   *
   * @param empleado Entidad Empleado
   * @return DTO básico
   */
  public static EmpleadoBasicoDTO toBasicoDTO(Empleado empleado) {
    if (empleado == null) {
      return null;
    }

    return new EmpleadoBasicoDTO(
        empleado.getId(),
        empleado.getNombres() + " " + empleado.getApellidos(),
        empleado.getCodigoEmpleado());
  }

  /**
   * Crea una entidad Empleado desde EmpleadoCreateDTO.
   *
   * @param dto DTO de creación
   * @param cargo Entidad Cargo a asignar
   * @param codigoEmpleado Código generado para el empleado
   * @return Nueva entidad Empleado
   */
  public static Empleado toEntity(EmpleadoCreateDTO dto, Cargo cargo, String codigoEmpleado) {
    if (dto == null) {
      return null;
    }

    Empleado empleado = new Empleado();
    empleado.setCodigoEmpleado(codigoEmpleado);
    empleado.setNombres(dto.nombres());
    empleado.setApellidos(dto.apellidos());
    empleado.setCedula(dto.cedula());
    empleado.setGenero(dto.genero());
    empleado.setTelefono(dto.telefono());
    empleado.setDireccion(dto.direccion());
    empleado.setEmail(dto.email());
    empleado.setCargo(cargo);
    empleado.setTipoContrato(dto.tipoContrato());
    empleado.setFechaIngreso(dto.fechaIngreso());
    empleado.setEstado(dto.estado());

    return empleado;
  }

  /**
   * Actualiza una entidad Empleado desde EmpleadoUpdateDTO.
   *
   * @param empleado Entidad a actualizar
   * @param dto DTO con los nuevos datos
   * @param cargo Nuevo cargo a asignar
   */
  public static void updateEntity(Empleado empleado, EmpleadoUpdateDTO dto, Cargo cargo) {
    if (empleado == null || dto == null) {
      return;
    }

    empleado.setNombres(dto.nombres());
    empleado.setApellidos(dto.apellidos());
    empleado.setCedula(dto.cedula());
    empleado.setGenero(dto.genero());
    empleado.setTelefono(dto.telefono());
    empleado.setDireccion(dto.direccion());
    empleado.setEmail(dto.email());
    empleado.setCargo(cargo);
    empleado.setTipoContrato(dto.tipoContrato());
    empleado.setFechaIngreso(dto.fechaIngreso());
    empleado.setFechaSalida(dto.fechaSalida());
    empleado.setEstado(dto.estado());
  }
}
