package gym.vitae.model.dtos.membresias;

public class MembresiaBeneficioListadoDTO {

  private Integer id;
  private String tipoMembresiaNombre;
  private String beneficioNombre;

  public MembresiaBeneficioListadoDTO() {}

  public MembresiaBeneficioListadoDTO(
      Integer id, String tipoMembresiaNombre, String beneficioNombre) {
    this.id = id;
    this.tipoMembresiaNombre = tipoMembresiaNombre;
    this.beneficioNombre = beneficioNombre;
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getTipoMembresiaNombre() {
    return tipoMembresiaNombre;
  }

  public void setTipoMembresiaNombre(String tipoMembresiaNombre) {
    this.tipoMembresiaNombre = tipoMembresiaNombre;
  }

  public String getBeneficioNombre() {
    return beneficioNombre;
  }

  public void setBeneficioNombre(String beneficioNombre) {
    this.beneficioNombre = beneficioNombre;
  }
}
