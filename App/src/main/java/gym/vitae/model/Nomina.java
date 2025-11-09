package gym.vitae.model;

import gym.vitae.model.enums.EstadoNomina;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(
    name = "nominas",
    uniqueConstraints = {
      @UniqueConstraint(
          name = "unique_nomina",
          columnNames = {"empleado_id", "mes", "anio"})
    },
    indexes = {
      @Index(name = "idx_fecha_pago", columnList = "fecha_pago"),
      @Index(name = "idx_estado", columnList = "estado"),
      @Index(name = "idx_mes_anio", columnList = "mes,anio")
    })
public class Nomina {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "empleado_id", nullable = false)
  private Empleado empleado;

  @Column(name = "mes", nullable = false)
  private Integer mes;

  @Column(name = "anio", nullable = false)
  private Integer anio;

  @Column(name = "salario_base", nullable = false, precision = 10, scale = 2)
  private BigDecimal salarioBase;

  @Column(name = "bonificaciones", precision = 10, scale = 2)
  private BigDecimal bonificaciones = BigDecimal.ZERO;

  @Column(name = "deducciones", precision = 10, scale = 2)
  private BigDecimal deducciones = BigDecimal.ZERO;

  @Column(name = "total_pagar", nullable = false, precision = 10, scale = 2)
  private BigDecimal totalPagar;

  @Column(name = "fecha_pago")
  private LocalDate fechaPago;

  @Enumerated(EnumType.STRING)
  @Column(name = "estado", length = 20)
  private EstadoNomina estado = EstadoNomina.PENDIENTE;

  @Lob
  @Column(name = "observaciones", columnDefinition = "TEXT")
  private String observaciones;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "generada_por")
  private Empleado generadaPor;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "aprobada_por")
  private Empleado aprobadaPor;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "pagada_por")
  private Empleado pagadaPor;

  @Column(name = "created_at", nullable = false, updatable = false)
  private Instant createdAt;

  @Column(name = "updated_at")
  private Instant updatedAt;

  // Constructores
  public Nomina() {}

  public Nomina(Empleado empleado, Integer mes, Integer anio, BigDecimal salarioBase) {
    this.empleado = empleado;
    this.mes = mes;
    this.anio = anio;
    this.salarioBase = salarioBase;
    this.totalPagar = salarioBase;
  }

  @PrePersist
  protected void onCreate() {
    createdAt = Instant.now();
    updatedAt = Instant.now();
    calcularTotal();
  }

  @PreUpdate
  protected void onUpdate() {
    updatedAt = Instant.now();
    calcularTotal();
  }

  private void calcularTotal() {
    if (salarioBase != null) {
      BigDecimal bonif = bonificaciones != null ? bonificaciones : BigDecimal.ZERO;
      BigDecimal deduc = deducciones != null ? deducciones : BigDecimal.ZERO;
      totalPagar = salarioBase.add(bonif).subtract(deduc);
    }
  }

  // Getters y Setters
  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public Empleado getEmpleado() {
    return empleado;
  }

  public void setEmpleado(Empleado empleado) {
    this.empleado = empleado;
  }

  public Integer getMes() {
    return mes;
  }

  public void setMes(Integer mes) {
    this.mes = mes;
  }

  public Integer getAnio() {
    return anio;
  }

  public void setAnio(Integer anio) {
    this.anio = anio;
  }

  public BigDecimal getSalarioBase() {
    return salarioBase;
  }

  public void setSalarioBase(BigDecimal salarioBase) {
    this.salarioBase = salarioBase;
  }

  public BigDecimal getBonificaciones() {
    return bonificaciones;
  }

  public void setBonificaciones(BigDecimal bonificaciones) {
    this.bonificaciones = bonificaciones;
  }

  public BigDecimal getDeducciones() {
    return deducciones;
  }

  public void setDeducciones(BigDecimal deducciones) {
    this.deducciones = deducciones;
  }

  public BigDecimal getTotalPagar() {
    return totalPagar;
  }

  public void setTotalPagar(BigDecimal totalPagar) {
    this.totalPagar = totalPagar;
  }

  public LocalDate getFechaPago() {
    return fechaPago;
  }

  public void setFechaPago(LocalDate fechaPago) {
    this.fechaPago = fechaPago;
  }

  public EstadoNomina getEstado() {
    return estado;
  }

  public void setEstado(EstadoNomina estado) {
    this.estado = estado;
  }

  public String getObservaciones() {
    return observaciones;
  }

  public void setObservaciones(String observaciones) {
    this.observaciones = observaciones;
  }

  public Empleado getGeneradaPor() {
    return generadaPor;
  }

  public void setGeneradaPor(Empleado generadaPor) {
    this.generadaPor = generadaPor;
  }

  public Empleado getAprobadaPor() {
    return aprobadaPor;
  }

  public void setAprobadaPor(Empleado aprobadaPor) {
    this.aprobadaPor = aprobadaPor;
  }

  public Empleado getPagadaPor() {
    return pagadaPor;
  }

  public void setPagadaPor(Empleado pagadaPor) {
    this.pagadaPor = pagadaPor;
  }

  public Instant getCreatedAt() {
    return createdAt;
  }

  public Instant getUpdatedAt() {
    return updatedAt;
  }

  @Override
  public String toString() {
    return "Nomina{id=" + id + ", mes=" + mes + ", anio=" + anio + ", total=" + totalPagar + "}";
  }
}
