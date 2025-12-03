package gym.vitae.model.dtos.inventario;

/**
 * DTO para la creación de proveedores. Incluye los datos necesarios para registrar un proveedor.
 */
public class ProveedorCreateDTO {

  /** Código del proveedor es generado automáticamente en el controlador. */
  private String codigo;

  private String nombre;
  private String contacto;
  private String telefono;
  private String email;
  private String direccion;

  /** Constructor por defecto para instanciar la clase. */
  public ProveedorCreateDTO() {}

  /**
   * Constructor para crear un nuevo proveedor. No se especifica el código del proveedor porque es
   * generado en el controlador
   *
   * @param nombre del proveedor
   * @param contacto del proveedor
   * @param telefono del proveedor
   * @param email del proveedor
   * @param direccion del proveedor
   */
  public ProveedorCreateDTO(
      String nombre, String contacto, String telefono, String email, String direccion) {
    this.nombre = nombre;
    this.contacto = contacto;
    this.telefono = telefono;
    this.email = email;
    this.direccion = direccion;
  }

  public String getCodigo() {
    return codigo;
  }

  public void setCodigo(String codigo) {
    this.codigo = codigo;
  }

  public String getNombre() {
    return nombre;
  }

  public void setNombre(String nombre) {
    this.nombre = nombre;
  }

  public String getContacto() {
    return contacto;
  }

  public void setContacto(String contacto) {
    this.contacto = contacto;
  }

  public String getTelefono() {
    return telefono;
  }

  public void setTelefono(String telefono) {
    this.telefono = telefono;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getDireccion() {
    return direccion;
  }

  public void setDireccion(String direccion) {
    this.direccion = direccion;
  }
}
