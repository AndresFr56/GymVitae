package gym.vitae.model.dtos.inventario;

/**
 * DTO para listado combinado de productos y equipos en inventario. Incluye información necesaria
 * para la tabla principal de inventario.
 */
public class InventarioListadoDTO {
  private Integer id;
  private String codigo;
  private String tipo; // "Producto" o "Equipo"
  private String nombre;
  private String descripcion;
  private String fechaAdquisicion;

  public InventarioListadoDTO() {}

  public InventarioListadoDTO(
      Integer id,
      String codigo,
      String tipo,
      String nombre,
      String descripcion,
      String fechaAdquisicion) {
    this.id = id;
    this.codigo = codigo;
    this.tipo = tipo;
    this.nombre = nombre;
    this.descripcion = descripcion;
    this.fechaAdquisicion = fechaAdquisicion;
  }

  // Getters y Setters
  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getCodigo() {
    return codigo;
  }

  public void setCodigo(String codigo) {
    this.codigo = codigo;
  }

  public String getTipo() {
    return tipo;
  }

  public void setTipo(String tipo) {
    this.tipo = tipo;
  }

  public String getNombre() {
    return nombre;
  }

  public void setNombre(String nombre) {
    this.nombre = nombre;
  }

  public String getDescripcion() {
    return descripcion;
  }

  public void setDescripcion(String descripcion) {
    this.descripcion = descripcion;
  }

  public String getFechaAdquisicion() {
    return fechaAdquisicion;
  }

  public void setFechaAdquisicion(String fechaAdquisicion) {
    this.fechaAdquisicion = fechaAdquisicion;
  }
}
