package gym.vitae.controller;

import static gym.vitae.controller.ValidationUtils.*;

import gym.vitae.mapper.MembresiaBeneficioMapper;
import gym.vitae.model.Beneficio;
import gym.vitae.model.Membresia;
import gym.vitae.model.MembresiaBeneficio;
import gym.vitae.model.dtos.membresias.MembresiaBeneficioCreateDTO;
import gym.vitae.model.dtos.membresias.MembresiaBeneficioDetalleDTO;
import gym.vitae.model.dtos.membresias.MembresiaBeneficioListadoDTO;
import gym.vitae.repositories.BeneficioRepository;
import gym.vitae.repositories.MembresiaBeneficioRepository;
import gym.vitae.repositories.MembresiaRepository;
import java.util.List;

public class MembresiaBeneficioController extends BaseController {

  private final MembresiaBeneficioRepository repository;
  private final MembresiaRepository membresiaRepository;
  private final BeneficioRepository beneficioRepository;

  public MembresiaBeneficioController() {
    super();
    this.repository = getRepository(MembresiaBeneficioRepository.class);
    this.membresiaRepository = getRepository(MembresiaRepository.class);
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

    Membresia membresia =
        membresiaRepository
            .findById(dto.getMembresiaId())
            .orElseThrow(() -> new IllegalArgumentException("Membresía no encontrada"));

    Beneficio beneficio =
        beneficioRepository
            .findById(dto.getBeneficioId())
            .orElseThrow(() -> new IllegalArgumentException("Beneficio no encontrado"));

    MembresiaBeneficio entity =
        MembresiaBeneficioMapper.toEntity(dto, membresia, beneficio);

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
