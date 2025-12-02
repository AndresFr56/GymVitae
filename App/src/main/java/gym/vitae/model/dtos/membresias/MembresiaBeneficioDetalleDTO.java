package gym.vitae.model.dtos.membresias;

import java.time.Instant;

public class MembresiaBeneficioDetalleDTO {

  private Integer id;
  private Instant createdAt;

  // Tipo Membresía
  private Integer tipoMembresiaId;
  private String tipoMembresiaNombre;

  // Beneficio
  private Integer beneficioId;
  private String beneficioNombre;
  private String beneficioDescripcion;

  public MembresiaBeneficioDetalleDTO() {}

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public Instant getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(Instant createdAt) {
    this.createdAt = createdAt;
  }

  public Integer getTipoMembresiaId() {
    return tipoMembresiaId;
  }

  public void setTipoMembresiaId(Integer tipoMembresiaId) {
    this.tipoMembresiaId = tipoMembresiaId;
  }

  public String getTipoMembresiaNombre() {
    return tipoMembresiaNombre;
  }

  public void setTipoMembresiaNombre(String tipoMembresiaNombre) {
    this.tipoMembresiaNombre = tipoMembresiaNombre;
  }

  public Integer getBeneficioId() {
    return beneficioId;
  }

  public void setBeneficioId(Integer beneficioId) {
    this.beneficioId = beneficioId;
  }

  public String getBeneficioNombre() {
    return beneficioNombre;
  }

  public void setBeneficioNombre(String beneficioNombre) {
    this.beneficioNombre = beneficioNombre;
  }

  public String getBeneficioDescripcion() {
    return beneficioDescripcion;
  }

  public void setBeneficioDescripcion(String beneficioDescripcion) {
    this.beneficioDescripcion = beneficioDescripcion;
  }
}
