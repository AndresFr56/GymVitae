package gym.vitae.controller;

import gym.vitae.model.Empleado;
import gym.vitae.repositories.EmpleadoRepository;
import java.util.List;

public class EmpleadosController extends BaseController {

  private final EmpleadoRepository empleadoRepository;

  public EmpleadosController() {
    super();
    this.empleadoRepository = getRepository(EmpleadoRepository.class);
  }

  EmpleadosController(EmpleadoRepository empleadoRepository) {
    super(null);
    this.empleadoRepository = empleadoRepository;
  }

  public List<Empleado> getEmpleados() {
    return empleadoRepository.findAll();
  }
}
