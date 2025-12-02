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
import gym.vitae.model.enums.EstadoMembresia; // Importación necesaria
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
    
    @InjectMocks
    private MembresiasController controller; 
  
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


    @Test
    @DisplayName("RC-CEV-01: Creación válida y flujo completo de factura")
    void createMembresia_validDto_success() {
        MembresiaCreateDTO dto = createValidDTO();
        int savedMembresiaId = 1;
        int savedFacturaId = 5;
        
        when(clienteRepository.findById(dto.getClienteId())).thenReturn(Optional.of(createCliente(dto.getClienteId())));
        when(tiposMembresiaRepository.findById(dto.getTipoMembresiaId())).thenReturn(Optional.of(createTipoMembresia(dto.getTipoMembresiaId())));
        when(authController.getEmpleadoActual()).thenReturn(createEmpleado(50));
        
        Factura savedFactura = new Factura();
        savedFactura.setId(savedFacturaId);
        when(facturaRepository.save(any(Factura.class))).thenReturn(savedFactura);
        
        Membresia savedMembresia = new Membresia();
        savedMembresia.setId(savedMembresiaId);
        when(membresiaRepository.save(any(Membresia.class))).thenReturn(savedMembresia);
        
        when(membresiaRepository.findDetalleById(savedMembresiaId)).thenReturn(Optional.of(createDetalleDTO(savedMembresiaId)));

        MembresiaDetalleDTO result = controller.createMembresia(dto);

        assertNotNull(result);
        assertEquals(savedMembresiaId, result.getId());
        
        verify(facturaRepository).save(any(Factura.class));
        verify(detallesFacturaRepository).save(any(DetallesFactura.class));
        verify(membresiaRepository).save(any(Membresia.class));
        verify(membresiaRepository).findDetalleById(savedMembresiaId);
    }
    
    @Test
    @DisplayName("RC-CEI-02: Fechas de inicio/fin nulas")
    void createMembresia_nullDates_throwsException() {
        MembresiaCreateDTO dto = createValidDTO();
        dto.setFechaInicio(null);
        dto.setFechaFin(null);
        
        IllegalArgumentException ex = assertThrows(
            IllegalArgumentException.class, 
            () -> controller.createMembresia(dto)
        );
        assertTrue(ex.getMessage().contains("Las fechas de inicio y fin son obligatorias"));
        verify(membresiaRepository, never()).save(any());
    }

    @Test
    @DisplayName("RC-CEI-03: Fecha de inicio es posterior a fecha de fin")
    void createMembresia_inicioAfterFin_throwsException() {
        MembresiaCreateDTO dto = createValidDTO();
        dto.setFechaInicio(LocalDate.now().plusDays(10));
        dto.setFechaFin(LocalDate.now().plusDays(5)); 
        
        when(clienteRepository.findById(dto.getClienteId())).thenReturn(Optional.of(createCliente(dto.getClienteId())));
        when(tiposMembresiaRepository.findById(dto.getTipoMembresiaId())).thenReturn(Optional.of(createTipoMembresia(dto.getTipoMembresiaId())));
        when(authController.getEmpleadoActual()).thenReturn(createEmpleado(50));
        
        IllegalArgumentException ex = assertThrows(
            IllegalArgumentException.class, 
            () -> controller.createMembresia(dto)
        );
        assertTrue(ex.getMessage().contains("La fecha de inicio no puede ser posterior a la fecha de fin"));
        verify(membresiaRepository, never()).save(any());
    }
    
    @ParameterizedTest(name = "RC-CEI-04: Precio pagado inválido: {0}")
    @ValueSource(doubles = {0.0, -10.0})
    void createMembresia_invalidPrecioPagado_throwsException(double precio) {
        MembresiaCreateDTO dto = createValidDTO();
        dto.setPrecioPagado(BigDecimal.valueOf(precio));
        
        IllegalArgumentException ex = assertThrows(
            IllegalArgumentException.class, 
            () -> controller.createMembresia(dto)
        );
        assertTrue(ex.getMessage().contains("El precio pagado debe ser mayor a 0"));
        verify(membresiaRepository, never()).save(any());
    }
    
    @Test
    @DisplayName("RC-CEI-05: Cliente no encontrado")
    void createMembresia_clienteNotFound_throwsException() {
        MembresiaCreateDTO dto = createValidDTO();
        when(clienteRepository.findById(dto.getClienteId())).thenReturn(Optional.empty());
        
        IllegalArgumentException ex = assertThrows(
            IllegalArgumentException.class, 
            () -> controller.createMembresia(dto)
        );
        assertTrue(ex.getMessage().contains("Cliente no encontrado"));
    }

    @Test
    @DisplayName("RC-CEI-06: Empleado no logueado")
    void createMembresia_noLoggedInEmployee_throwsException() {
        MembresiaCreateDTO dto = createValidDTO();
        
        when(clienteRepository.findById(dto.getClienteId())).thenReturn(Optional.of(createCliente(dto.getClienteId())));
        when(tiposMembresiaRepository.findById(dto.getTipoMembresiaId())).thenReturn(Optional.of(createTipoMembresia(dto.getTipoMembresiaId())));
        when(authController.getEmpleadoActual()).thenReturn(null);
        
        IllegalStateException ex = assertThrows(
            IllegalStateException.class, 
            () -> controller.createMembresia(dto)
        );
        assertTrue(ex.getMessage().contains("No hay un empleado logueado"));
        verify(membresiaRepository, never()).save(any());
    }


    @Test
    @DisplayName("RC-AEV-01: Actualización válida")
    void updateMembresia_validDto_success() {
        int id = 1;
        
        MembresiaUpdateDTO updateDto = new MembresiaUpdateDTO();
        updateDto.setFechaFin(LocalDate.now().plusMonths(2));
        updateDto.setEstado(EstadoMembresia.ACTIVA); 
        updateDto.setObservaciones(null); 

        Membresia existingMembresia = new Membresia();
        existingMembresia.setId(id);
        
        when(membresiaRepository.findById(id)).thenReturn(Optional.of(existingMembresia));
        when(membresiaRepository.findDetalleById(id)).thenReturn(Optional.of(createDetalleDTO(id)));
        
        MembresiaDetalleDTO result = controller.updateMembresia(id, updateDto);
        
        assertNotNull(result);
        verify(membresiaRepository).findById(id);
        verify(membresiaRepository).update(any(Membresia.class));
        verify(membresiaRepository).findDetalleById(id);
    }
    
    
    @Test
    void cancelarMembresia_exists_success() {
        int id = 1;
        when(membresiaRepository.existsById(id)).thenReturn(true);
        doNothing().when(membresiaRepository).delete(id);
        
        assertDoesNotThrow(() -> controller.cancelarMembresia(id));
        verify(membresiaRepository).existsById(id);
        verify(membresiaRepository).delete(id);
    }

    @Test
    void cancelarMembresia_notExists_throwsException() {
        int id = 99;
        when(membresiaRepository.existsById(id)).thenReturn(false);
        
        assertThrows(IllegalArgumentException.class, () -> controller.cancelarMembresia(id));
        verify(membresiaRepository).existsById(id);
        verify(membresiaRepository, never()).delete(anyInt());
    }
}