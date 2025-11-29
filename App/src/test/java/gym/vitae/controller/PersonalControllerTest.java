package gym.vitae.controller;

import gym.vitae.model.dtos.empleado.EmpleadoCreateDTO;
import gym.vitae.model.enums.Genero;
import gym.vitae.model.enums.TipoContrato;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDate;

class PersonalControllerTest {

    @Nested
    @DisplayName("Validaciones - Test Estáticos")
    class PersonalControllerStaticTest {

        private PersonalController personalController;

        @BeforeEach
        void setUp() {
            personalController = new PersonalController(null, null);
        }

        @Test
        @DisplayName("RE-CEV-1: Clases Válidas [1, 5, 9, 13, 14, 19, 22, 26, 27, 28]")
        void RE_CEV_1_todasClasesValidas() {
            // Arrange
            EmpleadoCreateDTO dto = new EmpleadoCreateDTO(
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
                    null
            );

            // Act & Assert
            assertDoesNotThrow(() -> personalController.validateEmpleadoCreate(dto));
        }

    }
}
