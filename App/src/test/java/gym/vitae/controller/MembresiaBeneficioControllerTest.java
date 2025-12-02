package gym.vitae.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

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
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MembresiaBeneficioControllerTest {

    @Mock
    private MembresiaBeneficioRepository repository;
    @Mock
    private TiposMembresiaRepository tiposRepository;
    @Mock
    private BeneficioRepository beneficioRepository;

    @InjectMocks
    private MembresiaBeneficioController controller;

    private MembresiaBeneficio asociacion;
    private TiposMembresia tipo;
    private Beneficio beneficio;
    private MembresiaBeneficioCreateDTO createDTO;
    private MembresiaBeneficioDetalleDTO detalleDTO;
    private MembresiaBeneficioListadoDTO listadoDTO;
    
    @BeforeEach
    void setUp() {
        controller = new MembresiaBeneficioController(repository, tiposRepository, beneficioRepository);
    }

    // --- Tests para Listado y Detalle ---


    @Test
    void getById_DebeLanzarExcepcionSiNoEncontrado() {
        // Arrange
        when(repository.findDetalleById(99)).thenReturn(Optional.empty());

        // Assert
        assertThrows(IllegalArgumentException.class, () -> controller.getById(99));
        verify(repository, times(1)).findDetalleById(99);
    }

    // --- Tests para Crear ---




    // --- Tests para Eliminar ---

    @Test
    void delete_DebeEliminarExitosamente() {
        // Arrange
        when(repository.existsById(10)).thenReturn(true);
        doNothing().when(repository).delete(10);

        // Act & Assert
        assertDoesNotThrow(() -> controller.delete(10));
        verify(repository, times(1)).existsById(10);
        verify(repository, times(1)).delete(10);
    }


}

