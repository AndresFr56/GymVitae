package gym.vitae.controller;

import static org.junit.jupiter.api.Assertions.*;

import gym.vitae.model.dtos.membresias.BeneficioCreateDTO;
import gym.vitae.model.dtos.membresias.BeneficioUpdateDTO;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class BeneficiosControllerTest {

    private BeneficiosController beneficiosController;

    @BeforeEach
    void setUp() {
        beneficiosController = new BeneficiosController(null);
    }

    @Nested
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
    @DisplayName("Tests Estáticos - Crear Beneficio (Equivalencia)")
    class CreateBeneficioValidationTests {

        /** RB [1, 5, 7]
         * 
         */
        @Test
        @Order(1)
        @DisplayName("RB [1, 5, 7] ")
        void RB_1_5_7_ClasesValidas() {
            BeneficioCreateDTO dto = new BeneficioCreateDTO();
            dto.setNombre("Nombre de Beneficio"); 
            dto.setDescripcion("Descripción corta."); 
            dto.setActivo(true); 

            assertThrows(NullPointerException.class, 
                         () -> beneficiosController.createBeneficio(dto));
        }
        

        /** RB [2, 5, 7]
         * 
         */
        @Test
        @Order(2)
        @DisplayName("RB [2, 5, 7] ")
        void RB_2_5_7_NombreVacio() {
            BeneficioCreateDTO dto = new BeneficioCreateDTO();
            dto.setNombre(""); 
            dto.setDescripcion("Descripción válida."); 
            dto.setActivo(true); 

            assertThrows(IllegalArgumentException.class, 
                         () -> beneficiosController.createBeneficio(dto));
        }

        /** * RB [3, 5, 7]
         * 
         */
        @Test
        @Order(3)
        @DisplayName("RB [3, 5, 7] ")
        void RB_3_5_7_NombreLargo() {
            String nombreLargo = "Nombre muy muy muy muy muy muy muy muy muy muy muy muy muy muy muy muy muy muy largo con mas de cincuenta caracteres";
            BeneficioCreateDTO dto = new BeneficioCreateDTO();
            dto.setNombre(nombreLargo); 
            dto.setDescripcion("Descripción válida."); 
            dto.setActivo(true); 

            assertThrows(IllegalArgumentException.class, 
                         () -> beneficiosController.createBeneficio(dto));
        }
        
        /** * RB [4, 5, 7]
         *
         */
        @Test
        @Order(4)
        @DisplayName("RB [4, 5, 7] - Nombre con Símbolos/Números (IllegalArgumentException)")
        void RB_4_5_7_NombreConSimbolos() {
            BeneficioCreateDTO dto = new BeneficioCreateDTO();
            dto.setNombre("Nombre con numeros 123!"); 
            dto.setDescripcion("Descripción válida."); 
            dto.setActivo(true); 

            assertThrows(IllegalArgumentException.class, 
                         () -> beneficiosController.createBeneficio(dto));
        }
        

        /** * RB [1, 6, 7]:
         * 
         */
        @Test
        @Order(5)
        @DisplayName("CEI [1, 6, 7]")
        void RB_1_6_7_DescripcionInvalida() {
            String descripcionLarga = "A".repeat(201);
            BeneficioCreateDTO dto = new BeneficioCreateDTO();
            dto.setNombre("Nombre Válido"); 
            dto.setDescripcion(descripcionLarga); 
            dto.setActivo(true); 

            assertThrows(IllegalArgumentException.class, 
                         () -> beneficiosController.createBeneficio(dto));
        }
        

        /**
         * Caso especial
         *
         */
        @Test
        @Order(6)
        @DisplayName("RB - DTO Nulo")
        void RB_DTONulo_ValidacionInicial() {
            assertThrows(
                IllegalArgumentException.class, 
                () -> beneficiosController.createBeneficio(null)
            );
        }
        
        /**
         * Comportamiento de Flujo
         * 
         */
        @Test
        @Order(7)
        @DisplayName("Flujo Fallido")
        void createBeneficio_ShouldFailAfterValidationDueToNullRepository() {
            BeneficioCreateDTO dto = new BeneficioCreateDTO();
            dto.setNombre("Nombre Válido");
            dto.setDescripcion("Descripción Válida");
            dto.setActivo(true);
            
            assertThrows(IllegalArgumentException.class, 
                         () -> beneficiosController.createBeneficio(dto));
        }
    }

    @Nested
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
    @DisplayName("Tests Estáticos - Actualizar Beneficio")
    class UpdateBeneficioValidationTests {
        
        @Test
        @Order(1)
        @DisplayName("RB [1, 5] - Válido")
        void CEV_1_5_todasClasesValidas() {
            BeneficioUpdateDTO dto = new BeneficioUpdateDTO();
            dto.setNombre("Nombre Actualizado"); 
            dto.setDescripcion("Descripción actualizada corta."); 
            dto.setActivo(false); 

            assertThrows(NullPointerException.class, 
                         () -> beneficiosController.updateBeneficio(1, dto));
        }
        
        @Test
        @Order(2)
        @DisplayName("RB [2] - Nombre Vacío")
        void RB_Update_2_NombreVacio() {
            BeneficioUpdateDTO dto = new BeneficioUpdateDTO();
            dto.setNombre(""); 
            dto.setDescripcion("Descripción válida."); 
            dto.setActivo(true); 

            // ACT & ASSERT
            assertThrows(NullPointerException.class, 
                         () -> beneficiosController.updateBeneficio(1, dto));
        }
        
        @Test
        @Order(3)
        @DisplayName("RB [3]")
        void RB_Update_3_NombreLargo() {
            String nombreLargo = "Nombre muy muy muy muy muy muy muy muy muy muy muy muy muy muy muy muy muy muy largo con mas de cincuenta caracteres";
            BeneficioUpdateDTO dto = new BeneficioUpdateDTO();
            dto.setNombre(nombreLargo); 
            dto.setDescripcion("Descripción válida."); 
            dto.setActivo(true); 

            assertThrows(NullPointerException.class, 
                         () -> beneficiosController.updateBeneficio(1, dto));
        }

        @Test
        @Order(4)
        @DisplayName("RB [4]")
        void RB_Update_4_NombreConSimbolos() {
            BeneficioUpdateDTO dto = new BeneficioUpdateDTO();
            dto.setNombre("Nombre con numeros 123!"); 
            dto.setDescripcion("Descripción válida."); 
            dto.setActivo(true); 

            assertThrows(NullPointerException.class, 
                         () -> beneficiosController.updateBeneficio(1, dto));
        }

        @Test
        @Order(5)
        @DisplayName("RB [6]")
        void RB_Update_6_DescripcionInvalida() {
            String descripcionLarga = "A".repeat(201);
            
            BeneficioUpdateDTO dto = new BeneficioUpdateDTO();
            dto.setNombre("Nombre Válido"); 
            dto.setDescripcion(descripcionLarga); 
            dto.setActivo(true); 

            assertThrows(NullPointerException.class, 
                         () -> beneficiosController.updateBeneficio(1, dto));
        }
    }

    @Nested
    @DisplayName("Tests de Flujo")
    class FlowTests {
        
        @Test
        @DisplayName("getBeneficios - Repositorio nulo")
        void getBeneficios_ShouldThrowNullPointerException() {
            assertThrows(NullPointerException.class, 
                         () -> beneficiosController.getBeneficios());
        }

        @Test
        @DisplayName("getBeneficioById - ID Válido")
        void getBeneficioById_ShouldThrowNullPointerException() {
            assertThrows(NullPointerException.class, 
                         () -> beneficiosController.getBeneficioById(1));
        }
        @Test
        @DisplayName("deleteBeneficio - Repositorio nulo")
        void deleteBeneficio_ShouldThrowNullPointerException() {
            assertThrows(NullPointerException.class, 
                         () -> beneficiosController.deleteBeneficio(1));
        }
    }
}