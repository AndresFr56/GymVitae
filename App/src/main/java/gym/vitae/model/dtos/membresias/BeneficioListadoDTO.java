package gym.vitae.model.dtos.membresias;

public class BeneficioListadoDTO {

  private Integer id;
  private String nombre;
  private String descripcion;
  private Boolean activo;

  public BeneficioListadoDTO() {}

  public BeneficioListadoDTO(Integer id, String nombre, String descripcion, Boolean activo) {
    this.id = id;
    this.nombre = nombre;
    this.descripcion = descripcion;
    this.activo = activo;
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
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

  public Boolean getActivo() {
    return activo;
  }

  public void setActivo(Boolean activo) {
    this.activo = activo;
  }

  /**
   * Sobrescribe el método toString para devolver el nombre del beneficio.
   * Esto soluciona el problema de visualización en componentes de Swing como JList o JComboBox.
   */
  @Override
  public String toString() {
    return nombre;
  }
}
