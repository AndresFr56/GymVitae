package gym.vitae.controller;

import gym.vitae.repositories.CargoRepository;
import gym.vitae.repositories.EmpleadoRepository;

public class PersonalController extends BaseController {

  private final EmpleadoRepository empleadoRepository;
  private final CargoRepository cargoRepository;

  public PersonalController() {
    super();
    this.empleadoRepository = getRepository(EmpleadoRepository.class);
    this.cargoRepository = getRepository(CargoRepository.class);
  }

  PersonalController(EmpleadoRepository empleadoRepository, CargoRepository cargoRepository) {
    super(null);
    this.empleadoRepository = empleadoRepository;
    this.cargoRepository = cargoRepository;
  }
}
