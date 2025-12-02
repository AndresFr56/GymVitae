package gym.vitae.controller;

import static gym.vitae.controller.ValidationUtils.validateDireccion;
import static gym.vitae.controller.ValidationUtils.validateEmail;
import static gym.vitae.controller.ValidationUtils.validateId;
import static gym.vitae.controller.ValidationUtils.validateNombres;
import static gym.vitae.controller.ValidationUtils.validateRequiredString;
import static gym.vitae.controller.ValidationUtils.validateTelefono;

import gym.vitae.mapper.ProveedorMapper;
import gym.vitae.model.Proveedore;
import gym.vitae.model.dtos.inventario.ProveedorCreateDTO;
import gym.vitae.model.dtos.inventario.ProveedorDetalleDTO;
import gym.vitae.model.dtos.inventario.ProveedorListadoDTO;
import gym.vitae.model.dtos.inventario.ProveedorUpdateDTO;
import gym.vitae.repositories.ProveedoreRepository;
import java.util.List;

/**
 * Controlador que gestionara la lógica de proveedores. Hereda del base controller
 */
public class ProveedoresController extends BaseController {

  private final ProveedoreRepository proveedorRepository;

  /**
   * Constructor por defecto que inicializa repositorio.
   */
  public ProveedoresController() {
    super();
    this.proveedorRepository = getRepository(ProveedoreRepository.class);
  }

  /**
   * Constructor para pruebas.
   *
   * @param repository del proveedor
   */
  ProveedoresController(ProveedoreRepository repository) {
    super(null);
    this.proveedorRepository = repository;
  }

  /**
   * Obtiene lista de proveedores registrados.
   *
   * @return lista de proveedores
   */
  public List<ProveedorListadoDTO> getProveedores() {
    return proveedorRepository.findAllListado();
  }

  /**
   * Obtiene detalle del proveedor por id.
   *
   * @param id del proveedor
   * @return detalle del proveedor
   *
   */
  public ProveedorDetalleDTO getProveedorById(int id) {
    validateId(id);
    return proveedorRepository
        .findDetalleById(id)
        .orElseThrow(() -> new IllegalArgumentException("Proveedor no encontrado con ID: " + id));
  }

  /**
   * Registrar un nuevo proveedor.
   *
   * @param proveedorCreateDto del proveedor
   * @return ProveedorDetalleDTO del proveedor creado
   */
  public ProveedorDetalleDTO createProveedor(ProveedorCreateDTO proveedorCreateDto) {
    validateProveedorCreate(proveedorCreateDto);

    // Código del proveedor
    String codigoProveedor = generateCodigoProveedor();

    // Mapeamos el proveedor
    Proveedore proveedor = ProveedorMapper.toEntity(proveedorCreateDto, codigoProveedor);

    // Guardamos el registro
    Proveedore saved = proveedorRepository.save(proveedor);

    return proveedorRepository
        .findDetalleById(saved.getId())
        .orElseThrow(() -> new IllegalStateException("Error al recuperar proveedor creado"));
  }

  /**
   * Actualiza un proveedor existente.
   *
   * @param id del proveedor
   * @param proveedorUpdateDto del proveedor
   * @return ProveedorDetalleDTO actualizado
   */
  public ProveedorDetalleDTO updateProveedor(int id, ProveedorUpdateDTO proveedorUpdateDto) {
    validateId(id);
    validateProveedorUpdate(proveedorUpdateDto);

    // Carga el proveedor existente
    Proveedore proveedor =
        proveedorRepository
            .findById(id)
            .orElseThrow(
                () -> new IllegalArgumentException("Proveedor no encontrado con ID: " + id));

    // Mapeamos la entidad
    ProveedorMapper.updateEntity(proveedor, proveedorUpdateDto);
    proveedorRepository.update(proveedor);

    // Retorno de DTO actualizado
    return proveedorRepository
        .findDetalleById(id)
        .orElseThrow(() -> new IllegalStateException("Error al recuperar proveedor actualizado"));
  }

  /**
   * Eliminacion fisica del proveedor.
   *
   * @param id del proveedor
   */
  public void physicalDeleteProveedor(int id) {
    validateId(id);

    if (!proveedorRepository.existsById(id)) {
      throw new IllegalArgumentException("Proveedor no encontrado con ID: " + id);
    }
    proveedorRepository.delete(id);
  }

  /**
   * Eliminación lógica del proveedor, cambio estado a inactivo.
   *
   * @param id del proveedor
   * @return ProveedorDetalleDTO
   */
  public ProveedorDetalleDTO logicalDeleteProveedor(int id) {
    validateId(id);
    Proveedore proveedor =
        proveedorRepository
            .findById(id)
            .orElseThrow(
                () -> new IllegalArgumentException("Proveedor no encontrado con ID: " + id));

    if (!proveedor.getActivo()) {
      throw new IllegalStateException("El proveedor ya se encuentra inactivo");
    }

    proveedor.setActivo(false);
    proveedorRepository.update(proveedor);

    return proveedorRepository
        .findDetalleById(id)
        .orElseThrow(() -> new IllegalStateException("Error al recuperar proveedor creado"));
  }

  /**
   * Válida datos requeridos para la creación del proveeedor.
   *
   * @param proveedorDto del proveedor
   */
  private void validateProveedorCreate(ProveedorCreateDTO proveedorDto) {
    if (proveedorDto == null) {
      throw new IllegalArgumentException("Los datos del proveedor no pueden estar vacios");
    }

    validateNombres(proveedorDto.getNombre());
    validateContacto(proveedorDto.getContacto());
    validateTelefono(proveedorDto.getTelefono());
    validateEmail(proveedorDto.getEmail());
    validateDireccion(proveedorDto.getDireccion());
  }

  /**
   * Válida datos requeridos para actualizar un proveedor.
   *
   * @param proveedorUpdateDto del proveedor
   */
  private void validateProveedorUpdate(ProveedorUpdateDTO proveedorUpdateDto) {
    if (proveedorUpdateDto == null) {
      throw new IllegalArgumentException("Los datos del proveedor no pueden estar vacios");
    }

    validateNombres(proveedorUpdateDto.getNombre());
    validateContacto(proveedorUpdateDto.getContacto());
    validateTelefono(proveedorUpdateDto.getTelefono());
    validateEmail(proveedorUpdateDto.getEmail());
    validateDireccion(proveedorUpdateDto.getDireccion());
  }

  /**
   * Válida el nombre del contacto del proveedor.
   *
   * @param contactoProveedor del proveedor
   */
  private void validateContacto(String contactoProveedor) {
    validateRequiredString(contactoProveedor, "El contacto del proveedor", 100);
  }

  /**
   * Genera código único para el proveedor.
   *
   * @return codigo del proveedor
   */
  private String generateCodigoProveedor() {
    long count = proveedorRepository.count();
    return String.format("PROV-%03d", count + 1);
  }

  /**
   * Obtiene próximo código para mostrar en el formulario.
   *
   * @return codigo del proveedor
   */
  public String getNextCodigoProveedor() {
    long count = proveedorRepository.count();
    return String.format("PROV-%03d", count + 1);
  }
}
