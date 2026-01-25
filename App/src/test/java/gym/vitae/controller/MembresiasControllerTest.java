package gym.vitae.controller;

import static org.junit.jupiter.api.Assertions.*;

import gym.vitae.model.dtos.membresias.MembresiaCreateDTO;
import gym.vitae.model.dtos.membresias.MembresiaUpdateDTO;
import java.math.BigDecimal;
import java.time.LocalDate;
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

class MembresiasControllerTest {

    private final LocalDate hoy = LocalDate.now();
    private final LocalDate manana = hoy.plusDays(1);
    private final LocalDate duracionCorrecta = hoy.plusMonths(1);
    private final LocalDate duracionIncorrecta = hoy.plusDays(10);
    private final BigDecimal precioValido = BigDecimal.valueOf(100.00);
    private MembresiasController controller;

    @BeforeEach
    void setUp() {
        controller =
                new MembresiasController(
                        null,
                        null, 
                        null, 
                        null, 
                        null, 
                        null 
                );
    }

    @Nested
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
    @DisplayName("Tests de Combinaciones - Crear Membresía")
    class CreateMembresiaValidationTests {

        /** * RM [1, 5, 9, 11, 14, 18] - Clase Válida */
        @Test
        @Order(1)
        @DisplayName("RM [1, 5, 9, 11, 14, 18] - Válido (Fallo de Flujo: NullPointerException)")
        void RM_TodasClasesValidas() {
            MembresiaCreateDTO dto =
                    new MembresiaCreateDTO(1, 5, null, hoy, manana, precioValido, "Observación válida");
            assertThrows(NullPointerException.class, 
                         () -> controller.createMembresia(dto));
        }

        // 1. RM [2, 5, 9, 11, 14, 18] - Cliente Inactivo */
        @Test
        @Order(2)
        @DisplayName("RM [2] - Cliente Inactivo (Fallo de Flujo: NullPointerException)")
        void RM_2_ClienteInactivo() {
            MembresiaCreateDTO dto =
                    new MembresiaCreateDTO(2, 5, null, hoy, manana, precioValido, "Observación válida");
            assertThrows(NullPointerException.class, 
                         () -> controller.createMembresia(dto));
        }

        // 2. RM [3, 5, 9, 11, 14, 18] - Cliente Inexistente */
        @Test
        @Order(3)
        @DisplayName("RM [3] - Cliente Inexistente (Fallo de Flujo: NullPointerException)")
        void RM_3_ClienteInexistente() {
            MembresiaCreateDTO dto =
                    new MembresiaCreateDTO(999, 5, null, hoy, duracionCorrecta, precioValido, "Observación válida");
            assertThrows(NullPointerException.class, 
                         () -> controller.createMembresia(dto));
        }

        // 3. RM [4, 5, 9, 11, 14, 18] - ClienteId Nulo/Vacío */
        @Test
        @Order(4)
        @DisplayName("RM [4] - ClienteId Nulo (NullPointerException por unboxing)")
        void RM_4_ClienteIdNulo() {
            MembresiaCreateDTO dto =
                    new MembresiaCreateDTO(null, 5, null, hoy, manana, precioValido, "Observación válida");
            assertThrows(NullPointerException.class, 
                         () -> controller.createMembresia(dto));
        }

        // 4. RM [1, 6, 9, 11, 14, 18] - Tipo Membresía Inactivo */
        @Test
        @Order(5)
        @DisplayName("RM [6] - Tipo Membresía Inactivo (Fallo de Flujo: NullPointerException)")
        void RM_6_TipoMembresiaInactivo() {
            MembresiaCreateDTO dto =
                    new MembresiaCreateDTO(1, 6, null, hoy, duracionCorrecta, precioValido, "Observación válida");
            assertThrows(NullPointerException.class, 
                         () -> controller.createMembresia(dto));
        }

        // 5. RM [1, 7, 9, 11, 14, 18] - Tipo Membresía Inexistente */
        @Test
        @Order(6)
        @DisplayName("RM [7] - Tipo Membresía Inexistente (Fallo de Flujo: NullPointerException)")
        void RM_7_TipoMembresiaInexistente() {
            MembresiaCreateDTO dto =
                    new MembresiaCreateDTO(1, 7, null, hoy, manana, precioValido, "Observación válida");
            assertThrows(NullPointerException.class, 
                         () -> controller.createMembresia(dto));
        }

        // 6. RM [1, 8, 9, 11, 14, 18] - Tipo MembresiaId Nulo/Vacío */
        @Test
        @Order(7)
        @DisplayName("RM [8] - TipoMembresiaId Nulo (NullPointerException por unboxing)")
        void RM_8_TipoMembresiaIdNulo() {
            MembresiaCreateDTO dto =
                    new MembresiaCreateDTO(1, null, null, hoy, manana, precioValido, "Observación válida");
            assertThrows(NullPointerException.class,
                         () -> controller.createMembresia(dto));
        }

        // 7. RM [1, 5, 10, 11, 14, 18] - Fecha Inicio Anterior */
        @Test
        @Order(8)
        @DisplayName("RM [10] - Fecha Inicio Anterior (Fallo de Flujo: NullPointerException)")
        void RM_10_FechaInicioAnterior() {
            LocalDate ayer = hoy.minusDays(1);
            MembresiaCreateDTO dto =
                    new MembresiaCreateDTO(1, 5, null, ayer, manana, precioValido, "Observación válida");
            assertThrows(NullPointerException.class, 
                         () -> controller.createMembresia(dto));
        }

        // 8. RM [1, 5, 9, 12, 15, 18] - Fecha Fin Incorrecta  */
        @Test
        @Order(9)
        @DisplayName("RM [12] - Fecha Fin Incorrecta (Fallo de Flujo: NullPointerException)")
        void RM_12_FechaFinIncorrecta() {
            MembresiaCreateDTO dto =
                    new MembresiaCreateDTO(1, 5, null, hoy, duracionIncorrecta, precioValido, "Observación válida");
            assertThrows(NullPointerException.class, 
                         () -> controller.createMembresia(dto));
        }

        // 9. RM [1, 5, 9, 13, 15, 18] - Fecha Nula/Formato inválido */
        @Test
        @Order(10)
        @DisplayName("RM [13a] - Fecha Inicio Nula (IllegalArgumentException)")
        void RM_13a_FechaInicioNula() {
            MembresiaCreateDTO dto =
                    new MembresiaCreateDTO(1, 5, null, null, manana, precioValido, "Observación válida");
            assertThrows(IllegalArgumentException.class, 
                         () -> controller.createMembresia(dto));
        }
        
        // Caso auxiliar de Fecha Nula (Fecha Fin) */
        @Test
        @Order(11)
        @DisplayName("RM [13b] - Fecha Fin Nula (IllegalArgumentException)")
        void RM_13b_FechaFinNula() {
            MembresiaCreateDTO dto =
                    new MembresiaCreateDTO(1, 5, null, hoy, null, precioValido, "Observación válida");
            assertThrows(IllegalArgumentException.class, 
                         () -> controller.createMembresia(dto));
        }

        // 10. RM [1, 5, 9, 11, 15, 18] - Precio Pagado */
        @Test
        @Order(12)
        @DisplayName("RM [15a] - Precio Pagado Cero (IllegalArgumentException)")
        void RM_15a_PrecioCero() {
            MembresiaCreateDTO dto =
                    new MembresiaCreateDTO(1, 5, null, hoy, manana, BigDecimal.ZERO, "Observación válida");
            assertThrows(IllegalArgumentException.class, 
                         () -> controller.createMembresia(dto));
        }
        
        // RM [1, 5, 9, 11, 15, 18]: Precio Pagado Negativo */
        @Test
        @Order(13)
        @DisplayName("RM [15b] - Precio Pagado Negativo (IllegalArgumentException)")
        void RM_15b_PrecioNegativo() {
            MembresiaCreateDTO dto =
                    new MembresiaCreateDTO(1, 5, null, hoy, manana, BigDecimal.valueOf(-10.00), "Observación válida");
            assertThrows(IllegalArgumentException.class, 
                         () -> controller.createMembresia(dto));
        }

        // 11. RM [1, 5, 9, 11, 16, 18] - Precio Pagado */
        @Test
        @Order(14)
        @DisplayName("RM [16] - Precio Mayor Al Limite (Fallo de Flujo: NullPointerException)")
        void RM_16_PrecioMayorAlLimite() {
            BigDecimal precioAlto = BigDecimal.valueOf(10001.00); 
            MembresiaCreateDTO dto =
                    new MembresiaCreateDTO(1, 5, null, hoy, manana, precioAlto, "Observación válida");
            assertThrows(NullPointerException.class, 
                         () -> controller.createMembresia(dto));
        }

        // 12. RM [1, 5, 9, 11, 17, 18] - Precio Pagado */
        @Test
        @Order(15)
        @DisplayName("RM [17] - Precio Pagado Nulo (IllegalArgumentException)")
        void RM_17_PrecioNulo() {
            MembresiaCreateDTO dto =
                    new MembresiaCreateDTO(1, 5, null, hoy, manana, null, "Observación válida");
            assertThrows(IllegalArgumentException.class, 
                         () -> controller.createMembresia(dto));
        }
        
        // 13. RM [1, 5, 9, 11, 14, 19] - Observaciones */
        @Test
        @Order(16)
        @DisplayName("RM [19] - Observaciones Largas (Fallo de Flujo: NullPointerException)")
        void RM_19_ObservacionesLargas() {
            String obsLarga = "A".repeat(201); 
            MembresiaCreateDTO dto =
                    new MembresiaCreateDTO(1, 5, null, hoy, manana, precioValido, obsLarga);
            assertThrows(NullPointerException.class, 
                         () -> controller.createMembresia(dto));
        }

        
        /** * Caso Especial: DTO Nulo */
        @Test
        @Order(17)
        @DisplayName("RM - DTO")
        void RM_DTONulo() {
            assertThrows(IllegalArgumentException.class, 
                         () -> controller.createMembresia(null));
        }
    }
    
    @Nested
    @DisplayName("Tests de Flujo y Otros Métodos (Fallo en Repositorio)")
    class FlowTests {
        
        @Test
        @DisplayName("getMembresias - Falla por Repositorio nulo")
        void getMembresias_ShouldThrowNullPointerException() {
            assertThrows(NullPointerException.class, 
                         () -> controller.getMembresias());
        }

        @Test
        @DisplayName("getMembresiaById - Falla por ID Válido (Flujo de Repositorio Nulo)")
        void getMembresiaById_ShouldThrowNullPointerException() {
            assertThrows(NullPointerException.class, 
                         () -> controller.getMembresiaById(1));
        }

        @DisplayName("getMembresiaById - Debe lanzar IllegalArgumentException para ID Inválido") 
        @ValueSource(ints = {0, -1})
        @ParameterizedTest(name = "ID: {0}") 
        void getMembresiaById_ShouldThrowIllegalArgumentExceptionForInvalidId(int id) { 
            assertThrows(IllegalArgumentException.class, 
            () -> controller.getMembresiaById(id));
            }
        
        @Test
        @DisplayName("updateMembresia - Falla por ID Válido (Flujo de Repositorio Nulo)")
        void updateMembresia_ShouldThrowNullPointerException() {
            MembresiaUpdateDTO dto = new MembresiaUpdateDTO();
            assertThrows(NullPointerException.class, 
                         () -> controller.updateMembresia(1, dto));
        }
        
        @Test
        @DisplayName("updateMembresia - DTO Nulo (Validación estática)")
        void updateMembresia_DTONulo_ShouldThrowIllegalArgumentException() {
            assertThrows(IllegalArgumentException.class, 
                         () -> controller.updateMembresia(1, null));
        }

        @Test
        @DisplayName("cancelarMembresia - Falla por Repositorio nulo")
        void cancelarMembresia_ShouldThrowNullPointerException() {
            assertThrows(NullPointerException.class, 
                         () -> controller.cancelarMembresia(1));
        }

        @DisplayName("cancelarMembresia - Debe lanzar IllegalArgumentException para ID Inválido") 
        @ValueSource(ints = {0, -1})
        @ParameterizedTest(name = "ID: {0}") 
        void cancelarMembresia_ShouldThrowIllegalArgumentExceptionForInvalidId(int id) {
            assertThrows(IllegalArgumentException.class, 
            () -> controller.cancelarMembresia(id));
        }
    }
}