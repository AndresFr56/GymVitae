package gym.vitae.controller;

import gym.vitae.repositories.PagoRepository;

public class PagosController extends BaseController {

	private final PagoRepository repository;

	public PagosController() {
		super();
		this.repository = getRepository(PagoRepository.class);
	}

    PagosController(PagoRepository repository) {
        super(null);
        this.repository = repository;
    }

}
