package gym.vitae.model.dtos.inventario;

public class ProveedorCreateDTO {

  private String codigo;
  private String nombre;
  private String contacto;
  private String telefono;
  private String email;
  private String direccion;

  public ProveedorCreateDTO() {}

  public ProveedorCreateDTO(
      String codigo,
      String nombre,
      String contacto,
      String telefono,
      String email,
      String direccion) {
    this.codigo = codigo;
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
