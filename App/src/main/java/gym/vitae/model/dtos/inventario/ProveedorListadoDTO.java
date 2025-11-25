package gym.vitae.model.dtos.inventario;

public class ProveedorListadoDTO {

  private Integer id;
  private String codigo;
  private String nombre;
  private String contacto;
  private String telefono;
  private String email;
  private Boolean activo;

  public ProveedorListadoDTO() {}

  public ProveedorListadoDTO(
      Integer id,
      String codigo,
      String nombre,
      String contacto,
      String telefono,
      String email,
      Boolean activo) {
    this.id = id;
    this.codigo = codigo;
    this.nombre = nombre;
    this.contacto = contacto;
    this.telefono = telefono;
    this.email = email;
    this.activo = activo;
  }

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

  public Boolean getActivo() {
    return activo;
  }

  public void setActivo(Boolean activo) {
    this.activo = activo;
  }
}
