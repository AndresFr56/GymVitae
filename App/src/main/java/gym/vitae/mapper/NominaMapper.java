package gym.vitae.mapper;

import gym.vitae.model.Empleado;
import gym.vitae.model.Nomina;
import gym.vitae.model.dtos.nomina.NominaCreateDTO;
import gym.vitae.model.dtos.nomina.NominaDetalleDTO;
import gym.vitae.model.dtos.nomina.NominaListadoDTO;
import gym.vitae.model.dtos.nomina.NominaUpdateDTO;
import java.util.List;
import java.util.stream.Collectors;

public class NominaMapper {

  private NominaMapper() {}

  public static NominaListadoDTO toListadoDTO(Nomina nomina) {
    if (nomina == null) return null;

    String empleadoNombre = null;
    String empleadoCargo = null;

    if (nomina.getEmpleado() != null) {
      empleadoNombre = nomina.getEmpleado().getNombres();
      if (nomina.getEmpleado().getCargo() != null) {
        empleadoCargo = nomina.getEmpleado().getCargo().getNombre();
      }
    }

    return new NominaListadoDTO(
        nomina.getId(),
        nomina.getMes(),
        nomina.getAnio(),
        nomina.getSalarioBase(),
        nomina.getBonificaciones(),
        nomina.getDeducciones(),
        nomina.getTotalPagar(),
        nomina.getFechaPago(),
        nomina.getEstado(),
        empleadoNombre,
        empleadoCargo);
  }

  public static NominaDetalleDTO toDetalleDTO(Nomina nomina) {
    if (nomina == null) return null;

    NominaDetalleDTO dto = new NominaDetalleDTO();
    dto.setId(nomina.getId());
    dto.setMes(nomina.getMes());
    dto.setAnio(nomina.getAnio());
    dto.setSalarioBase(nomina.getSalarioBase());
    dto.setBonificaciones(nomina.getBonificaciones());
    dto.setDeducciones(nomina.getDeducciones());
    dto.setTotalPagar(nomina.getTotalPagar());
    dto.setFechaPago(nomina.getFechaPago());
    dto.setEstado(nomina.getEstado());
    dto.setObservaciones(nomina.getObservaciones());
    dto.setCreatedAt(nomina.getCreatedAt());
    dto.setUpdatedAt(nomina.getUpdatedAt());

    // Empleado info
    if (nomina.getEmpleado() != null) {
      dto.setEmpleadoId(nomina.getEmpleado().getId());
      dto.setEmpleadoNombre(nomina.getEmpleado().getNombres());
      if (nomina.getEmpleado().getCargo() != null) {
        dto.setEmpleadoCargo(nomina.getEmpleado().getCargo().getNombre());
      }
    }

    // Responsables info
    if (nomina.getGeneradaPor() != null) {
      dto.setGeneradaPorNombre(nomina.getGeneradaPor().getNombres());
    }
    if (nomina.getAprobadaPor() != null) {
      dto.setAprobadaPorNombre(nomina.getAprobadaPor().getNombres());
    }
    if (nomina.getPagadaPor() != null) {
      dto.setPagadaPorNombre(nomina.getPagadaPor().getNombres());
    }

    return dto;
  }

  public static Nomina toEntity(NominaCreateDTO dto, Empleado empleado) {
    if (dto == null) return null;

    Nomina nomina = new Nomina();
    nomina.setEmpleado(empleado);
    nomina.setMes(dto.getMes());
    nomina.setAnio(dto.getAnio());
    nomina.setSalarioBase(dto.getSalarioBase());
    nomina.setBonificaciones(dto.getBonificaciones());
    nomina.setDeducciones(dto.getDeducciones());
    nomina.setObservaciones(dto.getObservaciones());

    return nomina;
  }

  public static void updateEntity(
      Nomina nomina, NominaUpdateDTO dto, Empleado aprobadaPor, Empleado pagadaPor) {
    if (nomina == null || dto == null) return;

    if (dto.getEstado() != null) {
      nomina.setEstado(dto.getEstado());
    }
    if (dto.getFechaPago() != null) {
      nomina.setFechaPago(dto.getFechaPago());
    }
    if (dto.getBonificaciones() != null) {
      nomina.setBonificaciones(dto.getBonificaciones());
    }
    if (dto.getDeducciones() != null) {
      nomina.setDeducciones(dto.getDeducciones());
    }
    if (dto.getObservaciones() != null) {
      nomina.setObservaciones(dto.getObservaciones());
    }
    if (aprobadaPor != null) {
      nomina.setAprobadaPor(aprobadaPor);
    }
    if (pagadaPor != null) {
      nomina.setPagadaPor(pagadaPor);
    }
  }

  public static List<NominaListadoDTO> toListadoDTOList(List<Nomina> nominas) {
    if (nominas == null) return List.of();
    return nominas.stream().map(NominaMapper::toListadoDTO).collect(Collectors.toList());
  }
}
