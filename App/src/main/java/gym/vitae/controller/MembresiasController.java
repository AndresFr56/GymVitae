package gym.vitae.controller;

import gym.vitae.repositories.MembresiaRepository;

public class MembresiasController extends BaseController {

  private final MembresiaRepository repository;

  public MembresiasController() {
    super();
    this.repository = getRepository(MembresiaRepository.class);
  }

  MembresiasController(MembresiaRepository repository) {
    super(null);
    this.repository = repository;
  }
}
