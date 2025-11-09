package gym.vitae.controller;

import gym.vitae.repositories.InscripcionesClaseRepository;

public class InscripcionesController extends BaseController {

  private final InscripcionesClaseRepository repository;

  public InscripcionesController() {
    super();
    this.repository = getRepository(InscripcionesClaseRepository.class);
  }

  InscripcionesController(InscripcionesClaseRepository repository) {
    super(null);
    this.repository = repository;
  }
}
