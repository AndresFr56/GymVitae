package gym.vitae.controller;

import gym.vitae.model.Categoria;
import gym.vitae.model.Producto;
import gym.vitae.model.Proveedore;
import gym.vitae.model.dtos.inventario.EquipoListadoDTO;
import gym.vitae.model.dtos.inventario.InventarioListadoDTO;
import gym.vitae.model.dtos.inventario.ProductoListadoDTO;
import gym.vitae.repositories.CategoriaRepository;
import gym.vitae.repositories.EquipoRepository;
import gym.vitae.repositories.ProductoRepository;
import gym.vitae.repositories.ProveedoreRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class IventarioController extends BaseController {

  private final EquipoRepository equipoRepository;
  private final ProductoRepository productoRepository;
  private final ProveedoreRepository proveedorRepository;
  private final CategoriaRepository categoriaRepository;


    public IventarioController() {
    super();
    this.equipoRepository = getRepository(EquipoRepository.class);
    this.productoRepository = getRepository(ProductoRepository.class);
    this.proveedorRepository = getRepository(ProveedoreRepository.class);
    this.categoriaRepository = getRepository(CategoriaRepository.class);
  }

  IventarioController(EquipoRepository equipoRepository, ProductoRepository productoRepository, ProveedoreRepository proveedorRepository, CategoriaRepository categoriaRepository) {
    super(null);
    this.equipoRepository = equipoRepository;
    this.productoRepository = productoRepository;
    this.proveedorRepository = proveedorRepository;
    this.categoriaRepository = categoriaRepository;
  }

    /** Devuelve todos los productos y equipos combinados en un listado de Inventario */
    public List<InventarioListadoDTO> getInventario() {
        List<InventarioListadoDTO> result = new ArrayList<>();

        // Productos
        List<ProductoListadoDTO> productos = productoRepository.findAllListado();
        result.addAll(productos.stream()
                .map(p -> new InventarioListadoDTO(
                        p.getCodigo(),
                        "Producto",
                        p.getNombre(),
                        p.getDescripcion(),
                        p.getFechaIngreso() != null ? p.getFechaIngreso().toString() : ""
                ))
                .collect(Collectors.toList())
        );

        // Equipos
        List<EquipoListadoDTO> equipos = equipoRepository.findAllListado();
        result.addAll(equipos.stream()
                .map(e -> new InventarioListadoDTO(
                        e.getCodigo(),
                        "Equipo",
                        e.getNombre(),
                        e.getDescripcion(),
                        e.getFechaAdquisicion() != null ? e.getFechaAdquisicion().toString() : ""
                ))
                .collect(Collectors.toList())
        );

        return result;
    }

    public List<InventarioListadoDTO> getInventarioConFiltros(String searchText, String tipo) {
        return getInventario().stream()
                .filter(i -> (tipo == null || tipo.equalsIgnoreCase("Todos") || i.tipo().equalsIgnoreCase(tipo)))
                .filter(i -> searchText == null || searchText.isBlank() ||
                        i.nombre().toLowerCase().contains(searchText.toLowerCase()) ||
                        i.codigo().toLowerCase().contains(searchText.toLowerCase())
                )
                .collect(Collectors.toList());
    }

    // 1️⃣ Devuelve todos los proveedores registrados (activos)
    public List<Proveedore> getAllProveedores() {
        return proveedorRepository.findAll();
        // O si quieres solo activos: proveedorRepository.findProveedoresActivos();
    }

    // 2️⃣ Devuelve la categoría según el tipo
    public Categoria getCategoriaByTipo(String tipo) {
        // Asumiendo que el nombre de la categoría es igual al tipo: "Alimenticio" o "Herramienta"
        return categoriaRepository.findAll().stream()
                .filter(c -> c.getNombre().equalsIgnoreCase(tipo))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada para tipo: " + tipo));
    }

    // 3️⃣ Devuelve el proveedor por id
    public Proveedore getProveedorById(Integer id) {
        return proveedorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Proveedor no encontrado con id: " + id));
    }

    // 4️⃣ Guarda un producto en la base de datos
    public void guardarProducto(Producto producto) {
        productoRepository.save(producto);
    }

    public String generarCodigoProducto() {
        Random random = new Random();
        String codigo;

        while (true) {
            int num = 100000000 + random.nextInt(900000000); // 9 dígitos
            codigo = String.valueOf(num);

            final String codigoFinal = codigo; // variable efectivamente final para lambda
            boolean exists = productoRepository.findAll().stream()
                    .anyMatch(p -> p.getCodigo().equals(codigoFinal));

            if (!exists) break;
        }

        return codigo;
    }



}
