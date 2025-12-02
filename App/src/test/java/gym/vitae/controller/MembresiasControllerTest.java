package gym.vitae.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

import gym.vitae.model.Cliente;
import gym.vitae.model.DetallesFactura;
import gym.vitae.model.Empleado;
import gym.vitae.model.Factura;
import gym.vitae.model.Membresia;
import gym.vitae.model.TiposMembresia;
import gym.vitae.model.dtos.membresias.MembresiaCreateDTO;
import gym.vitae.model.dtos.membresias.MembresiaDetalleDTO;
import gym.vitae.model.dtos.membresias.MembresiaUpdateDTO;
import gym.vitae.model.enums.EstadoFactura;
import gym.vitae.model.enums.EstadoMembresia;
import gym.vitae.repositories.ClienteRepository;
import gym.vitae.repositories.DetallesFacturaRepository;
import gym.vitae.repositories.FacturaRepository;
import gym.vitae.repositories.MembresiaRepository;
import gym.vitae.repositories.TiposMembresiaRepository;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("Tests para MembresiasController")
class MembresiasControllerTest {

    @Mock private MembresiaRepository membresiaRepository;
    @Mock private ClienteRepository clienteRepository;
    @Mock private TiposMembresiaRepository tiposMembresiaRepository;
    @Mock private FacturaRepository facturaRepository;
    @Mock private DetallesFacturaRepository detallesFacturaRepository;
    @Mock private AuthController authController;
    
    // CORRECCIÓN: @InjectMocks se encarga de inyectar los Mocks de arriba
    @InjectMocks
    private MembresiasController controller; 
    
    @BeforeEach
        void setUp() {
            controller = new MembresiasController(
                membresiaRepository, 
                clienteRepository, 
                tiposMembresiaRepository, 
                facturaRepository, 
                detallesFacturaRepository, 
                authController
            );
        }
    
    private MembresiaCreateDTO createValidDTO() {
        return new MembresiaCreateDTO(
            10, 
            1, 
            null, 
            LocalDate.now(), 
            LocalDate.now().plusMonths(1), 
            new BigDecimal("60.00"),
            null
        );
    }
    
    private Cliente createCliente(int id) {
        Cliente c = new Cliente();
        c.setId(id);
        return c;
    }
    
    private TiposMembresia createTipoMembresia(int id) {
        TiposMembresia t = new TiposMembresia();
        t.setId(id);
        return t;
    }
    
    private Empleado createEmpleado(int id) {
        Empleado e = new Empleado();
        e.setId(id);
        return e;
    }
    
    private MembresiaDetalleDTO createDetalleDTO(int id) {
        MembresiaDetalleDTO dto = new MembresiaDetalleDTO();
        dto.setId(id);
        dto.setClienteNombre("Nombre Cliente");
        dto.setTipoMembresiaNombre("Nombre Tipo");
        dto.setFechaInicio(LocalDate.now());
        dto.setFechaFin(LocalDate.now().plusMonths(1));
        dto.setPrecioPagado(new BigDecimal("60.00"));
        dto.setEstado(EstadoMembresia.ACTIVA);
        return dto;
    }

    // --- Tests de Creación (Validaciones) ---

    @Test
    @DisplayName(" Creación válida y flujo completo de factura")
    void createMembresia_validDto_success() {
        // Arrange
        MembresiaCreateDTO dto = createValidDTO();
        int savedMembresiaId = 1;
        int savedFacturaId = 5;
        
        // Mocks de existencias
        when(clienteRepository.findById(dto.getClienteId())).thenReturn(Optional.of(createCliente(dto.getClienteId())));
        when(tiposMembresiaRepository.findById(dto.getTipoMembresiaId())).thenReturn(Optional.of(createTipoMembresia(dto.getTipoMembresiaId())));
        when(authController.getEmpleadoActual()).thenReturn(createEmpleado(50));
        
        // Mocks de guardado (Factura -> Membresia)
        Factura savedFactura = new Factura();
        savedFactura.setId(savedFacturaId);
        when(facturaRepository.save(any(Factura.class))).thenReturn(savedFactura);
        
        Membresia savedMembresia = new Membresia();
        savedMembresia.setId(savedMembresiaId);
        when(membresiaRepository.save(any(Membresia.class))).thenReturn(savedMembresia);
        
        // Mock de retorno final
        when(membresiaRepository.findDetalleById(savedMembresiaId)).thenReturn(Optional.of(createDetalleDTO(savedMembresiaId)));

        // Act
        MembresiaDetalleDTO result = controller.createMembresia(dto);

        // Assert
        assertNotNull(result);
        assertEquals(savedMembresiaId, result.getId());
        
        verify(facturaRepository).save(any(Factura.class));
        verify(detallesFacturaRepository).save(any(DetallesFactura.class));
        verify(membresiaRepository).save(any(Membresia.class));
        verify(membresiaRepository).findDetalleById(savedMembresiaId);
    }
    
    @Test
    @DisplayName("Fechas de inicio/fin nulas")
    void createMembresia_nullDates_throwsException() {
        // Arrange
        MembresiaCreateDTO dto = createValidDTO();
        dto.setFechaInicio(null);
        dto.setFechaFin(null);
        
        // Act & Assert
        IllegalArgumentException ex = assertThrows(
            IllegalArgumentException.class, 
            () -> controller.createMembresia(dto)
        );
        assertTrue(ex.getMessage().contains("Las fechas de inicio y fin son obligatorias"));
        verify(membresiaRepository, never()).save(any());
    }
    
    



    @Test
    @DisplayName(" Cliente no encontrado")
    void createMembresia_clienteNotFound_throwsException() {
        // Arrange
        MembresiaCreateDTO dto = createValidDTO();
        when(clienteRepository.findById(dto.getClienteId())).thenReturn(Optional.empty());
        
        // Act & Assert
        IllegalArgumentException ex = assertThrows(
            IllegalArgumentException.class, 
            () -> controller.createMembresia(dto)
        );
        assertTrue(ex.getMessage().contains("Cliente no encontrado"));
    }

    @Test
    @DisplayName(" Empleado no logueado")
    void createMembresia_noLoggedInEmployee_throwsException() {
        // Arrange
        MembresiaCreateDTO dto = createValidDTO();
        
        when(clienteRepository.findById(dto.getClienteId())).thenReturn(Optional.of(createCliente(dto.getClienteId())));
        when(tiposMembresiaRepository.findById(dto.getTipoMembresiaId())).thenReturn(Optional.of(createTipoMembresia(dto.getTipoMembresiaId())));
        when(authController.getEmpleadoActual()).thenReturn(null);
        
        // Act & Assert
        IllegalStateException ex = assertThrows(
            IllegalStateException.class, 
            () -> controller.createMembresia(dto)
        );
        assertTrue(ex.getMessage().contains("No hay un empleado logueado"));
        verify(membresiaRepository, never()).save(any());
    }

    // --- Tests de Actualización ---

    @Test
    @DisplayName(" Actualización válida")
    void updateMembresia_validDto_success() {
        // Arrange
        int id = 1;
        
        MembresiaUpdateDTO updateDto = new MembresiaUpdateDTO();
        updateDto.setFechaFin(LocalDate.now().plusMonths(2));
        updateDto.setEstado(EstadoMembresia.ACTIVA); 
        updateDto.setObservaciones(null); 

        Membresia existingMembresia = new Membresia();
        existingMembresia.setId(id);
        
        when(membresiaRepository.findById(id)).thenReturn(Optional.of(existingMembresia));
        when(membresiaRepository.findDetalleById(id)).thenReturn(Optional.of(createDetalleDTO(id)));
        
        // Act
        MembresiaDetalleDTO result = controller.updateMembresia(id, updateDto);
        
        // Assert
        assertNotNull(result);
        verify(membresiaRepository).findById(id);
        verify(membresiaRepository).update(any(Membresia.class));
        verify(membresiaRepository).findDetalleById(id);
    }
    
    // --- Tests de Cancelación ---
    
    @Test
    void cancelarMembresia_exists_success() {
        // Arrange
        int id = 1;
        when(membresiaRepository.existsById(id)).thenReturn(true);
        doNothing().when(membresiaRepository).delete(id);
        
        // Act & Assert
        assertDoesNotThrow(() -> controller.cancelarMembresia(id));
        verify(membresiaRepository).existsById(id);
        verify(membresiaRepository).delete(id);
    }

    @Test
    void cancelarMembresia_notExists_throwsException() {
        // Arrange
        int id = 99;
        when(membresiaRepository.existsById(id)).thenReturn(false);
        
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> controller.cancelarMembresia(id));
        verify(membresiaRepository).existsById(id);
        verify(membresiaRepository, never()).delete(anyInt());
    }
}