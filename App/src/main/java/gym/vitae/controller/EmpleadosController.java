package gym.vitae.controller;

import gym.vitae.repositories.EmpleadoRepository;

public class EmpleadosController extends BaseController{

    private final EmpleadoRepository empleadoRepository;

    public EmpleadosController(){
        super();
        this.empleadoRepository = getRepository(EmpleadoRepository.class);
    }

    EmpleadosController(EmpleadoRepository empleadoRepository){
        super(null);
        this.empleadoRepository = empleadoRepository;
    }
}
