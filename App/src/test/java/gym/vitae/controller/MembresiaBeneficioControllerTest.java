package gym.vitae.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

import gym.vitae.model.Beneficio;
import gym.vitae.model.MembresiaBeneficio;
import gym.vitae.model.TiposMembresia;
import gym.vitae.model.dtos.membresias.MembresiaBeneficioCreateDTO;
import gym.vitae.model.dtos.membresias.MembresiaBeneficioDetalleDTO;
import gym.vitae.repositories.BeneficioRepository;
import gym.vitae.repositories.MembresiaBeneficioRepository;
import gym.vitae.repositories.TiposMembresiaRepository;
import java.util.Optional;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks; 
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("Tests para MembresiaBeneficioController")
class MembresiaBeneficioControllerTest {

    @Mock private MembresiaBeneficioRepository repository;
    @Mock private TiposMembresiaRepository tiposRepository;
    @Mock private BeneficioRepository beneficioRepository;

    // CORRECCIÓN: @InjectMocks se encarga de inyectar los Mocks de arriba
    @InjectMocks
    private MembresiaBeneficioController controller;

    // **NOTA:** Se eliminó el constructor de prueba manual.

    // --- Data de prueba simulada ---
    
    private MembresiaBeneficioCreateDTO createValidDTO() {
        return new MembresiaBeneficioCreateDTO(1, 10);
    }

    private TiposMembresia createTipoMembresia(int id) {
        TiposMembresia t = new TiposMembresia();
        t.setId(id);
        return t;
    }
    
    private Beneficio createBeneficio(int id) {
        Beneficio b = new Beneficio();
        b.setId(id);
        return b;
    }
    
    private MembresiaBeneficio createEntity(int id) {
        MembresiaBeneficio e = new MembresiaBeneficio();
        e.setId(id);
        return e;
    }
    
    private MembresiaBeneficioDetalleDTO createDetalleDTO(int id) {
        MembresiaBeneficioDetalleDTO dto = new MembresiaBeneficioDetalleDTO();
        dto.setId(id);
        return dto;
    }

    // --- Tests de Creación ---

    @Test
    void create_validDto_success() {
        // Arrange
        MembresiaBeneficioCreateDTO dto = createValidDTO();
        MembresiaBeneficio savedEntity = createEntity(1);
        MembresiaBeneficioDetalleDTO expectedDetalle = createDetalleDTO(1);
        
        when(tiposRepository.findById(dto.getMembresiaId())).thenReturn(Optional.of(createTipoMembresia(dto.getMembresiaId())));
        when(beneficioRepository.findById(dto.getBeneficioId())).thenReturn(Optional.of(createBeneficio(dto.getBeneficioId())));
        when(repository.save(any(MembresiaBeneficio.class))).thenReturn(savedEntity);
        when(repository.findDetalleById(1)).thenReturn(Optional.of(expectedDetalle));
        
        // Act
        MembresiaBeneficioDetalleDTO result = controller.create(dto);
        
        // Assert
        assertNotNull(result);
        assertEquals(1, result.getId());
        verify(repository).save(any(MembresiaBeneficio.class));
        verify(repository).findDetalleById(1);
    }
    
    @Test
    void create_tiposMembresiaNotFound_throwsException() {
        // Arrange
        MembresiaBeneficioCreateDTO dto = createValidDTO();
        
        when(tiposRepository.findById(dto.getMembresiaId())).thenReturn(Optional.empty()); // No encontrado
        
        // Act & Assert
        IllegalArgumentException ex = assertThrows(
            IllegalArgumentException.class, 
            () -> controller.create(dto)
        );
        assertTrue(ex.getMessage().contains("Tipo de membresía no encontrado"));
        verify(tiposRepository).findById(dto.getMembresiaId());
        verify(beneficioRepository, never()).findById(anyInt());
        verify(repository, never()).save(any());
    }
    
    @Test
    void create_beneficioNotFound_throwsException() {
        // Arrange
        MembresiaBeneficioCreateDTO dto = createValidDTO();
        
        when(tiposRepository.findById(dto.getMembresiaId())).thenReturn(Optional.of(createTipoMembresia(dto.getMembresiaId())));
        when(beneficioRepository.findById(dto.getBeneficioId())).thenReturn(Optional.empty()); // No encontrado
        
        // Act & Assert
        IllegalArgumentException ex = assertThrows(
            IllegalArgumentException.class, 
            () -> controller.create(dto)
        );
        assertTrue(ex.getMessage().contains("Beneficio no encontrado"));
        verify(tiposRepository).findById(dto.getMembresiaId());
        verify(beneficioRepository).findById(dto.getBeneficioId());
        verify(repository, never()).save(any());
    }

    // --- Tests de Eliminación ---

    @Test
    void delete_exists_success() {
        // Arrange
        int id = 1;
        when(repository.existsById(id)).thenReturn(true);
        doNothing().when(repository).delete(id);
        
        // Act & Assert
        assertDoesNotThrow(() -> controller.delete(id));
        verify(repository).existsById(id);
        verify(repository).delete(id);
    }

    @Test
    void delete_notExists_throwsException() {
        // Arrange
        int id = 99;
        when(repository.existsById(id)).thenReturn(false);
        
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> controller.delete(id));
        verify(repository).existsById(id);
        verify(repository, never()).delete(anyInt());
    }
}