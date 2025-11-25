package gym.vitae.model;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import org.hibernate.annotations.BatchSize;

@Entity
@Table(
    name = "cargos",
    indexes = {
      @Index(name = "idx_nombre", columnList = "nombre"),
      @Index(name = "idx_activo", columnList = "activo")
    })
public class Cargo {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(name = "nombre", nullable = false, length = 100)
  private String nombre;

  @Column(name = "salario_base", nullable = false, precision = 10, scale = 2)
  private BigDecimal salarioBase;

  @Lob
  @Column(name = "descripcion", columnDefinition = "TEXT")
  private String descripcion;

  @Column(name = "activo")
  private Boolean activo = true;

  @Column(name = "created_at", nullable = false, updatable = false)
  private Instant createdAt;

  @Column(name = "updated_at")
  private Instant updatedAt;

  @OneToMany(mappedBy = "cargo", cascade = CascadeType.ALL)
  @BatchSize(size = 20)
  private Set<Empleado> empleados = new HashSet<>();

  public Cargo() {}

  public Cargo(String nombre, BigDecimal salarioBase) {
    this.nombre = nombre;
    this.salarioBase = salarioBase;
  }

  @PrePersist
  protected void onCreate() {
    createdAt = Instant.now();
    updatedAt = Instant.now();
  }

  @PreUpdate
  protected void onUpdate() {
    updatedAt = Instant.now();
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

  public BigDecimal getSalarioBase() {
    return salarioBase;
  }

  public void setSalarioBase(BigDecimal salarioBase) {
    this.salarioBase = salarioBase;
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

  public Instant getCreatedAt() {
    return createdAt;
  }

  public Instant getUpdatedAt() {
    return updatedAt;
  }

  public Set<Empleado> getEmpleados() {
    return empleados;
  }

  public void setEmpleados(Set<Empleado> empleados) {
    this.empleados = empleados;
  }

  @Override
  public String toString() {
    return "Cargo{id=" + id + ", nombre='" + nombre + "', salarioBase=" + salarioBase + "}";
  }
}
