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
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.mockito.InjectMocks; 

@ExtendWith(MockitoExtension.class)
@DisplayName("Tests para MembresiaBeneficioController")
class MembresiaBeneficioControllerTest {

    @Mock private MembresiaBeneficioRepository repository;
    @Mock private TiposMembresiaRepository tiposRepository;
    @Mock private BeneficioRepository beneficioRepository;

    @InjectMocks
    private MembresiaBeneficioController controller;

    @BeforeEach
    void setUp() {
    }


    private MembresiaBeneficioCreateDTO createValidDTO() {
        return new MembresiaBeneficioCreateDTO(1, 10); // MembresiaId: 1, BeneficioId: 10
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
        MembresiaBeneficio entity = new MembresiaBeneficio();
        entity.setId(id);
        return entity;
    }

    private MembresiaBeneficioDetalleDTO createDetalleDTO(int id) {
        MembresiaBeneficioDetalleDTO dto = new MembresiaBeneficioDetalleDTO();
        dto.setId(id);
        dto.setTipoMembresiaNombre("Tipo Estandar");
        dto.setBeneficioNombre("Acceso Piscina");
        dto.setTipoMembresiaId(1); 
        dto.setBeneficioId(10);
        return dto;
    }


    @Test
    @DisplayName("RC-CEV-01: Creación de asociación válida")
    void create_validAssociation_success() {
        MembresiaBeneficioCreateDTO dto = createValidDTO();
        int savedId = 5;

        when(tiposRepository.findById(dto.getMembresiaId())).thenReturn(Optional.of(createTipoMembresia(dto.getMembresiaId())));
        when(beneficioRepository.findById(dto.getBeneficioId())).thenReturn(Optional.of(createBeneficio(dto.getBeneficioId())));
        
        MembresiaBeneficio savedEntity = createEntity(savedId);
        when(repository.save(any(MembresiaBeneficio.class))).thenReturn(savedEntity);
        when(repository.findDetalleById(savedId)).thenReturn(Optional.of(createDetalleDTO(savedId)));

        MembresiaBeneficioDetalleDTO result = controller.create(dto);

        assertNotNull(result);
        assertEquals(savedId, result.getId());
        verify(tiposRepository).findById(dto.getMembresiaId());
        verify(beneficioRepository).findById(dto.getBeneficioId());
        verify(repository).save(any(MembresiaBeneficio.class));
    }
    
    @Test
    @DisplayName("RC-CEI-02: Tipo de Membresía no encontrado")
    void create_tipoMembresiaNotFound_throwsException() {
        MembresiaBeneficioCreateDTO dto = createValidDTO();
        when(tiposRepository.findById(dto.getMembresiaId())).thenReturn(Optional.empty()); // No encontrado
        
        IllegalArgumentException ex = assertThrows(
            IllegalArgumentException.class, 
            () -> controller.create(dto)
        );
        assertTrue(ex.getMessage().contains("Tipo de membresía no encontrado"));
        verify(beneficioRepository, never()).findById(anyInt());
        verify(repository, never()).save(any());
    }

    @Test
    @DisplayName("RC-CEI-03: Beneficio no encontrado")
    void create_beneficioNotFound_throwsException() {
        MembresiaBeneficioCreateDTO dto = createValidDTO();
        
        when(tiposRepository.findById(dto.getMembresiaId())).thenReturn(Optional.of(createTipoMembresia(dto.getMembresiaId())));
        when(beneficioRepository.findById(dto.getBeneficioId())).thenReturn(Optional.empty()); // No encontrado
        
        IllegalArgumentException ex = assertThrows(
            IllegalArgumentException.class, 
            () -> controller.create(dto)
        );
        assertTrue(ex.getMessage().contains("Beneficio no encontrado"));
        verify(tiposRepository).findById(dto.getMembresiaId());
        verify(beneficioRepository).findById(dto.getBeneficioId());
        verify(repository, never()).save(any());
    }

    // Eliminación 

    @Test
    void delete_exists_success() {
        int id = 1;
        when(repository.existsById(id)).thenReturn(true);
        doNothing().when(repository).delete(id);
        
        assertDoesNotThrow(() -> controller.delete(id));
        verify(repository).existsById(id);
        verify(repository).delete(id);
    }

    @Test
    void delete_notExists_throwsException() {
        int id = 99;
        when(repository.existsById(id)).thenReturn(false);
        
        assertThrows(IllegalArgumentException.class, () -> controller.delete(id));
        verify(repository).existsById(id);
        verify(repository, never()).delete(anyInt());
    }
}