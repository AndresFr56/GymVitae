package gym.vitae.controller;

import gym.vitae.model.Nomina;
import gym.vitae.repositories.IRepository;

public class NominaController extends BaseController {

	private final IRepository<Nomina> repository;

	@SuppressWarnings("unchecked")
	public NominaController() {
		super();
		this.repository = getRepository((Class) IRepository.class);
	}

    NominaController(IRepository<Nomina> repository) {
        super(null);
        this.repository = repository;
    }

}
