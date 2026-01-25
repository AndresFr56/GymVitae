package gym.vitae.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import gym.vitae.model.Cliente;
import gym.vitae.model.dtos.cliente.ClienteCreateDTO;
import gym.vitae.model.dtos.cliente.ClienteDetalleDTO;
import gym.vitae.model.dtos.cliente.ClienteListadoDTO;
import gym.vitae.model.dtos.cliente.ClienteUpdateDTO;
import gym.vitae.model.enums.EstadoCliente;
import gym.vitae.model.enums.Genero;
import gym.vitae.repositories.ClienteRepository;
import java.lang.reflect.Method;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/** Tests para `ClienteController` — Validaciones estáticas y tests dinámicos (Mockito). */
class ClienteControllerTest {

  @Nested
  @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
  @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
  @DisplayName("Test Estáticos")
  class ClienteControllerStaticTest {

    private ClienteController controller;

    @BeforeEach
    void setUp() {
      controller = new ClienteController(null);
    }

    /**
     * Helper para invocar método privado validateClienteCreate
     *
     * @param dto DTO de creación de cliente
     * @throws Exception si hay error de reflexión
     */
    private void invokeValidateClienteCreate(ClienteCreateDTO dto) throws Exception {
      Method m =
          ClienteController.class.getDeclaredMethod(
              "validateClienteCreate", ClienteCreateDTO.class);
      m.setAccessible(true);
      m.invoke(controller, dto);
    }

    /**
     * Helper para invocar método privado validateClienteUpdate
     *
     * @param dto DTO de actualización de cliente
     * @throws Exception si hay error de reflexión
     */
    private void invokeValidateClienteUpdate(ClienteUpdateDTO dto) throws Exception {
      Method m =
          ClienteController.class.getDeclaredMethod(
              "validateClienteUpdate", ClienteUpdateDTO.class);
      m.setAccessible(true);
      m.invoke(controller, dto);
    }

    // ==================== PRUEBAS DE CREACIÓN ====================

    @Test
    @Order(1)
    @DisplayName("CC-CEV-01: creación válida con datos completos")
    void CC_CEV_01_creacionValida_datosCompletos() {
      ClienteCreateDTO dto =
          new ClienteCreateDTO(
              "Pedro",
              "Sánchez Ruiz",
              "0967890123",
              Genero.MASCULINO,
              "0932109876",
              "Av. Principal #123",
              "pedro.sanchez@example.com",
              LocalDate.of(1990, 3, 15),
              "María Sánchez",
              "0987654321");

      assertDoesNotThrow(() -> invokeValidateClienteCreate(dto));
    }

    @ParameterizedTest(name = "CC-CEI-0{index}: nombres inválidos -> [{0}]")
    @NullAndEmptySource
    @ValueSource(
        strings = {
          "   ",
          "Pedro123",
          "NombreMuyLargoQueSobrepasaLosCienCaracteres.............................................................................."
        })
    @Order(2)
    @DisplayName("CC-CEI-[1-3]: Clases inválidas campo Nombres en registrar cliente")
    void CC_CEI_01_nombresInvalidos_create(String nombres) {
      ClienteCreateDTO dto =
          new ClienteCreateDTO(
              nombres,
              "Sánchez Ruiz",
              "0967890123",
              Genero.MASCULINO,
              "0932109876",
              "Av. Principal #123",
              "pedro.sanchez@example.com",
              LocalDate.of(1990, 3, 15),
              "María Sánchez",
              "0987654321");

      Exception ex = assertThrows(Exception.class, () -> invokeValidateClienteCreate(dto));
      assertNotNull(ex);
    }

    @ParameterizedTest(name = "CC-CEI-0{index}: apellidos inválidos -> [{0}]")
    @NullAndEmptySource
    @ValueSource(
        strings = {
          "   ",
          "Sánchez123",
          "ApellidoMuyLargoQueSobrepasaLosCienCaracteres.............................................................................."
        })
    @Order(3)
    @DisplayName("CC-CEI-[4-6]: Clases inválidas campo Apellidos en registrar cliente")
    void CC_CEI_02_apellidosInvalidos_create(String apellidos) {
      ClienteCreateDTO dto =
          new ClienteCreateDTO(
              "Pedro",
              apellidos,
              "0967890123",
              Genero.MASCULINO,
              "0932109876",
              "Av. Principal #123",
              "pedro.sanchez@example.com",
              LocalDate.of(1990, 3, 15),
              "María Sánchez",
              "0987654321");

      Exception ex = assertThrows(Exception.class, () -> invokeValidateClienteCreate(dto));
      assertNotNull(ex);
    }

    @ParameterizedTest(name = "CC-CEI-0{index}: cédula inválida -> [{0}]")
    @ValueSource(strings = {"asdsadadasd!!!", "091234567", "0912345678912", ""})
    @Order(4)
    @DisplayName("CC-CEI-[7-10]: Clases inválidas campo Cédula en registrar cliente")
    void CC_CEI_03_cedulaInvalida_create(String cedula) {
      ClienteCreateDTO dto =
          new ClienteCreateDTO(
              "Pedro",
              "Sánchez Ruiz",
              cedula,
              Genero.MASCULINO,
              "0932109876",
              "Av. Principal #123",
              "pedro.sanchez@example.com",
              LocalDate.of(1990, 3, 15),
              "María Sánchez",
              "0987654321");

      Exception ex = assertThrows(Exception.class, () -> invokeValidateClienteCreate(dto));
      assertNotNull(ex);
    }

    @ParameterizedTest(name = "CC-CEI-0{index}: teléfono inválido -> [{0}]")
    @ValueSource(strings = {"WQEQE!!!...", "0987654", "09921312312312", "0513001122", ""})
    @Order(5)
    @DisplayName("CC-CEI-[11-15]: Clases inválidas campo Teléfono en registrar cliente")
    void CC_CEI_04_telefonoInvalido_create(String telefono) {
      ClienteCreateDTO dto =
          new ClienteCreateDTO(
              "Pedro",
              "Sánchez Ruiz",
              "0967890123",
              Genero.MASCULINO,
              telefono,
              "Av. Principal #123",
              "pedro.sanchez@example.com",
              LocalDate.of(1990, 3, 15),
              "María Sánchez",
              "0987654321");

      Exception ex = assertThrows(Exception.class, () -> invokeValidateClienteCreate(dto));
      assertNotNull(ex);
    }

    @ParameterizedTest(name = "CC-CEI-0{index}: email inválido -> [{0}]")
    @ValueSource(
        strings = {
          "pedro.sanchez",
          "pedro@",
          "pedro@.com",
          "pedro sanchez@example.com",
          "NombreConMasDeIenCaracteresYQueExcedeLaLimitacionDelCampoDeEmail@example.com.a"
        })
    @Order(6)
    @DisplayName("CC-CEI-[16-20]: Clases inválidas campo Email en registrar cliente")
    void CC_CEI_05_emailInvalido_create(String email) {
      ClienteCreateDTO dto =
          new ClienteCreateDTO(
              "Pedro",
              "Sánchez Ruiz",
              "0967890123",
              Genero.MASCULINO,
              "0932109876",
              "Av. Principal #123",
              email,
              LocalDate.of(1990, 3, 15),
              "María Sánchez",
              "0987654321");

      Exception ex = assertThrows(Exception.class, () -> invokeValidateClienteCreate(dto));
      assertNotNull(ex);
    }

    @ParameterizedTest(name = "CC-CEI-0{index}: dirección inválida -> [{0}]")
    @NullAndEmptySource
    @ValueSource(
        strings = {
          "   ",
          "NombreDireccionMuyLargoQueSobrepasaLosCienCaracteres.................................................................."
        })
    @Order(7)
    @DisplayName("CC-CEI-[21-23]: Clases inválidas campo Dirección en registrar cliente")
    void CC_CEI_06_direccionInvalida_create(String direccion) {
      ClienteCreateDTO dto =
          new ClienteCreateDTO(
              "Pedro",
              "Sánchez Ruiz",
              "0967890123",
              Genero.MASCULINO,
              "0932109876",
              direccion,
              "pedro.sanchez@example.com",
              LocalDate.of(1990, 3, 15),
              "María Sánchez",
              "0987654321");

      Exception ex = assertThrows(Exception.class, () -> invokeValidateClienteCreate(dto));
      assertNotNull(ex);
    }

    @ParameterizedTest(name = "CC-CEI-0{index}: género inválido -> [null]")
    @ValueSource(strings = {})
    @Order(8)
    @DisplayName("CC-CEI-[24]: Clase inválida cuando género es null en registrar cliente")
    void CC_CEI_07_generoInvalido_create() {
      ClienteCreateDTO dto =
          new ClienteCreateDTO(
              "Pedro",
              "Sánchez Ruiz",
              "0967890123",
              null,
              "0932109876",
              "Av. Principal #123",
              "pedro.sanchez@example.com",
              LocalDate.of(1990, 3, 15),
              "María Sánchez",
              "0987654321");

      Exception ex = assertThrows(Exception.class, () -> invokeValidateClienteCreate(dto));
      assertNotNull(ex);
    }

    // ==================== PRUEBAS DE ACTUALIZACIÓN ====================

    @Test
    @Order(9)
    @DisplayName("CU-CEV-01: actualización válida con datos completos")
    void CU_CEV_01_actualizacionValida_datosCompletos() {
      ClienteUpdateDTO dto =
          new ClienteUpdateDTO(
              "Carlos",
              "Mendoza Silva",
              "0912345679",
              Genero.MASCULINO,
              "0987654321",
              "Calle Nueva #456",
              "carlos.mendoza.nuevo@example.com",
              LocalDate.of(1985, 1, 15),
              "Ana Mendoza",
              "0998765432",
              EstadoCliente.ACTIVO);

      assertDoesNotThrow(() -> invokeValidateClienteUpdate(dto));
    }

    @ParameterizedTest(name = "CU-CEI-0{index}: nombres inválidos -> [{0}]")
    @NullAndEmptySource
    @ValueSource(
        strings = {
          "   ",
          "Carlos123",
          "NombreMuyLargoQueSobrepasaLosCienCaracteres.............................................................................."
        })
    @Order(10)
    @DisplayName("CU-CEI-[1-3]: Clases inválidas campo Nombres en actualizar cliente")
    void CU_CEI_01_nombresInvalidos_update(String nombres) {
      ClienteUpdateDTO dto =
          new ClienteUpdateDTO(
              nombres,
              "Mendoza Silva",
              "0912345679",
              Genero.MASCULINO,
              "0987654321",
              "Calle Nueva #456",
              "carlos.mendoza@example.com",
              LocalDate.of(1985, 1, 15),
              "Ana Mendoza",
              "0998765432",
              EstadoCliente.ACTIVO);

      Exception ex = assertThrows(Exception.class, () -> invokeValidateClienteUpdate(dto));
      assertNotNull(ex);
    }

    @ParameterizedTest(name = "CU-CEI-0{index}: cédula inválida -> [{0}]")
    @ValueSource(strings = {"asdsadadasd!!!", "091234567", "0912345678912", ""})
    @Order(11)
    @DisplayName("CU-CEI-[4-7]: Clases inválidas campo Cédula en actualizar cliente")
    void CU_CEI_02_cedulaInvalida_update(String cedula) {
      ClienteUpdateDTO dto =
          new ClienteUpdateDTO(
              "Carlos",
              "Mendoza Silva",
              cedula,
              Genero.MASCULINO,
              "0987654321",
              "Calle Nueva #456",
              "carlos.mendoza@example.com",
              LocalDate.of(1985, 1, 15),
              "Ana Mendoza",
              "0998765432",
              EstadoCliente.ACTIVO);

      Exception ex = assertThrows(Exception.class, () -> invokeValidateClienteUpdate(dto));
      assertNotNull(ex);
    }

    @ParameterizedTest(name = "CU-CEI-0{index}: email inválido -> [{0}]")
    @ValueSource(
        strings = {"carlos.mendoza", "carlos@", "carlos@.com", "carlos mendoza@example.com"})
    @Order(12)
    @DisplayName("CU-CEI-[8-11]: Clases inválidas campo Email en actualizar cliente")
    void CU_CEI_03_emailInvalido_update(String email) {
      ClienteUpdateDTO dto =
          new ClienteUpdateDTO(
              "Carlos",
              "Mendoza Silva",
              "0912345679",
              Genero.MASCULINO,
              "0987654321",
              "Calle Nueva #456",
              email,
              LocalDate.of(1985, 1, 15),
              "Ana Mendoza",
              "0998765432",
              EstadoCliente.ACTIVO);

      Exception ex = assertThrows(Exception.class, () -> invokeValidateClienteUpdate(dto));
      assertNotNull(ex);
    }

    @ParameterizedTest(name = "CU-CEI-0{index}: estado inválido -> [null]")
    @ValueSource(strings = {})
    @Order(13)
    @DisplayName("CU-CEI-[12]: Clase inválida cuando estado es null en actualizar cliente")
    void CU_CEI_04_estadoInvalido_update() {
      ClienteUpdateDTO dto =
          new ClienteUpdateDTO(
              "Carlos",
              "Mendoza Silva",
              "0912345679",
              Genero.MASCULINO,
              "0987654321",
              "Calle Nueva #456",
              "carlos.mendoza@example.com",
              LocalDate.of(1985, 1, 15),
              "Ana Mendoza",
              "0998765432",
              null);

      Exception ex = assertThrows(Exception.class, () -> invokeValidateClienteUpdate(dto));
      assertNotNull(ex);
    }

    @Nested
    @ExtendWith(MockitoExtension.class)
    @DisplayName("Tests Dinámicos (Mockito)")
    class ClienteControllerDynamicTest {

      @Mock private ClienteRepository repository;

      private ClienteController controller;

      @BeforeEach
      void setUp() {
        controller = new ClienteController(repository);
      }

      @Test
      @DisplayName("CC-CEV-DYNAMIC-01: createCliente éxito — guarda y devuelve detalle")
      void CC_CEV_DYNAMIC_01_create_success() {
        ClienteCreateDTO dto =
            new ClienteCreateDTO(
                "Pedro",
                "Sánchez Ruiz",
                "0967890123",
                Genero.MASCULINO,
                "0932109876",
                "Av. Principal #123",
                "pedro.sanchez@example.com",
                LocalDate.of(1990, 3, 15),
                "María Sánchez",
                "0987654321");

        Cliente saved = new Cliente();
        saved.setId(1);
        saved.setCodigoCliente("CLI-202601");
        saved.setNombres("Pedro");
        saved.setApellidos("Sánchez Ruiz");

        ClienteListadoDTO existing =
            new ClienteListadoDTO(
                2,
                "CLI-202602",
                "Laura Martínez Flores",
                "0912345678",
                "0987654321",
                EstadoCliente.ACTIVO);

        when(repository.findAllListado()).thenReturn(List.of(existing));
        when(repository.existsByCedula("0967890123", null)).thenReturn(false);
        when(repository.existsByNombresApellidos("Pedro", "Sánchez Ruiz", null)).thenReturn(false);
        when(repository.existsByEmail("pedro.sanchez@example.com", null)).thenReturn(false);
        when(repository.save(any())).thenReturn(saved);
        when(repository.findByIdDetalle(1))
            .thenReturn(
                Optional.of(
                    new ClienteDetalleDTO(
                        1,
                        "CLI-202601",
                        "Pedro",
                        "Sánchez Ruiz",
                        "0967890123",
                        Genero.MASCULINO,
                        "0932109876",
                        "pedro.sanchez@example.com",
                        "Av. Principal #123",
                        LocalDate.of(1990, 3, 15),
                        "María Sánchez",
                        "0987654321",
                        EstadoCliente.ACTIVO
                            )));

        ClienteDetalleDTO out = controller.createCliente(dto);

        assertNotNull(out);
        assertEquals("Pedro", out.nombres());
        assertEquals("CLI-202601", out.codigoCliente());
        verify(repository).existsByCedula("0967890123", null);
        verify(repository).existsByNombresApellidos("Pedro", "Sánchez Ruiz", null);
        verify(repository).existsByEmail("pedro.sanchez@example.com", null);
        verify(repository).save(any(Cliente.class));
        verify(repository).findByIdDetalle(1);
      }

      @Test
      @DisplayName("CC-CEI-DYNAMIC-01: createCliente cédula duplicada lanza excepción")
      void CC_CEI_DYNAMIC_01_create_cedulaDuplicada() {
        ClienteCreateDTO dto =
            new ClienteCreateDTO(
                "Pedro",
                "Sánchez Ruiz",
                "0967890123",
                Genero.MASCULINO,
                "0932109876",
                "Av. Principal #123",
                "pedro.sanchez@example.com",
                LocalDate.of(1990, 3, 15),
                "María Sánchez",
                "0987654321");

        when(repository.existsByCedula("0967890123", null)).thenReturn(true);

        IllegalArgumentException ex =
            assertThrows(IllegalArgumentException.class, () -> controller.createCliente(dto));
        assertTrue(ex.getMessage().contains("Cédula existente"));
        verify(repository).existsByCedula("0967890123", null);
        verify(repository, never()).save(any());
      }

      @Test
      @DisplayName("CC-CEI-DYNAMIC-02: createCliente nombres+apellidos duplicados lanza excepción")
      void CC_CEI_DYNAMIC_02_create_nombresApellidosDuplicados() {
        ClienteCreateDTO dto =
            new ClienteCreateDTO(
                "Pedro",
                "Sánchez Ruiz",
                "0967890123",
                Genero.MASCULINO,
                "0932109876",
                "Av. Principal #123",
                "pedro.sanchez@example.com",
                LocalDate.of(1990, 3, 15),
                "María Sánchez",
                "0987654321");

        when(repository.existsByCedula("0967890123", null)).thenReturn(false);
        when(repository.existsByNombresApellidos("Pedro", "Sánchez Ruiz", null)).thenReturn(true);

        IllegalArgumentException ex =
            assertThrows(IllegalArgumentException.class, () -> controller.createCliente(dto));
        assertTrue(ex.getMessage().contains("Nombres y Apellidos existentes"));
        verify(repository).existsByCedula("0967890123", null);
        verify(repository).existsByNombresApellidos("Pedro", "Sánchez Ruiz", null);
        verify(repository, never()).save(any());
      }

      @Test
      @DisplayName("CC-CEI-DYNAMIC-03: createCliente email duplicado lanza excepción")
      void CC_CEI_DYNAMIC_03_create_emailDuplicado() {
        ClienteCreateDTO dto =
            new ClienteCreateDTO(
                "Pedro",
                "Sánchez Ruiz",
                "0967890123",
                Genero.MASCULINO,
                "0932109876",
                "Av. Principal #123",
                "pedro.sanchez@example.com",
                LocalDate.of(1990, 3, 15),
                "María Sánchez",
                "0987654321");

        when(repository.existsByCedula("0967890123", null)).thenReturn(false);
        when(repository.existsByNombresApellidos("Pedro", "Sánchez Ruiz", null)).thenReturn(false);
        when(repository.existsByEmail("pedro.sanchez@example.com", null)).thenReturn(true);

        IllegalArgumentException ex =
            assertThrows(IllegalArgumentException.class, () -> controller.createCliente(dto));
        assertTrue(ex.getMessage().contains("Email existente"));
        verify(repository).existsByCedula("0967890123", null);
        verify(repository).existsByNombresApellidos("Pedro", "Sánchez Ruiz", null);
        verify(repository).existsByEmail("pedro.sanchez@example.com", null);
        verify(repository, never()).save(any());
      }

      @Test
      @DisplayName("CU-CEV-DYNAMIC-01: updateCliente éxito — actualiza y devuelve detalle")
      void CU_CEV_DYNAMIC_01_update_success() {
        int clienteId = 1;
        ClienteUpdateDTO dto =
            new ClienteUpdateDTO(
                "Carlos",
                "Mendoza Silva",
                "0912345679",
                Genero.MASCULINO,
                "0987654321",
                "Calle Nueva #456",
                "carlos.mendoza.nuevo@example.com",
                LocalDate.of(1985, 1, 15),
                "Ana Mendoza",
                "0998765432",
                EstadoCliente.ACTIVO);

        Cliente existing = new Cliente();
        existing.setId(clienteId);
        existing.setCodigoCliente("CLI-202401");

        when(repository.findById(clienteId)).thenReturn(Optional.of(existing));
        when(repository.existsByCedula("0912345679", clienteId)).thenReturn(false);
        when(repository.existsByNombresApellidos("Carlos", "Mendoza Silva", clienteId))
            .thenReturn(false);
        when(repository.existsByEmail("carlos.mendoza.nuevo@example.com", clienteId))
            .thenReturn(false);
        when(repository.findByIdDetalle(clienteId))
            .thenReturn(
                Optional.of(
                    new ClienteDetalleDTO(
                        clienteId,
                        "CLI-202401",
                        "Carlos",
                        "Mendoza Silva",
                        "0912345679",
                        Genero.MASCULINO,
                        "0987654321",
                        "Calle Nueva #456",
                        "carlos.mendoza.nuevo@example.com",
                        LocalDate.of(1985, 1, 15),
                        "Ana Mendoza",
                        "0998765432",
                        EstadoCliente.ACTIVO
                        )));

        ClienteDetalleDTO out = controller.updateCliente(clienteId, dto);

        assertNotNull(out);
        assertEquals("Carlos", out.nombres());
        assertEquals("CLI-202401", out.codigoCliente());
        verify(repository).findById(clienteId);
        verify(repository).existsByCedula("0912345679", clienteId);
        verify(repository).existsByNombresApellidos("Carlos", "Mendoza Silva", clienteId);
        verify(repository).existsByEmail("carlos.mendoza.nuevo@example.com", clienteId);
        verify(repository).update(any(Cliente.class));
        verify(repository).findByIdDetalle(clienteId);
      }

      @Test
      @DisplayName("CU-CEI-DYNAMIC-01: updateCliente cédula duplicada lanza excepción")
      void CU_CEI_DYNAMIC_01_update_cedulaDuplicada() {
        int clienteId = 1;
        ClienteUpdateDTO dto =
            new ClienteUpdateDTO(
                "Carlos",
                "Mendoza Silva",
                "0912345679",
                Genero.MASCULINO,
                "0987654321",
                "Calle Nueva #456",
                "carlos.mendoza@example.com",
                LocalDate.of(1985, 1, 15),
                "Ana Mendoza",
                "0998765432",
                EstadoCliente.ACTIVO);

        Cliente existing = new Cliente();
        existing.setId(clienteId);

        when(repository.findById(clienteId)).thenReturn(Optional.of(existing));
        when(repository.existsByCedula("0912345679", clienteId)).thenReturn(true);

        IllegalArgumentException ex =
            assertThrows(
                IllegalArgumentException.class, () -> controller.updateCliente(clienteId, dto));
        assertTrue(ex.getMessage().contains("Cédula existente"));
        verify(repository).findById(clienteId);
        verify(repository).existsByCedula("0912345679", clienteId);
        verify(repository, never()).update(any());
      }

      @Test
      @DisplayName("CU-CEI-DYNAMIC-02: updateCliente email duplicado lanza excepción")
      void CU_CEI_DYNAMIC_02_update_emailDuplicado() {
        int clienteId = 1;
        ClienteUpdateDTO dto =
            new ClienteUpdateDTO(
                "Carlos",
                "Mendoza Silva",
                "0912345679",
                Genero.MASCULINO,
                "0987654321",
                "Calle Nueva #456",
                "carlos.mendoza.nuevo@example.com",
                LocalDate.of(1985, 1, 15),
                "Ana Mendoza",
                "0998765432",
                EstadoCliente.ACTIVO);

        Cliente existing = new Cliente();
        existing.setId(clienteId);

        when(repository.findById(clienteId)).thenReturn(Optional.of(existing));
        when(repository.existsByCedula("0912345679", clienteId)).thenReturn(false);
        when(repository.existsByNombresApellidos("Carlos", "Mendoza Silva", clienteId))
            .thenReturn(false);
        when(repository.existsByEmail("carlos.mendoza.nuevo@example.com", clienteId))
            .thenReturn(true);

        IllegalArgumentException ex =
            assertThrows(
                IllegalArgumentException.class, () -> controller.updateCliente(clienteId, dto));
        assertTrue(ex.getMessage().contains("Email existente"));
        verify(repository).findById(clienteId);
        verify(repository).existsByCedula("0912345679", clienteId);
        verify(repository).existsByNombresApellidos("Carlos", "Mendoza Silva", clienteId);
        verify(repository).existsByEmail("carlos.mendoza.nuevo@example.com", clienteId);
        verify(repository, never()).update(any());
      }
    }
  }
}
