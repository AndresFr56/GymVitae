package gym.vitae.controller;

import gym.vitae.mapper.CategoriaMapper;
import gym.vitae.mapper.EquipoMapper;
import gym.vitae.mapper.ProductoMapper;
import gym.vitae.model.Categoria;
import gym.vitae.model.Equipo;
import gym.vitae.model.Producto;
import gym.vitae.model.Proveedore;
import gym.vitae.model.dtos.inventario.CategoriaCreateDTO;
import gym.vitae.model.dtos.inventario.CategoriaDetalleDTO;
import gym.vitae.model.dtos.inventario.CategoriaListadoDTO;
import gym.vitae.model.dtos.inventario.CategoriaUpdateDTO;
import gym.vitae.model.dtos.inventario.EquipoCreateDTO;
import gym.vitae.model.dtos.inventario.EquipoDetalleDTO;
import gym.vitae.model.dtos.inventario.EquipoListadoDTO;
import gym.vitae.model.dtos.inventario.EquipoUpdateDTO;
import gym.vitae.model.dtos.inventario.InventarioListadoDTO;
import gym.vitae.model.dtos.inventario.ProductoCreateDTO;
import gym.vitae.model.dtos.inventario.ProductoDetalleDTO;
import gym.vitae.model.dtos.inventario.ProductoListadoDTO;
import gym.vitae.model.dtos.inventario.ProductoUpdateDTO;
import gym.vitae.model.dtos.inventario.ProveedorListadoDTO;
import gym.vitae.model.enums.EstadoEquipo;
import gym.vitae.model.enums.TipoCategoria;
import gym.vitae.repositories.CategoriaRepository;
import gym.vitae.repositories.EquipoRepository;
import gym.vitae.repositories.ProductoRepository;
import gym.vitae.repositories.ProveedoreRepository;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/** Controlador Inventario. Gestiona productos, equipos y proveedores según RF-17 a RF-24. */
public class InventarioController extends BaseController {

  // Subtipos válidos de acuerdo al tipo (restricción de la base de datos)
  private static final String[] SUBTIPOS_PRODUCTO = {
    "PRODUCTO", "ACCESORIO", "MATERIAL_DE_LIMPIEZA"
  };
  private static final String[] SUBTIPOS_EQUIPO = {"CARDIO", "PESAS", "FUNCIONAL"};
  private final ProductoRepository productoRepository;
  private final EquipoRepository equipoRepository;
  private final ProveedoreRepository proveedorRepository;
  private final CategoriaRepository categoriaRepository;

  // ============ INVENTARIO GENERAL (RF-17) ============

  /** Constructor inyección de dependencias. */
  public InventarioController() {
    super();
    this.productoRepository = getRepository(ProductoRepository.class);
    this.equipoRepository = getRepository(EquipoRepository.class);
    this.proveedorRepository = getRepository(ProveedoreRepository.class);
    this.categoriaRepository = getRepository(CategoriaRepository.class);
  }

  /**
   * Constructor para pruebas.
   *
   * @param productoRepository Repositorio de productos
   * @param equipoRepository Repositorio de equipos
   * @param proveedorRepository Repositorio de proveedores
   * @param categoriaRepository Repositorio de categorías
   */
  InventarioController(
      ProductoRepository productoRepository,
      EquipoRepository equipoRepository,
      ProveedoreRepository proveedorRepository,
      CategoriaRepository categoriaRepository) {
    super(null);
    this.productoRepository = productoRepository;
    this.equipoRepository = equipoRepository;
    this.proveedorRepository = proveedorRepository;
    this.categoriaRepository = categoriaRepository;
  }

  /**
   * Obtiene productos y equipos activos y en stock combinados en un listado de Inventario.
   *
   * @return Lista de InventarioListadoDTO
   */
  public List<InventarioListadoDTO> getInventarioGeneral() {
    List<InventarioListadoDTO> result = new ArrayList<>();

    // Productos en stock
    List<ProductoListadoDTO> productos = productoRepository.findActivosListado();
    result.addAll(
        productos.stream()
            .map(
                p ->
                    new InventarioListadoDTO(
                        p.getId(),
                        p.getCodigo(),
                        "Producto",
                        p.getNombre(),
                        p.getDescripcion(),
                        p.getFechaIngreso() != null ? p.getFechaIngreso().toString() : ""))
            .toList());

    // Equipos activos (no fuera de servicio)
    List<EquipoListadoDTO> equipos = equipoRepository.findActivosListado();
    result.addAll(
        equipos.stream()
            .map(
                e ->
                    new InventarioListadoDTO(
                        e.getId(),
                        e.getCodigo(),
                        "Equipo",
                        e.getNombre(),
                        e.getDescripcion(),
                        e.getFechaAdquisicion() != null ? e.getFechaAdquisicion().toString() : ""))
            .toList());

    return result;
  }

  /**
   * Obtiene inventario con paginación.
   *
   * @param offset Posición inicial
   * @param limit Cantidad de registros
   * @return Lista de InventarioListadoDTO
   */
  public List<InventarioListadoDTO> getInventarioGeneral(int offset, int limit) {
    List<InventarioListadoDTO> all = getInventarioGeneral();
    int fromIndex = Math.min(offset, all.size());
    int toIndex = Math.min(offset + limit, all.size());
    return all.subList(fromIndex, toIndex);
  }

  /**
   * Obtiene inventario con filtros por tipo, proveedor y texto de búsqueda.
   *
   * @param searchText Texto de búsqueda en nombre/código
   * @param tipo Tipo de item (Producto, Equipo o null para todos)
   * @param proveedorId ID de proveedor (solo aplica a productos)
   * @return Lista filtrada de InventarioListadoDTO
   */
  public List<InventarioListadoDTO> getInventarioWithFilters(
      String searchText, String tipo, Integer proveedorId) {
    List<InventarioListadoDTO> result = new ArrayList<>();

    boolean incluirProductos =
        tipo == null || tipo.equalsIgnoreCase("Todos") || tipo.equalsIgnoreCase("Producto");
    boolean incluirEquipos =
        tipo == null || tipo.equalsIgnoreCase("Todos") || tipo.equalsIgnoreCase("Equipo");

    // Filtrar productos
    if (incluirProductos) {
      List<ProductoListadoDTO> productos =
          productoRepository.findAllListadoWithFilters(
              searchText, null, proveedorId, true, 0, 1000);
      result.addAll(
          productos.stream()
              .map(
                  p ->
                      new InventarioListadoDTO(
                          p.getId(),
                          p.getCodigo(),
                          "Producto",
                          p.getNombre(),
                          p.getDescripcion(),
                          p.getFechaIngreso() != null ? p.getFechaIngreso().toString() : ""))
              .toList());
    }

    // Filtrar equipos (no tienen proveedor, ignorar proveedorId)
    if (incluirEquipos && proveedorId == null) {
      List<EquipoListadoDTO> equipos =
          equipoRepository.findAllListadoWithFilters(searchText, null, null, 0, 1000);
      result.addAll(
          equipos.stream()
              .map(
                  e ->
                      new InventarioListadoDTO(
                          e.getId(),
                          e.getCodigo(),
                          "Equipo",
                          e.getNombre(),
                          e.getDescripcion(),
                          e.getFechaAdquisicion() != null
                              ? e.getFechaAdquisicion().toString()
                              : ""))
              .toList());
    }

    return result;
  }

  /**
   * Obtiene inventario con filtros y paginación.
   *
   * @param searchText Texto de búsqueda en nombre/código
   * @param tipo Tipo de item (Producto, Equipo o null para todos)
   * @param proveedorId ID de proveedor (solo aplica a productos)
   * @param offset Posición inicial
   * @param limit Cantidad de registros
   * @return Lista filtrada y paginada de InventarioListadoDTO
   */
  public List<InventarioListadoDTO> getInventarioWithFilters(
      String searchText, String tipo, Integer proveedorId, int offset, int limit) {
    List<InventarioListadoDTO> filtered = getInventarioWithFilters(searchText, tipo, proveedorId);
    int fromIndex = Math.min(offset, filtered.size());
    int toIndex = Math.min(offset + limit, filtered.size());
    return filtered.subList(fromIndex, toIndex);
  }

  // ============ PRODUCTOS (RF-19, RF-20, RF-21) ============

  /** Cuenta el total de items en inventario activos. */
  public long countInventario() {
    return productoRepository.countWithFilters(null, null, null, true)
        + equipoRepository.countWithFilters(null, null, null);
  }

  /**
   * Cuenta items en inventario con filtros.
   *
   * @param searchText Texto de búsqueda
   * @param tipo Tipo de item
   * @param proveedorId ID de proveedor
   * @return Cantidad de items
   */
  public long countInventarioWithFilters(String searchText, String tipo, Integer proveedorId) {
    return getInventarioWithFilters(searchText, tipo, proveedorId).size();
  }

  /** Obtiene todos los productos como DTOs de listado. */
  public List<ProductoListadoDTO> getProductos() {
    return productoRepository.findAllListado();
  }

  /** Obtiene productos con paginación. */
  public List<ProductoListadoDTO> getProductos(int offset, int limit) {
    validatePagination(offset, limit);
    return productoRepository.findAllListadoPaginated(offset, limit);
  }

  /** Obtiene todos los productos activos. */
  public List<ProductoListadoDTO> getProductosActivos() {
    return productoRepository.findActivosListado();
  }

  /**
   * Obtiene productos con filtros y paginación.
   *
   * @param searchText Texto de búsqueda en nombre/código
   * @param categoriaId ID de categoría
   * @param proveedorId ID de proveedor
   * @param offset Posición inicial
   * @param limit Cantidad de registros
   * @return Lista de ProductoListadoDTO
   */
  public List<ProductoListadoDTO> getProductosWithFilters(
      String searchText, Integer categoriaId, Integer proveedorId, int offset, int limit) {
    return productoRepository.findAllListadoWithFilters(
        searchText, categoriaId, proveedorId, true, offset, limit);
  }

  /** Cuenta productos totales. */
  public long countProductos() {
    return productoRepository.count();
  }

  /** Cuenta productos con filtros. */
  public long countProductosWithFilters(
      String searchText, Integer categoriaId, Integer proveedorId) {
    return productoRepository.countWithFilters(searchText, categoriaId, proveedorId, true);
  }

  /** Obtiene detalle de un producto por ID. */
  public ProductoDetalleDTO getProductoById(int id) {
    validateId(id);
    return productoRepository
        .findDetalleById(id)
        .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado con ID: " + id));
  }

  /**
   * Crea un nuevo producto (RF-19).
   *
   * @param dto DTO con datos del producto
   * @return ProductoDetalleDTO creado
   */
  public ProductoDetalleDTO createProducto(ProductoCreateDTO dto) {
    validateProductoCreate(dto);

    final Categoria categoria =
        categoriaRepository
            .findById(dto.getCategoriaId())
            .orElseThrow(
                () ->
                    new IllegalArgumentException(
                        "Categoría no encontrada con ID: " + dto.getCategoriaId()));

    Proveedore proveedor = null;
    if (dto.getProveedorId() != null) {
      proveedor =
          proveedorRepository
              .findById(dto.getProveedorId())
              .orElseThrow(
                  () ->
                      new IllegalArgumentException(
                          "Proveedor no encontrado con ID: " + dto.getProveedorId()));
    }

    // Generar código automático
    String codigo = generateCodigoProducto();
    dto.setCodigo(codigo);

    // Establecer fecha de ingreso automática
    dto.setFechaIngreso(LocalDate.now());

    Producto producto = ProductoMapper.toEntity(dto, categoria, proveedor);
    producto.setActivo(true);
    Producto saved = productoRepository.save(producto);

    return productoRepository
        .findDetalleById(saved.getId())
        .orElseThrow(() -> new IllegalStateException("Error al recuperar producto creado"));
  }

  /**
   * Actualiza un producto existente (RF-20).
   *
   * @param id ID del producto
   * @param dto DTO con datos actualizados
   * @return ProductoDetalleDTO actualizado
   */
  public ProductoDetalleDTO updateProducto(int id, ProductoUpdateDTO dto) {
    validateProductoUpdate(id, dto);

    final Producto producto =
        productoRepository
            .findById(id)
            .orElseThrow(
                () -> new IllegalArgumentException("Producto no encontrado con ID: " + id));

    Categoria categoria = null;
    if (dto.getCategoriaId() != null) {
      categoria =
          categoriaRepository
              .findById(dto.getCategoriaId())
              .orElseThrow(
                  () ->
                      new IllegalArgumentException(
                          "Categoría no encontrada con ID: " + dto.getCategoriaId()));
    }

    Proveedore proveedor = null;
    if (dto.getProveedorId() != null) {
      proveedor =
          proveedorRepository
              .findById(dto.getProveedorId())
              .orElseThrow(
                  () ->
                      new IllegalArgumentException(
                          "Proveedor no encontrado con ID: " + dto.getProveedorId()));
    }

    // No permitir modificar código ni fecha de ingreso (RF-20)
    dto.setCodigo(null);
    dto.setFechaIngreso(null);

    ProductoMapper.updateEntity(producto, dto, categoria, proveedor);
    productoRepository.update(producto);

    return productoRepository
        .findDetalleById(id)
        .orElseThrow(() -> new IllegalStateException("Error al recuperar producto actualizado"));
  }

  /**
   * Da de baja un producto (RF-21). Cambio lógico de activo a inactivo.
   *
   * @param id ID del producto
   * @return ProductoDetalleDTO actualizado
   */
  public ProductoDetalleDTO darDeBajaProducto(int id) {
    validateId(id);

    Producto producto =
        productoRepository
            .findById(id)
            .orElseThrow(
                () -> new IllegalArgumentException("Producto no encontrado con ID: " + id));

    if (Boolean.FALSE.equals(producto.getActivo())) {
      throw new IllegalArgumentException("El producto ya está dado de baja");
    }

    producto.setActivo(false);
    productoRepository.update(producto);

    return productoRepository
        .findDetalleById(id)
        .orElseThrow(() -> new IllegalStateException("Error al recuperar producto actualizado"));
  }

  /** Genera código único de producto (10 dígitos). */
  public String generateCodigoProducto() {
    Random random = new Random();
    String codigo;
    int intentos = 0;
    int maxIntentos = 100;

    do {
      int num = 100000000 + random.nextInt(900000000);
      codigo = String.valueOf(num);
      intentos++;
      if (intentos >= maxIntentos) {
        throw new IllegalStateException("No se pudo generar un código único de producto");
      }
    } while (existeCodigoProducto(codigo));

    return codigo;
  }

  private boolean existeCodigoProducto(String codigo) {
    return productoRepository.findAll().stream().anyMatch(p -> p.getCodigo().equals(codigo));
  }

  /** Obtiene todos los equipos como DTOs de listado. */
  public List<EquipoListadoDTO> getEquipos() {
    return equipoRepository.findAllListado();
  }

  /** Obtiene equipos con paginación. */
  public List<EquipoListadoDTO> getEquipos(int offset, int limit) {
    validatePagination(offset, limit);
    return equipoRepository.findAllListadoPaginated(offset, limit);
  }

  /** Obtiene todos los equipos activos (no fuera de servicio). */
  public List<EquipoListadoDTO> getEquiposActivos() {
    return equipoRepository.findActivosListado();
  }

  /**
   * Obtiene equipos con filtros y paginación.
   *
   * @param searchText Texto de búsqueda en nombre/código
   * @param categoriaId ID de categoría
   * @param estado Estado del equipo
   * @param offset Posición inicial
   * @param limit Cantidad de registros
   * @return Lista de EquipoListadoDTO
   */
  public List<EquipoListadoDTO> getEquiposWithFilters(
      String searchText, Integer categoriaId, EstadoEquipo estado, int offset, int limit) {
    return equipoRepository.findAllListadoWithFilters(
        searchText, categoriaId, estado, offset, limit);
  }

  /** Cuenta equipos totales. */
  public long countEquipos() {
    return equipoRepository.count();
  }

  /** Cuenta equipos con filtros. */
  public long countEquiposWithFilters(String searchText, Integer categoriaId, EstadoEquipo estado) {
    return equipoRepository.countWithFilters(searchText, categoriaId, estado);
  }

  /** Obtiene detalle de un equipo por ID. */
  public EquipoDetalleDTO getEquipoById(int id) {
    validateId(id);
    return equipoRepository
        .findDetalleById(id)
        .orElseThrow(() -> new IllegalArgumentException("Equipo no encontrado con ID: " + id));
  }

  /**
   * Crea un nuevo equipo (RF-22).
   *
   * @param dto DTO con datos del equipo
   * @return EquipoDetalleDTO creado
   */
  public EquipoDetalleDTO createEquipo(EquipoCreateDTO dto) {
    validateEquipoCreate(dto);

    Categoria categoria =
        categoriaRepository
            .findById(dto.getCategoriaId())
            .orElseThrow(
                () ->
                    new IllegalArgumentException(
                        "Categoría no encontrada con ID: " + dto.getCategoriaId()));

    // Generar código automático
    String codigo = generateCodigoEquipo();
    dto.setCodigo(codigo);

    // Establecer fecha de adquisición automática
    dto.setFechaAdquisicion(LocalDate.now());

    Equipo equipo = EquipoMapper.toEntity(dto, categoria);
    // Estado por defecto: OPERATIVO (equivalente a "Nuevo")
    equipo.setEstado(EstadoEquipo.OPERATIVO);

    Equipo saved = equipoRepository.save(equipo);

    return equipoRepository
        .findDetalleById(saved.getId())
        .orElseThrow(() -> new IllegalStateException("Error al recuperar equipo creado"));
  }

  /**
   * Actualiza un equipo existente (RF-23).
   *
   * @param id ID del equipo
   * @param dto DTO con datos actualizados
   * @return EquipoDetalleDTO actualizado
   */
  public EquipoDetalleDTO updateEquipo(int id, EquipoUpdateDTO dto) {
    validateEquipoUpdate(id, dto);

    final Equipo equipo =
        equipoRepository
            .findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Equipo no encontrado con ID: " + id));

    Categoria categoria = null;
    if (dto.getCategoriaId() != null) {
      categoria =
          categoriaRepository
              .findById(dto.getCategoriaId())
              .orElseThrow(
                  () ->
                      new IllegalArgumentException(
                          "Categoría no encontrada con ID: " + dto.getCategoriaId()));
    }

    // No permitir modificar código ni fecha de adquisición (RF-23)
    dto.setCodigo(null);
    dto.setFechaAdquisicion(null);

    EquipoMapper.updateEntity(equipo, dto, categoria);
    equipoRepository.update(equipo);

    return equipoRepository
        .findDetalleById(id)
        .orElseThrow(() -> new IllegalStateException("Error al recuperar equipo actualizado"));
  }

  /**
   * Da de baja un equipo (RF-24). Cambio lógico de estado a FUERA_SERVICIO.
   *
   * @param id ID del equipo
   * @return EquipoDetalleDTO actualizado
   */
  public EquipoDetalleDTO darDeBajaEquipo(int id) {
    validateId(id);

    Equipo equipo =
        equipoRepository
            .findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Equipo no encontrado con ID: " + id));

    if (equipo.getEstado() == EstadoEquipo.FUERA_SERVICIO) {
      throw new IllegalArgumentException("Equipo ya está dado de baja");
    }

    equipo.setEstado(EstadoEquipo.FUERA_SERVICIO);
    equipoRepository.update(equipo);

    return equipoRepository
        .findDetalleById(id)
        .orElseThrow(() -> new IllegalStateException("Error al recuperar equipo actualizado"));
  }

  /** Genera código único de equipo (10 dígitos). */
  public String generateCodigoEquipo() {
    Random random = new Random();
    String codigo;
    int intentos = 0;
    int maxIntentos = 100;

    do {
      int num = 100000000 + random.nextInt(900000000);
      codigo = String.valueOf(num);
      intentos++;
      if (intentos >= maxIntentos) {
        throw new IllegalStateException("No se pudo generar un código único de equipo");
      }
    } while (existeCodigoEquipo(codigo));

    return codigo;
  }

  private boolean existeCodigoEquipo(String codigo) {
    return equipoRepository.findAll().stream().anyMatch(e -> e.getCodigo().equals(codigo));
  }

  /** Obtiene todos los proveedores como DTOs de listado. */
  public List<ProveedorListadoDTO> getProveedores() {
    return proveedorRepository.findAllListado();
  }

  /** Obtiene todos los proveedores activos. */
  public List<ProveedorListadoDTO> getProveedoresActivos() {
    return proveedorRepository.findProveedoresActivos();
  }

  /** Obtiene todas las categorías activas. */
  public List<Categoria> getCategorias() {
    return categoriaRepository.findActivos();
  }

  /** Obtiene todas las categorías (activas e inactivas). */
  public List<CategoriaListadoDTO> getCategoriasListado() {
    return categoriaRepository.findAllListado();
  }

  /**
   * Obtiene categorías por tipo.
   *
   * @param tipo Tipo de categoría (PRODUCTO o EQUIPO)
   * @return Lista de Categoria
   */
  public List<Categoria> getCategoriasByTipo(TipoCategoria tipo) {
    return categoriaRepository.findByTipo(tipo);
  }

  /**
   * Obtiene una categoría por nombre.
   *
   * @param nombre Nombre de la categoría
   * @return Categoria encontrada
   */
  public Categoria getCategoriaByNombre(String nombre) {
    return categoriaRepository
        .findByNombre(nombre)
        .orElseThrow(
            () -> new IllegalArgumentException("Categoría no encontrada con nombre: " + nombre));
  }

  /** Obtiene detalle de una categoría por ID. */
  public CategoriaDetalleDTO getCategoriaById(int id) {
    validateId(id);
    return categoriaRepository
        .findDetalleById(id)
        .orElseThrow(() -> new IllegalArgumentException("Categoría no encontrada con ID: " + id));
  }

  /** Cuenta categorías totales. */
  public long countCategorias() {
    return categoriaRepository.count();
  }

  /**
   * Crea una nueva categoría.
   *
   * @param dto DTO con datos de la categoría
   * @return CategoriaDetalleDTO creada
   */
  public CategoriaDetalleDTO createCategoria(CategoriaCreateDTO dto) {
    validateCategoriaCreate(dto);

    // Verificar que no exista otra categoría con el mismo nombre
    if (categoriaRepository.findByNombre(dto.getNombre()).isPresent()) {
      throw new IllegalArgumentException(
          "Ya existe una categoría con el nombre: " + dto.getNombre());
    }

    Categoria categoria = CategoriaMapper.toEntity(dto);
    Categoria saved = categoriaRepository.save(categoria);

    return categoriaRepository
        .findDetalleById(saved.getId())
        .orElseThrow(() -> new IllegalStateException("Error al recuperar categoría creada"));
  }

  /**
   * Actualiza una categoría existente.
   *
   * @param id ID de la categoría
   * @param dto DTO con datos actualizados
   * @return CategoriaDetalleDTO actualizada
   */
  public CategoriaDetalleDTO updateCategoria(int id, CategoriaUpdateDTO dto) {
    validateCategoriaUpdate(id, dto);

    Categoria categoria =
        categoriaRepository
            .findById(id)
            .orElseThrow(
                () -> new IllegalArgumentException("Categoría no encontrada con ID: " + id));

    // Verificar que no exista otra categoría con el mismo nombre (si se está cambiando)
    if (dto.getNombre() != null
        && !dto.getNombre().equals(categoria.getNombre())
        && categoriaRepository.findByNombre(dto.getNombre()).isPresent()) {
      throw new IllegalArgumentException(
          "Ya existe una categoría con el nombre: " + dto.getNombre());
    }

    CategoriaMapper.updateEntity(categoria, dto);
    categoriaRepository.update(categoria);

    return categoriaRepository
        .findDetalleById(id)
        .orElseThrow(() -> new IllegalStateException("Error al recuperar categoría actualizada"));
  }

  /**
   * Da de baja una categoría. Cambio lógico de activo a inactivo.
   *
   * @param id ID de la categoría
   * @return CategoriaDetalleDTO actualizada
   */
  public CategoriaDetalleDTO darDeBajaCategoria(int id) {
    validateId(id);

    Categoria categoria =
        categoriaRepository
            .findById(id)
            .orElseThrow(
                () -> new IllegalArgumentException("Categoría no encontrada con ID: " + id));

    if (!Boolean.TRUE.equals(categoria.getActivo())) {
      throw new IllegalArgumentException("La categoría ya está dada de baja");
    }

    categoria.setActivo(false);
    categoriaRepository.update(categoria);

    return categoriaRepository
        .findDetalleById(id)
        .orElseThrow(() -> new IllegalStateException("Error al recuperar categoría actualizada"));
  }

  /** Valida los datos para crear una categoría. */
  public void validateCategoriaCreate(CategoriaCreateDTO dto) {
    if (dto == null) {
      throw new IllegalArgumentException("Los datos de la categoría no pueden ser nulos");
    }

    validateNombreCategoria(dto.getNombre());
    validateTipoCategoria(dto.getTipo());
    validateSubtipoCategoria(dto.getSubtipo(), dto.getTipo());
    validateDescripcionCategoria(dto.getDescripcion());
  }

  /** Valida los datos para actualizar una categoría. */
  public void validateCategoriaUpdate(int id, CategoriaUpdateDTO dto) {
    if (dto == null) {
      throw new IllegalArgumentException("Los datos de la categoría no pueden ser nulos");
    }

    validateId(id);

    if (dto.getNombre() != null) {
      validateNombreCategoria(dto.getNombre());
    }
    // Para actualización, si se actualiza tipo o subtipo, ambos deben ser consistentes
    if (dto.getTipo() != null || dto.getSubtipo() != null) {
      validateSubtipoCategoria(dto.getSubtipo(), dto.getTipo());
    }
    if (dto.getDescripcion() != null) {
      validateDescripcionCategoria(dto.getDescripcion());
    }
  }

  /** Valida el nombre de una categoría. */
  private void validateNombreCategoria(String nombre) {
    if (nombre == null || nombre.trim().isEmpty()) {
      throw new IllegalArgumentException("El nombre de la categoría es obligatorio");
    }
    if (nombre.length() > 100) {
      throw new IllegalArgumentException(
          "El nombre de la categoría no puede exceder 100 caracteres");
    }
  }

  /** Valida el tipo de una categoría. */
  private void validateTipoCategoria(TipoCategoria tipo) {
    if (tipo == null) {
      throw new IllegalArgumentException("El tipo de categoría es obligatorio");
    }
  }

  /** Valida el subtipo de una categoría según el tipo. */
  private void validateSubtipoCategoria(String subtipo, TipoCategoria tipo) {
    if (subtipo == null || subtipo.trim().isEmpty()) {
      throw new IllegalArgumentException("El subtipo de categoría es obligatorio");
    }
    if (subtipo.length() > 50) {
      throw new IllegalArgumentException("El subtipo no puede exceder 50 caracteres");
    }

    // Validar que el subtipo corresponda al tipo
    if (tipo != null) {
      boolean subtipoValido = false;
      String[] subtiposPermitidos;

      switch (tipo) {
        case TipoCategoria.PRODUCTO -> subtiposPermitidos = SUBTIPOS_PRODUCTO;
        case TipoCategoria.EQUIPO -> subtiposPermitidos = SUBTIPOS_EQUIPO;
        default -> throw new IllegalArgumentException("Tipo de categoría no reconocido");
      }

      for (String s : subtiposPermitidos) {
        if (s.equals(subtipo)) {
          subtipoValido = true;
          break;
        }
      }

      if (!subtipoValido) {
        throw new IllegalArgumentException(
            "El subtipo '"
                + subtipo
                + "' no es válido para el tipo "
                + tipo
                + ". Valores permitidos: "
                + String.join(", ", subtiposPermitidos));
      }
    }
  }

  /** Valida la descripción de una categoría. */
  private void validateDescripcionCategoria(String descripcion) {
    if (descripcion != null && descripcion.length() > 500) {
      throw new IllegalArgumentException("La descripción no puede exceder 500 caracteres");
    }
  }

  /** Valida los datos para crear un producto (RF-19). */
  public void validateProductoCreate(ProductoCreateDTO dto) {
    if (dto == null) {
      throw new IllegalArgumentException("Los datos del producto no pueden ser nulos");
    }

    validateCategoriaId(dto.getCategoriaId());
    validateProveedorIdOpcional(dto.getProveedorId());
    validateNombreInventario(dto.getNombre(), "producto");
    validateDescripcionInventario(dto.getDescripcion());
    validatePrecioUnitario(dto.getPrecioUnitario());
    validateStock(dto.getStock());
    validateUnidadMedida(dto.getUnidadMedida());
  }

  /** Valida los datos para actualizar un producto (RF-20). */
  public void validateProductoUpdate(int id, ProductoUpdateDTO dto) {
    if (dto == null) {
      throw new IllegalArgumentException("Los datos del producto no pueden ser nulos");
    }

    validateId(id);

    if (dto.getCategoriaId() != null) {
      validateCategoriaId(dto.getCategoriaId());
    }
    validateProveedorIdOpcional(dto.getProveedorId());
    if (dto.getNombre() != null) {
      validateNombreInventario(dto.getNombre(), "producto");
    }
    if (dto.getDescripcion() != null) {
      validateDescripcionInventario(dto.getDescripcion());
    }
    if (dto.getPrecioUnitario() != null) {
      validatePrecioUnitario(dto.getPrecioUnitario());
    }
    if (dto.getStock() != null) {
      validateStock(dto.getStock());
    }
    if (dto.getUnidadMedida() != null) {
      validateUnidadMedida(dto.getUnidadMedida());
    }
  }

  /** Valida los datos para crear un equipo (RF-22). */
  public void validateEquipoCreate(EquipoCreateDTO dto) {
    if (dto == null) {
      throw new IllegalArgumentException("Los datos del equipo no pueden ser nulos");
    }

    validateCategoriaId(dto.getCategoriaId());
    validateNombreInventario(dto.getNombre(), "equipo");
    validateDescripcionInventario(dto.getDescripcion());
    validateMarca(dto.getMarca());
    validateModelo(dto.getModelo());
    validateCosto(dto.getCosto());
    validateUbicacion(dto.getUbicacion());
    validateObservaciones(dto.getObservaciones());
  }

  /** Valida los datos para actualizar un equipo (RF-23). */
  public void validateEquipoUpdate(int id, EquipoUpdateDTO dto) {
    if (dto == null) {
      throw new IllegalArgumentException("Los datos del equipo no pueden ser nulos");
    }

    validateId(id);

    if (dto.getCategoriaId() != null) {
      validateCategoriaId(dto.getCategoriaId());
    }
    if (dto.getNombre() != null) {
      validateNombreInventario(dto.getNombre(), "equipo");
    }
    if (dto.getDescripcion() != null) {
      validateDescripcionInventario(dto.getDescripcion());
    }
    if (dto.getMarca() != null) {
      validateMarca(dto.getMarca());
    }
    if (dto.getModelo() != null) {
      validateModelo(dto.getModelo());
    }
    if (dto.getCosto() != null) {
      validateCosto(dto.getCosto());
    }
    if (dto.getEstado() != null) {
      validateEstadoEquipo(dto.getEstado());
    }
    if (dto.getUbicacion() != null) {
      validateUbicacion(dto.getUbicacion());
    }
    if (dto.getObservaciones() != null) {
      validateObservaciones(dto.getObservaciones());
    }
  }

  /** Valida que un ID sea válido. */
  private void validateId(int id) {
    if (id <= 0) {
      throw new IllegalArgumentException("El ID no es valido");
    }
  }

  /** Valida los parámetros de paginación. */
  private void validatePagination(int offset, int limit) {
    ValidationUtils.validatePagination(offset, limit);
  }

  /** Valida el nombre de un producto o equipo. Solo letras y espacios, máximo 100 caracteres. */
  private void validateNombreInventario(String nombre, String tipoItem) {
    if (nombre == null || nombre.trim().isEmpty()) {
      throw new IllegalArgumentException("El nombre del " + tipoItem + " es obligatorio");
    }
    if (nombre.length() > 100) {
      throw new IllegalArgumentException(
          "El nombre del " + tipoItem + " no puede exceder 100 caracteres");
    }
    if (!nombre.matches("^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+$")) {
      throw new IllegalArgumentException(
          "El nombre del " + tipoItem + " solo puede contener letras y espacios");
    }
  }

  /**
   * Valida la descripción de un producto o equipo. Solo letras y espacios, máximo 100 caracteres,
   * opcional.
   */
  private void validateDescripcionInventario(String descripcion) {
    if (descripcion == null) {
      throw new IllegalArgumentException("La descripción es obligatoria");
    }
    if (descripcion.length() > 100) {
      throw new IllegalArgumentException("La descripción no puede exceder 500 caracteres");
    }
    if (descripcion.length() < 10) {
      throw new IllegalArgumentException("La descripción debe tener al menos 10 caracteres");
    }
  }

  /**
   * Valida el precio unitario de un producto. Campo numérico decimal, obligatorio, máximo 2
   * decimales.
   */
  private void validatePrecioUnitario(BigDecimal precio) {
    if (precio == null) {
      throw new IllegalArgumentException("Precio unitario obligatorio");
    }
    if (precio.compareTo(BigDecimal.ZERO) <= 0) {
      throw new IllegalArgumentException("Precio unitario debe ser mayor a 0");
    }
    if (precio.scale() > 2) {
      throw new IllegalArgumentException("Precio unitario solo puede tener hasta 2 decimales");
    }
  }

  /**
   * Valida el stock de un producto. Campo numérico de enteros positivos, máximo 3 dígitos (0-999).
   */
  private void validateStock(Integer stock) {
    if (stock == null) {
      throw new IllegalArgumentException("Stock obligatorio");
    }
    if (stock < 0) {
      throw new IllegalArgumentException("Stock no puede ser negativo");
    }
    if (stock > 999) {
      throw new IllegalArgumentException("Stock no puede exceder 999 unidades");
    }
  }

  /** Valida la unidad de medida del producto. */
  private void validateUnidadMedida(String unidadMedida) {
    if (unidadMedida == null || unidadMedida.trim().isEmpty()) {
      throw new IllegalArgumentException("La unidad de medida del producto es obligatoria");
    }
    String[] unidadesValidas = {"Unidad", "Kg", "Litro", "Caja", "Paquete"};
    boolean esValida = false;
    for (String unidad : unidadesValidas) {
      if (unidad.equalsIgnoreCase(unidadMedida)) {
        esValida = true;
        break;
      }
    }
    if (!esValida) {
      throw new IllegalArgumentException(
          "La unidad de medida debe ser: Unidad, Kg, Litro, Caja o Paquete");
    }
  }

  /** Valida el costo de un equipo. Campo numérico decimal, opcional, máximo 2 decimales. */
  private void validateCosto(BigDecimal costo) {
    if (costo != null) {
      if (costo.compareTo(BigDecimal.ZERO) < 0) {
        throw new IllegalArgumentException("El costo no puede ser negativo");
      }
      if (costo.scale() > 2) {
        throw new IllegalArgumentException("El costo solo puede tener hasta 2 decimales");
      }
    }
  }

  /**
   * Valida la marca de un equipo. Alfanumérico (letras, números, espacios y guiones), máximo 100
   * caracteres.
   */
  private void validateMarca(String marca) {
    if (marca != null && !marca.trim().isEmpty()) {
      if (marca.length() > 100) {
        throw new IllegalArgumentException("La marca no puede exceder 100 caracteres");
      }
      if (!marca.matches("^[a-zA-Z0-9áéíóúÁÉÍÓÚñÑ\\s\\-]+$")) {
        throw new IllegalArgumentException(
            "La marca solo puede contener letras, números, espacios y guiones");
      }
    }
  }

  /**
   * Valida el modelo de un equipo. Alfanumérico (letras, números, espacios y guiones), máximo 100
   * caracteres.
   */
  private void validateModelo(String modelo) {
    if (modelo != null && !modelo.trim().isEmpty()) {
      if (modelo.length() > 100) {
        throw new IllegalArgumentException("El modelo no puede exceder los 100 caracteres");
      }
      if (!modelo.matches("^[a-zA-Z0-9áéíóúÁÉÍÓÚñÑ\\s\\-]+$")) {
        throw new IllegalArgumentException(
            "El modelo solo debe contener letras, números, espacios y guiones");
      }
    }
  }

  /** Valida ubicación de un equipo. */
  private void validateUbicacion(String ubicacion) {
    if (ubicacion != null && !ubicacion.trim().isEmpty()) {
      String[] ubicacionesValidas = {"Área de cardio", "Zona de peso libre", "Bodega"};
      boolean esValida = false;
      for (String ub : ubicacionesValidas) {
        if (ub.equalsIgnoreCase(ubicacion)) {
          esValida = true;
          break;
        }
      }
      if (!esValida) {
        throw new IllegalArgumentException(
            "La ubicación debe ser: Área de cardio, Zona peso libre o Bodega");
      }
    }
  }

  /**
   * Valida las observaciones de un equipo. Alfanumérico y caracteres especiales, máximo 500
   * caracteres.
   */
  private void validateObservaciones(String observaciones) {
    if (observaciones != null && observaciones.length() > 500) {
      throw new IllegalArgumentException("Las observaciones no pueden exceder 500 caracteres");
    }
  }

  /** Valida el estado de un equipo. */
  private void validateEstadoEquipo(EstadoEquipo estado) {
    if (estado == null) {
      throw new IllegalArgumentException("El estado del equipo es obligatorio");
    }
  }

  /** Valida el ID de categoría. */
  private void validateCategoriaId(Integer categoriaId) {
    if (categoriaId == null || categoriaId <= 0) {
      throw new IllegalArgumentException("La categoría es obligatoria");
    }
  }

  /** Valida el ID de proveedor (opcional para productos). */
  private void validateProveedorIdOpcional(Integer proveedorId) {
    if (proveedorId != null && proveedorId <= 0) {
      throw new IllegalArgumentException("El ID de proveedor no es válido");
    }
  }

  /** Valida el nombre de un proveedor. Campo obligatorio, máximo 100 caracteres. */
  private void validateNombreProveedor(String nombre) {
    if (nombre == null || nombre.trim().isEmpty()) {
      throw new IllegalArgumentException("El nombre del proveedor es obligatorio");
    }
    if (nombre.length() > 100) {
      throw new IllegalArgumentException("El nombre del proveedor no puede exceder 100 caracteres");
    }
  }

  /** Valida el contacto de un proveedor. Campo opcional, máximo 100 caracteres. */
  private void validateContacto(String contacto) {
    if (contacto != null && contacto.length() > 100) {
      throw new IllegalArgumentException("El contacto no puede exceder 100 caracteres");
    }
  }

  /** Valida el teléfono de un proveedor (formato flexible). */
  private void validateTelefonoProveedor(String telefono) {
    if (telefono != null && !telefono.trim().isEmpty()) {
      if (telefono.length() > 20) {
        throw new IllegalArgumentException("El teléfono no puede exceder 20 caracteres");
      }
      if (!telefono.matches("^[0-9\\-\\+\\s\\(\\)]+$")) {
        throw new IllegalArgumentException("El formato del teléfono no es válido");
      }
    }
  }

  /** Valida el email (opcional). */
  private void validateEmailOpcional(String email) {
    if (email != null && !email.trim().isEmpty()) {
      if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
        throw new IllegalArgumentException("El formato del email no es el válido");
      }
      if (email.length() > 100) {
        throw new IllegalArgumentException("El email no puede exceder los 100 caracteres");
      }
    }
  }

  /** Valida la dirección de un proveedor. */
  private void validateDireccionProveedor(String direccion) {
    if (direccion != null && direccion.length() > 255) {
      throw new IllegalArgumentException("La dirección no puede exceder los 255 caracteres");
    }
  }
}
