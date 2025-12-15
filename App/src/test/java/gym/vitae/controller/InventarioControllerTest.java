package gym.vitae.controller;

import static org.junit.jupiter.api.Assertions.*;

import gym.vitae.model.dtos.inventario.ProductoCreateDTO;
import gym.vitae.model.dtos.inventario.ProductoUpdateDTO;
import java.math.BigDecimal;
import java.time.LocalDate;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

/** Tests para InventarioController - Validaciones de Registrar Producto. */
class InventarioControllerTest {

  @Nested
  @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
  @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
  @DisplayName("Tests Estáticos - Registrar Producto (Clases de Equivalencia)")
  class RegistrarProductoValidationTests {

    private InventarioController controller;

    @BeforeEach
    void setUp() {
      controller = new InventarioController(null, null, null, null);
    }

    @Test
    @Order(1)
    @DisplayName("RP-CEV [1, 4, 7, 10, 13, 17, 21] - Todas las clases válidas")
    void RP_CEV_todasClasesValidas() {
      ProductoCreateDTO dto =
          new ProductoCreateDTO(
              1, // categoriaId válido (7)
              1, // proveedorId válido (10)
              null, // codigo (autogenerado)
              "Proteína Whey", // nombre válido 1 ≤ longitud ≤ 100 (1)
              "Suplemento proteico", // descripción válida 1 ≤ longitud ≤ 100 (4)
              new BigDecimal("45.99"), // precio válido decimal > 0, 2 decimales (13)
              100, // stock válido 1 ≤ stock ≤ 999 (17)
              "Unidad", // unidad de medida válida (21)
              LocalDate.now() // fecha ingreso
              );

      assertDoesNotThrow(() -> controller.validateProductoCreate(dto));
    }

    @Order(2)
    @ParameterizedTest(name = "RP-CEI-02-[{index}]: Nombre inválido -> [{0}]")
    @NullAndEmptySource
    @ValueSource(strings = {"   "})
    @DisplayName(
        "RP-CEI-[2]: Clase Inválida [2, 4, 7, 10, 13, 17, 21] - Nombre vacío (Longitud < 1)")
    void RP_CEI_02_nombreVacio(String nombre) {
      ProductoCreateDTO dto =
          new ProductoCreateDTO(
              1,
              1,
              null,
              nombre, // Nombre inválido - vacío (CE-2)
              "Descripción válida",
              new BigDecimal("45.99"),
              100,
              "Unidad",
              LocalDate.now());

      assertThrows(IllegalArgumentException.class, () -> controller.validateProductoCreate(dto));
    }

    @Test
    @Order(3)
    @DisplayName(
        "RP-CEI-[3]: Clase Inválida [3, 4, 7, 10, 13, 17, 21] - Nombre excede 100 caracteres (Longitud > 100)")
    void RP_CEI_03_nombreExcedeLongitud() {
      String nombreLargo = "A".repeat(101); // 101 caracteres
      ProductoCreateDTO dto =
          new ProductoCreateDTO(
              1,
              1,
              null,
              nombreLargo, // Nombre inválido - excede 100 caracteres (CE-3)
              "Descripción válida",
              new BigDecimal("45.99"),
              100,
              "Unidad",
              LocalDate.now());

      assertThrows(IllegalArgumentException.class, () -> controller.validateProductoCreate(dto));
    }

    @Order(4)
    @ParameterizedTest(name = "RP-CEI-05-[{index}]: Descripción inválida -> [{0}]")
    @ValueSource(strings = {" "})
    @DisplayName(
        "RP-CEI-[5]: Clase Inválida [1, 5, 7, 10, 13, 17, 21] - Descripción vacía (Longitud < 1)")
    void RP_CEI_05_descripcionVacia(String descripcion) {
      ProductoCreateDTO dto =
          new ProductoCreateDTO(
              1,
              1,
              null,
              "Producto válido",
              descripcion, // Descripción inválida - vacía (CE-5)
              new BigDecimal("45.99"),
              100,
              "Unidad",
              LocalDate.now());

      assertThrows(IllegalArgumentException.class, () -> controller.validateProductoCreate(dto));
    }

    @Test
    @Order(5)
    @DisplayName(
        "RP-CEI-[6]: Clase Inválida [1, 6, 7, 10, 13, 17, 21] - Descripción excede 100 caracteres (Longitud > 100)")
    void RP_CEI_06_descripcionExcedeLongitud() {
      String descripcionLarga = "A".repeat(101); // 101 caracteres
      ProductoCreateDTO dto =
          new ProductoCreateDTO(
              1,
              1,
              null,
              "Producto válido",
              descripcionLarga, // Descripción inválida - excede 100 caracteres (CE-6)
              new BigDecimal("45.99"),
              100,
              "Unidad",
              LocalDate.now());

      assertThrows(IllegalArgumentException.class, () -> controller.validateProductoCreate(dto));
    }

    @Test
    @Order(6)
    @DisplayName(
        "RP-CEI-[8]: Clase Inválida [1, 4, 8, 10, 13, 17, 21] - Categoría vacía (Valor vacío)")
    void RP_CEI_08_categoriaVacia() {
      ProductoCreateDTO dto =
          new ProductoCreateDTO(
              null, // Categoría inválida - vacía (CE-8)
              1,
              null,
              "Producto válido",
              "Descripción válida",
              new BigDecimal("45.99"),
              100,
              "Unidad",
              LocalDate.now());

      assertThrows(IllegalArgumentException.class, () -> controller.validateProductoCreate(dto));
    }

    @Test
    @Order(7)
    @DisplayName(
        "RP-CEI-[9]: Clase Inválida [1, 4, 9, 10, 13, 17, 21] - Categoría no válida (Valor no en lista)")
    void RP_CEI_09_categoriaNoValida() {
      ProductoCreateDTO dto =
          new ProductoCreateDTO(
              -1, // Categoría inválida - ID negativo (CE-9)
              1,
              null,
              "Producto válido",
              "Descripción válida",
              new BigDecimal("45.99"),
              100,
              "Unidad",
              LocalDate.now());

      assertThrows(IllegalArgumentException.class, () -> controller.validateProductoCreate(dto));
    }

    @Test
    @Order(8)
    @DisplayName(
        "RP-CEI-[11]: Clase Inválida [1, 4, 7, 11, 13, 17, 21] - Proveedor vacío (Valor vacío)")
    void RP_CEI_11_proveedorVacio() {
      // Nota: En este sistema el proveedor es opcional, pero si el DTO espera un valor
      // y se pasa null cuando debería ser requerido, esto podría fallar según la lógica de negocio
      // Para este test, validamos el comportamiento con proveedor null
      ProductoCreateDTO dto =
          new ProductoCreateDTO(
              1,
              null, // Proveedor vacío (CE-11) - es opcional en este sistema
              null,
              "Producto válido",
              "Descripción válida",
              new BigDecimal("45.99"),
              100,
              "Unidad",
              LocalDate.now());

      // Proveedor es opcional, por lo que null es válido
      assertDoesNotThrow(() -> controller.validateProductoCreate(dto));
    }

    @Test
    @Order(9)
    @DisplayName(
        "RP-CEI-[12]: Clase Inválida [1, 4, 7, 12, 13, 17, 21] - Proveedor no válido (Valor no en lista)")
    void RP_CEI_12_proveedorNoValido() {
      ProductoCreateDTO dto =
          new ProductoCreateDTO(
              1,
              -1, // Proveedor inválido - ID negativo (CE-12)
              null,
              "Producto válido",
              "Descripción válida",
              new BigDecimal("45.99"),
              100,
              "Unidad",
              LocalDate.now());

      assertThrows(IllegalArgumentException.class, () -> controller.validateProductoCreate(dto));
    }

    @Test
    @Order(10)
    @DisplayName(
        "RP-CEI-[14]: Clase Inválida [1, 4, 7, 10, 14, 17, 21] - Precio nulo (Letras/No numérico)")
    void RP_CEI_14_precioNulo() {
      ProductoCreateDTO dto =
          new ProductoCreateDTO(
              1,
              1,
              null,
              "Producto válido",
              "Descripción válida",
              null, // Precio inválido - nulo (CE-14)
              100,
              "Unidad",
              LocalDate.now());

      assertThrows(IllegalArgumentException.class, () -> controller.validateProductoCreate(dto));
    }

    @Test
    @Order(11)
    @DisplayName(
        "RP-CEI-[15]: Clase Inválida [1, 4, 7, 10, 15, 17, 21] - Precio negativo (Precio < 0)")
    void RP_CEI_15_precioNegativo() {
      ProductoCreateDTO dto =
          new ProductoCreateDTO(
              1,
              1,
              null,
              "Producto válido",
              "Descripción válida",
              new BigDecimal("-10.00"), // Precio inválido - negativo (CE-15)
              100,
              "Unidad",
              LocalDate.now());

      assertThrows(IllegalArgumentException.class, () -> controller.validateProductoCreate(dto));
    }

    @Test
    @Order(12)
    @DisplayName("RP-CEI-[15b]: Clase Inválida [1, 4, 7, 10, 15, 17, 21] - Precio igual a cero")
    void RP_CEI_15b_precioCero() {
      ProductoCreateDTO dto =
          new ProductoCreateDTO(
              1,
              1,
              null,
              "Producto válido",
              "Descripción válida",
              BigDecimal.ZERO, // Precio inválido - igual a cero (CE-15)
              100,
              "Unidad",
              LocalDate.now());

      assertThrows(IllegalArgumentException.class, () -> controller.validateProductoCreate(dto));
    }

    @Test
    @Order(13)
    @DisplayName(
        "RP-CEI-[16]: Clase Inválida [1, 4, 7, 10, 16, 17, 21] - Precio con más de 2 decimales (Longitud > 2 decimales)")
    void RP_CEI_16_precioMasDe2Decimales() {
      ProductoCreateDTO dto =
          new ProductoCreateDTO(
              1,
              1,
              null,
              "Producto válido",
              "Descripción válida",
              new BigDecimal("45.999"), // Precio inválido - 3 decimales (CE-16)
              100,
              "Unidad",
              LocalDate.now());

      assertThrows(IllegalArgumentException.class, () -> controller.validateProductoCreate(dto));
    }

    @Test
    @Order(14)
    @DisplayName(
        "RP-CEI-[18]: Clase Inválida [1, 4, 7, 10, 13, 18, 21] - Stock nulo (Letras/No numérico)")
    void RP_CEI_18_stockNulo() {
      ProductoCreateDTO dto =
          new ProductoCreateDTO(
              1,
              1,
              null,
              "Producto válido",
              "Descripción válida",
              new BigDecimal("45.99"),
              null, // Stock inválido - nulo (CE-18)
              "Unidad",
              LocalDate.now());

      assertThrows(IllegalArgumentException.class, () -> controller.validateProductoCreate(dto));
    }

    @Test
    @Order(15)
    @DisplayName(
        "RP-CEI-[19]: Clase Inválida [1, 4, 7, 10, 13, 19, 21] - Stock negativo (Stock < 0)")
    void RP_CEI_19_stockNegativo() {
      ProductoCreateDTO dto =
          new ProductoCreateDTO(
              1,
              1,
              null,
              "Producto válido",
              "Descripción válida",
              new BigDecimal("45.99"),
              -1, // Stock inválido - negativo (CE-19)
              "Unidad",
              LocalDate.now());

      assertThrows(IllegalArgumentException.class, () -> controller.validateProductoCreate(dto));
    }

    @Test
    @Order(16)
    @DisplayName(
        "RP-CEI-[20]: Clase Inválida [1, 4, 7, 10, 13, 20, 21] - Stock excede 999 (Stock > 999)")
    void RP_CEI_20_stockExcede999() {
      ProductoCreateDTO dto =
          new ProductoCreateDTO(
              1,
              1,
              null,
              "Producto válido",
              "Descripción válida",
              new BigDecimal("45.99"),
              1000, // Stock inválido - excede 999 (CE-20)
              "Unidad",
              LocalDate.now());

      assertThrows(IllegalArgumentException.class, () -> controller.validateProductoCreate(dto));
    }

    @Order(17)
    @ParameterizedTest(name = "RP-CEI-22-[{index}]: Unidad de medida vacía -> [{0}]")
    @NullAndEmptySource
    @ValueSource(strings = {"   "})
    @DisplayName(
        "RP-CEI-[22]: Clase Inválida [1, 4, 7, 10, 13, 17, 22] - Unidad de medida vacía (Valor vacío)")
    void RP_CEI_22_unidadMedidaVacia(String unidadMedida) {
      ProductoCreateDTO dto =
          new ProductoCreateDTO(
              1,
              1,
              null,
              "Producto válido",
              "Descripción válida",
              new BigDecimal("45.99"),
              100,
              unidadMedida, // Unidad de medida inválida - vacía (CE-22)
              LocalDate.now());

      assertThrows(IllegalArgumentException.class, () -> controller.validateProductoCreate(dto));
    }

    @Order(18)
    @ParameterizedTest(name = "RP-CEI-23-[{index}]: Unidad de medida no válida -> [{0}]")
    @ValueSource(strings = {"Metro", "Gramo", "Docena", "InvalidUnit", "123"})
    @DisplayName(
        "RP-CEI-[23]: Clase Inválida [1, 4, 7, 10, 13, 17, 23] - Unidad de medida no en lista (Valor que no esté en lista)")
    void RP_CEI_23_unidadMedidaNoEnLista(String unidadMedida) {
      ProductoCreateDTO dto =
          new ProductoCreateDTO(
              1,
              1,
              null,
              "Producto válido",
              "Descripción válida",
              new BigDecimal("45.99"),
              100,
              unidadMedida, // Unidad de medida inválida - no en lista (CE-23)
              LocalDate.now());

      assertThrows(IllegalArgumentException.class, () -> controller.validateProductoCreate(dto));
    }

    @Test
    @Order(19)
    @DisplayName(
        "RP-CEV-COMBO-01: Registrar Producto - Todos los campos válidos con valores límite")
    void RP_CEV_COMBO_01_todosLosCamposValidosConValoresLimite() {
      String nombreMaximo = "A".repeat(100);
      String descripcionMaxima = "B".repeat(100);
      ProductoCreateDTO dto =
          new ProductoCreateDTO(
              1, // categoriaId válido
              1, // proveedorId válido
              null, // codigo (autogenerado)
              nombreMaximo, // nombre válido - exactamente 100 caracteres
              descripcionMaxima, // descripción válida - exactamente 100 caracteres
              new BigDecimal("9999.99"), // precio válido - 2 decimales
              999, // stock válido - límite máximo
              "Kg", // unidad de medida válida
              LocalDate.now());

      assertDoesNotThrow(() -> controller.validateProductoCreate(dto));
    }

    @Test
    @Order(20)
    @DisplayName("RP-CEI-COMBO-01: Registrar Producto - Múltiples campos inválidos simultáneamente")
    void RP_CEI_COMBO_01_multiplesCamposInvalidos() {
      // Test con nombre vacío Y precio negativo Y stock excedido
      ProductoCreateDTO dto =
          new ProductoCreateDTO(
              null, // categoriaId inválido - null (CE-8)
              -1, // proveedorId inválido - negativo (CE-12)
              null,
              "", // nombre inválido - vacío (CE-2)
              "", // descripción inválida - vacía (CE-5)
              new BigDecimal("-10.00"), // precio inválido - negativo (CE-15)
              1500, // stock inválido - excede 999 (CE-20)
              "InvalidUnit", // unidad de medida inválida (CE-23)
              LocalDate.now());

      // Debe lanzar excepción por al menos uno de los campos inválidos
      assertThrows(IllegalArgumentException.class, () -> controller.validateProductoCreate(dto));
    }

    @Test
    @Order(21)
    @DisplayName("AP-CEV-01: Actualizar Producto - Todos los campos válidos")
    void AP_CEV_01_actualizarProductoTodosLosCamposValidos() {
      // Crear DTO de actualización con todos los campos válidos
      ProductoUpdateDTO dto = new ProductoUpdateDTO();
      dto.setCategoriaId(1); // categoriaId válido
      dto.setProveedorId(1); // proveedorId válido
      dto.setNombre("Proteína Actualizada"); // nombre válido
      dto.setDescripcion("Nueva descripción del producto"); // descripción válida
      dto.setPrecioUnitario(new BigDecimal("59.99")); // precio válido
      dto.setStock(500); // stock válido
      dto.setUnidadMedida("Caja"); // unidad de medida válida
      dto.setActivo(true);

      // ID válido para actualización
      int idValido = 1;

      assertDoesNotThrow(() -> controller.validateProductoUpdate(idValido, dto));
    }

    @Test
    @Order(22)
    @DisplayName("AP-CEV-02: Actualizar Producto - Solo campos parciales válidos")
    void AP_CEV_02_actualizarProductoCamposParciales() {
      // Actualización parcial - solo algunos campos
      ProductoUpdateDTO dto = new ProductoUpdateDTO();
      dto.setNombre("Nuevo Nombre Producto"); // solo actualizar nombre
      dto.setPrecioUnitario(new BigDecimal("75.50")); // solo actualizar precio

      int idValido = 1;

      assertDoesNotThrow(() -> controller.validateProductoUpdate(idValido, dto));
    }

    @Test
    @Order(23)
    @DisplayName("AP-CEI-01: Actualizar Producto - ID inválido (ID <= 0)")
    void AP_CEI_01_actualizarProductoIdInvalido() {
      ProductoUpdateDTO dto = new ProductoUpdateDTO();
      dto.setNombre("Producto válido");
      dto.setPrecioUnitario(new BigDecimal("45.99"));

      int idInvalido = 0; // ID inválido

      assertThrows(
          IllegalArgumentException.class, () -> controller.validateProductoUpdate(idInvalido, dto));
    }

    @Test
    @Order(24)
    @DisplayName("AP-CEI-02: Actualizar Producto - ID negativo")
    void AP_CEI_02_actualizarProductoIdNegativo() {
      ProductoUpdateDTO dto = new ProductoUpdateDTO();
      dto.setNombre("Producto válido");

      int idNegativo = -5; // ID negativo

      assertThrows(
          IllegalArgumentException.class, () -> controller.validateProductoUpdate(idNegativo, dto));
    }

    @Test
    @Order(25)
    @DisplayName("AP-CEI-03: Actualizar Producto - Nombre excede 100 caracteres")
    void AP_CEI_03_actualizarProductoNombreExcedeLongitud() {
      ProductoUpdateDTO dto = new ProductoUpdateDTO();
      dto.setNombre("A".repeat(101)); // nombre inválido - 101 caracteres

      int idValido = 1;

      assertThrows(
          IllegalArgumentException.class, () -> controller.validateProductoUpdate(idValido, dto));
    }

    @Test
    @Order(26)
    @DisplayName("AP-CEI-04: Actualizar Producto - Descripción excede 100 caracteres")
    void AP_CEI_04_actualizarProductoDescripcionExcedeLongitud() {
      ProductoUpdateDTO dto = new ProductoUpdateDTO();
      dto.setDescripcion("B".repeat(101)); // descripción inválida - 101 caracteres

      int idValido = 1;

      assertThrows(
          IllegalArgumentException.class, () -> controller.validateProductoUpdate(idValido, dto));
    }

    @Test
    @Order(27)
    @DisplayName("AP-CEI-05: Actualizar Producto - Categoría inválida (ID negativo)")
    void AP_CEI_05_actualizarProductoCategoriaInvalida() {
      ProductoUpdateDTO dto = new ProductoUpdateDTO();
      dto.setCategoriaId(-1); // categoría inválida

      int idValido = 1;

      assertThrows(
          IllegalArgumentException.class, () -> controller.validateProductoUpdate(idValido, dto));
    }

    @Test
    @Order(28)
    @DisplayName("AP-CEI-06: Actualizar Producto - Proveedor inválido (ID negativo)")
    void AP_CEI_06_actualizarProductoProveedorInvalido() {
      ProductoUpdateDTO dto = new ProductoUpdateDTO();
      dto.setProveedorId(-1); // proveedor inválido

      int idValido = 1;

      assertThrows(
          IllegalArgumentException.class, () -> controller.validateProductoUpdate(idValido, dto));
    }

    @Test
    @Order(29)
    @DisplayName("AP-CEI-07: Actualizar Producto - Precio negativo")
    void AP_CEI_07_actualizarProductoPrecioNegativo() {
      ProductoUpdateDTO dto = new ProductoUpdateDTO();
      dto.setPrecioUnitario(new BigDecimal("-25.00")); // precio inválido - negativo

      int idValido = 1;

      assertThrows(
          IllegalArgumentException.class, () -> controller.validateProductoUpdate(idValido, dto));
    }

    @Test
    @Order(30)
    @DisplayName("AP-CEI-08: Actualizar Producto - Precio con más de 2 decimales")
    void AP_CEI_08_actualizarProductoPrecioMasDe2Decimales() {
      ProductoUpdateDTO dto = new ProductoUpdateDTO();
      dto.setPrecioUnitario(new BigDecimal("45.999")); // precio inválido - 3 decimales

      int idValido = 1;

      assertThrows(
          IllegalArgumentException.class, () -> controller.validateProductoUpdate(idValido, dto));
    }

    @Test
    @Order(31)
    @DisplayName("AP-CEI-09: Actualizar Producto - Stock negativo")
    void AP_CEI_09_actualizarProductoStockNegativo() {
      ProductoUpdateDTO dto = new ProductoUpdateDTO();
      dto.setStock(-10); // stock inválido - negativo

      int idValido = 1;

      assertThrows(
          IllegalArgumentException.class, () -> controller.validateProductoUpdate(idValido, dto));
    }

    @Test
    @Order(32)
    @DisplayName("AP-CEI-10: Actualizar Producto - Stock excede 999")
    void AP_CEI_10_actualizarProductoStockExcede999() {
      ProductoUpdateDTO dto = new ProductoUpdateDTO();
      dto.setStock(1000); // stock inválido - excede 999

      int idValido = 1;

      assertThrows(
          IllegalArgumentException.class, () -> controller.validateProductoUpdate(idValido, dto));
    }

    @Test
    @Order(33)
    @DisplayName("AP-CEI-11: Actualizar Producto - Unidad de medida no válida")
    void AP_CEI_11_actualizarProductoUnidadMedidaNoValida() {
      ProductoUpdateDTO dto = new ProductoUpdateDTO();
      dto.setUnidadMedida("Metro"); // unidad de medida inválida - no en lista

      int idValido = 1;

      assertThrows(
          IllegalArgumentException.class, () -> controller.validateProductoUpdate(idValido, dto));
    }

    @Test
    @Order(34)
    @DisplayName("AP-CEI-12: Actualizar Producto - Múltiples campos inválidos simultáneamente")
    void AP_CEI_12_actualizarProductoMultiplesCamposInvalidos() {
      ProductoUpdateDTO dto = new ProductoUpdateDTO();
      dto.setCategoriaId(-1); // categoría inválida
      dto.setProveedorId(-1); // proveedor inválido
      dto.setNombre("A".repeat(101)); // nombre excede longitud
      dto.setPrecioUnitario(new BigDecimal("-50.00")); // precio negativo
      dto.setStock(5000); // stock excede 999
      dto.setUnidadMedida("InvalidUnit"); // unidad no válida

      int idValido = 1;

      // Debe lanzar excepción por al menos uno de los campos inválidos
      assertThrows(
          IllegalArgumentException.class, () -> controller.validateProductoUpdate(idValido, dto));
    }
  }
}
