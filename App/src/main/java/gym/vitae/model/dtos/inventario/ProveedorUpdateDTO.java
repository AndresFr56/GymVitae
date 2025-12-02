package gym.vitae.model.dtos.inventario;

/**
 * DTO para la actualización de proveedores.
 */
public class ProveedorUpdateDTO {

  private String codigo;
  private String nombre;
  private String contacto;
  private String telefono;
  private String email;
  private String direccion;
  private Boolean activo;

  /**
   * Constructor por defecto para instanciar la clase.
   */
  public ProveedorUpdateDTO() {
  }

  /**
   * Constructor para actualizar datos del proveedor. El código del proveedor no es modificable, el
   * estado no se puede cambiar al actualizar, solo al dar de baja
   *
   * @param nombre del proveedor
   * @param contacto del proveedor
   * @param telefono del proveedor
   * @param email del proveedor
   * @param direccion del proveedor
   */
  public ProveedorUpdateDTO(
      String nombre,
      String contacto,
      String telefono,
      String email,
      String direccion) {
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

  public Boolean getActivo() {
    return activo;
  }

  public void setActivo(Boolean activo) {
    this.activo = activo;
  }
}
