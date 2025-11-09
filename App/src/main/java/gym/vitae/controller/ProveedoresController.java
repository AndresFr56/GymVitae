package gym.vitae.controller;

import gym.vitae.repositories.ProveedoreRepository;

public class ProveedoresController extends BaseController {

	private final ProveedoreRepository repository;

	public ProveedoresController() {
		super();
		this.repository = getRepository(ProveedoreRepository.class);
	}

    ProveedoresController(ProveedoreRepository repository) {
        super(null);
        this.repository = repository;
    }

}
