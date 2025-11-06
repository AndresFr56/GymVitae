package gym.vitae.model;

import java.sql.Timestamp;

public class MembresiaBeneficios {
    private Integer id;
    private Integer tipoMembresiaId;
    private Integer beneficioId;
    private Timestamp createdAt;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Integer getTipoMembresiaId() { return tipoMembresiaId; }
    public void setTipoMembresiaId(Integer tipoMembresiaId) { this.tipoMembresiaId = tipoMembresiaId; }

    public Integer getBeneficioId() { return beneficioId; }
    public void setBeneficioId(Integer beneficioId) { this.beneficioId = beneficioId; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }
}
