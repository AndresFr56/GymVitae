package gym.vitae.controller;

import static org.junit.jupiter.api.Assertions.*;

import gym.vitae.model.dtos.empleado.EmpleadoCreateDTO;
import gym.vitae.model.dtos.empleado.EmpleadoUpdateDTO;
import gym.vitae.model.enums.EstadoEmpleado;
import gym.vitae.model.enums.Genero;
import gym.vitae.model.enums.TipoContrato;
import java.time.LocalDate;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class PersonalControllerTest {

  @Nested
  @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
  @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
  @DisplayName("Test Estáticos")
  class PersonalControllerStaticTest {

    private PersonalController personalController;

    @BeforeEach
    void setUp() {
      personalController = new PersonalController(null, null);
    }

    @Test
    @Order(1)
    @DisplayName("RE-CEV [1, 5, 9, 13, 14, 19, 22, 26, 27, 28]")
    void RE_CEV_1_todasClasesValidas() {
      EmpleadoCreateDTO dto =
          new EmpleadoCreateDTO(
              "Juan Carlos",
              "Pérez López",
              "0912345678",
              Genero.MASCULINO,
              "0987654321",
              "Av Principal 123",
              "juan.perez@gymvitae.com",
              1,
              TipoContrato.TIEMPO_COMPLETO,
              LocalDate.now().minusDays(1),
              null);

      // Act & Assert
      assertDoesNotThrow(() -> personalController.validateEmpleadoCreate(dto));
    }

    @Order(2)
    @ParameterizedTest(name = "RE-CEI-[{index}]")
    @ValueSource(
        strings = {
          "Juanito123!",
          "Lorem Ipsum es simplemente el texto de relleno de las imprentas y archivos de texto. "
              + "Lorem Ipsum ha sido el texto de relleno estándar de las industrias desde el año 1500, cuando un impresor "
              + "(N. del T. persona que se dedica a la imprenta) desconocido usó una galería de textos y los mezcló de tal "
              + "manera que logró hacer un libro de textos especimen. No sólo sobrevivió 500 años, sino que tambien "
              + "ingresó como texto de relleno en documentos electrónicos, quedando esencialmente igual al original. "
              + "Fue popularizado en los 60s con la creación de las hojas \"Letraset\", las cuales contenian pasajes de "
              + "Lorem Ipsum, y más recientemente con software de autoedición",
          ""
        })
    @DisplayName(
        "RE-CEI-[01-03}: Clases Inválidas [ - , 5, 9, 13, 14, 19, 22, 26, 27, 28] Campo Nombres")
    void RE_CEI_NOMBRES(String nombres) {
      EmpleadoCreateDTO dto =
          new EmpleadoCreateDTO(
              nombres, // Nombres inválidos
              "Pérez López",
              "0912345678",
              Genero.MASCULINO,
              "0987654321",
              "Av Principal 123",
              "juan.perez@gmail.com",
              1,
              TipoContrato.TIEMPO_COMPLETO,
              LocalDate.now().plusDays(1),
              null);

      // Act & Assert
      assertThrows(
          IllegalArgumentException.class,
          () -> {
            personalController.validateEmpleadoCreate(dto);
          });
    }

    @Order(3)
    @ParameterizedTest(name = "RE-CEI-[5+{index}]")
    @ValueSource(
        strings = {
          "López123!",
          "Lorem Ipsum es simplemente el texto de relleno de las imprentas y archivos de texto. "
              + "Lorem Ipsum ha sido el texto de relleno estándar de las industrias desde el año 1500, cuando un impresor "
              + "(N. del T. persona que se dedica a la imprenta) desconocido usó una galería de textos y los mezcló de tal "
              + "manera que logró hacer un libro de textos especimen. No sólo sobrevivió 500 años, sino que tambien "
              + "ingresó como texto de relleno en documentos electrónicos, quedando esencialmente igual al original. "
              + "Fue popularizado en los 60s con la creación de las hojas \"Letraset\", las cuales contenian pasajes de "
              + "Lorem Ipsum, y más recientemente con software de autoedición",
          ""
        })
    @DisplayName(
        "RE-CEI-{04-06}: Clases Inválidas [1, - , 9, 13, 14, 19, 22, 26, 27, 28] Campo Apellidos")
    void RE_CEI_APELLIDOS(String apellidos) {
      EmpleadoCreateDTO dto =
          new EmpleadoCreateDTO(
              "Juan Carlos",
              apellidos, // Apellidos inválidos
              "0912345678",
              Genero.MASCULINO,
              "0987654321",
              "Av Principal 123",
              "juan.perez@gmail.com",
              1,
              TipoContrato.TIEMPO_COMPLETO,
              LocalDate.now().plusDays(1),
              null);

      // Act & Assert
      assertThrows(
          IllegalArgumentException.class,
          () -> {
            personalController.validateEmpleadoCreate(dto);
          });
    }

    @Order(4)
    @ParameterizedTest(name = "RE-CEI-[9+{index}]")
    @ValueSource(strings = {"asdsadadasd!!!", "091234567", "0912345678912"})
    @DisplayName(
        "RE-CEI-{10-12}: Clases Inválidas [1, 5, - , 13, 14, 19, 22, 26, 27, 28] Campo Cédula")
    void RE_CEI_CEDULA(String cedula) {
      EmpleadoCreateDTO dto =
          new EmpleadoCreateDTO(
              "Juan Carlos",
              "Pérez López",
              cedula, // Cédula inválida
              Genero.MASCULINO,
              "0987654321",
              "Av Principal 123",
              "sasdasda@gmail.com",
              1,
              TipoContrato.TIEMPO_COMPLETO,
              LocalDate.now().plusDays(1),
              null);

      // Act & Assert
      assertThrows(
          IllegalArgumentException.class,
          () -> {
            personalController.validateEmpleadoCreate(dto);
          });
    }

    @Order(5)
    @ParameterizedTest(name = "RE-CEI-[9+{index}]")
    @ValueSource(strings = {"WQEQE!!!...", "0987654", "09921312312312", "0513001122"})
    @DisplayName(
        "RE-CEI-{10-13}: Clases Inválidas [1, 5, 9, 13, -, 19, 22, 26, 27, 28] Campo Teléfono")
    void RE_CEI_TELEFONO(String telefono) {
      EmpleadoCreateDTO dto =
          new EmpleadoCreateDTO(
              "Juan Carlos",
              "Pérez López",
              "0912345678",
              Genero.MASCULINO,
              telefono,
              "Av Principal 123",
              "juan.caballo@gmail.com",
              1,
              TipoContrato.TIEMPO_COMPLETO,
              LocalDate.now().plusDays(1),
              null);
      // Act & Assert
      assertThrows(
          IllegalArgumentException.class,
          () -> {
            personalController.validateEmpleadoCreate(dto);
          });
    }

    @Order(6)
    @ParameterizedTest(name = "RE-CEI-[13+{index}]")
    @ValueSource(
        strings = {
          "",
          "Lorem Ipsum es simplemente el texto de relleno de las imprentas y archivos de texto. "
              + "Lorem Ipsum ha sido el texto de relleno estándar de las industrias desde el año 1500, cuando un impresor "
              + "(N. del T. persona que se dedica a la imprenta) desconocido usó una galería de textos y los mezcló de tal "
              + "manera que logró hacer un libro de textos especimen. No sólo sobrevivió 500 años, sino que tambien "
              + "ingresó como texto de relleno en documentos electrónicos, quedando esencialmente igual al original. "
              + "Fue popularizado en los 60s con la creación de las hojas \"Letraset\", las cuales contenian pasajes de "
              + "Lorem Ipsum, y más recientemente con software de autoedición",
        })
    @DisplayName(
        "RE-CEI-{14-15}: Clases Inválidas [1, 5, 9, 13, 14, -, 22, 26, 27, 28] Campo Dirección")
    void RE_CEI_DIRECCION(String direccion) {
      EmpleadoCreateDTO dto =
          new EmpleadoCreateDTO(
              "Juan Carlos",
              "Pérez López",
              "0912345678",
              Genero.MASCULINO,
              "0987654321",
              direccion,
              "juan.elcaballo@gmail.com",
              1,
              TipoContrato.TIEMPO_COMPLETO,
              LocalDate.now().plusDays(1),
              null);
      // Act & Assert
      assertThrows(
          IllegalArgumentException.class,
          () -> {
            personalController.validateEmpleadoCreate(dto);
          });
    }

    @Order(7)
    @ParameterizedTest(name = "RE-CEI-[15+{index}]")
    @ValueSource(strings = {"juan.elcaballo", "juan.com", "JUANl caballo@gmail.com"})
    @DisplayName(
        "RE-CEI-{16-18}: Clases Inválidas [1, 5, 9, 13, 14, 19, -, 26, 27, 28] Campo Email")
    void RE_CEI_EMAIL(String email) {
      EmpleadoCreateDTO dto =
          new EmpleadoCreateDTO(
              "Juan Carlos",
              "Pérez López",
              "0912345678",
              Genero.MASCULINO,
              "0987654321",
              "Av Principal 123",
              email,
              1,
              TipoContrato.TIEMPO_COMPLETO,
              LocalDate.now().plusDays(1),
              null);
      // Act & Assert
      assertThrows(
          IllegalArgumentException.class,
          () -> {
            personalController.validateEmpleadoCreate(dto);
          });
    }

    @Order(8)
    @ParameterizedTest(name = "RE-CEI-[19]")
    @ValueSource(strings = {"2030-01-01"})
    @DisplayName(
        "RE-CEI-{19}: Clases Inválidas [1, 5, 9, 13, 14, 19, 22, 26, 27, - ] Campo Fecha Ingreso")
    void RE_CEI_FECHA_INGRESO(String fechaIngresoStr) {
      LocalDate fechaIngreso = LocalDate.parse(fechaIngresoStr);
      EmpleadoCreateDTO dto =
          new EmpleadoCreateDTO(
              "Juan Carlos",
              "Pérez López",
              "0912345678",
              Genero.MASCULINO,
              "0987654321",
              "Av Principal 123",
              "juanelcaballo@gmail.com",
              1,
              TipoContrato.TIEMPO_COMPLETO,
              fechaIngreso,
              null);
      // Act & Assert
      assertThrows(
          IllegalArgumentException.class,
          () -> {
            personalController.validateEmpleadoCreate(dto);
          });
    }

    // Actualizar personal/empleado

    @Order(9)
    @Test
    @DisplayName("AE-CEV Classes Válidas [1, 2, 6, 10, 14, 15, 20, 23, 27, 28, 29, 31]")
    void AE_CEV_1_todasClasesValidas() {
      EmpleadoUpdateDTO dto =
          new EmpleadoUpdateDTO(
              "Juan Carlos",
              "Pérez López",
              "0912345678",
              Genero.MASCULINO,
              "0912345678",
              "0987654321",
              "juannito@gmail.com",
              1,
              TipoContrato.TIEMPO_COMPLETO,
              LocalDate.now(),
              EstadoEmpleado.ACTIVO);

      // Act & Assert
      assertDoesNotThrow(() -> personalController.validateEmpleadoUpdate(1, dto));
    }

    @Order(10)
    @ParameterizedTest(name = "AE-CEI-[1 + {index}]")
    @ValueSource(
        strings = {
          "Juanito123!",
          "Lorem Ipsum es simplemente el texto de relleno de las imprentas y archivos de texto. "
              + "Lorem Ipsum ha sido el texto de relleno estándar de las industrias desde el año 1500, cuando un impresor "
              + "(N. del T. persona que se dedica a la imprenta) desconocido usó una galería de textos y los mezcló de tal "
              + "manera que logró hacer un libro de textos especimen. No sólo sobrevivió 500 años, sino que tambien "
              + "ingresó como texto de relleno en documentos electrónicos, quedando esencialmente igual al original. "
              + "Fue popularizado en los 60s con la creación de las hojas \"Letraset\", las cuales contenian pasajes de "
              + "Lorem Ipsum, y más recientemente con software de autoedición",
          ""
        })
    @DisplayName(
        "AE-CEI-{01-03}: Clases Inválidas [ 1, - , 6, 10, 14, 15, 20, 23, 27, 28, 29, 31] Campo Nombres")
    void AE_CEI_NOMBRES(String nombres) {
      EmpleadoUpdateDTO dto =
          new EmpleadoUpdateDTO(
              nombres,
              "Pérez López",
              "0912345678",
              Genero.MASCULINO,
              "0987654321",
              "Av Principal 123",
              "juan.perez@gmail.com",
              1,
              TipoContrato.TIEMPO_COMPLETO,
              LocalDate.now().plusDays(1),
              EstadoEmpleado.ACTIVO);

      // Act & Assert
      assertThrows(
          IllegalArgumentException.class,
          () -> {
            personalController.validateEmpleadoUpdate(1, dto);
          });
    }

    @Order(11)
    @ParameterizedTest(name = "RE-CEI-[6+{index}]")
    @ValueSource(
        strings = {
          "López123!",
          "Lorem Ipsum es simplemente el texto de relleno de las imprentas y archivos de texto. "
              + "Lorem Ipsum ha sido el texto de relleno estándar de las industrias desde el año 1500, cuando un impresor "
              + "(N. del T. persona que se dedica a la imprenta) desconocido usó una galería de textos y los mezcló de tal "
              + "manera que logró hacer un libro de textos especimen. No sólo sobrevivió 500 años, sino que tambien "
              + "ingresó como texto de relleno en documentos electrónicos, quedando esencialmente igual al original. "
              + "Fue popularizado en los 60s con la creación de las hojas \"Letraset\", las cuales contenian pasajes de "
              + "Lorem Ipsum, y más recientemente con software de autoedición",
          ""
        })
    @DisplayName(
        "AE-CEI-{04-06}: Clases Inválidas [1, 2, -, 10, 14, 15, 20, 23, 27, 28, 29, 31] Campo Apellidos")
    void AE_CEI_APELLIDOS(String apellidos) {
      EmpleadoUpdateDTO dto =
          new EmpleadoUpdateDTO(
              "Juan Carlos",
              apellidos, // Apellidos inválidos
              "0912345678",
              Genero.MASCULINO,
              "0987654321",
              "Av Principal 123",
              "juan.perez@gmail.com",
              1,
              TipoContrato.TIEMPO_COMPLETO,
              LocalDate.now().plusDays(1),
              EstadoEmpleado.ACTIVO);

      // Act & Assert
      assertThrows(
          IllegalArgumentException.class,
          () -> {
            personalController.validateEmpleadoUpdate(1, dto);
          });
    }

    @Order(12)
    @ParameterizedTest(name = "AE-CEI-[10+{index}]")
    @ValueSource(strings = {"asdsadadasd!!!", "091234567", "0912345678912"})
    @DisplayName(
        "AE-CEI-{7-9}: Clases Inválidas [1, 2, 6, -, 14, 15, 20, 23, 27, 28, 29, 31] Campo Cédula")
    void AE_CEI_CEDULA(String cedula) {
      EmpleadoUpdateDTO dto =
          new EmpleadoUpdateDTO(
              "Juan Carlos",
              "Pérez López",
              cedula, // Cédula inválida
              Genero.MASCULINO,
              "0987654321",
              "Av Principal 123",
              "sasdasda@gmail.com",
              1,
              TipoContrato.TIEMPO_COMPLETO,
              LocalDate.now().plusDays(1),
              null);

      // Act & Assert
      assertThrows(
          IllegalArgumentException.class,
          () -> {
            personalController.validateEmpleadoUpdate(1, dto);
          });
    }

    @Order(13)
    @ParameterizedTest(name = "RE-CEI-[15+{index}]")
    @ValueSource(strings = {"WQEQE!!!...", "0987654", "09921312312312", "0513001122"})
    @DisplayName(
        "AE-CEI-{10-13}: Clases Inválidas [1, 2, 6, 10, 14, -, 20, 23, 27, 28, 29, 31] Campo Teléfono")
    void AE_CEI_TELEFONO(String telefono) {
      EmpleadoUpdateDTO dto =
          new EmpleadoUpdateDTO(
              "Juan Carlos",
              "Pérez López",
              "0912345678",
              Genero.MASCULINO,
              telefono,
              "Av Principal 123",
              "juan.caballo@gmail.com",
              1,
              TipoContrato.TIEMPO_COMPLETO,
              LocalDate.now().plusDays(1),
              null);
      // Act & Assert
      assertThrows(
          IllegalArgumentException.class,
          () -> {
            personalController.validateEmpleadoUpdate(1, dto);
          });
    }

    @Order(14)
    @ParameterizedTest(name = "RE-CEI-[20+{index}]")
    @ValueSource(
        strings = {
          "",
          "Lorem Ipsum es simplemente el texto de relleno de las imprentas y archivos de texto. "
              + "Lorem Ipsum ha sido el texto de relleno estándar de las industrias desde el año 1500, cuando un impresor "
              + "(N. del T. persona que se dedica a la imprenta) desconocido usó una galería de textos y los mezcló de tal "
              + "manera que logró hacer un libro de textos especimen. No sólo sobrevivió 500 años, sino que tambien "
              + "ingresó como texto de relleno en documentos electrónicos, quedando esencialmente igual al original. "
              + "Fue popularizado en los 60s con la creación de las hojas \"Letraset\", las cuales contenian pasajes de "
              + "Lorem Ipsum, y más recientemente con software de autoedición",
        })
    @DisplayName(
        "AE-CEI-{14-15}: Clases Inválidas [1, 2, 6, 10, 14, 15, -, 23, 27, 28, 29, 31] Campo Dirección")
    void AE_CEI_DIRECCION(String direccion) {
      EmpleadoUpdateDTO dto =
          new EmpleadoUpdateDTO(
              "Juan Carlos",
              "Pérez López",
              "0912345678",
              Genero.MASCULINO,
              "0987654321",
              direccion,
              "juan.elcaballo@gmail.com",
              1,
              TipoContrato.TIEMPO_COMPLETO,
              LocalDate.now().plusDays(1),
              EstadoEmpleado.ACTIVO);
      // Act & Assert
      assertThrows(
          IllegalArgumentException.class,
          () -> {
            personalController.validateEmpleadoUpdate(1, dto);
          });
    }

    @Order(15)
    @ParameterizedTest(name = "AE-CEI-[23+{index}]")
    @ValueSource(strings = {"juan.elcaballo", "juan.com", "JUANl caballo@gmail.com"})
    @DisplayName(
        "AE-CEI-{16-18}: Clases Inválidas [1, 2, 6, 10, 14, 15, 20, -, 27, 28, 29, 31] Campo Email")
    void AE_CEI_EMAIL(String email) {
      EmpleadoUpdateDTO dto =
          new EmpleadoUpdateDTO(
              "Juan Carlos",
              "Pérez López",
              "0912345678",
              Genero.MASCULINO,
              "0987654321",
              "Av Principal 123",
              email,
              1,
              TipoContrato.TIEMPO_COMPLETO,
              LocalDate.now().plusDays(1),
              EstadoEmpleado.ACTIVO);
      // Act & Assert
      assertThrows(
          IllegalArgumentException.class,
          () -> {
            personalController.validateEmpleadoUpdate(1, dto);
          });
    }

    @Order(16)
    @ParameterizedTest(name = "AE-CEI-[29]")
    @ValueSource(strings = {"2030-01-01"})
    @DisplayName(
        "AE-CEI-{19}: Clases Inválidas [1, 2, 6, 10, 14, 15, 20, 23, 27, 28, -, 31] Campo Fecha Ingreso")
    void AE_CEI_FECHA_INGRESO(String fechaIngresoStr) {
      LocalDate fechaIngreso = LocalDate.parse(fechaIngresoStr);
      EmpleadoUpdateDTO dto =
          new EmpleadoUpdateDTO(
              "Juan Carlos",
              "Pérez López",
              "0912345678",
              Genero.MASCULINO,
              "0987654321",
              "Av Principal 123",
              "juanelcaballo@gmail.com",
              1,
              TipoContrato.TIEMPO_COMPLETO,
              fechaIngreso,
              EstadoEmpleado.ACTIVO);
      // Act & Assert
      assertThrows(
          IllegalArgumentException.class,
          () -> {
            personalController.validateEmpleadoUpdate(1, dto);
          });
    }
  }
}
