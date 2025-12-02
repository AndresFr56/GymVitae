package gym.vitae.controller;

import static gym.vitae.controller.ValidationUtils.*;

import gym.vitae.mapper.TipoMembresiaMapper;
import gym.vitae.model.TiposMembresia;
import gym.vitae.model.dtos.membresias.MembresiaBeneficioCreateDTO;
import gym.vitae.model.dtos.membresias.TipoMembresiaCreateDTO;
import gym.vitae.model.dtos.membresias.TipoMembresiaDetalleDTO;
import gym.vitae.model.dtos.membresias.TipoMembresiaListadoDTO;
import gym.vitae.model.dtos.membresias.TipoMembresiaUpdateDTO;
import gym.vitae.repositories.TiposMembresiaRepository;
import java.util.List;

public class TiposMembresiaController extends BaseController {

    private final TiposMembresiaRepository repository;
    private final MembresiaBeneficioController asociacionController;

    public TiposMembresiaController() {
        super();
        this.repository = getRepository(TiposMembresiaRepository.class);
        this.asociacionController = new MembresiaBeneficioController();
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
    
        validateRequiredString(dto.getNombre(), "El nombre del tipo de membresía", 50);

        if (!dto.getNombre().matches("^[a-zA-Z\\s]+$")) {
            throw new IllegalArgumentException("El nombre del tipo de membresía solo debe contener letras y espacios.");
        }

        validateOptionalString(dto.getDescripcion(), "La descripción del tipo de membresía", 255);

        if (dto.getDuracionDias() == null || dto.getDuracionDias() <= 0) {
            throw new IllegalArgumentException("La duración en días es obligatoria y debe ser mayor a 0");
        }

        if (dto.getCosto() == null || dto.getCosto().doubleValue() <= 0) {
            throw new IllegalArgumentException("El costo es obligatorio y debe ser mayor a 0");
        }
        
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
        
        if (dto == null) {
            throw new IllegalArgumentException("Los datos actualizados del tipo de membresía son obligatorios");
        }
        
        validateRequiredString(dto.getNombre(), "El nombre del tipo de membresía", 50);
        
        if (!dto.getNombre().matches("^[a-zA-Z\\s]+$")) {
            throw new IllegalArgumentException("El nombre del tipo de membresía solo debe contener letras y espacios.");
        }


        validateOptionalString(dto.getDescripcion(), "La descripción del tipo de membresía", 255);

        if (dto.getDuracionDias() == null || dto.getDuracionDias() <= 0) {
            throw new IllegalArgumentException("La duración en días es obligatoria y debe ser mayor a 0");
        }

        if (dto.getCosto() == null || dto.getCosto().doubleValue() <= 0) {
            throw new IllegalArgumentException("El costo es obligatorio y debe ser mayor a 0");
        }
        
        TipoMembresiaMapper.updateEntity(tipo, dto);
        repository.update(tipo);

        return repository
            .findDetalleById(id)
            .orElseThrow(() -> new IllegalStateException("Error al recuperar el tipo actualizado"));
    }

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

    public TipoMembresiaDetalleDTO createTipoConBeneficios(TipoMembresiaCreateDTO dto) {
        
        if (dto == null) {
            throw new IllegalArgumentException("Los datos del tipo de membresía son obligatorios");
        }

        validateRequiredString(dto.getNombre(), "El nombre del tipo de membresía", 50);
        
        if (!dto.getNombre().matches("^[a-zA-Z\\s]+$")) {
            throw new IllegalArgumentException("El nombre del tipo de membresía solo debe contener letras y espacios.");
        }

        validateOptionalString(dto.getDescripcion(), "La descripción del tipo de membresía", 255);

        if (dto.getDuracionDias() == null || dto.getDuracionDias() <= 0) {
            throw new IllegalArgumentException("La duración en días es obligatoria y debe ser mayor a 0");
        }

        if (dto.getCosto() == null || dto.getCosto().doubleValue() <= 0) {
            throw new IllegalArgumentException("El costo es obligatorio y debe ser mayor a 0");
        }

        TipoMembresiaCreateDTO baseDto = new TipoMembresiaCreateDTO(
            dto.getNombre(),
            dto.getDescripcion(),
            dto.getDuracionDias(),
            dto.getCosto(),
            dto.getAccesoCompleto(),
            null
        );

        TiposMembresia tipo = TipoMembresiaMapper.toEntity(baseDto);
        TiposMembresia savedTipo = repository.save(tipo);
        Integer nuevoTipoId = savedTipo.getId();

        if (dto.getBeneficiosIds() != null && !dto.getBeneficiosIds().isEmpty()) {
            for (Integer beneficioId : dto.getBeneficiosIds()) {
                try {
                    MembresiaBeneficioCreateDTO asociacionDto = new MembresiaBeneficioCreateDTO(
                        nuevoTipoId,
                        beneficioId
                    );
                    asociacionController.create(asociacionDto); 
                } catch (IllegalArgumentException e) {
                    System.err.println("Advertencia: Beneficio ID " + beneficioId + " no válido o ya asignado. Continuando.");
                }
            }
        }

        return repository
            .findDetalleById(nuevoTipoId)
            .orElseThrow(() -> new IllegalStateException("No se pudo recuperar el tipo creado"));
    }

}