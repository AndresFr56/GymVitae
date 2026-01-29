package gym.vitae.controller;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import gym.vitae.model.dtos.inventario.ProveedorCreateDTO;
import gym.vitae.model.dtos.inventario.ProveedorUpdateDTO;
import java.lang.reflect.Method;
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

class ProveedoresControllerTest {

  @Nested
  @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
  @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
  @DisplayName("Test Estáticos Proveedores")
  class ProveedoresControllerStaticTest {

    private ProveedoresController proveedoresController;

    @BeforeEach
    void setUp() {
      proveedoresController = new ProveedoresController(null);
    }

    // Utilizado para llamar metodo privado del controlador
    private void invokeValidateProveedorCreate(ProveedorCreateDTO dto) throws Exception {
      Method m =
          ProveedoresController.class.getDeclaredMethod(
              "validateProveedorCreate", ProveedorCreateDTO.class);
      m.setAccessible(true);
      m.invoke(proveedoresController, dto);
    }

    // Caso de prueba 1 - Todos los datos de entrada correctos
    @Test
    @Order(1)
    @DisplayName("RE-CPV [1, 5, 9, 13, 17]")
    void RE_CPV_1_todasClasesValidas() {
      ProveedorCreateDTO dtoProveedor =
          new ProveedorCreateDTO(
              "Distribuidora Fitness",
              "Juan Sanchez",
              "0910293024",
              "distrifit@gmail.com",
              "Avenida Huancavilca");

      assertDoesNotThrow(() -> invokeValidateProveedorCreate(dtoProveedor));
    }

    // Caso de prueba 2 - Nombre no cumple con las validaciones
    @Order(2)
    @ParameterizedTest(name = "RE-CPI-NOMBRES-[{index}]")
    @ValueSource(
        strings = {
          "",
          "   ",
          "Lorem Ipsum es simplemente el texto de relleno de las imprentas y archivos de texto. "
              + "Lorem Ipsum ha sido el texto de relleno estándar de las industrias desde el año 1500, cuando un impresor."
        })
    @DisplayName("RE-CPI: Nombres Inválidos (Caracteres prohibidos)")
    void RE_CPI_NOMBRES(String nombreInvalido) {
      ProveedorCreateDTO dtoProveedor =
          new ProveedorCreateDTO(
              nombreInvalido,
              "Jose Mieles",
              "0940321234",
              "corport@gmail.com",
              "Avenida Barcelona");
      assertThrows(
          IllegalArgumentException.class,
          () -> proveedoresController.createProveedor(dtoProveedor));
    }

    // Caso de prueba 3 - Contacto no cumple con las validaciones
    @Order(3)
    @ParameterizedTest(name = "RE-CPI-CONTACTO-[{index}]")
    @ValueSource(
        strings = {
          "",
          "   ",
          "Lorem Ipsum es simplemente el texto de relleno de las imprentas y archivos de texto. "
              + "Lorem Ipsum ha sido el texto de relleno estándar de las industrias desde el año 1500."
        })
    @DisplayName("RE-CPI: Contacto Inválido (Vacío o Excede Longitud)")
    void RE_CPI_CONTACTO(String contactoInvalido) {
      ProveedorCreateDTO dto =
          new ProveedorCreateDTO(
              "Distribuidora Weights",
              contactoInvalido,
              "0990293949",
              "weighdis@gmail.com",
              "Av. Las Américas");

      assertThrows(
          IllegalArgumentException.class, () -> proveedoresController.createProveedor(dto));
    }

    // Caso de prueba 4 - Teléfono no cumple con las validaciones
    @Order(4)
    @ParameterizedTest(name = "RE-CPI-TELEFONO-[{index}]")
    @ValueSource(
        strings = {"", "   ", "098765432", "09876543210", "ABCDEFGHIJ", "091234567a", "09-1234567"})
    @DisplayName("RE-CPI: Teléfono Inválido (Vacío, longitud incorrecta o no numérico)")
    void RE_CPI_TELEFONO(String telefonoInvalido) {
      ProveedorCreateDTO dto =
          new ProveedorCreateDTO(
              "NutriCorp",
              "Ana Sanchez",
              telefonoInvalido,
              "nutricorp@gmail.com",
              "Av. Las Américas 100");

      assertThrows(
          IllegalArgumentException.class, () -> proveedoresController.createProveedor(dto));
    }

    // Caso de prueba 5 - Email no cumple con las validaciones
    @Order(5)
    @ParameterizedTest(name = "RE-CPI-EMAIL-[{index}]")
    @ValueSource(
        strings = {
          "",
          "   ",
          "fabiani.gmail.com",
          "fabia@",
          "@gmail.com",
          "un_correo_extremadamente_largo_y_repetitivo_para_superar_el_limite_de_cien_caracteres"
              + "_permitidos_en_la_base_de_datos@gmail.com"
        })
    @DisplayName("RE-CPI: Email Inválido (Vacío, formato incorrecto o muy largo)")
    void RE_CPI_EMAIL(String emailInvalido) {
      ProveedorCreateDTO dto =
          new ProveedorCreateDTO(
              "FitCorp Distri", "Domenica Polo", "0912345678", emailInvalido, "Gomez Rendon");

      assertThrows(
          IllegalArgumentException.class, () -> proveedoresController.createProveedor(dto));
    }

    // Caso de prueba 6 - Dirección no cumple con las validaciones
    @Order(6)
    @ParameterizedTest(name = "RE-CPI-DIRECCION-[{index}]")
    @ValueSource(
        strings = {
          "Esta dirección es absurdamente larga y definitivamente tiene más de cien caracteres "
              + "para probar que el sistema valida correctamente la longitud máxima permitida "
              + "en la base de datos para este campo opcional del proveedor."
        })
    @DisplayName("RE-CPI: Dirección Inválida (Excede longitud máxima)")
    void RE_CPI_DIRECCION(String direccionInvalida) {
      ProveedorCreateDTO dto =
          new ProveedorCreateDTO(
              "ExerciseCompany",
              "Julia Rosario",
              "0928323458",
              "exercisecom@gmail.com",
              direccionInvalida);

      assertThrows(
          IllegalArgumentException.class, () -> proveedoresController.createProveedor(dto));
    }

    // Caso de prueba 7 - Nombre inválido al actualizar
    @Order(7)
    @ParameterizedTest(name = "AE-CPI-NOMBRES-[{index}]")
    @ValueSource(
        strings = {
          "",
          "   ",
          "Lorem Ipsum es simplemente el texto de relleno de las imprentas y archivos de texto. "
              + "Lorem Ipsum ha sido el texto de relleno estándar de las industrias."
        })
    @DisplayName("AE-CPI: Actualización Inválida - Campo Nombre")
    void AE_CPI_NOMBRES(String nombreInvalido) {
      ProveedorUpdateDTO dto =
          new ProveedorUpdateDTO(
              nombreInvalido,
              "Jose Cantos",
              "0990283921",
              "corpfit@gmail.com",
              "Calle 9 de octubre",
              true);

      assertThrows(
          IllegalArgumentException.class,
          () -> {
            proveedoresController.updateProveedor(1, dto);
          });
    }

    // Caso de prueba 8 - Contacto inválido al actualizar
    @Order(8)
    @ParameterizedTest(name = "AE-CPI-CONTACTO-[{index}]")
    @ValueSource(
        strings = {
          "",
          "   ",
          "Lorem Ipsum es simplemente el texto de relleno de las imprentas y archivos de texto. "
              + "Lorem Ipsum ha sido el texto de relleno estándar de las industrias."
        })
    @DisplayName("AE-CPI: Actualización Inválida - Campo Contacto")
    void AE_CPI_CONTACTO(String contactoInvalido) {
      ProveedorUpdateDTO dto =
          new ProveedorUpdateDTO(
              "DistriPlastics",
              contactoInvalido,
              "0950345678",
              "distriplas@gmail.com",
              "Avenida Juan Tanca",
              true);

      assertThrows(
          IllegalArgumentException.class,
          () -> {
            proveedoresController.updateProveedor(1, dto);
          });
    }

    // Caso de prueba 9 - Teléfono inválido al actualizar
    @Order(9)
    @ParameterizedTest(name = "AE-CPI-TELEFONO-[{index}]")
    @ValueSource(strings = {"", "   ", "098765432", "09876543210", "ABCDEFGHIJ", "09-1234567"})
    @DisplayName("AE-CPI: Actualización Inválida - Campo Teléfono")
    void AE_CPI_TELEFONO(String telefonoInvalido) {
      ProveedorUpdateDTO dto =
          new ProveedorUpdateDTO(
              "DistriProducts",
              "Helma Cortez",
              telefonoInvalido,
              "distriproducts@gmail.com",
              "Daule km 20",
              true);

      assertThrows(
          IllegalArgumentException.class,
          () -> {
            proveedoresController.updateProveedor(1, dto);
          });
    }

    // Caso de prueba 10 - Email inválido al actualizar
    @Order(10)
    @ParameterizedTest(name = "AE-CPI-EMAIL-[{index}]")
    @ValueSource(
        strings = {
          "helma.elestudiante",
          "helma.com",
          "HELMA estudiante@gmail.com",
          "correo@",
          "@gmail.com"
        })
    @DisplayName("AE-CPI: Actualización Inválida - Campo Email")
    void AE_CPI_EMAIL(String emailInvalido) {
      ProveedorUpdateDTO dto =
          new ProveedorUpdateDTO(
              "DistriFit", "Julia Peralta", "0990345678", emailInvalido, "Daule km 19", true);

      assertThrows(
          IllegalArgumentException.class,
          () -> {
            proveedoresController.updateProveedor(1, dto);
          });
    }

    @Order(11)
    @ParameterizedTest(name = "AE-CPI-DIRECCION-[{index}]")
    @ValueSource(
        strings = {
          "Esta dirección es absurdamente larga y definitivamente tiene más de cien caracteres para probar "
              + "que el sistema valida correctamente el límite permitido."
        })
    @DisplayName("AE-CPI: Actualización Inválida - Campo Dirección")
    void AE_CPI_DIRECCION(String direccionInvalida) {
      ProveedorUpdateDTO dto =
          new ProveedorUpdateDTO(
              "YourProducts S.A.",
              "Kelly Lopez",
              "0939203434",
              "youproduct@hotmail.com",
              direccionInvalida,
              true);

      assertThrows(
          IllegalArgumentException.class,
          () -> {
            proveedoresController.updateProveedor(1, dto);
          });
    }
  }
}
