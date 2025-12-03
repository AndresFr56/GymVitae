package gym.vitae.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

import gym.vitae.mapper.TipoMembresiaMapper;
import gym.vitae.model.TiposMembresia;
import gym.vitae.model.dtos.membresias.MembresiaBeneficioCreateDTO;
import gym.vitae.model.dtos.membresias.TipoMembresiaCreateDTO;
import gym.vitae.model.dtos.membresias.TipoMembresiaDetalleDTO;
import gym.vitae.model.dtos.membresias.TipoMembresiaUpdateDTO;
import gym.vitae.repositories.TiposMembresiaRepository;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
// import org.mockito.InjectMocks; // <--- ELIMINADO
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("Tests para TiposMembresiaController")
class TiposMembresiaControllerTest {

    @Mock
    private TiposMembresiaRepository repository;
    
    @Mock
    private MembresiaBeneficioController asociacionController;
    
    @Mock 
    private TipoMembresiaMapper mapper;

    // Se usa una declaración simple, ya que se inicializa en setUp()
    private TiposMembresiaController controller;
    
    @BeforeEach
    void setUp() {
        // CORRECCIÓN CLAVE: Asegurar que el 'mapper' sea pasado al constructor
        controller = new TiposMembresiaController(repository, asociacionController, mapper);
    }


    private TipoMembresiaCreateDTO createValidDTO() {
        TipoMembresiaCreateDTO dto = new TipoMembresiaCreateDTO();
        dto.setNombre("Premium");
        dto.setDescripcion("Acceso total");
        dto.setDuracionDias(30);
        
        // Usar setCosto
        dto.setCosto(new BigDecimal("100.00")); 
        
        dto.setAccesoCompleto(true);
        // Usar un listado de beneficios por defecto para que las pruebas funcionen
        dto.setBeneficiosIds(List.of(10, 20)); 
        return dto;
    }
    
    private TiposMembresia createEntity(int id, String nombre) {
        TiposMembresia entity = new TiposMembresia();
        entity.setId(id);
        entity.setNombre(nombre);
        return entity;
    }

    private TipoMembresiaDetalleDTO createDetalleDTO(int id) {
        TipoMembresiaDetalleDTO dto = new TipoMembresiaDetalleDTO();
        dto.setId(id);
        dto.setNombre("Premium");
        dto.setDescripcion("Acceso total");
        dto.setDuracionDias(30);
        dto.setCosto(new BigDecimal("100.00")); // Costo consistente
        dto.setAccesoCompleto(true);
        dto.setActivo(true); 
        return dto;
    }

    // --- Listado y Búsqueda ---
    
    @Test
    void getTipoById_encontrado_devuelveDetalle() {
        int id = 1;
        TipoMembresiaDetalleDTO expected = createDetalleDTO(id);
        when(repository.findDetalleById(id)).thenReturn(Optional.of(expected));
        
        TipoMembresiaDetalleDTO result = controller.getTipoById(id);
        
        assertNotNull(result);
        assertEquals(id, result.getId());
        verify(repository).findDetalleById(id);
    }
    
    @Test
    void getTipoById_noEncontrado_lanzaExcepcion() {
        int id = 99;
        when(repository.findDetalleById(id)).thenReturn(Optional.empty());
        
        assertThrows(IllegalArgumentException.class, () -> controller.getTipoById(id));
        verify(repository).findDetalleById(id);
    }



    
    @ParameterizedTest(name = " Nombre solo letras y espacios: {0}")
    @ValueSource(strings = {"Mensual 123", "Basica-Plus", "Premium!"})
    void createTipo_nombreInvalidoCaracteres_throwsException(String nombre) {
        TipoMembresiaCreateDTO dto = createValidDTO();
        dto.setNombre(nombre); 

        IllegalArgumentException ex = assertThrows(
            IllegalArgumentException.class, 
            () -> controller.createTipo(dto)
        );
        assertTrue(ex.getMessage().contains("solo debe contener letras y espacios"));
        verify(repository, never()).save(any());
    }
    
    @ParameterizedTest(name = " Duración en días inválida: {0}")
    @ValueSource(ints = {0, -5})
    @NullSource
    void createTipo_duracionInvalida_throwsException(Integer dias) {
        TipoMembresiaCreateDTO dto = createValidDTO();
        dto.setDuracionDias(dias); 

        IllegalArgumentException ex = assertThrows(
            IllegalArgumentException.class, 
            () -> controller.createTipo(dto)
        );
        assertTrue(ex.getMessage().contains("La duración en días es obligatoria y debe ser mayor a 0"));
        verify(repository, never()).save(any());
    }

    @Test
    @DisplayName(" Costo inválido (cero)")
    void createTipo_costoCero_throwsException() {
        TipoMembresiaCreateDTO dto = createValidDTO();
        dto.setCosto(BigDecimal.ZERO); 

        IllegalArgumentException ex = assertThrows(
            IllegalArgumentException.class, 
            () -> controller.createTipo(dto)
        );
        assertTrue(ex.getMessage().contains("El costo es obligatorio y debe ser mayor a 0"));
        verify(repository, never()).save(any());
    }


    @Test
    @DisplayName(" Actualización válida")
    void updateTipo_validDto_success() {
        int id = 1;
        // Usando el constructor de TipoMembresiaUpdateDTO
        TipoMembresiaUpdateDTO updateDto = new TipoMembresiaUpdateDTO(
            "Premium Actualizado", "Descripcion", 90, new BigDecimal("100.00"), true, true 
        );
        TiposMembresia existingEntity = createEntity(id, "Antiguo Nombre");
        TipoMembresiaDetalleDTO expectedDetalle = createDetalleDTO(id);
        
        when(repository.findById(id)).thenReturn(Optional.of(existingEntity));
        // Se asume que el controller usa el mapper.update
        when(repository.findDetalleById(id)).thenReturn(Optional.of(expectedDetalle)); 
        
        TipoMembresiaDetalleDTO result = controller.updateTipo(id, updateDto);
        
        assertNotNull(result);
        assertEquals(id, result.getId());
        verify(repository).findById(id);
        verify(repository).update(any(TiposMembresia.class)); 
        verify(repository).findDetalleById(id);
    }
    
    @Test
    @DisplayName(" Actualizar tipo no encontrado")
    void updateTipo_tipoNoEncontrado_throwsException() {
        int id = 99;
        TipoMembresiaUpdateDTO updateDto = new TipoMembresiaUpdateDTO("Nombre", "Desc", 30, new BigDecimal("50"), true, true);
        when(repository.findById(id)).thenReturn(Optional.empty());
        
        assertThrows(IllegalArgumentException.class, () -> controller.updateTipo(id, updateDto));
        verify(repository).findById(id);
        verify(repository, never()).update(any());
    }

    @Test
    @DisplayName(" Actualizar con nombre inválido (caracteres)")
    void updateTipo_nombreInvalidoCaracteres_throwsException() {
        int id = 1;
        TipoMembresiaUpdateDTO updateDto = new TipoMembresiaUpdateDTO("Mensual!", "Desc", 30, new BigDecimal("50"), true, true);
        TiposMembresia existingEntity = createEntity(id, "Antiguo Nombre");
        when(repository.findById(id)).thenReturn(Optional.of(existingEntity));
        
        IllegalArgumentException ex = assertThrows(
            IllegalArgumentException.class, 
            () -> controller.updateTipo(id, updateDto)
        );
        assertTrue(ex.getMessage().contains("solo debe contener letras y espacios"));
        verify(repository, never()).update(any());
    }


    @Test
    void deleteTipo_exists_success() {
        int id = 1;
        when(repository.existsById(id)).thenReturn(true);
        doNothing().when(repository).delete(id);
        
        assertDoesNotThrow(() -> controller.deleteTipo(id));
        verify(repository).existsById(id);
        verify(repository).delete(id);
    }

    @Test
    void deleteTipo_notExists_throwsException() {
        int id = 99;
        when(repository.existsById(id)).thenReturn(false);
        
        assertThrows(IllegalArgumentException.class, () -> controller.deleteTipo(id));
        verify(repository).existsById(id);
        verify(repository, never()).delete(anyInt());
    }

    

}