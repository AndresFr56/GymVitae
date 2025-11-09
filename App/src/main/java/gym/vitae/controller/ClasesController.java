package gym.vitae.controller;

import gym.vitae.repositories.ClaseRepository;

public class ClasesController extends BaseController {

	private final ClaseRepository repository;

	public ClasesController() {
		super();
		this.repository = getRepository(ClaseRepository.class);
	}

    ClasesController(ClaseRepository repository) {
        super(null);
        this.repository = repository;
    }

}
