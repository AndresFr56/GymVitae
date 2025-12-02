package gym.vitae.controller;

import static gym.vitae.controller.ValidationUtils.*;

import gym.vitae.mapper.TipoMembresiaMapper;
import gym.vitae.model.TiposMembresia;
import gym.vitae.model.dtos.membresias.TipoMembresiaCreateDTO;
import gym.vitae.model.dtos.membresias.TipoMembresiaDetalleDTO;
import gym.vitae.model.dtos.membresias.TipoMembresiaListadoDTO;
import gym.vitae.model.dtos.membresias.TipoMembresiaUpdateDTO;
import gym.vitae.repositories.TiposMembresiaRepository;
import java.util.List;

public class TiposMembresiaController extends BaseController {

  private final TiposMembresiaRepository repository;

  public TiposMembresiaController() {
    super();
    this.repository = getRepository(TiposMembresiaRepository.class);
  }

  // Listado
  public List<TipoMembresiaListadoDTO> getTipos() {
    return repository.findAllListado();
  }

  public TipoMembresiaDetalleDTO getTipoById(int id) {
    validateId(id);
    return repository
        .findDetalleById(id)
        .orElseThrow(() -> new IllegalArgumentException("Tipo de membresía no encontrado: " + id));
  }
  
  // Crear
  public TipoMembresiaDetalleDTO createTipo(TipoMembresiaCreateDTO dto) {
  
      if (dto == null) {
          throw new IllegalArgumentException("Los datos del tipo de membresía son obligatorios");
      }
  
      validateRequiredString(dto.getNombre(), "El nombre del tipo de membresía", 100);
  
      TiposMembresia tipo = TipoMembresiaMapper.toEntity(dto);
      TiposMembresia saved = repository.save(tipo);
  
      return repository
          .findDetalleById(saved.getId())
          .orElseThrow(() -> new IllegalStateException("No se pudo recuperar el tipo creado"));
  }


  // Actualizar
  public TipoMembresiaDetalleDTO updateTipo(int id, TipoMembresiaUpdateDTO dto) {
    validateId(id);

    TiposMembresia tipo =
        repository
            .findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Tipo no encontrado: " + id));

    TipoMembresiaMapper.updateEntity(tipo, dto);
    repository.update(tipo);

    return repository
        .findDetalleById(id)
        .orElseThrow(() -> new IllegalStateException("Error al recuperar el tipo actualizado"));
  }

  // Eliminar
  public void deleteTipo(int id) {
    validateId(id);

    if (!repository.existsById(id)) {
      throw new IllegalArgumentException("Tipo no encontrado: " + id);
    }

    repository.delete(id);
  }

  public List<TipoMembresiaListadoDTO> getPaged(int page, int size) {
    int offset = page * size;
    try {
        return repository.findAllListadoPaginated(offset, size);
    } catch (Exception e) {
        return repository.findAllListado();
    }}

}
