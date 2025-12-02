package gym.vitae.controller;

import static gym.vitae.controller.ValidationUtils.*;

import gym.vitae.mapper.BeneficioMapper;
import gym.vitae.model.Beneficio;
import gym.vitae.model.dtos.membresias.BeneficioCreateDTO;
import gym.vitae.model.dtos.membresias.BeneficioDetalleDTO;
import gym.vitae.model.dtos.membresias.BeneficioListadoDTO;
import gym.vitae.model.dtos.membresias.BeneficioUpdateDTO;
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
        
        if (dto == null || dto.getNombre() == null || dto.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del beneficio es obligatorio");
        }

        // 1. Nombre: Obligatorio, Máximo 50 caracteres
        validateRequiredString(dto.getNombre(), "El nombre del beneficio", 50);

        // 2. Nombre: Solo letras y espacios (Validación manual)
        if (!dto.getNombre().matches("^[a-zA-Z\\s]+$")) {
            throw new IllegalArgumentException("El nombre del beneficio solo debe contener letras y espacios.");
        }
        
        // 3. Descripción: Opcional, Máximo 200 caracteres
        validateOptionalString(dto.getDescripcion(), "La descripción del beneficio", 200);
        
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

        // 1. Nombre: Obligatorio, Máximo 50 caracteres
        validateRequiredString(dto.getNombre(), "El nombre del beneficio", 50);

        // 2. Nombre: Solo letras y espacios (Validación manual)
        if (!dto.getNombre().matches("^[a-zA-Z\\s]+$")) {
            throw new IllegalArgumentException("El nombre del beneficio solo debe contener letras y espacios.");
        }
        
        // 3. Descripción: Opcional, Máximo 200 caracteres
        validateOptionalString(dto.getDescripcion(), "La descripción del beneficio", 200);
        
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