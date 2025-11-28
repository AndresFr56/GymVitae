package gym.vitae.controller;

import static gym.vitae.controller.ValidationUtils.*;

import gym.vitae.mapper.BeneficioMapper;
import gym.vitae.model.Beneficio;
import gym.vitae.model.dtos.beneficios.BeneficioCreateDTO;
import gym.vitae.model.dtos.beneficios.BeneficioDetalleDTO;
import gym.vitae.model.dtos.beneficios.BeneficioListadoDTO;
import gym.vitae.model.dtos.beneficios.BeneficioUpdateDTO;
import gym.vitae.repositories.BeneficioRepository;
import java.util.List;

public class BeneficiosController extends BaseController {

  private final BeneficioRepository beneficioRepository;

  public BeneficiosController() {
    super();
    this.beneficioRepository = getRepository(BeneficioRepository.class);
  }

  // Listado
  public List<BeneficioListadoDTO> getBeneficios() {
    return beneficioRepository.findAllListado();
  }

  public BeneficioDetalleDTO getBeneficioById(int id) {
    validateId(id);
    return beneficioRepository
        .findDetalleById(id)
        .orElseThrow(() -> new IllegalArgumentException("Beneficio no encontrado con ID: " + id));
  }

  // Crear
  public BeneficioDetalleDTO createBeneficio(BeneficioCreateDTO dto) {
    if (dto == null || isNullOrEmpty(dto.getNombre())) {
      throw new IllegalArgumentException("Los datos del beneficio son inválidos");
    }

    Beneficio entity = BeneficioMapper.toEntity(dto);
    Beneficio saved = beneficioRepository.save(entity);

    return beneficioRepository
        .findDetalleById(saved.getId())
        .orElseThrow(() -> new IllegalStateException("Error al recuperar el beneficio creado"));
  }

  // Actualizar
  public BeneficioDetalleDTO updateBeneficio(int id, BeneficioUpdateDTO dto) {
    validateId(id);

    Beneficio beneficio =
        beneficioRepository
            .findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Beneficio no encontrado con ID: " + id));

    BeneficioMapper.updateEntity(beneficio, dto);
    beneficioRepository.update(beneficio);

    return beneficioRepository
        .findDetalleById(id)
        .orElseThrow(() -> new IllegalStateException("Error al recuperar el beneficio actualizado"));
  }

  // Eliminar
  public void deleteBeneficio(int id) {
    validateId(id);

    if (!beneficioRepository.existsById(id)) {
      throw new IllegalArgumentException("Beneficio no encontrado con ID: " + id);
    }

    beneficioRepository.delete(id);
  }
}
