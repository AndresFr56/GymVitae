package gym.vitae.controller;

import gym.vitae.repositories.ClienteRepository;

public class ClienteController extends BaseController{

    private final ClienteRepository clienteRepository;

    public ClienteController(){
        super();
        this.clienteRepository = getRepository(ClienteRepository.class);
    }

    ClienteController(ClienteRepository clienteRepository){
        super(null);
        this.clienteRepository = clienteRepository;
    }

}
