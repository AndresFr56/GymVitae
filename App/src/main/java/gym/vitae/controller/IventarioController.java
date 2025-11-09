package gym.vitae.controller;

import gym.vitae.repositories.EquipoRepository;
import gym.vitae.repositories.ProductoRepository;

public class IventarioController extends BaseController{

    private final EquipoRepository equipoRepository;
    private final ProductoRepository productoRepository;


    public IventarioController() {
        super();
        this.equipoRepository = getRepository(EquipoRepository.class);
        this.productoRepository = getRepository(ProductoRepository.class);
    }

    IventarioController(EquipoRepository equipoRepository, ProductoRepository productoRepository){
        super(null);
        this.equipoRepository = equipoRepository;
        this.productoRepository = productoRepository;
    }

}
