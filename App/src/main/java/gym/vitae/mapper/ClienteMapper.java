package gym.vitae.mapper;

import gym.vitae.model.Cliente;
import gym.vitae.model.dtos.cliente.ClienteCreateDTO;
import gym.vitae.model.dtos.cliente.ClienteDetalleDTO;
import gym.vitae.model.dtos.cliente.ClienteListadoDTO;
import gym.vitae.model.dtos.cliente.ClienteUpdateDTO;
import gym.vitae.model.dtos.common.ClienteBasicoDTO;
import gym.vitae.model.enums.EstadoCliente;
import java.util.List;

/** Mapper para conversiones entre Cliente entity y DTOs. */
public final class ClienteMapper {

  private ClienteMapper() {
    throw new UnsupportedOperationException("Utility class");
  }

  /**
   * Convierte Cliente a ClienteListadoDTO.
   *
   * @param cliente Entity Cliente
   * @return ClienteListadoDTO o null si el entity es null
   */
  public static ClienteListadoDTO toListadoDTO(Cliente cliente) {
    if (cliente == null) {
      return null;
    }

    String nombreCompleto = cliente.getNombres() + " " + cliente.getApellidos();

    return new ClienteListadoDTO(
        cliente.getId(),
        cliente.getCodigoCliente(),
        nombreCompleto,
        cliente.getCedula(),
        cliente.getTelefono(),
        cliente.getEstado());
  }

  /**
   * Convierte lista de Cliente a lista de ClienteListadoDTO.
   *
   * @param clientes Lista de entities Cliente
   * @return Lista de ClienteListadoDTO
   */
  public static List<ClienteListadoDTO> toListadoDTOList(List<Cliente> clientes) {
    if (clientes == null) {
      return List.of();
    }
    return clientes.stream().map(ClienteMapper::toListadoDTO).toList();
  }

  /**
   * Convierte Cliente a ClienteDetalleDTO.
   *
   * @param cliente Entity Cliente
   * @return ClienteDetalleDTO o null si el entity es null
   */
  public static ClienteDetalleDTO toDetalleDTO(Cliente cliente) {
    if (cliente == null) {
      return null;
    }

    return new ClienteDetalleDTO(
        cliente.getId(),
        cliente.getCodigoCliente(),
        cliente.getNombres(),
        cliente.getApellidos(),
        cliente.getCedula(),
        cliente.getGenero(),
        cliente.getTelefono(),
        cliente.getDireccion(),
        cliente.getEmail(),
        cliente.getFechaNacimiento(),
        cliente.getContactoEmergencia(),
        cliente.getTelefonoEmergencia(),
        cliente.getEstado());
  }

  /**
   * Convierte Cliente a ClienteBasicoDTO.
   *
   * @param cliente Entity Cliente
   * @return ClienteBasicoDTO o null si el entity es null
   */
  public static ClienteBasicoDTO toBasicoDTO(Cliente cliente) {
    if (cliente == null) {
      return null;
    }

    String nombreCompleto = cliente.getNombres() + " " + cliente.getApellidos();

    return new ClienteBasicoDTO(
        cliente.getId(), nombreCompleto, cliente.getCedula(), cliente.getCodigoCliente());
  }

  /**
   * Crea una nueva entidad Cliente desde ClienteCreateDTO.
   *
   * @param dto DTO con datos para creación
   * @param codigoCliente Código generado automáticamente
   * @return Nueva instancia de Cliente
   */
  public static Cliente toEntity(ClienteCreateDTO dto, String codigoCliente) {
    if (dto == null) {
      return null;
    }

    Cliente cliente = new Cliente();
    cliente.setCodigoCliente(codigoCliente);
    cliente.setNombres(dto.nombres());
    cliente.setApellidos(dto.apellidos());
    cliente.setCedula(dto.cedula());
    cliente.setGenero(dto.genero());
    cliente.setTelefono(dto.telefono());
    cliente.setDireccion(dto.direccion());
    cliente.setEmail(dto.email());
    cliente.setFechaNacimiento(dto.fechaNacimiento());
    cliente.setContactoEmergencia(dto.contactoEmergencia());
    cliente.setTelefonoEmergencia(dto.telefonoEmergencia());
    cliente.setEstado(EstadoCliente.ACTIVO);

    return cliente;
  }

  /**
   * Actualiza una entidad Cliente existente desde ClienteUpdateDTO.
   *
   * @param cliente Entity Cliente a actualizar
   * @param dto DTO con datos actualizados
   */
  public static void updateEntity(Cliente cliente, ClienteUpdateDTO dto) {
    if (cliente == null || dto == null) {
      return;
    }

    cliente.setNombres(dto.nombres());
    cliente.setApellidos(dto.apellidos());
    cliente.setCedula(dto.cedula());
    cliente.setGenero(dto.genero());
    cliente.setTelefono(dto.telefono());
    cliente.setDireccion(dto.direccion());
    cliente.setEmail(dto.email());
    cliente.setFechaNacimiento(dto.fechaNacimiento());
    cliente.setContactoEmergencia(dto.contactoEmergencia());
    cliente.setTelefonoEmergencia(dto.telefonoEmergencia());
    cliente.setEstado(dto.estado());
  }
}
