package gym.vitae.controller;

import static gym.vitae.controller.ValidationUtils.*;

import gym.vitae.mapper.MembresiaBeneficioMapper;
import gym.vitae.model.Beneficio;
import gym.vitae.model.MembresiaBeneficio;
import gym.vitae.model.TiposMembresia;
import gym.vitae.model.dtos.membresias.MembresiaBeneficioCreateDTO;
import gym.vitae.model.dtos.membresias.MembresiaBeneficioDetalleDTO;
import gym.vitae.model.dtos.membresias.MembresiaBeneficioListadoDTO;
import gym.vitae.repositories.BeneficioRepository;
import gym.vitae.repositories.MembresiaBeneficioRepository;
import gym.vitae.repositories.TiposMembresiaRepository;
import java.util.List;

public class MembresiaBeneficioController extends BaseController {

  private final MembresiaBeneficioRepository repository;
  private final TiposMembresiaRepository tiposRepository;
  private final BeneficioRepository beneficioRepository;

  public MembresiaBeneficioController() {
    super();
    this.repository = getRepository(MembresiaBeneficioRepository.class);
    this.tiposRepository = getRepository(TiposMembresiaRepository.class);
    this.beneficioRepository = getRepository(BeneficioRepository.class);
  }

  // Listar todas
  public List<MembresiaBeneficioListadoDTO> getAll() {
    return repository.findAllListado();
  }

  // Listar por ID
  public MembresiaBeneficioDetalleDTO getById(int id) {
    validateId(id);
    return repository
        .findDetalleById(id)
        .orElseThrow(() -> new IllegalArgumentException("Asociación no encontrada: " + id));
  }

  // Crear asociación
  public MembresiaBeneficioDetalleDTO create(MembresiaBeneficioCreateDTO dto) {

    validateId(dto.getMembresiaId());
    validateId(dto.getBeneficioId());

    TiposMembresia tipo =
        tiposRepository
            .findById(dto.getMembresiaId())
            .orElseThrow(() -> new IllegalArgumentException("Tipo de membresía no encontrado"));

    Beneficio beneficio =
        beneficioRepository
            .findById(dto.getBeneficioId())
            .orElseThrow(() -> new IllegalArgumentException("Beneficio no encontrado"));

    MembresiaBeneficio entity =
        MembresiaBeneficioMapper.toEntity(dto, tipo, beneficio);

    MembresiaBeneficio saved = repository.save(entity);

    return repository
        .findDetalleById(saved.getId())
        .orElseThrow(() -> new IllegalStateException("Error al recuperar la asociación creada"));
  }

  // Eliminar
  public void delete(int id) {
    validateId(id);

    if (!repository.existsById(id)) {
      throw new IllegalArgumentException("Asociación no encontrada");
    }

    repository.delete(id);
  }
}
