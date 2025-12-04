package gym.vitae.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import gym.vitae.mapper.TipoMembresiaMapper;
import gym.vitae.model.TiposMembresia;
import gym.vitae.model.dtos.membresias.MembresiaBeneficioCreateDTO;
import gym.vitae.model.dtos.membresias.TipoMembresiaCreateDTO;
import gym.vitae.model.dtos.membresias.TipoMembresiaDetalleDTO;
import gym.vitae.model.dtos.membresias.TipoMembresiaUpdateDTO;
import gym.vitae.repositories.TiposMembresiaRepository;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.internal.matchers.Null;

class TiposMembresiaControllerTest {

    private TiposMembresiaRepository repository;
    private MembresiaBeneficioController asociacionController;
    private TipoMembresiaMapper mapper;

    private TiposMembresiaController controller;

    private final String nombreValido = "Estandar";
    private final String descripcionValida = "Acceso basico al gimnasio.";
    private final String descripcionLarga = "A".repeat(256); // Inválida
    private final Integer duracionValida = 30;
    private final BigDecimal costoValido = BigDecimal.valueOf(50.00);
    private final BigDecimal costoValidoMinimo = BigDecimal.valueOf(0.01);
    private final BigDecimal costoInvalidoCero = BigDecimal.ZERO;
    private final BigDecimal costoInvalidoNegativo = BigDecimal.valueOf(-10.00);
    private final BigDecimal costoInvalidoAlto = BigDecimal.valueOf(10001.00);
    private final Boolean accesoCompletoValido = true;
    private final List<Integer> beneficiosIdsValidos = List.of(1, 2);
    private final List<Integer> beneficiosIdsVacios = Collections.emptyList();
    private final int TIPO_ID = 1;

    private TiposMembresia tipoBase;
    private TipoMembresiaDetalleDTO detalleDTO;

    private final Integer duracionInvalidaMayor365 = 366; 
    private final BigDecimal costoInvalidoMasDeDosDecimales = BigDecimal.valueOf(10.123);

    @BeforeEach
    void setUp() {
        repository = mock(TiposMembresiaRepository.class);
        asociacionController = mock(MembresiaBeneficioController.class);
        mapper = mock(TipoMembresiaMapper.class); 

        controller =
                new TiposMembresiaController(
                        repository, 
                        asociacionController, 
                        mapper
                );

        tipoBase = new TiposMembresia();
        tipoBase.setId(TIPO_ID);
        tipoBase.setNombre(nombreValido);

        detalleDTO = new TipoMembresiaDetalleDTO();
        detalleDTO.setId(TIPO_ID);
        detalleDTO.setNombre(nombreValido);
    }

    @Nested
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
    @DisplayName("Tests de Validacion - Crear Tipo Membresia con Beneficios")
    class CreateTipoConBeneficiosValidationTests {

        @Test
        @Order(1)
        @DisplayName("RTM - DTO Nulo")
        void RTM_DTONulo() {
            assertThrows(IllegalArgumentException.class, 
                         () -> new TiposMembresiaController(null, null, null).createTipoConBeneficios(null));
        }

        @Test
        @Order(2)
        @DisplayName("RTM [2] - Nombre Nulo")
        void RTM_2_NombreNulo() {
            TipoMembresiaCreateDTO dto = new TipoMembresiaCreateDTO(null, descripcionValida, duracionValida, costoValido, accesoCompletoValido, beneficiosIdsValidos);
            assertThrows(IllegalArgumentException.class, () -> controller.createTipoConBeneficios(dto));
        }

        @Test
        @Order(3)
        @DisplayName("RTM [3] - Nombre Largo (> 50)")
        void RTM_3_NombreLargo() {
            String nombreLargo = "A".repeat(51);
            TipoMembresiaCreateDTO dto = new TipoMembresiaCreateDTO(nombreLargo, descripcionValida, duracionValida, costoValido, accesoCompletoValido, beneficiosIdsValidos);
            assertThrows(IllegalArgumentException.class, () -> controller.createTipoConBeneficios(dto));
        }

        @Test
        @Order(4)
        @DisplayName("CEI [4] - Nombre con Numeros")
        void RTM_4_NombreConNumeros() {
            TipoMembresiaCreateDTO dto = new TipoMembresiaCreateDTO("Tipo123", descripcionValida, duracionValida, costoValido, accesoCompletoValido, beneficiosIdsValidos);
            assertThrows(IllegalArgumentException.class, () -> controller.createTipoConBeneficios(dto));
        }

        @Test
        @Order(5)
        @DisplayName("CEI [6] - Descripcion Larga (> 255)")
        void RTM_6_DescripcionLarga() {
            TipoMembresiaCreateDTO dto = new TipoMembresiaCreateDTO(nombreValido, descripcionLarga, duracionValida, costoValido, accesoCompletoValido, beneficiosIdsValidos);
            assertThrows(IllegalArgumentException.class, () -> controller.createTipoConBeneficios(dto));
        }

        @Test
        @Order(6)
        @DisplayName("RTM [8a] - Duracion Nula")
        void RTM_8a_DuracionNula() {
            TipoMembresiaCreateDTO dto = new TipoMembresiaCreateDTO(nombreValido, descripcionValida, null, costoValido, accesoCompletoValido, beneficiosIdsValidos);
            assertThrows(IllegalArgumentException.class, () -> controller.createTipoConBeneficios(dto));
        }

        @Test
        @Order(7)
        @DisplayName("RTM [8b] - Duracion Cero")
        void RTM_8b_DuracionCero() {
            TipoMembresiaCreateDTO dto = new TipoMembresiaCreateDTO(nombreValido, descripcionValida, 0, costoValido, accesoCompletoValido, beneficiosIdsValidos);
            assertThrows(IllegalArgumentException.class, () -> controller.createTipoConBeneficios(dto));
        }

        @Test
        @Order(8)
        @DisplayName("RTM [8c] - Duracion Negativa")
        void RTM_8c_DuracionNegativa() {
            TipoMembresiaCreateDTO dto = new TipoMembresiaCreateDTO(nombreValido, descripcionValida, -10, costoValido, accesoCompletoValido, beneficiosIdsValidos);
            assertThrows(IllegalArgumentException.class, () -> controller.createTipoConBeneficios(dto));
        }
        
        @Test
        @Order(9)
        @DisplayName("RTM [9] - Duracion Mayor a 365 Dias")
        void RTM_9_DuracionMayor365() {
            TipoMembresiaCreateDTO dto = new TipoMembresiaCreateDTO(nombreValido, descripcionValida, duracionInvalidaMayor365, costoValido, accesoCompletoValido, beneficiosIdsValidos);
            assertThrows(NullPointerException.class, () -> controller.createTipoConBeneficios(dto));
        }
        
        @Test
        @Order(10) 
        @DisplayName("RTM [12a] - Costo Nulo")
        void RTM_12a_CostoNulo() {
            TipoMembresiaCreateDTO dto = new TipoMembresiaCreateDTO(nombreValido, descripcionValida, duracionValida, null, accesoCompletoValido, beneficiosIdsValidos);
            assertThrows(IllegalArgumentException.class, () -> controller.createTipoConBeneficios(dto));
        }

        @Test
        @Order(11)
        @DisplayName("RTM [12b] - Costo Cero")
        void RTM_12b_CostoCero() {
            TipoMembresiaCreateDTO dto = new TipoMembresiaCreateDTO(nombreValido, descripcionValida, duracionValida, costoInvalidoCero, accesoCompletoValido, beneficiosIdsValidos);
            assertThrows(IllegalArgumentException.class, () -> controller.createTipoConBeneficios(dto));
        }

        @Test
        @Order(12)
        @DisplayName("RTM [12c] - Costo Negativo")
        void RTM_12c_CostoNegativo() {
            TipoMembresiaCreateDTO dto = new TipoMembresiaCreateDTO(nombreValido, descripcionValida, duracionValida, costoInvalidoNegativo, accesoCompletoValido, beneficiosIdsValidos);
            assertThrows(IllegalArgumentException.class, () -> controller.createTipoConBeneficios(dto));
        }
        
        
        @Test
        @Order(13)
        @DisplayName("RTM [13] - Costo Alto")
        void RTM_13_CostoAlto() {
            TipoMembresiaCreateDTO dto = new TipoMembresiaCreateDTO(nombreValido, descripcionValida, duracionValida, costoInvalidoAlto, accesoCompletoValido, beneficiosIdsValidos);
            assertThrows(Exception.class, () -> controller.createTipoConBeneficios(dto));
        }

        @Test
        @Order(14)
        @DisplayName("RTM [14] - Costo Más de 2 Decimales")
        void RTM_14_CostoMasDeDosDecimales() {
            TipoMembresiaCreateDTO dto = new TipoMembresiaCreateDTO(nombreValido, descripcionValida, duracionValida, costoInvalidoMasDeDosDecimales, accesoCompletoValido, beneficiosIdsValidos);
            assertThrows(NullPointerException.class, () -> controller.createTipoConBeneficios(dto));
        }

        @Test
        @Order(15)
        @DisplayName("CEI [18] - Beneficios IDs Vacios")
        void RTM_18_BeneficiosVacios() {
            TipoMembresiaCreateDTO dto = new TipoMembresiaCreateDTO(nombreValido, descripcionValida, duracionValida, costoValido, accesoCompletoValido, beneficiosIdsVacios);
            assertThrows(Exception.class, () -> controller.createTipoConBeneficios(dto));
        }

        @Test
        @Order(16)
        @DisplayName("CEI [19] - Beneficios Inexistentes (Falla de Flujo: Mock no configurado)")
        void RTM_19_BeneficiosInexistentes() {
            List<Integer> idsInexistentes = List.of(999);
            TipoMembresiaCreateDTO dto = new TipoMembresiaCreateDTO(nombreValido, descripcionValida, duracionValida, costoValido, accesoCompletoValido, idsInexistentes);
            assertThrows(Exception.class, () -> controller.createTipoConBeneficios(dto));
        }
    }
    
    @Nested
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
    @DisplayName("Tests de Flujo y Casos Validos - Crear Tipo Membresia con Beneficios (CEV)")
    class CreateTipoConBeneficiosSuccessTests {

        @BeforeEach
        void setupMocks() {
            when(repository.save(any(TiposMembresia.class))).thenReturn(tipoBase);
            when(repository.findDetalleById(TIPO_ID)).thenReturn(Optional.of(detalleDTO));
        }

        @Test
        @Order(1)
        @DisplayName("RTM [1] - Clase valida con beneficios")
        void RTM_1_clasevalida_Conbeneficios() {
            TipoMembresiaCreateDTO dto = 
                new TipoMembresiaCreateDTO(nombreValido, descripcionValida, duracionValida, costoValido, accesoCompletoValido, beneficiosIdsValidos);
            
            TipoMembresiaDetalleDTO result = controller.createTipoConBeneficios(dto);
            
            assertNotNull(result);
            assertEquals(TIPO_ID, result.getId());
            
            verify(asociacionController, times(beneficiosIdsValidos.size())).create(any(MembresiaBeneficioCreateDTO.class));
            verify(repository).save(any(TiposMembresia.class));
            verify(repository).findDetalleById(TIPO_ID);
        }

        @Test
        @Order(2)
        @DisplayName("CEV [1] - Campos Validos Minimos")
        void RTM_1_CamposValidos_Minimo_BeneficiosNulos() {
            TipoMembresiaCreateDTO dto = 
                new TipoMembresiaCreateDTO(nombreValido, null, duracionValida, costoValido, accesoCompletoValido, null);
            
            TipoMembresiaDetalleDTO result = controller.createTipoConBeneficios(dto);
            
            assertNotNull(result);
            
            verify(asociacionController, times(0)).create(any(MembresiaBeneficioCreateDTO.class));
        }
        
        @Test
        @Order(3)
        @DisplayName("RTM [5] - Campos Validos")
        void RTM_5_DescripcionVacia_BeneficiosVacios() {
            TipoMembresiaCreateDTO dto = 
                new TipoMembresiaCreateDTO(nombreValido, "", duracionValida, costoValido, accesoCompletoValido, beneficiosIdsVacios);
            
            TipoMembresiaDetalleDTO result = controller.createTipoConBeneficios(dto);
            
            assertNotNull(result);
            
            verify(asociacionController, times(0)).create(any(MembresiaBeneficioCreateDTO.class));
        }
        
        @Test
        @Order(4)
        @DisplayName("CEV [14] - Costo Minimo (0.01)")
        void RTM_14_CostoMinimo() {
            TipoMembresiaCreateDTO dto = 
                new TipoMembresiaCreateDTO(nombreValido, descripcionValida, duracionValida, costoValidoMinimo, accesoCompletoValido, beneficiosIdsVacios);
            
            TipoMembresiaDetalleDTO result = controller.createTipoConBeneficios(dto);
            
            assertNotNull(result);
            verify(repository).save(any(TiposMembresia.class));
        }

        @Test
        @Order(5)
        @DisplayName("FLUJO - Beneficio con ID invalido")
        void FLUJO_BeneficioInvalidoEsOmitido() {
            List<Integer> idsMixtos = List.of(1, 999, 3);
            TipoMembresiaCreateDTO dto = 
                new TipoMembresiaCreateDTO(nombreValido, descripcionValida, duracionValida, costoValido, accesoCompletoValido, idsMixtos);
            
            doThrow(new IllegalArgumentException("Beneficio no existe"))
                .when(asociacionController).create(new MembresiaBeneficioCreateDTO(TIPO_ID, 999));

            TipoMembresiaDetalleDTO result = controller.createTipoConBeneficios(dto);

            assertNotNull(result);
            verify(asociacionController, times(3)).create(any(MembresiaBeneficioCreateDTO.class));
        }

        @Test
        @Order(6)
        @DisplayName("FLUJO - Falla al recuperar detalle")
        void FLUJO_FallaRecuperarDetalle() {
            TipoMembresiaCreateDTO dto = 
                new TipoMembresiaCreateDTO(nombreValido, descripcionValida, duracionValida, costoValido, accesoCompletoValido, null);
            
            when(repository.findDetalleById(TIPO_ID)).thenReturn(Optional.empty());
            
            assertThrows(IllegalStateException.class, 
                         () -> controller.createTipoConBeneficios(dto));
        }
    }

    @Nested
    @DisplayName("Tests de Flujo y Otros Métodos (Con Mocks)")
    class FlowTestsWithMocks {
        
        
        @Test
        @DisplayName("getTipos - Ejecucion Exitosa")
        void getTipos_ShouldReturnList() {
            when(repository.findAllListado()).thenReturn(Collections.emptyList());
            assertNotNull(controller.getTipos());
            verify(repository).findAllListado();
        }

        @Test
        @DisplayName("getTipoById - Encontrado")
        void getTipoById_Found() {
            when(repository.findDetalleById(TIPO_ID)).thenReturn(Optional.of(detalleDTO));
            TipoMembresiaDetalleDTO result = controller.getTipoById(TIPO_ID);
            assertNotNull(result);
            assertEquals(TIPO_ID, result.getId());
        }

        @Test
        @DisplayName("getTipoById - No Encontrado (IllegalArgumentException)")
        void getTipoById_NotFound() {
            when(repository.findDetalleById(anyInt())).thenReturn(Optional.empty());
            assertThrows(IllegalArgumentException.class, 
                         () -> controller.getTipoById(999));
        }
        
        @Test
        @DisplayName("getPaged - Ejecucion Exitosa")
        void getPaged_ShouldReturnList() {
            when(repository.findAllListadoPaginated(anyInt(), anyInt())).thenReturn(Collections.emptyList());
            assertNotNull(controller.getPaged(0, 10));
            verify(repository).findAllListadoPaginated(0, 10);
        }
        
        @Test
        @DisplayName("updateTipo - Tipo No Encontrado")
        void updateTipo_NotFound() {
            TipoMembresiaUpdateDTO dto = new TipoMembresiaUpdateDTO();
            dto.setNombre(nombreValido);
            dto.setDuracionDias(duracionValida);
            dto.setCosto(costoValido);
            
            when(repository.findById(anyInt())).thenReturn(Optional.empty());
            
            assertThrows(IllegalArgumentException.class, 
                         () -> controller.updateTipo(999, dto));
            verify(repository).findById(999);
        }

        @Test
        @DisplayName("updateTipo - Flujo Completo Exitoso")
        void updateTipo_Success() {
            TipoMembresiaUpdateDTO dto = new TipoMembresiaUpdateDTO();
            dto.setNombre("Premium Editado");
            dto.setDuracionDias(60);
            dto.setCosto(BigDecimal.valueOf(100.00));
            
            TiposMembresia tipoOriginal = new TiposMembresia();
            tipoOriginal.setId(TIPO_ID);
            
            TipoMembresiaDetalleDTO detalleActualizado = new TipoMembresiaDetalleDTO();
            detalleActualizado.setId(TIPO_ID);
            detalleActualizado.setNombre("Premium Editado");

            when(repository.findById(TIPO_ID)).thenReturn(Optional.of(tipoOriginal));
            when(repository.findDetalleById(TIPO_ID)).thenReturn(Optional.of(detalleActualizado));
            
            TipoMembresiaDetalleDTO result = controller.updateTipo(TIPO_ID, dto);

            assertNotNull(result);
            assertEquals("Premium Editado", result.getNombre());
            
            verify(repository).findById(TIPO_ID);
            verify(repository).update(tipoOriginal); 
            verify(repository).findDetalleById(TIPO_ID);
        }
        
        @Test
        @DisplayName("deleteTipo - Tipo No Encontrado")
        void deleteTipo_NotFound() {
            when(repository.existsById(anyInt())).thenReturn(false);
            
            assertThrows(IllegalArgumentException.class, 
                         () -> controller.deleteTipo(999));
            verify(repository).existsById(999);
            verify(repository, times(0)).delete(anyInt());
        }

        @Test
        @DisplayName("deleteTipo - Flujo Exitoso")
        void deleteTipo_Success() {
            when(repository.existsById(TIPO_ID)).thenReturn(true);
            
            controller.deleteTipo(TIPO_ID);
            
            verify(repository).existsById(TIPO_ID);
            verify(repository).delete(TIPO_ID);
        }
    }

    @Nested
    @DisplayName("Tests de Validacion ID/Null")
    class FlowTests {
        

        @Test
        @DisplayName("getTipoById") 
        void getTipoById_ShouldThrowIllegalArgumentExceptionForInvalidId() { 
            assertThrows(IllegalArgumentException.class, () -> controller.getTipoById(0));
            assertThrows(IllegalArgumentException.class, () -> controller.getTipoById(-1));
        }
        
        @Test
        @DisplayName("updateTipo - DTO Nulo")
        void updateTipo_DTONulo_ShouldThrowIllegalArgumentException() {
            // Usa el controller mockeado, la validación de ID pasa, pero falla en el if(dto == null)
            assertThrows(IllegalArgumentException.class, 
                         () -> controller.updateTipo(1, null));
        }

        @Test
        @DisplayName("updateTipo ") 
        void updateTipo_ShouldThrowIllegalArgumentExceptionForInvalidId() {
            TipoMembresiaUpdateDTO dto = new TipoMembresiaUpdateDTO();
            assertThrows(IllegalArgumentException.class, () -> controller.updateTipo(0, dto));
            assertThrows(IllegalArgumentException.class, () -> controller.updateTipo(-1, dto));
        }

        @Test
        @DisplayName("deleteTipo") 
        void deleteTipo_ShouldThrowIllegalArgumentExceptionForInvalidId() {
            assertThrows(IllegalArgumentException.class, () -> controller.deleteTipo(0));
            assertThrows(IllegalArgumentException.class, () -> controller.deleteTipo(-1));
        }
    }
}