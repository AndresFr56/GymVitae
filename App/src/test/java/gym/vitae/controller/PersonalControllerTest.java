package gym.vitae.controller;

import gym.vitae.model.enums.EstadoEmpleado;
import gym.vitae.model.enums.Genero;
import gym.vitae.model.enums.TipoContrato;
import gym.vitae.repositories.CargoRepository;
import org.junit.jupiter.api.*;
import gym.vitae.model.Empleado;
import gym.vitae.repositories.EmpleadoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(MockitoExtension.class)
@DisplayName("Modulo de Empleados")
class PersonalControllerTest {

  @Mock private EmpleadoRepository mockEmpleadoRepository;
  @Mock private CargoRepository mockCargoRepository;

  private PersonalController controller;

  @BeforeEach
  void setUp() {
    controller = new PersonalController(mockEmpleadoRepository, mockCargoRepository);
  }

  @Test
  @DisplayName("Debe retornar lista vacía cuando no hay empleados")
  void getEmpleados_DebeRetornarListaVacia_CuandoNoHayEmpleados() {
    // simular que el repositorio retorna una lista vacía
    when(mockEmpleadoRepository.findAll()).thenReturn(Collections.emptyList());

    // Act
    List<Empleado> resultado = controller.getEmpleados();

    // Assert
    assertNotNull(resultado);
    assertTrue(resultado.isEmpty());
    assertEquals(0, resultado.size());

    verify(mockEmpleadoRepository, times(1)).findAll();
  }

  @Test
  @DisplayName("Debe retornar lista con empleados cuando existen registros")
  void getEmpleados_DebeRetornarListaConEmpleados_CuandoExistenEmpleados() {

    // Arrange - Crear empleados SIN mockear (usando new directamente)
    Empleado empleado1 = new Empleado();
    empleado1.setId(1);
    empleado1.setCodigoEmpleado("EMP001");
    empleado1.setNombres("Juan");
    empleado1.setApellidos("Pérez García");
    empleado1.setCedula("0912345678");
    empleado1.setGenero(Genero.MASCULINO);
    empleado1.setTelefono("0987654321");
    empleado1.setEmail("juan.perez@gym.com");
    empleado1.setFechaIngreso(LocalDate.of(2023, 1, 15));
    empleado1.setTipoContrato(TipoContrato.TIEMPO_COMPLETO);
    empleado1.setEstado(EstadoEmpleado.ACTIVO);

    Empleado empleado2 = new Empleado();
    empleado2.setId(2);
    empleado2.setCodigoEmpleado("EMP002");
    empleado2.setNombres("María");
    empleado2.setApellidos("García López");
    empleado2.setCedula("0923456789");
    empleado2.setGenero(Genero.FEMENINO);
    empleado2.setTelefono("0998765432");
    empleado2.setEmail("maria.garcia@gym.com");
    empleado2.setFechaIngreso(LocalDate.of(2023, 3, 20));
    empleado2.setTipoContrato(TipoContrato.MEDIO_TIEMPO);
    empleado2.setEstado(EstadoEmpleado.ACTIVO);

    Empleado empleado3 = new Empleado();
    empleado3.setId(3);
    empleado3.setCodigoEmpleado("EMP003");
    empleado3.setNombres("Carlos");
    empleado3.setApellidos("López Martínez");
    empleado3.setCedula("0934567890");
    empleado3.setGenero(Genero.MASCULINO);
    empleado3.setTelefono("0976543210");
    empleado3.setEmail("carlos.lopez@gym.com");
    empleado3.setFechaIngreso(LocalDate.of(2023, 6, 10));
    empleado3.setTipoContrato(TipoContrato.TIEMPO_COMPLETO);
    empleado3.setEstado(EstadoEmpleado.ACTIVO);

    List<Empleado> empleadosEsperados = Arrays.asList(empleado1, empleado2, empleado3);
    when(mockEmpleadoRepository.findAll()).thenReturn(empleadosEsperados);

    // Act
    List<Empleado> resultado = controller.getEmpleados();

    // Assert
    assertNotNull(resultado);
    assertEquals(3, resultado.size());

    // Verificar primer empleado
    assertEquals("Juan", resultado.get(0).getNombres());
    assertEquals("Pérez García", resultado.get(0).getApellidos());
    assertEquals("EMP001", resultado.get(0).getCodigoEmpleado());
    assertEquals(Genero.MASCULINO, resultado.get(0).getGenero());
    assertEquals(EstadoEmpleado.ACTIVO, resultado.get(0).getEstado());

    // Verificar segundo empleado
    assertEquals("María", resultado.get(1).getNombres());
    assertEquals(TipoContrato.MEDIO_TIEMPO, resultado.get(1).getTipoContrato());

    // Verificar tercer empleado
    assertEquals("Carlos", resultado.get(2).getNombres());
    assertEquals(TipoContrato.TIEMPO_COMPLETO, resultado.get(2).getTipoContrato());

    verify(mockEmpleadoRepository, times(1)).findAll();
    verifyNoMoreInteractions(mockEmpleadoRepository);
  }

  @Test
  @DisplayName("Debe retornar un solo empleado cuando solo existe uno")
  void getEmpleados_DebeRetornarUnSoloEmpleado_CuandoSoloExisteUno() {
    // Arrange
    Empleado empleado = new Empleado();
    empleado.setId(1);
    empleado.setCodigoEmpleado("EMP001");
    empleado.setNombres("Juan");
    empleado.setApellidos("Pérez");
    empleado.setCedula("0912345678");
    empleado.setGenero(Genero.MASCULINO);
    empleado.setTelefono("0987654321");
    empleado.setEmail("juan.perez@gym.com");
    empleado.setFechaIngreso(LocalDate.now());
    empleado.setTipoContrato(TipoContrato.TIEMPO_COMPLETO);
    empleado.setEstado(EstadoEmpleado.ACTIVO);

    when(mockEmpleadoRepository.findAll()).thenReturn(Collections.singletonList(empleado));

    // Act
    List<Empleado> resultado = controller.getEmpleados();

    // Assert
    assertNotNull(resultado);
    assertEquals(1, resultado.size());
    assertEquals("Juan", resultado.get(0).getNombres());
    assertEquals(Genero.MASCULINO, resultado.get(0).getGenero());
    assertEquals(EstadoEmpleado.ACTIVO, resultado.get(0).getEstado());

    verify(mockEmpleadoRepository).findAll();
  }

  @Test
  @DisplayName("Debe retornar empleados con diferentes estados")
  void getEmpleados_DebeRetornarEmpleadosConDiferentesEstados() {
    // Arrange
    Empleado activo = new Empleado();
    activo.setId(1);
    activo.setCodigoEmpleado("EMP001");
    activo.setNombres("Juan");
    activo.setApellidos("Activo");
    activo.setCedula("0901234567");
    activo.setGenero(Genero.MASCULINO);
    activo.setTelefono("0987654321");
    activo.setEmail("juan@gym.com");
    activo.setFechaIngreso(LocalDate.now());
    activo.setTipoContrato(TipoContrato.TIEMPO_COMPLETO);
    activo.setEstado(EstadoEmpleado.ACTIVO);

    Empleado inactivo = new Empleado();
    inactivo.setId(2);
    inactivo.setCodigoEmpleado("EMP002");
    inactivo.setNombres("Pedro");
    inactivo.setApellidos("Inactivo");
    inactivo.setCedula("0902345678");
    inactivo.setGenero(Genero.MASCULINO);
    inactivo.setTelefono("0987654322");
    inactivo.setEmail("pedro@gym.com");
    inactivo.setFechaIngreso(LocalDate.now());
    inactivo.setFechaSalida(LocalDate.now());
    inactivo.setTipoContrato(TipoContrato.TIEMPO_COMPLETO);
    inactivo.setEstado(EstadoEmpleado.INACTIVO);

    Empleado suspendido = new Empleado();
    suspendido.setId(3);
    suspendido.setCodigoEmpleado("EMP003");
    suspendido.setNombres("Ana");
    suspendido.setApellidos("Suspendida");
    suspendido.setCedula("0903456789");
    suspendido.setGenero(Genero.FEMENINO);
    suspendido.setTelefono("0987654323");
    suspendido.setEmail("ana@gym.com");
    suspendido.setFechaIngreso(LocalDate.now());
    suspendido.setTipoContrato(TipoContrato.MEDIO_TIEMPO);
    suspendido.setEstado(EstadoEmpleado.VACACIONES);

    when(mockEmpleadoRepository.findAll()).thenReturn(Arrays.asList(activo, inactivo, suspendido));

    // Act
    List<Empleado> resultado = controller.getEmpleados();

    // Assert
    assertEquals(3, resultado.size());
    assertEquals(EstadoEmpleado.ACTIVO, resultado.get(0).getEstado());
    assertEquals(EstadoEmpleado.INACTIVO, resultado.get(1).getEstado());
    assertEquals(EstadoEmpleado.VACACIONES, resultado.get(2).getEstado());
    assertNull(resultado.get(0).getFechaSalida());
    assertNotNull(resultado.get(1).getFechaSalida());

    resultado.forEach(
        empleado ->
            System.out.println(
                "Empleado: "
                    + empleado.getNombres()
                    + ", Estado: "
                    + empleado.getEstado()
                    + ", Fecha Salida: "
                    + empleado.getFechaSalida()));

    verify(mockEmpleadoRepository).findAll();
  }

  @Test
  @DisplayName("Debe llamar al repositorio solo una vez")
  void getEmpleados_DebeLlamarAlRepositorioUnaSolaVez() {
    // Arrange
    when(mockEmpleadoRepository.findAll()).thenReturn(Collections.emptyList());

    // Act
    controller.getEmpleados();

    // Assert
    verify(mockEmpleadoRepository, times(1)).findAll();
    verifyNoMoreInteractions(mockEmpleadoRepository);
  }

  @Test
  @DisplayName("Debe retornar empleados con diferentes géneros")
  void getEmpleados_DebeRetornarEmpleadosConDiferentesGeneros() {
    // Arrange
    Empleado masculino = new Empleado();
    masculino.setId(1);
    masculino.setCodigoEmpleado("EMP001");
    masculino.setNombres("Pedro");
    masculino.setApellidos("González");
    masculino.setCedula("0901234567");
    masculino.setGenero(Genero.MASCULINO);
    masculino.setTelefono("0987654321");
    masculino.setEmail("pedro@gym.com");
    masculino.setFechaIngreso(LocalDate.now());
    masculino.setTipoContrato(TipoContrato.TIEMPO_COMPLETO);
    masculino.setEstado(EstadoEmpleado.ACTIVO);

    Empleado femenino = new Empleado();
    femenino.setId(2);
    femenino.setCodigoEmpleado("EMP002");
    femenino.setNombres("Ana");
    femenino.setApellidos("Martínez");
    femenino.setCedula("0902345678");
    femenino.setGenero(Genero.FEMENINO);
    femenino.setTelefono("0987654322");
    femenino.setEmail("ana@gym.com");
    femenino.setFechaIngreso(LocalDate.now());
    femenino.setTipoContrato(TipoContrato.MEDIO_TIEMPO);
    femenino.setEstado(EstadoEmpleado.ACTIVO);

    when(mockEmpleadoRepository.findAll()).thenReturn(Arrays.asList(masculino, femenino));

    // Act
    List<Empleado> resultado = controller.getEmpleados();

    // Assert
    assertEquals(2, resultado.size());
    assertEquals(Genero.MASCULINO, resultado.get(0).getGenero());
    assertEquals(Genero.FEMENINO, resultado.get(1).getGenero());

    verify(mockEmpleadoRepository).findAll();
  }
}
