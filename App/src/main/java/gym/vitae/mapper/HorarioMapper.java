package gym.vitae.mapper;

import gym.vitae.model.Clase;
import gym.vitae.model.Empleado;
import gym.vitae.model.Horario;
import gym.vitae.model.dtos.horarios.HorarioCreateDTO;
import gym.vitae.model.dtos.horarios.HorarioDetalleDTO;
import gym.vitae.model.dtos.horarios.HorarioListadoDTO;
import gym.vitae.model.dtos.horarios.HorarioUpdateDTO;
import java.util.List;

public class HorarioMapper {

  private HorarioMapper() {}

  public static HorarioListadoDTO toListadoDTO(Horario horario) {
    if (horario == null) return null;

    return new HorarioListadoDTO(
        horario.getId(),
        horario.getDiaSemana(),
        horario.getHoraInicio(),
        horario.getHoraFin(),
        horario.getCuposDisponibles(),
        horario.getActivo(),
        horario.getClase() != null ? horario.getClase().getNombre() : null,
        horario.getClase() != null ? horario.getClase().getNivel() : null,
        horario.getInstructor() != null ? horario.getInstructor().getNombres() : null);
  }

  public static HorarioDetalleDTO toDetalleDTO(Horario horario) {
    if (horario == null) return null;

    HorarioDetalleDTO dto = new HorarioDetalleDTO();
    dto.setId(horario.getId());
    dto.setDiaSemana(horario.getDiaSemana());
    dto.setHoraInicio(horario.getHoraInicio());
    dto.setHoraFin(horario.getHoraFin());
    dto.setCuposDisponibles(horario.getCuposDisponibles());
    dto.setActivo(horario.getActivo());
    dto.setCreatedAt(horario.getCreatedAt());
    dto.setUpdatedAt(horario.getUpdatedAt());

    // Clase info
    if (horario.getClase() != null) {
      dto.setClaseId(horario.getClase().getId());
      dto.setClaseNombre(horario.getClase().getNombre());
      dto.setClaseNivel(horario.getClase().getNivel());
    }

    // Instructor info
    if (horario.getInstructor() != null) {
      dto.setInstructorId(horario.getInstructor().getId());
      dto.setInstructorNombre(horario.getInstructor().getNombres());
    }

    return dto;
  }

  public static Horario toEntity(HorarioCreateDTO dto, Clase clase, Empleado instructor) {
    if (dto == null) return null;

    Horario horario = new Horario();
    horario.setClase(clase);
    horario.setInstructor(instructor);
    horario.setDiaSemana(dto.getDiaSemana());
    horario.setHoraInicio(dto.getHoraInicio());
    horario.setHoraFin(dto.getHoraFin());
    horario.setCuposDisponibles(dto.getCuposDisponibles());
    horario.setActivo(true);

    return horario;
  }

  public static void updateEntity(
      Horario horario, HorarioUpdateDTO dto, Clase clase, Empleado instructor) {
    if (horario == null || dto == null) return;

    if (clase != null) {
      horario.setClase(clase);
    }
    if (instructor != null) {
      horario.setInstructor(instructor);
    }
    if (dto.getDiaSemana() != null) {
      horario.setDiaSemana(dto.getDiaSemana());
    }
    if (dto.getHoraInicio() != null) {
      horario.setHoraInicio(dto.getHoraInicio());
    }
    if (dto.getHoraFin() != null) {
      horario.setHoraFin(dto.getHoraFin());
    }
    if (dto.getCuposDisponibles() != null) {
      horario.setCuposDisponibles(dto.getCuposDisponibles());
    }
    if (dto.getActivo() != null) {
      horario.setActivo(dto.getActivo());
    }
  }

  public static List<HorarioListadoDTO> toListadoDTOList(List<Horario> horarios) {
    if (horarios == null) return List.of();
    return horarios.stream().map(HorarioMapper::toListadoDTO).toList();
  }
}
