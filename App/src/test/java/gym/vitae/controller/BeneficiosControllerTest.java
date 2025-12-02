package gym.vitae.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

import gym.vitae.model.Beneficio;
import gym.vitae.model.dtos.membresias.BeneficioCreateDTO;
import gym.vitae.model.dtos.membresias.BeneficioDetalleDTO;
import gym.vitae.model.dtos.membresias.BeneficioUpdateDTO;
import gym.vitae.repositories.BeneficioRepository;
import java.util.Optional;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.InjectMocks; 

@ExtendWith(MockitoExtension.class)
@DisplayName("Tests para BeneficiosController")
class BeneficiosControllerTest {

    @Mock
    private BeneficioRepository beneficioRepository;
    
    @InjectMocks
    private BeneficiosController controller;
    
    @BeforeEach
    void setUp() {
        // Inicialización manual de Mockito
        beneficioRepository = mock(BeneficioRepository.class);
        // Uso del constructor de prueba
        controller = new BeneficiosController(beneficioRepository);
    }

    private BeneficioCreateDTO createValidCreateDTO() {
        BeneficioCreateDTO dto = new BeneficioCreateDTO();
        dto.setNombre("Acceso a Sauna");
        dto.setDescripcion("Entrada gratuita a la sauna");
        dto.setActivo(true);
        return dto;
    }
    
    private BeneficioUpdateDTO createValidUpdateDTO() {
        BeneficioUpdateDTO dto = new BeneficioUpdateDTO();
        dto.setNombre("Acceso a Piscina");
        dto.setDescripcion("Acceso a la piscina olímpica");
        dto.setActivo(true);
        return dto;
    }

    private Beneficio createEntity(int id, String nombre) {
        Beneficio b = new Beneficio();
        b.setId(id);
        b.setNombre(nombre);
        b.setActivo(true);
        return b;
    }

    private BeneficioDetalleDTO createDetalleDTO(int id) {
        BeneficioDetalleDTO dto = new BeneficioDetalleDTO();
        dto.setId(id);
        dto.setNombre("Acceso a Sauna");
        dto.setDescripcion("Entrada gratuita a la sauna");
        dto.setActivo(true);
        return dto;
    }

    
    @Test
    void getBeneficioById_encontrado_devuelveDetalle() {
        int id = 1;
        BeneficioDetalleDTO expected = createDetalleDTO(id);
        when(beneficioRepository.findDetalleById(id)).thenReturn(Optional.of(expected));
        
        BeneficioDetalleDTO result = controller.getBeneficioById(id);
        
        assertNotNull(result);
        assertEquals(id, result.getId());
        verify(beneficioRepository).findDetalleById(id);
    }
    
    @Test
    void getBeneficioById_noEncontrado_lanzaExcepcion() {
        int id = 99;
        when(beneficioRepository.findDetalleById(id)).thenReturn(Optional.empty());
        
        assertThrows(IllegalArgumentException.class, () -> controller.getBeneficioById(id));
        verify(beneficioRepository).findDetalleById(id);
    }


    @Test
    @DisplayName(" Creación válida")
    void createBeneficio_validDto_success() {
        BeneficioCreateDTO dto = createValidCreateDTO();
        Beneficio savedEntity = createEntity(1, dto.getNombre());
        BeneficioDetalleDTO expectedDetalle = createDetalleDTO(1);

        when(beneficioRepository.save(any(Beneficio.class))).thenReturn(savedEntity);
        when(beneficioRepository.findDetalleById(1)).thenReturn(Optional.of(expectedDetalle));

        BeneficioDetalleDTO result = controller.createBeneficio(dto);

        assertNotNull(result);
        assertEquals(1, result.getId());
        verify(beneficioRepository).save(any(Beneficio.class));
        verify(beneficioRepository).findDetalleById(1);
    }

    

    @Test
    @DisplayName(" Actualización válida")
    void updateBeneficio_validDto_success() {
        int id = 1;
        BeneficioUpdateDTO updateDto = createValidUpdateDTO(); 
        Beneficio existingEntity = createEntity(id, "Antiguo Nombre");
        BeneficioDetalleDTO expectedDetalle = createDetalleDTO(id);
        
        when(beneficioRepository.findById(id)).thenReturn(Optional.of(existingEntity));
        when(beneficioRepository.findDetalleById(id)).thenReturn(Optional.of(expectedDetalle));
        
        BeneficioDetalleDTO result = controller.updateBeneficio(id, updateDto);
        
        assertNotNull(result);
        verify(beneficioRepository).findById(id);
        verify(beneficioRepository).update(any(Beneficio.class)); 
        verify(beneficioRepository).findDetalleById(id);
    }
    
    @Test
    @DisplayName(" Actualizar beneficio no encontrado")
    void updateBeneficio_beneficioNoEncontrado_throwsException() {
        int id = 99;
        BeneficioUpdateDTO updateDto = createValidUpdateDTO();
        when(beneficioRepository.findById(id)).thenReturn(Optional.empty());
        
        assertThrows(IllegalArgumentException.class, () -> controller.updateBeneficio(id, updateDto));
        verify(beneficioRepository).findById(id);
        verify(beneficioRepository, never()).update(any());
    }

    @Test
    @DisplayName(" Actualizar con nombre inválido (caracteres)")
    void updateBeneficio_nombreInvalidoCaracteres_throwsException() {
        int id = 1;
        BeneficioUpdateDTO updateDto = new BeneficioUpdateDTO();
        updateDto.setNombre("Sauna @");
        
        Beneficio existingEntity = createEntity(id, "Antiguo Nombre");
        when(beneficioRepository.findById(id)).thenReturn(Optional.of(existingEntity));
        
        IllegalArgumentException ex = assertThrows(
            IllegalArgumentException.class, 
            () -> controller.updateBeneficio(id, updateDto)
        );
        assertTrue(ex.getMessage().contains("solo debe contener letras y espacios"));
        verify(beneficioRepository, never()).update(any());
    }


    @Test
    void deleteBeneficio_exists_success() {
        int id = 1;
        when(beneficioRepository.existsById(id)).thenReturn(true);
        doNothing().when(beneficioRepository).delete(id);
        
        assertDoesNotThrow(() -> controller.deleteBeneficio(id));
        verify(beneficioRepository).existsById(id);
        verify(beneficioRepository).delete(id);
    }

    @Test
    void deleteBeneficio_notExists_throwsException() {
        int id = 99;
        when(beneficioRepository.existsById(id)).thenReturn(false);
        
        assertThrows(IllegalArgumentException.class, () -> controller.deleteBeneficio(id));
        verify(beneficioRepository).existsById(id);
        verify(beneficioRepository, never()).delete(anyInt());
    }
}