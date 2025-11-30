package gym.vitae.model.dtos.membresias;

public class MembresiaBeneficioCreateDTO {

  private Integer membresiaId;
  private Integer beneficioId;

  public MembresiaBeneficioCreateDTO() {}

  public MembresiaBeneficioCreateDTO(Integer membresiaId, Integer beneficioId) {
    this.membresiaId = membresiaId;
    this.beneficioId = beneficioId;
  }

  public Integer getMembresiaId() {
    return membresiaId;
  }

  public void setMembresiaId(Integer membresiaId) {
    this.membresiaId = membresiaId;
  }

  public Integer getBeneficioId() {
    return beneficioId;
  }

  public void setBeneficioId(Integer beneficioId) {
    this.beneficioId = beneficioId;
  }
}
