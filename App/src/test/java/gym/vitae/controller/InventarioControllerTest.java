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

    // ==================== REGISTRAR PRODUCTO ====================

    @Test
    @Order(1)
    @DisplayName("RP-CEV [1, 4, 7, 10, 12, 16, 20] - Todas las clases válidas")
    void RP_CEV_todasClasesValidas() {
      ProductoCreateDTO dto =
          new ProductoCreateDTO(
              1, // categoriaId válido (7)
              1, // proveedorId válido (10)
              null, // codigo (autogenerado)
              "Proteína Whey", // nombre válido 1 ≤ longitud ≤ 100 (1)
              "Suplemento proteico", // descripción válida 1 ≤ longitud ≤ 100 (4)
              new BigDecimal("45.99"), // precio válido decimal > 0, 2 decimales (12)
              100, // stock válido 1 ≤ stock ≤ 999 (16)
              "Unidad", // unidad de medida válida (20)
              LocalDate.now() // fecha ingreso
              );

      assertDoesNotThrow(() -> controller.validateProductoCreate(dto));
    }

    @Order(2)
    @ParameterizedTest(name = "RP-CEI-[2,3] [{index}] {0}")
    @NullAndEmptySource
    @ValueSource(
        strings = {
          "   ",
          "NombreQueExcedeLosLimitesDeCienCaracteresYPorLoTantoDeberiaFallarLaValidacionDelControladorDeInventarioXX"
        })
    @DisplayName("RP-CEI-[2,3]: Clases Inválidas [-, 4, 7, 10, 12, 16, 20] Campo Nombre")
    void RP_CEI_NOMBRE(String nombre) {
      ProductoCreateDTO dto =
          new ProductoCreateDTO(
              1,
              1,
              null,
              nombre, // Nombre inválido
              "Descripción válida",
              new BigDecimal("45.99"),
              100,
              "Unidad",
              LocalDate.now());

      assertThrows(
          IllegalArgumentException.class,
          () -> {
            controller.validateProductoCreate(dto);
          });
    }

    @Order(3)
    @ParameterizedTest(name = "RP-CEI-[5,6] [{index}] {0}")
    @ValueSource(
        strings = {
          " ",
          "DescripcionQueExcedeLosLimitesDeCienCaracteresYPorLoTantoDeberiaFallarLaValidacionDelControladorInventarioX"
        })
    @DisplayName("RP-CEI-[5,6]: Clases Inválidas [1, -, 7, 10, 12, 16, 20] Campo Descripción")
    void RP_CEI_DESCRIPCION(String descripcion) {
      ProductoCreateDTO dto =
          new ProductoCreateDTO(
              1,
              1,
              null,
              "Producto válido",
              descripcion, // Descripción inválida
              new BigDecimal("45.99"),
              100,
              "Unidad",
              LocalDate.now());

      assertThrows(
          IllegalArgumentException.class,
          () -> {
            controller.validateProductoCreate(dto);
          });
    }

    @Order(4)
    @ParameterizedTest(name = "RP-CEI-[8,9] [{index}] categoriaId={0}")
    @ValueSource(ints = {-1, 0, -100})
    @DisplayName("RP-CEI-[8,9]: Clases Inválidas [1, 4, -, 10, 12, 16, 20] Campo Categoría")
    void RP_CEI_CATEGORIA(int categoriaId) {
      ProductoCreateDTO dto =
          new ProductoCreateDTO(
              categoriaId, // Categoría inválida
              1,
              null,
              "Producto válido",
              "Descripción válida",
              new BigDecimal("45.99"),
              100,
              "Unidad",
              LocalDate.now());

      assertThrows(
          IllegalArgumentException.class,
          () -> {
            controller.validateProductoCreate(dto);
          });
    }

    @Test
    @Order(5)
    @DisplayName(
        "RP-CEI-[11]: Clase Inválida [1, 4, 7, 11, 12, 16, 20] - Proveedor vacío (Opcional)")
    void RP_CEI_11_proveedorVacio() {
      ProductoCreateDTO dto =
          new ProductoCreateDTO(
              1,
              null, // Proveedor vacío - es opcional en este sistema
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

    @Order(6)
    @ParameterizedTest(name = "RP-CEI-[13,14,15] [{index}] precio={0}")
    @ValueSource(doubles = {-10.00, 0, 45.999, -0.01})
    @DisplayName("RP-CEI-[13,14,15]: Clases Inválidas [1, 4, 7, 10, -, 16, 20] Campo Precio")
    void RP_CEI_PRECIO(double precioValue) {
      BigDecimal precio = BigDecimal.valueOf(precioValue);
      ProductoCreateDTO dto =
          new ProductoCreateDTO(
              1,
              1,
              null,
              "Producto válido",
              "Descripción válida",
              precio, // Precio inválido
              100,
              "Unidad",
              LocalDate.now());

      assertThrows(
          IllegalArgumentException.class,
          () -> {
            controller.validateProductoCreate(dto);
          });
    }

    @Order(7)
    @ParameterizedTest(name = "RP-CEI-[17,18,19] [{index}] stock={0}")
    @ValueSource(ints = {-1, -100, 1000, 5000})
    @DisplayName("RP-CEI-[17,18,19]: Clases Inválidas [1, 4, 7, 10, 12, -, 20] Campo Stock")
    void RP_CEI_STOCK(int stock) {
      ProductoCreateDTO dto =
          new ProductoCreateDTO(
              1,
              1,
              null,
              "Producto válido",
              "Descripción válida",
              new BigDecimal("45.99"),
              stock, // Stock inválido
              "Unidad",
              LocalDate.now());

      assertThrows(
          IllegalArgumentException.class,
          () -> {
            controller.validateProductoCreate(dto);
          });
    }

    @Order(8)
    @ParameterizedTest(name = "RP-CEI-[21] [{index}] {0}")
    @NullAndEmptySource
    @ValueSource(strings = {"   "})
    @DisplayName("RP-CEI-[21]: Clases Inválidas [1, 4, 7, 10, 12, 16, -] Campo Unidad de Medida")
    void RP_CEI_UNIDAD_MEDIDA(String unidadMedida) {
      ProductoCreateDTO dto =
          new ProductoCreateDTO(
              1,
              1,
              null,
              "Producto válido",
              "Descripción válida",
              new BigDecimal("45.99"),
              100,
              unidadMedida, // Unidad de medida inválida
              LocalDate.now());

      assertThrows(
          IllegalArgumentException.class,
          () -> {
            controller.validateProductoCreate(dto);
          });
    }

    @Test
    @Order(9)
    @DisplayName("RP-CEV-COMBO: Registrar Producto - Todos los campos válidos con valores límite")
    void RP_CEV_COMBO_todosLosCamposValidosConValoresLimite() {
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
    @Order(10)
    @DisplayName("RP-CEI-COMBO: Registrar Producto - Múltiples campos inválidos simultáneamente")
    void RP_CEI_COMBO_multiplesCamposInvalidos() {
      ProductoCreateDTO dto =
          new ProductoCreateDTO(
              null, // categoriaId inválido
              null, // proveedorId vacío (opcional)
              null,
              "", // nombre inválido - vacío
              "", // descripción inválida - vacía
              new BigDecimal("-10.00"), // precio inválido - negativo
              1500, // stock inválido - excede 999
              "", // unidad de medida inválida - vacía
              LocalDate.now());

      assertThrows(
          IllegalArgumentException.class,
          () -> {
            controller.validateProductoCreate(dto);
          });
    }

    // ==================== ACTUALIZAR PRODUCTO ====================

    @Test
    @Order(11)
    @DisplayName("AP-CEV-01: Actualizar Producto - Todos los campos válidos")
    void AP_CEV_01_actualizarProductoTodosLosCamposValidos() {
      ProductoUpdateDTO dto = new ProductoUpdateDTO();
      dto.setCategoriaId(1);
      dto.setProveedorId(1);
      dto.setNombre("Proteína Actualizada");
      dto.setDescripcion("Nueva descripción del producto");
      dto.setPrecioUnitario(new BigDecimal("59.99"));
      dto.setStock(500);
      dto.setUnidadMedida("Caja");
      dto.setActivo(true);

      int idValido = 1;

      assertDoesNotThrow(() -> controller.validateProductoUpdate(idValido, dto));
    }

    @Test
    @Order(12)
    @DisplayName("AP-CEV-02: Actualizar Producto - Solo campos parciales válidos")
    void AP_CEV_02_actualizarProductoCamposParciales() {
      ProductoUpdateDTO dto = new ProductoUpdateDTO();
      dto.setNombre("Nuevo Nombre Producto");
      dto.setPrecioUnitario(new BigDecimal("75.50"));

      int idValido = 1;

      assertDoesNotThrow(() -> controller.validateProductoUpdate(idValido, dto));
    }

    @Order(13)
    @ParameterizedTest(name = "AP-CEI-[ID] [{index}] id={0}")
    @ValueSource(ints = {0, -1, -5, -100})
    @DisplayName("AP-CEI-[ID]: Clases Inválidas - Campo ID del Producto")
    void AP_CEI_ID(int idInvalido) {
      ProductoUpdateDTO dto = new ProductoUpdateDTO();
      dto.setNombre("Producto válido");
      dto.setPrecioUnitario(new BigDecimal("45.99"));

      assertThrows(
          IllegalArgumentException.class,
          () -> {
            controller.validateProductoUpdate(idInvalido, dto);
          });
    }

    @Order(14)
    @ParameterizedTest(name = "AP-CEI-[Nombre] [{index}] {0}")
    @ValueSource(
        strings = {
          "NombreQueExcedeLosLimitesDeCienCaracteresYPorLoTantoDeberiaFallarLaValidacionDelControladorDeInventarioXX"
        })
    @DisplayName("AP-CEI-[Nombre]: Clases Inválidas - Campo Nombre en Actualizar")
    void AP_CEI_NOMBRE(String nombre) {
      ProductoUpdateDTO dto = new ProductoUpdateDTO();
      dto.setNombre(nombre);

      int idValido = 1;

      assertThrows(
          IllegalArgumentException.class,
          () -> {
            controller.validateProductoUpdate(idValido, dto);
          });
    }

    @Order(15)
    @ParameterizedTest(name = "AP-CEI-[Descripción] [{index}] {0}")
    @ValueSource(
        strings = {
          "DescripcionQueExcedeLosLimitesDeCienCaracteresYPorLoTantoDeberiaFallarLaValidacionDelControladorInventarioX"
        })
    @DisplayName("AP-CEI-[Descripción]: Clases Inválidas - Campo Descripción en Actualizar")
    void AP_CEI_DESCRIPCION(String descripcion) {
      ProductoUpdateDTO dto = new ProductoUpdateDTO();
      dto.setDescripcion(descripcion);

      int idValido = 1;

      assertThrows(
          IllegalArgumentException.class,
          () -> {
            controller.validateProductoUpdate(idValido, dto);
          });
    }

    @Order(16)
    @ParameterizedTest(name = "AP-CEI-[Categoría] [{index}] categoriaId={0}")
    @ValueSource(ints = {-1, -5, 0})
    @DisplayName("AP-CEI-[Categoría]: Clases Inválidas - Campo Categoría en Actualizar")
    void AP_CEI_CATEGORIA(int categoriaId) {
      ProductoUpdateDTO dto = new ProductoUpdateDTO();
      dto.setCategoriaId(categoriaId);

      int idValido = 1;

      assertThrows(
          IllegalArgumentException.class,
          () -> {
            controller.validateProductoUpdate(idValido, dto);
          });
    }

    @Order(17)
    @ParameterizedTest(name = "AP-CEI-[Precio] [{index}] precio={0}")
    @ValueSource(doubles = {-25.00, -0.01, 45.999, 0.001})
    @DisplayName("AP-CEI-[Precio]: Clases Inválidas - Campo Precio en Actualizar")
    void AP_CEI_PRECIO(double precioValue) {
      BigDecimal precio = BigDecimal.valueOf(precioValue);
      ProductoUpdateDTO dto = new ProductoUpdateDTO();
      dto.setPrecioUnitario(precio);

      int idValido = 1;

      assertThrows(
          IllegalArgumentException.class,
          () -> {
            controller.validateProductoUpdate(idValido, dto);
          });
    }

    @Order(18)
    @ParameterizedTest(name = "AP-CEI-[Stock] [{index}] stock={0}")
    @ValueSource(ints = {-10, -1, 1000, 5000})
    @DisplayName("AP-CEI-[Stock]: Clases Inválidas - Campo Stock en Actualizar")
    void AP_CEI_STOCK(int stock) {
      ProductoUpdateDTO dto = new ProductoUpdateDTO();
      dto.setStock(stock);

      int idValido = 1;

      assertThrows(
          IllegalArgumentException.class,
          () -> {
            controller.validateProductoUpdate(idValido, dto);
          });
    }

    @Order(19)
    @ParameterizedTest(name = "AP-CEI-[Unidad] [{index}] {0}")
    @ValueSource(strings = {"", "   "})
    @DisplayName("AP-CEI-[Unidad]: Clases Inválidas - Campo Unidad de Medida en Actualizar")
    void AP_CEI_UNIDAD_MEDIDA(String unidadMedida) {
      ProductoUpdateDTO dto = new ProductoUpdateDTO();
      dto.setUnidadMedida(unidadMedida);

      int idValido = 1;

      assertThrows(
          IllegalArgumentException.class,
          () -> {
            controller.validateProductoUpdate(idValido, dto);
          });
    }

    @Test
    @Order(20)
    @DisplayName("AP-CEI-COMBO: Actualizar Producto - Múltiples campos inválidos simultáneamente")
    void AP_CEI_COMBO_actualizarProductoMultiplesCamposInvalidos() {
      ProductoUpdateDTO dto = new ProductoUpdateDTO();
      dto.setCategoriaId(-1);
      dto.setNombre("A".repeat(101));
      dto.setPrecioUnitario(new BigDecimal("-50.00"));
      dto.setStock(5000);
      dto.setUnidadMedida("");

      int idValido = 1;

      assertThrows(
          IllegalArgumentException.class,
          () -> {
            controller.validateProductoUpdate(idValido, dto);
          });
    }

    // ==================== MÉTODOS DE REGISTRO Y ACTUALIZACIÓN ====================

    @Test
    @Order(21)
    @DisplayName("Método de Registrar Producto - Válidos")
    void METODO_REGISTRAR_PRODUCTO_VALIDO() {
      ProductoCreateDTO dto =
          new ProductoCreateDTO(
              1,
              1,
              null,
              "Proteína Whey",
              "Suplemento proteico",
              new BigDecimal("45.99"),
              100,
              "Unidad",
              LocalDate.now());

      assertDoesNotThrow(() -> controller.validateProductoCreate(dto));
    }

    @Order(22)
    @ParameterizedTest(name = "Método Registrar Producto - Campo Nombre Inválido [{index}] {0}")
    @ValueSource(
        strings = {
          "",
          "   ",
          "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"
        })
    @DisplayName("Método de Registrar Producto - Campo Nombre Inválidos")
    void METODO_REGISTRAR_PRODUCTO_INVALIDO(String nombre) {
      ProductoCreateDTO dto =
          new ProductoCreateDTO(
              1,
              1,
              null,
              nombre,
              "Descripción válida",
              new BigDecimal("45.99"),
              100,
              "Unidad",
              LocalDate.now());

      assertThrows(
          IllegalArgumentException.class,
          () -> {
            controller.validateProductoCreate(dto);
          });
    }

    @Test
    @Order(23)
    @DisplayName("Método de Actualizar Producto - Válidos")
    void METODO_ACTUALIZAR_PRODUCTO_VALIDO() {
      ProductoUpdateDTO dto = new ProductoUpdateDTO();
      dto.setCategoriaId(1);
      dto.setProveedorId(1);
      dto.setNombre("Producto Actualizado");
      dto.setDescripcion("Descripción actualizada");
      dto.setPrecioUnitario(new BigDecimal("59.99"));
      dto.setStock(500);
      dto.setUnidadMedida("Caja");
      dto.setActivo(true);

      assertDoesNotThrow(() -> controller.validateProductoUpdate(1, dto));
    }

    @Order(24)
    @ParameterizedTest(
        name = "Método Actualizar Producto - Campo Precio Inválido [{index}] precio={0}")
    @ValueSource(doubles = {-25.00, 0, 45.999})
    @DisplayName("Método de Actualizar Producto - Campo Precio Inválidos")
    void METODO_ACTUALIZAR_PRODUCTO_INVALIDO(double precioValue) {
      BigDecimal precio = BigDecimal.valueOf(precioValue);
      ProductoUpdateDTO dto = new ProductoUpdateDTO();
      dto.setPrecioUnitario(precio);

      assertThrows(
          IllegalArgumentException.class,
          () -> {
            controller.validateProductoUpdate(1, dto);
          });
    }
  }
}
