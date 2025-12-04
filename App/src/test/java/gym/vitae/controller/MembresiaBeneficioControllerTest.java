package gym.vitae.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class MembresiaBeneficioControllerTest {

    private MembresiaBeneficioRepository repository;
    private TiposMembresiaRepository tiposRepository;
    private BeneficioRepository beneficioRepository;
    private MembresiaBeneficioController controller;

    private final int ASOCIACION_ID = 1;
    private final int MEMBRESIA_ID = 10;
    private final String MEMBRESIA_NOMBRE = "Premium";
    private final int BENEFICIO_ID = 20;
    private final String BENEFICIO_NOMBRE = "Acceso a Piscina";
    private final String BENEFICIO_DESCRIPCION = "Piscina olímpica y sauna";
    private final int ID_INVALIDO = -5;
    private final Instant CREATED_AT = Instant.now();

    private MembresiaBeneficioDetalleDTO detalleDTO;
    private MembresiaBeneficioListadoDTO listadoDTO;
    private MembresiaBeneficioCreateDTO createDTO;
    private TiposMembresia tipoMembresia;
    private Beneficio beneficio;
    private MembresiaBeneficio asociacionBase;

    @BeforeEach
    void setUp() {
        repository = mock(MembresiaBeneficioRepository.class);
        tiposRepository = mock(TiposMembresiaRepository.class);
        beneficioRepository = mock(BeneficioRepository.class);

        controller =
                new MembresiaBeneficioController(
                        repository, tiposRepository, beneficioRepository);

        detalleDTO = new MembresiaBeneficioDetalleDTO();
        detalleDTO.setId(ASOCIACION_ID);
        detalleDTO.setCreatedAt(CREATED_AT);
        detalleDTO.setTipoMembresiaId(MEMBRESIA_ID);
        detalleDTO.setTipoMembresiaNombre(MEMBRESIA_NOMBRE);
        detalleDTO.setBeneficioId(BENEFICIO_ID);
        detalleDTO.setBeneficioNombre(BENEFICIO_NOMBRE);
        detalleDTO.setBeneficioDescripcion(BENEFICIO_DESCRIPCION);

        listadoDTO =
                new MembresiaBeneficioListadoDTO(
                        ASOCIACION_ID, "Membresia Estándar", "Clases Grupales");
        createDTO = new MembresiaBeneficioCreateDTO(MEMBRESIA_ID, BENEFICIO_ID);

        tipoMembresia = new TiposMembresia();
        tipoMembresia.setId(MEMBRESIA_ID);

        beneficio = new Beneficio();
        beneficio.setId(BENEFICIO_ID);

        asociacionBase = new MembresiaBeneficio();
        asociacionBase.setId(ASOCIACION_ID);
        asociacionBase.setTipoMembresia(tipoMembresia);
        asociacionBase.setBeneficio(beneficio);
    }


    @Nested
    @DisplayName("🧪 Tests de Creación (create)")
    class CreateTests {

        @Test
        @DisplayName("CEV - Creación Exitosa")
        void create_Success() {
            when(tiposRepository.findById(MEMBRESIA_ID)).thenReturn(Optional.of(tipoMembresia));
            when(beneficioRepository.findById(BENEFICIO_ID)).thenReturn(Optional.of(beneficio));
            when(repository.save(any(MembresiaBeneficio.class))).thenReturn(asociacionBase);
            when(repository.findDetalleById(ASOCIACION_ID)).thenReturn(Optional.of(detalleDTO));

            MembresiaBeneficioDetalleDTO result = controller.create(createDTO);

            assertNotNull(result);
            assertEquals(ASOCIACION_ID, result.getId());
            assertEquals(MEMBRESIA_ID, result.getTipoMembresiaId());
            assertEquals(MEMBRESIA_NOMBRE, result.getTipoMembresiaNombre());
            assertEquals(BENEFICIO_ID, result.getBeneficioId());
            assertEquals(BENEFICIO_NOMBRE, result.getBeneficioNombre());
            assertEquals(BENEFICIO_DESCRIPCION, result.getBeneficioDescripcion());
            
            verify(tiposRepository).findById(MEMBRESIA_ID);
            verify(beneficioRepository).findById(BENEFICIO_ID);
            verify(repository).save(any(MembresiaBeneficio.class));
            verify(repository).findDetalleById(ASOCIACION_ID);
        }

        @Test
        @DisplayName("RTM - ID de Membresía Inválido (Cero)")
        void create_InvalidMembresiaId_ShouldThrowException() {
            MembresiaBeneficioCreateDTO dto = new MembresiaBeneficioCreateDTO(0, BENEFICIO_ID);
            assertThrows(IllegalArgumentException.class, () -> controller.create(dto));
        }

        @Test
        @DisplayName("CEI - Beneficio No Encontrado")
        void create_BeneficioNotFound_ShouldThrowException() {
            when(tiposRepository.findById(MEMBRESIA_ID)).thenReturn(Optional.of(tipoMembresia));
            when(beneficioRepository.findById(BENEFICIO_ID)).thenReturn(Optional.empty());

            assertThrows(
                    IllegalArgumentException.class,
                    () -> controller.create(createDTO),
                    "Beneficio no encontrado");
        }
        


        @Test
        @DisplayName("FLUJO - Falla al Recuperar Detalle Post-Creación")
        void create_FailToRetrieveDetail_ShouldThrowIllegalStateException() {
            when(tiposRepository.findById(MEMBRESIA_ID)).thenReturn(Optional.of(tipoMembresia));
            when(beneficioRepository.findById(BENEFICIO_ID)).thenReturn(Optional.of(beneficio));
            when(repository.save(any(MembresiaBeneficio.class))).thenReturn(asociacionBase);
            when(repository.findDetalleById(ASOCIACION_ID)).thenReturn(Optional.empty());

            assertThrows(
                    IllegalStateException.class,
                    () -> controller.create(createDTO),
                    "Error al recuperar la asociación creada");
        }
    }


    @Nested
    @DisplayName("🔎 Tests de Consulta (getById)")
    class GetByIdTests {

        @Test
        @DisplayName("CEV - Consulta por ID Exitosa")
        void getById_Success() {
            when(repository.findDetalleById(ASOCIACION_ID)).thenReturn(Optional.of(detalleDTO));

            MembresiaBeneficioDetalleDTO result = controller.getById(ASOCIACION_ID);

            assertNotNull(result);
            assertEquals(ASOCIACION_ID, result.getId());
            assertEquals(MEMBRESIA_NOMBRE, result.getTipoMembresiaNombre());
            assertEquals(BENEFICIO_DESCRIPCION, result.getBeneficioDescripcion());
            verify(repository).findDetalleById(ASOCIACION_ID);
        }

        @Test
        @DisplayName("CEI - Asociación No Encontrada")
        void getById_NotFound_ShouldThrowException() {
            when(repository.findDetalleById(ASOCIACION_ID)).thenReturn(Optional.empty());

            assertThrows(
                    IllegalArgumentException.class,
                    () -> controller.getById(ASOCIACION_ID),
                    "Asociación no encontrada");
        }
    }


    @Nested
    @DisplayName("📋 Tests de Listado (getAll)")
    class GetAllTests {

        @Test
        @DisplayName("CEV - Listado Exitoso con Datos")
        void getAll_SuccessWithData() {
            MembresiaBeneficioListadoDTO listadoIncompleto = new MembresiaBeneficioListadoDTO(ASOCIACION_ID, null, null);
            List<MembresiaBeneficioListadoDTO> listaIncompleta = List.of(listadoIncompleto);

            when(repository.findAllListado()).thenReturn(listaIncompleta);
            when(repository.findDetalleById(ASOCIACION_ID)).thenReturn(Optional.of(detalleDTO));
            
            List<MembresiaBeneficioListadoDTO> result = controller.getAll();

            assertNotNull(result);
            assertEquals(1, result.size());
            assertEquals(ASOCIACION_ID, result.get(0).getId());
            assertEquals(detalleDTO.getTipoMembresiaNombre(), result.get(0).getTipoMembresiaNombre());
            assertEquals(detalleDTO.getBeneficioNombre(), result.get(0).getBeneficioNombre());

            verify(repository).findAllListado();
            verify(repository, times(1)).findDetalleById(ASOCIACION_ID);
        }

        @Test
        @DisplayName("CEV - Listado Vacío")
        void getAll_EmptyList() {
            when(repository.findAllListado()).thenReturn(Collections.emptyList());

            List<MembresiaBeneficioListadoDTO> result = controller.getAll();

            assertNotNull(result);
            assertTrue(result.isEmpty());
            verify(repository).findAllListado();
            verify(repository, times(0)).findDetalleById(anyInt());
        }
    }
    

    @Nested
    @DisplayName("Tests de Eliminación")
    class DeleteTests {

        @Test
        @DisplayName("CEV - Eliminación")
        void delete_Success() {
            when(repository.existsById(ASOCIACION_ID)).thenReturn(true);

            controller.delete(ASOCIACION_ID);

            verify(repository).existsById(ASOCIACION_ID);
            verify(repository).delete(ASOCIACION_ID);
        }
        
        @Test
        @DisplayName("CEI - Asociación No Encontrada")
        void delete_NotFound_ShouldThrowException() {
            when(repository.existsById(ASOCIACION_ID)).thenReturn(false);

            assertThrows(
                    IllegalArgumentException.class,
                    () -> controller.delete(ASOCIACION_ID),
                    "Asociación no encontrada");

            verify(repository).existsById(ASOCIACION_ID);
            verify(repository, times(0)).delete(anyInt());
        }
    }
}