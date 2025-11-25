package gym.vitae.mapper;

import gym.vitae.model.Cliente;
import gym.vitae.model.Horario;
import gym.vitae.model.InscripcionesClase;
import gym.vitae.model.dtos.horarios.InscripcionClaseCreateDTO;
import gym.vitae.model.dtos.horarios.InscripcionClaseDetalleDTO;
import gym.vitae.model.dtos.horarios.InscripcionClaseListadoDTO;
import gym.vitae.model.dtos.horarios.InscripcionClaseUpdateDTO;
import java.util.List;

public class InscripcionClaseMapper {

  private InscripcionClaseMapper() {}

  public static InscripcionClaseListadoDTO toListadoDTO(InscripcionesClase inscripcion) {
    if (inscripcion == null) return null;

    String clienteNombre = null;
    String clienteDocumento = null;
    String claseNombre = null;
    String instructorNombre = null;

    if (inscripcion.getCliente() != null) {
      clienteNombre = inscripcion.getCliente().getNombres();
      clienteDocumento = inscripcion.getCliente().getCedula();
    }

    if (inscripcion.getHorario() != null) {
      if (inscripcion.getHorario().getClase() != null) {
        claseNombre = inscripcion.getHorario().getClase().getNombre();
      }
      if (inscripcion.getHorario().getInstructor() != null) {
        instructorNombre = inscripcion.getHorario().getInstructor().getNombres();
      }
    }

    return new InscripcionClaseListadoDTO(
        inscripcion.getId(),
        inscripcion.getFechaInscripcion(),
        inscripcion.getEstado(),
        clienteNombre,
        clienteDocumento,
        claseNombre,
        inscripcion.getHorario() != null ? inscripcion.getHorario().getDiaSemana() : null,
        inscripcion.getHorario() != null ? inscripcion.getHorario().getHoraInicio() : null,
        instructorNombre);
  }

  public static InscripcionClaseDetalleDTO toDetalleDTO(InscripcionesClase inscripcion) {
    if (inscripcion == null) return null;

    InscripcionClaseDetalleDTO dto = new InscripcionClaseDetalleDTO();
    dto.setId(inscripcion.getId());
    dto.setFechaInscripcion(inscripcion.getFechaInscripcion());
    dto.setEstado(inscripcion.getEstado());
    dto.setCreatedAt(inscripcion.getCreatedAt());
    dto.setUpdatedAt(inscripcion.getUpdatedAt());

    // Cliente info
    if (inscripcion.getCliente() != null) {
      dto.setClienteId(inscripcion.getCliente().getId());
      dto.setClienteNombre(inscripcion.getCliente().getNombres());
      dto.setClienteDocumento(inscripcion.getCliente().getCedula());
    }

    // Horario info
    if (inscripcion.getHorario() != null) {
      dto.setHorarioId(inscripcion.getHorario().getId());
      dto.setDiaSemana(inscripcion.getHorario().getDiaSemana());
      dto.setHoraInicio(inscripcion.getHorario().getHoraInicio());
      dto.setHoraFin(inscripcion.getHorario().getHoraFin());

      if (inscripcion.getHorario().getClase() != null) {
        dto.setClaseNombre(inscripcion.getHorario().getClase().getNombre());
      }
      if (inscripcion.getHorario().getInstructor() != null) {
        dto.setInstructorNombre(inscripcion.getHorario().getInstructor().getNombres());
      }
    }

    return dto;
  }

  public static InscripcionesClase toEntity(
      InscripcionClaseCreateDTO dto, Horario horario, Cliente cliente) {
    if (dto == null) return null;

    InscripcionesClase inscripcion = new InscripcionesClase();
    inscripcion.setHorario(horario);
    inscripcion.setCliente(cliente);
    inscripcion.setFechaInscripcion(dto.getFechaInscripcion());

    return inscripcion;
  }

  public static void updateEntity(InscripcionesClase inscripcion, InscripcionClaseUpdateDTO dto) {
    if (inscripcion == null || dto == null) return;

    if (dto.getEstado() != null) {
      inscripcion.setEstado(dto.getEstado());
    }
  }

  public static List<InscripcionClaseListadoDTO> toListadoDTOList(
      List<InscripcionesClase> inscripciones) {
    if (inscripciones == null) return List.of();
    return inscripciones.stream().map(InscripcionClaseMapper::toListadoDTO).toList();
  }
}
