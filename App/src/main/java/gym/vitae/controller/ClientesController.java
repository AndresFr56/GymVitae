package gym.vitae.controller;

import gym.vitae.repositories.ClienteRepository;

public class ClientesController extends BaseController {

	private final ClienteRepository repository;

	public ClientesController() {
		super();
		this.repository = getRepository(ClienteRepository.class);
	}

    ClientesController(ClienteRepository repository) {
        super(null);
        this.repository = repository;
    }

}
