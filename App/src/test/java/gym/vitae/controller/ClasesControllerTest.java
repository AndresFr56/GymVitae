package gym.vitae.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import gym.vitae.model.Clase;
import gym.vitae.model.dtos.clase.ClaseCreateDTO;
import gym.vitae.model.dtos.clase.ClaseDetalleDTO;
import gym.vitae.model.dtos.clase.ClaseListadoDTO;
import gym.vitae.model.dtos.clase.ClaseUpdateDTO;
import gym.vitae.model.enums.NivelClase;
import gym.vitae.repositories.ClaseRepository;
import java.lang.reflect.Method;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/** Tests para `ClasesController` — Validaciones estáticas y tests dinámicos (Mockito). */
class ClasesControllerTest {

  @Nested
  @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
  @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
  @DisplayName("Test Estáticos")
  class ClassesControllerStaticTest {

    private ClasesController controller;

    @BeforeEach
    void setUp() {
      controller = new ClasesController(null);
    }

    /**
     * Helpe
     *
     * @param dto
     * @throws Exception
     */
    private void invokeValidateClaseCreate(ClaseCreateDTO dto) throws Exception {
      Method m =
          ClasesController.class.getDeclaredMethod("validateClaseCreate", ClaseCreateDTO.class);
      m.setAccessible(true);
      m.invoke(controller, dto);
    }

    private void invokeValidateClaseUpdate(ClaseUpdateDTO dto) throws Exception {
      Method m =
          ClasesController.class.getDeclaredMethod("validateClaseUpdate", ClaseUpdateDTO.class);
      m.setAccessible(true);
      m.invoke(controller, dto);
    }

    @Test
    @Order(1)
    @DisplayName("RC-CEV-01: creación válida")
    void RC_CEV_01_creacionValida_datosMinimos() {
      ClaseCreateDTO dto =
          new ClaseCreateDTO("Yoga", "Clase de relajación", 60, 15, NivelClase.TODOS);

      assertDoesNotThrow(() -> invokeValidateClaseCreate(dto));
    }

    @ParameterizedTest(name = "RC-CEI-0{index}: nombres inválidos -> [{0}]")
    @NullAndEmptySource
    @ValueSource(
        strings = {
          "   ",
          "Yoga123",
          "NombreMuyLargoQueSobrepasaLosCienCaracteres.............................................................................."
        })
    @Order(3)
    @DisplayName("RC-CEI-[2-4]: Clases invaidas [-, 5, 7, 12, 16] Campo Nombre en registrar")
    void RC_CEI_01_nombresInvalidos_create(String nombre) {
      ClaseCreateDTO dto = new ClaseCreateDTO(nombre, "Desc", 60, 10, NivelClase.TODOS);
      Exception ex = assertThrows(Exception.class, () -> invokeValidateClaseCreate(dto));
      assertNotNull(ex);
    }

    @ParameterizedTest(name = "RA-CEI-[5+ {index}]: Descripcion inválida -> [{0}]")
    @ValueSource(
        strings = {
          "DescripcionMuyLargaQueSobrepasaLosDoscientosCaracteres........................................................................................................................."
              + "........................................................................................................................."
        })
    @Order(4)
    @DisplayName("RA-CEI-[6]: Clases invaidas [1, -, 7, 12, 16] Campo Descripción en registrar")
    void RA_CEI_05_descripcionInvalida_update(String descripcion) {
      ClaseCreateDTO dto =
          new ClaseCreateDTO("Pilates", descripcion, 45, 12, NivelClase.INTERMEDIO);
      Exception ex = assertThrows(Exception.class, () -> invokeValidateClaseCreate(dto));
      assertNotNull(ex);
    }

    @ParameterizedTest(name = "RC-CEI-[7+ {index}]: Duración inválida -> [{0}]")
    @ValueSource(ints = {0, 300, 69, -5})
    @Order(5)
    @DisplayName("RC-CEI-[8]: Clases invaidas [1, 5, -, 12, 16] Campo Duración en registrar")
    void RC_CEI_07_duracionInvalida_create(int duracion) {
      ClaseCreateDTO dto = new ClaseCreateDTO("Yoga", "Desc", duracion, 10, NivelClase.TODOS);
      Exception ex = assertThrows(Exception.class, () -> invokeValidateClaseCreate(dto));
      assertNotNull(ex);
    }

    @ParameterizedTest(name = "RA-CEI-[12+ {index}]: Capacidad inválida -> [{0}]")
    @ValueSource(ints = {0, 101, -3})
    @Order(6)
    @DisplayName("RA-CEI-[13]: Clases invaidas [1, 5, 7, -, 16] Campo Capacidad en registrar")
    void RC_CEI_12_capacidadInvalida_create(int capacidad) {
      ClaseCreateDTO dto = new ClaseCreateDTO("Yoga", "Desc", 60, capacidad, NivelClase.TODOS);
      Exception ex = assertThrows(Exception.class, () -> invokeValidateClaseCreate(dto));
      assertNotNull(ex);
    }

    @Nested
    @ExtendWith(MockitoExtension.class)
    @DisplayName("Tests Dinámicos (Mockito)")
    class ClassesControllerDynamicTest {

      @Mock private ClaseRepository repository;

      private ClasesController controller;

      @BeforeEach
      void setUp() {
        controller = new ClasesController(repository);
      }

      @Test
      @DisplayName("RC-CEV-DYNAMIC-01: createClase éxito — guarda y devuelve detalle")
      void AC_CEV_DYNAMIC_01_create_success() {
        ClaseCreateDTO dto = new ClaseCreateDTO("Yoga", "Relax", 60, 15, NivelClase.TODOS);

        Clase saved = new Clase();
        saved.setId(1);
        saved.setNombre("Yoga");

        ClaseListadoDTO existing = new ClaseListadoDTO(2, "Otra", 45, 10, NivelClase.TODOS, true);

        when(repository.findAllListado()).thenReturn(List.of(existing));
        when(repository.save(any())).thenReturn(saved);
        when(repository.findByIdDetalle(1))
            .thenReturn(
                Optional.of(
                    new ClaseDetalleDTO(
                        1,
                        "Yoga",
                        "Relax",
                        60,
                        15,
                        NivelClase.TODOS,
                        true,
                        Instant.now(),
                        Instant.now())));

        ClaseDetalleDTO out = controller.createClase(dto);

        assertNotNull(out);
        assertEquals("Yoga", out.nombre());
        verify(repository).findAllListado();
        verify(repository).save(any(Clase.class));
        verify(repository).findByIdDetalle(1);
      }

      @Test
      @DisplayName("RC-CEI-DYNAMIC-01: createClase nombre duplicado lanza excepción")
      void RC_CEI_DYNAMIC_01_create_nombreDuplicado() {
        ClaseCreateDTO dto = new ClaseCreateDTO("Yoga", "Relax", 60, 15, NivelClase.TODOS);

        // Simular que ya existe una clase con nombre "Yoga"
        ClaseListadoDTO existente = new ClaseListadoDTO(1, "Yoga", 60, 15, NivelClase.TODOS, true);
        when(repository.findAllListado()).thenReturn(List.of(existente));

        IllegalArgumentException ex =
            assertThrows(IllegalArgumentException.class, () -> controller.createClase(dto));
        assertTrue(ex.getMessage().contains("Ya existe una clase"));
        verify(repository).findAllListado();
        verify(repository, never()).save(any());
      }
    }
  }
}
