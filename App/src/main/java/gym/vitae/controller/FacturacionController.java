package gym.vitae.controller;

import gym.vitae.repositories.DetallesFacturaRepository;
import gym.vitae.repositories.FacturaRepository;

public class FacturacionController extends BaseController {

  private final FacturaRepository facturaRepo;
  private final DetallesFacturaRepository detallesFactura;

  public FacturacionController() {
    super();
    this.facturaRepo = getRepository(FacturaRepository.class);
    this.detallesFactura = getRepository(DetallesFacturaRepository.class);
  }

  FacturacionController(FacturaRepository facturaRepo, DetallesFacturaRepository membresiaRepo) {
    super(null);
    this.facturaRepo = facturaRepo;
    this.detallesFactura = membresiaRepo;
  }
}
